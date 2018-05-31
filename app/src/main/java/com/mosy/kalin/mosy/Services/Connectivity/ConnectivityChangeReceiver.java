package com.mosy.kalin.mosy.Services.Connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;

public class ConnectivityChangeReceiver
        extends BroadcastReceiver {

    public static final String NETWORK_AVAILABLE_ACTION = "com.mosy.kalin.mosy.NetworkAvailable";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
        networkStateIntent.putExtra(IS_NETWORK_AVAILABLE,  ConnectivityHelper.isConnected(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
    }
}
