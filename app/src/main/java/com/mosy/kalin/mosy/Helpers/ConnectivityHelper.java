package com.mosy.kalin.mosy.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityHelper {

    /* checks whether the device connected or not*/
    public static boolean isConnected(Context contect) {
        try {
            ConnectivityManager cm = (ConnectivityManager) contect.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
