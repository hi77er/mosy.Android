package com.mosy.kalin.mosy;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mosy.kalin.mosy.Helpers.LocaleHelper;
import com.mosy.kalin.mosy.Services.Connectivity.ConnectionStateMonitor;

import static android.content.pm.PackageManager.GET_META_DATA;

public abstract class BaseActivity
        extends AppCompatActivity {

    protected Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.applicationContext = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
        resetTitle();
    }

    private void resetTitle() {
        try {
            PackageManager manager = getPackageManager();
            ComponentName componentName = getComponentName();
            if (componentName != null){
                ActivityInfo info = manager.getActivityInfo(componentName, GET_META_DATA);
                int labelId = info.labelRes;
                if (labelId != 0) {
                    setTitle(labelId);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}