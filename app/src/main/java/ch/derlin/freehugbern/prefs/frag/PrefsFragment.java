package ch.derlin.freehugbern.prefs.frag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import ch.derlin.freehugbern.R;
import ch.derlin.freehugbern.bt.BluetoothService;
import ch.derlin.freehugbern.bt.DeviceListActivity;


/**
 * This class is the fragment responsible for the App Settings.
 * <p>
 * There are three kind of settings:
 * <ul>
 * <li>Core Settings: id of the HuggiShirt and data the former sends during a hug</li>
 * <li>Commands: allow the user to send predefined commands to the HuggiSHirt</li>
 * <li>Reset: clear database, reset application</li>
 * </ul>
 * <p>
 * creation date    01.12.2014
 * context          Projet de semestre Hugginess, EIA-FR, I3 2014-2015
 *
 * @author Lucy Linder
 */
public class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference
        .OnPreferenceChangeListener{

    private static final int REQUEST_CONNECT_DEVICE = 123;
    private Preference mPairingPref, mResetPref, mForceSyncPref;
    private ListPreference mDisplayListPref;


    //-------------------------------------------------------------


    public void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );


        // Load the preferences from an XML resource
        addPreferencesFromResource( R.xml.prefscreen );


        mPairingPref = findPreference( "pref_paired_device" );
        mPairingPref.setOnPreferenceClickListener( this );
        mPairingPref.setSummary( PreferenceManager.getDefaultSharedPreferences( getActivity() ).getString(
                "pref_paired_device", "" ) );

        mResetPref = findPreference( "pref_reset" );
        mResetPref.setOnPreferenceClickListener( this );

        mForceSyncPref = findPreference( "pref_sync" );
        mForceSyncPref.setOnPreferenceClickListener( this );

        mDisplayListPref = ( ListPreference ) findPreference( "pref_display_type" );
        mDisplayListPref.setOnPreferenceChangeListener( this );

    }


    @Override
    public boolean onPreferenceClick( Preference preference ){


        if( preference == mPairingPref ){
            Intent intent = new Intent( getActivity(), DeviceListActivity.class );
            startActivityForResult( intent, REQUEST_CONNECT_DEVICE );

        }else if( preference == mResetPref ){
            BluetoothService.getInstance().disconnect();
            PreferenceManager.getDefaultSharedPreferences( getActivity() ).edit() //
                    .putString( "pref_paired_device", null ).commit();
            mPairingPref.setSummary( "" );
            sendToBt( "R" );

        }else if( preference == mForceSyncPref ){
            sendToBt( "I" );
        }

        return true;
    }


    @Override
    public boolean onPreferenceChange( Preference preference, Object newValue ){
        // handle preferences with input (configure HuggiShirt data + terminal nbr of lines)

        if( preference == mDisplayListPref ){
            String v = mDisplayListPref.getValue();
            int index = mDisplayListPref.findIndexOfValue( v );
            sendToBt( index == 0 ? "C" : "D" );

            // Set the summary to reflect the new value.
            preference.setSummary( index >= 0 ? mDisplayListPref.getEntries()[ index ] : null );
        }

        return true;

    }


    private void sendToBt( String msg ){
        BluetoothService bt = BluetoothService.getInstance();
        if( bt != null && bt.isConnected() ){
            bt.send( msg, false );

        }else{
            Toast.makeText( getActivity(), "Not connected", Toast.LENGTH_SHORT ).show();
        }
    }


    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data ){

        if( requestCode == REQUEST_CONNECT_DEVICE ){

            if( resultCode == Activity.RESULT_OK ){
                BluetoothService mSPP = BluetoothService.getInstance();
                String macAddress = data.getExtras().getString( DeviceListActivity.EXTRA_DEVICE_ADDRESS );
                if( mSPP.isConnected() ){
                    if( macAddress.equals( mSPP.getDeviceAddress() ) ){
                        // already connected to the right device
                        Toast.makeText( getActivity(), "already connected to device", Toast.LENGTH_SHORT ).show();
                        return;
                    }
                    // not the right device, disconnect
                    mSPP.disconnect();
                }

                // save pref
                PreferenceManager.getDefaultSharedPreferences( getActivity() ).edit() //
                        .putString( "pref_paired_device", macAddress ).commit();
                mPairingPref.setSummary( macAddress );

                // connect to the device
                Toast.makeText( getActivity(), "Trying to connect to " + macAddress, Toast.LENGTH_SHORT ).show();
                mSPP.connect( macAddress );

            }else{
                // something went wrong... Simply quit the app
                Log.e( getActivity().getPackageName(), "FirstLaunchActivity: step 2b: something went wrong " + //
                        "in return from the devicelist activity..." );
            }
        }
    }


}//end class
