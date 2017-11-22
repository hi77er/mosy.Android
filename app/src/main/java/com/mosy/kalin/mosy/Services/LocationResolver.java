package com.mosy.kalin.mosy.Services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import static android.content.Context.LOCATION_SERVICE;

public class LocationResolver implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, android.location.LocationListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters  //The minimum distance to change Updates in meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 3 ; // 1 minute //The minimum time between updates in milliseconds

    private final int REQUEST_LOCATION = 2; //Location Request code
    private GoogleApiClient mGoogleApiClient; //Google Api Client
    private LocationRequest mLocationRequest; //Location request for google fused location Api
    private LocationManager mLocationManager; //Location manager for location services

    private OnLocationResolved mOnLocationResolved;
    private Activity mActivity;

    private AlertDialog alertDialog; //Location permission Dialog


    public LocationResolver(Activity activity){
        mActivity=activity;
        buildGoogleApiClient();
        mLocationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        createLocationRequest();
    }

    public void resolveLocation(Activity activity, OnLocationResolved onLocationResolved){
        this.mOnLocationResolved = onLocationResolved;
        this.mActivity=activity;

        if (isEveryThingEnabled()){
            startLocationPooling();
        }
    }

    public interface OnLocationResolved{
        void onLocationResolved(Location location);
    }

    /* Checking every criteria are enabled for getting location from device */
    public boolean isEveryThingEnabled() {
        if (!isLocationPermissionEnabled()) {
            showPermissionRequestDialog();
            return false;
        } else if (!isLocationEnabled(mActivity)) {
            showLocationSettingsDialog();
            return false;
        } else if (!isConnected()) {
            showWifiSettingsDialog(mActivity);
            return false;
        }

        return true;
    }

    /* This function checks if location permissions are granted or not */
    public boolean isLocationPermissionEnabled() {
        int fineLocationPermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
        //mActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION); // min API 23

        return (fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED);
    }

    /* Previous location permissions were denied , this function opens app settings page
    *  So user can enable permission manually */
    private void startAppDetailsActivity() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + mActivity.getPackageName()));

        mActivity.startActivity(i);
    }

    private void showLocationSettingsDialog() {
        if (alertDialog != null) alertDialog.cancel();

        alertDialog = new AlertDialog.Builder(mActivity, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Need Location")
                .setMessage("In order for the app to work seamlessly.Please  enable Location Service.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startLocationSettings();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void startLocationSettings() {
        mActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationPooling();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) &&
                        ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        showPermissionRequestDialog();
                    } else {
                        showPermissionDeniedDialog();
                    }
                }
            }
        }
    }

    /*
    * Starting location pooling
    * */
    public void startLocationPooling() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
            mOnLocationResolved.onLocationResolved(location);
        } else {
            if (mGoogleApiClient.isConnected())//if googleClient can get location from device the go for location update
                startLocationUpdates();
            else getLocation(); //Google Client cannot connected to its server. so we are fetching location directly from device
        }
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void onDestroy() {
        mGoogleApiClient.disconnect();
    }

    public void onStop() {
        if (alertDialog != null) alertDialog.cancel();

        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }

    public void onStart() {
        mGoogleApiClient.connect();
    }

    public void onResume() {
        mGoogleApiClient.connect();
    }



    @Override
    public void onConnected(Bundle bundle) {
        // startLocationPooling();
    }

    /* checks whether the device connected or not*/
    public boolean isConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mOnLocationResolved.onLocationResolved(location);
            stopLocationUpdates();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(mActivity, ConnectionResult.RESOLUTION_REQUIRED);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("TAG", "Location services connection failed with code==>" + connectionResult.getErrorCode());
            Log.e("TAG", "Location services connection failed Because of==> " + connectionResult.getErrorMessage());
        }
    }

    private void createLocationRequest() {
        Log.i("TAG", "CreateLocationRequest");
        mLocationRequest = new LocationRequest();
        long UPDATE_INTERVAL = 10 * 1000;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        long FASTEST_INTERVAL = 10000;
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

    }

    private void startLocationUpdates() {

        Log.i("TAG", "StartLocationUpdates");

        if (Build.VERSION.SDK_INT >= 23) {
            if (mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    mActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient,
                        mLocationRequest,
                        this);
            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }

    }

    private void stopLocationUpdates() {

        try {
            if (mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

            if (mLocationManager != null) {
                mLocationManager.removeUpdates(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getLocation() {
        try {


            // getting GPS status
            Boolean isGPSEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            Boolean isNetworkEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.e("Location", "No provider enabled");
            } else {
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    return;
                }
                Location location = null;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (mLocationManager != null) {
                        location = mLocationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            mOnLocationResolved.onLocationResolved(location);
                        } else {
                            mLocationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("Network", "Network");
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (mLocationManager != null) {
                            location = mLocationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                mOnLocationResolved.onLocationResolved(location);
                            } else {
                                mLocationManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER,
                                        MIN_TIME_BW_UPDATES,
                                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                                Log.d("GPS Enabled", "GPS Enabled");
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* checks whether the device connected or not*/
    public static boolean isNetWorkConnected(Context  context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            return netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }


    private void showWifiSettingsDialog(final Context context) {
        if (alertDialog != null) alertDialog.cancel();

        alertDialog = new AlertDialog.Builder(mActivity, android.R.style.Theme_Material_Dialog_Alert)
            .setTitle("Need Internet")
            .setMessage("Please enable your internet connection")
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    startWifiSettings(context);
                }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private void startWifiSettings(Context context) {
        try {
            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } catch (Exception e) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /* location permissions were denied with "do not show"  unchecked.. this function shows a dialog describing why this app
    *  need location permission. */
    private void showPermissionRequestDialog() {
        if (alertDialog != null) alertDialog.cancel();

        alertDialog = new AlertDialog.Builder(mActivity, android.R.style.Theme_Material_Dialog_Alert)
            .setTitle("You need location permission")
            .setMessage("Enable location permission")
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    /*  Previously Permission Request was cancelled with 'Dont Ask Again',
    *   Redirect to Settings after showing Information about why you need the permission */
    private void showPermissionDeniedDialog() {
        if (alertDialog != null) alertDialog.cancel();

        alertDialog = new AlertDialog.Builder(mActivity, android.R.style.Theme_Material_Dialog_Alert)
            .setTitle("Need Location Permission")
            .setMessage("Enable location permission")
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    startAppDetailsActivity();
                }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

}