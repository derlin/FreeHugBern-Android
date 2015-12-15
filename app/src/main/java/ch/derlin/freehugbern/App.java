package ch.derlin.freehugbern;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import ch.derlin.freehugbern.bt.BluetoothService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: lucy
 * Date: 24/11/14
 * Version: 0.1
 */

/**
 * This class is the Hugginess Application.
 * Its main role is to start/stop the bluetooth service and to make a
 * context available through a static method (useful when
 * you need a context in a regular class).
 * <p>
 * creation date    24.11.2014
 * context          Projet de semestre Hugginess, EIA-FR, I3 2014-2015
 *
 * @author Lucy Linder
 */
public class App extends Application{

    static Context sAppContext;
    static final String LOG_FILE_NAME = "hugs_log.file";


    /** @return the application context * */
    public static Context getAppContext(){
        return sAppContext;
    }

    //-------------------------------------------------------------


    @Override
    public void onCreate(){
        super.onCreate();

        sAppContext = this.getApplicationContext();
        // start the bluetooth service
        Intent i = new Intent( this, BluetoothService.class );
        this.startService( i );

    }


    @Override
    public void onTerminate(){
        // stop the bluetooth service
        this.stopService( new Intent( this, BluetoothService.class ) );
        super.onTerminate();
    }

    // ----------------------------------------------------


    public static void clearLog(){
        File sd = Environment.getExternalStorageDirectory();
        File logFile = new File( sd, LOG_FILE_NAME );

        if( logFile.exists() ){
            logFile.renameTo( new File( sd, LOG_FILE_NAME + "_" + System.currentTimeMillis() ) );
        }
    }


    public static void appendLog( String text ){
        File logFile = new File( Environment.getExternalStorageDirectory(), LOG_FILE_NAME );

        try{
            if( !logFile.exists() ){
                logFile.createNewFile();
            }

            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter( new FileWriter( logFile, true ) );
            buf.append( String.format( "[%d] ", System.currentTimeMillis() ) );
            buf.append( text );
            buf.newLine();
            buf.close();

        }catch( IOException e ){
            Log.d( "FREEHUG", "could not log " + text );
        }
    }
}
