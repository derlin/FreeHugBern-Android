package ch.derlin.freehugbern.bt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import static ch.derlin.freehugbern.bt.BluetoothConstants.*;

/**
 * Context:
 * @author Lucy Linder
 * Date 09/12/15.
 */
public class BtBroadcastReceiver extends BroadcastReceiver{


    private static final IntentFilter INTENT_FILTER = new IntentFilter( BTSERVICE_INTENT_FILTER );


    // ----------------------------------------------------


    /**
     * Register this receiver to the local broadcast manager to start receiving events.
     *
     * @param context the context
     */
    public void registerSelf( Context context ){
        LocalBroadcastManager.getInstance( context ).registerReceiver( this, INTENT_FILTER );
    }


    /**
     * Unregister this receiver from the local broadcast manager to stop receiving events.
     *
     * @param context the context
     */
    public void unregisterSelf( Context context ){
        LocalBroadcastManager.getInstance( context ).unregisterReceiver( this );
    }

    // ----------------------------------------------------


    /** Called when the bluetooth adapter is turned on. * */
    public void onBtTurnedOn(){ }


    /** Called when the bluetooth adapter is turned off. * */
    public void onBtTurnedOff(){ }


    /** Called upon a successful connection. * */
    public void onBtConnected(){ }


    /** Called upon a disconnection. * */
    public void onBtDisonnected(){ }


    /** Called when the bluetooth state changed (turn on/off, connected/disconnected). * */
    public void onBtStateChanged(){}


    /** Called upon an unsuccessful connection. * */
    public void onBtConnectionFailed(){ }


    /**
     * Called when a new line has been received.
     *
     * @param line the new line, without linebreak.
     */
    public void onBtDataReceived( String line ){

    }

   // ----------------------------------------------------

    @Override
    public void onReceive( Context context, Intent intent ){
        // handle the different kind of events
        switch( intent.getStringExtra( EXTRA_EVT_TYPE ) ){

            case EVT_BT_TURNED_ON:
                onBtTurnedOn();
                onBtStateChanged();

            case EVT_BT_TURNED_OFF:
                onBtTurnedOff();
                onBtStateChanged();
                break;

            case EVT_CONNECTED:
                onBtConnected();
                onBtStateChanged();
                break;


            case EVT_DISCONNECTED:
                onBtDisonnected();
                onBtStateChanged();
                break;

            case EVT_CONNECTION_FAILED:
                onBtConnectionFailed();
                break;

            case EVT_DATA_RECEIVED:
                String line = intent.getStringExtra( EVT_EXTRA_DATA );
                onBtDataReceived( line );
                break;
        }
    }
}
