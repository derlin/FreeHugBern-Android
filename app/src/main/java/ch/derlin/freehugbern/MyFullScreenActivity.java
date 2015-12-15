package ch.derlin.freehugbern;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ch.derlin.freehugbern.bt.BluetoothService;
import ch.derlin.freehugbern.bt.BtBroadcastReceiver;
import ch.derlin.freehugbern.prefs.PrefsActivity;
import ch.derlin.freehugbern.prefs.frag.PrefsFragment;

/**
 * Context:
 *
 * @author Lucy Linder
 *         Date 09/12/15.
 */
public class MyFullScreenActivity extends FullscreenActivity implements View.OnClickListener{
    private Button settingsButton;
    private TextView cnterTextView, noConnectionTextView;
    private BluetoothService mSPP;
    private MenuItem mMenuConnect;

    private BtBroadcastReceiver mBtReceiver = new BtBroadcastReceiver(){

        @Override
        public void onBtConnected(){
            mMenuConnect.setEnabled( true );
            Toast.makeText( MyFullScreenActivity.this, "Connected to " + mSPP.getDeviceAddress(), Toast.LENGTH_LONG ).show();
            noConnectionTextView.setVisibility( View.INVISIBLE );
            if(mMenuConnect != null){
                mMenuConnect.setIcon( getResources().getDrawable( R.drawable.connected ) );
            }
            mSPP.send( "I", false );  // get infos (counter/duration)
        }


        @Override
        public void onBtConnectionFailed(){
            mMenuConnect.setEnabled( true );
            Toast.makeText( MyFullScreenActivity.this, "Connection failed", Toast.LENGTH_SHORT ).show();
        }


        @Override
        public void onBtDisonnected(){
            mMenuConnect.setIcon( getResources().getDrawable( R.drawable.disconnected ) );
            noConnectionTextView.setVisibility( View.VISIBLE );
        }


        @Override
        public void onBtDataReceived( String line ){
            super.onBtDataReceived( line );
            cnterTextView.setText( line.split( " " )[ 0 ] );
        }

    };



    // ----------------------------------------------------


    @Override
    public void onDestroy(){
        mBtReceiver.unregisterSelf( this );
        super.onDestroy();
    }

    // ----------------------------------------------------


    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );

        mBtReceiver.registerSelf( this );

        settingsButton = ( Button ) findViewById( R.id.settings_button );
        settingsButton.setOnClickListener( this );
        settingsButton.setEnabled( false ); // wait for the bt service to start
        cnterTextView = ( TextView ) findViewById( R.id.fullscreen_content );
        noConnectionTextView = ( TextView ) findViewById( R.id.no_connection_text );

        // wait until the bluetooth service has been started
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground( Void... params ){
                while( BluetoothService.getInstance() == null ){
                    try{
                        Thread.sleep( 200 );
                    }catch( InterruptedException e ){
                        e.printStackTrace();
                    }
                }

                mSPP = BluetoothService.getInstance();
                return null;
            }


            @Override
            protected void onPostExecute( Void aVoid ){
                settingsButton.setEnabled( true );
                onBtServiceStarted();
            }
        }.execute();
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu ){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.toolbar_menu, menu );
        mMenuConnect = menu.findItem( R.id.toolbar_connect );
        if(mSPP != null && mSPP.isConnected()){
            mMenuConnect.setIcon( getResources().getDrawable( R.drawable.connected ) );
        }
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // toggle connect/disconnect
            case R.id.toolbar_connect:
                if(mSPP.isConnected()){
                    mSPP.disconnect();

                }else{
                    // if already a device, connect, else, start the settings activity
                    String addr = PreferenceManager.getDefaultSharedPreferences( this ).getString(
                            "pref_paired_device", null );
                    if(addr == null){
                        startActivity( new Intent( this, PrefsFragment.class ) );
                    }else{
                        mMenuConnect.setEnabled( false );
                        mSPP.connect(addr);
                    }
                }
                break;
            default:
                break;
        }

        return true;
    }


    private void onBtServiceStarted(){

        if(!mSPP.isBluetoothEnabled()){
            mSPP.enable();
        }
        String macAddress = PreferenceManager.getDefaultSharedPreferences( this ).getString( "pref_paired_device", null );
        if( macAddress == null ){
            settingsButton.callOnClick();
        }else{
            Toast.makeText( this, "Trying to connect to " + macAddress, Toast.LENGTH_SHORT ).show();
            mSPP.connect( macAddress );
        }
    }


    @Override
    public void onClick( View v ){
        if( v == settingsButton ){
            startActivity( new Intent( this, PrefsActivity.class ) );
        }

    }
}
