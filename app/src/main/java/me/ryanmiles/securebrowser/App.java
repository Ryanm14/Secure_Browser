package me.ryanmiles.securebrowser;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Ryan Miles on 8/22/2016.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Data.TABOUTLIST = new ArrayList<>();
    }
}
