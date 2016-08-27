package me.ryanmiles.securebrowser;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.ArrayList;

import static me.ryanmiles.securebrowser.Data.FIRST_NAME;
import static me.ryanmiles.securebrowser.Data.LAST_NAME;
import static me.ryanmiles.securebrowser.Data.SHAREDPREF_FIRST_NAME_KEY;
import static me.ryanmiles.securebrowser.Data.SHAREDPREF_LAST_NAME_KEY;
import static me.ryanmiles.securebrowser.Data.SHAREDPREF_STUDENT_ID_KEY;
import static me.ryanmiles.securebrowser.Data.STUDENT_ID;

/**
 * Created by Ryan Miles on 8/22/2016.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Data.TABOUTLIST = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("me.ryanmiles.securebrowser", MODE_PRIVATE);
        FIRST_NAME = prefs.getString(SHAREDPREF_FIRST_NAME_KEY, null);
        LAST_NAME = prefs.getString(SHAREDPREF_LAST_NAME_KEY, null);
        STUDENT_ID = prefs.getInt(SHAREDPREF_STUDENT_ID_KEY, 0);
    }


}
