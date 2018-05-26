package com.mosy.kalin.mosy;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.mosy.kalin.mosy.Helpers.LocaleHelper;

import java.util.Locale;

public class App extends Application {

    public static Locale sDefSystemLocale;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();

        //Getting Device Default Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            sDefSystemLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
        else
            sDefSystemLocale = Resources.getSystem().getConfiguration().locale;

        // Required initialization logic here!
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //Getting Device Default Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            sDefSystemLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
        else
            sDefSystemLocale = Resources.getSystem().getConfiguration().locale;


        LocaleHelper.onAttach(this);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}