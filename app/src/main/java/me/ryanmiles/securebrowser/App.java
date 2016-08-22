package me.ryanmiles.securebrowser;

import android.app.Application;

import com.testfairy.TestFairy;

/**
 * Created by Ryan Miles on 8/22/2016.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(!BuildConfig.DEBUG) {
            TestFairy.begin(this, "873cb9ae0c2b9898c9bf38457d012dd54681fbfa");
        }
    }
}
