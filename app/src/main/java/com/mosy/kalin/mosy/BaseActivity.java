package com.mosy.kalin.mosy;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mosy.kalin.mosy.Helpers.LocaleHelper;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Services.Connectivity.ConnectionStateMonitor;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import static android.content.pm.PackageManager.GET_META_DATA;

@EActivity
public abstract class BaseActivity
        extends AppCompatActivity {

    protected boolean activityStopped = false;
    protected boolean isDevelopersModeActivated = false;

    protected boolean isUserAuthenticated = false;

    protected Context applicationContext;
    protected Context baseContext;


    AccountService accountService;

    protected ConnectionStateMonitor connectionStateMonitor;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
        resetTitle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.applicationContext = getApplicationContext();
        this.baseContext = getBaseContext();

        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_collectionName_developersMode), MODE_PRIVATE);
        this.isDevelopersModeActivated = prefs.getBoolean(getString(R.string.pref_developersModeEnabled), false);

        this.accountService = new AccountService();
        isUserAuthenticated = this.accountService.checkUserTokenValid(this.baseContext);
    }

    @Override
    protected void onStart(){
        super.onStart();

        connectionStateMonitor = new ConnectionStateMonitor();
        connectionStateMonitor.onAvailable = this::onNetworkAvailable;
        connectionStateMonitor.onLost = this::onNetworkLost;
        connectionStateMonitor.enable(baseContext);
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

    protected void onNetworkLost() {
    }

    protected void onNetworkAvailable() {
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}