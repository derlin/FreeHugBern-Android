package ch.derlin.freehugbern.prefs;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import ch.derlin.freehugbern.prefs.frag.PrefsFragment;

/**
 * This class is the preferences activity, which is launched when the user
 * clicks on "App Settings" in the actionbar menu.
 * <p/>
 * It only handles the home back button: the settings management is done
 * in the fragment (see {@link .PrefsFragment}).
 * <p/>
 * creation date    30.11.2014
 * context          Projet de semestre Hugginess, EIA-FR, I3 2014-2015
 *
 * @author Lucy Linder
 */
public class PrefsActivity extends PreferenceActivity{


    //-------------------------------------------------------------


    @Override
    protected void onNewIntent( Intent intent ){
        // overriding this method fixes the bugs related to
        // configuration change. The activity is no longer restarted !
        super.onNewIntent( intent );
        setIntent( intent );
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    // ----------------------------------------------------


    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );

        // allow to go back to main activity on home pressed
        ActionBar actionBar = getActionBar();
        if( actionBar != null )
            actionBar.setDisplayHomeAsUpEnabled( true );

        // directly display the fragment as the main content (skip headers)
        getFragmentManager().beginTransaction().replace( android.R.id.content, new PrefsFragment() ).commit();
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item ){
        switch( item.getItemId() ){
            case android.R.id.home:{
                onBackPressed();
                return true;
            }
            default:
                return super.onOptionsItemSelected( item );
        }
    }


    //-------------------------------------------------------------
    @Override
    protected boolean isValidFragment( String fragmentName ){
        return "ch.derlin.freehugbern.prefs.frag.PrefsFragment".equals( fragmentName );
    }


}