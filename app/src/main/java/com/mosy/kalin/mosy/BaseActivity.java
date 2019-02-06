package com.mosy.kalin.mosy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.mosy.kalin.mosy.Helpers.LocaleHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Services.Connectivity.ConnectionStateMonitor;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.json.JSONException;

import java.util.Arrays;

import static android.content.pm.PackageManager.GET_META_DATA;

@EActivity
public abstract class BaseActivity
        extends AppCompatActivity {

    protected static final int DEFAULT_MINIMAL_DISTANCE_FILTER_METERS = 3000;
    protected static final boolean DEFAULT_APPLY_WORKING_STATUS_FILTER = true;
    protected static final boolean DEFAULT_APPLY_RECOMMENDED_FILTER = true;

    protected boolean activityStopped = false;
    protected boolean isDevelopersModeActivated = false;

    protected boolean isUserAuthenticated = false;
    protected String username = StringHelper.empty();

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
        username = this.accountService.getUsername(this.baseContext);
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

    public void goToLoginActivity() {
        Intent intent = new Intent(BaseActivity.this, LoginActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        overridePendingTransition( R.transition.slide_in_right, R.transition.slide_out_right );
    }
}