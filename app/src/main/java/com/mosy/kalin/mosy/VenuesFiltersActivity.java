package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_filters_venues)
public class VenuesFiltersActivity
        extends BaseActivity {

    private boolean afterViewsFinished = false;
    private boolean networkLost = false;
    private int distanceFilterStep = 100;
    private int distanceFilterMinValue = 100;
    private int distanceFilterMaxValue = 10000;
    private int distanceFilterFormattedValue;
    private boolean selectedApplyWorkingStatusFilter;

    @Extra
    static boolean PreselectedApplyWorkingStatusFilter;
    @Extra
    static int PreselectedDistanceFilterValue;

    @ViewById(resName = "filters_venues_sbWorkingTimeFilter")
    public Switch workingStatusFilter;
    @ViewById(resName = "filterVenues_GoButton")
    public Button goButton;

    @ViewById(resName = "filters_venues_tvDistanceLabel")
    public TextView distanceLabel;
    @ViewById(resName = "filters_venues_sbDistanceFilter")
    public SeekBar distanceSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels*.9), (int)(dm.heightPixels*.7));

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }

    @SuppressLint("SetTextI18n")
    @AfterViews
    public void afterViews(){
        this.workingStatusFilter.setChecked(PreselectedApplyWorkingStatusFilter);
        this.workingStatusFilter.setOnCheckedChangeListener(
            (compoundButton, b) -> selectedApplyWorkingStatusFilter = compoundButton.isChecked()
        );

        if (ConnectivityHelper.isConnected(applicationContext)) {
            networkLost = false;
            showLoaded();
        }
        else {
            networkLost = true;
            showLoading();
        }

        this.distanceFilterFormattedValue = PreselectedDistanceFilterValue;
        this.distanceLabel.setText(this.getDistanceFilterLabelText(PreselectedDistanceFilterValue));
        this.distanceSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimarySalmon), PorterDuff.Mode.SRC_IN);
        this.distanceSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimarySalmon), PorterDuff.Mode.SRC_IN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.distanceSeekBar.setMin(distanceFilterMinValue);
        }
        this.distanceSeekBar.setMax(distanceFilterMaxValue);
        this.distanceSeekBar.incrementProgressBy(distanceFilterStep);
        this.distanceSeekBar.setProgress(PreselectedDistanceFilterValue);
        this.distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = formatProgressDivideBy100(progress);
                if (progress > distanceFilterMaxValue) progress = distanceFilterMaxValue;
                if (progress < distanceFilterMinValue) progress = distanceFilterMinValue;
                distanceFilterFormattedValue = progress;
                distanceLabel.setText(getDistanceFilterLabelText(progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        afterViewsFinished = true;
    }


    @Override
    protected void onStart(){
        super.onStart();

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_activity_filters_venues);
    }

    @Override
    protected void onNetworkAvailable() {
        if (afterViewsFinished && networkLost) {
            runOnUiThread(this::showLoaded);
            networkLost = false;
        }
    }

    @Override
    protected void onNetworkLost() {
        if (afterViewsFinished) {
            runOnUiThread(this::showLoading);
        }
        networkLost = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.connectionStateMonitor.onAvailable = null;
        this.connectionStateMonitor.onLost = null;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(VenuesFiltersActivity.this, WallActivity_.class);

//        distanceFilterFormattedValue = formatProgressDivideBy100(this.distanceSeekBar.getProgress());
        selectedApplyWorkingStatusFilter = this.workingStatusFilter.isChecked();

        if (ConnectivityHelper.isConnected(applicationContext)) {
            if (checkFiltersStateChanged(distanceFilterFormattedValue, selectedApplyWorkingStatusFilter)) {
                intent.putExtra("ApplyDistanceFilterToVenues", distanceFilterFormattedValue);
                intent.putExtra("ApplyWorkingStatusFilterToVenues", selectedApplyWorkingStatusFilter);
                startActivity(intent);
            }
        } // In both "else"s do nothing. Simply close this activity without passing any values or initiating a "start" of the Wall activity.
    }

    private void showLoading() {
        goButton.setVisibility(View.GONE);
    }

    private void showLoaded() {
        goButton.setVisibility(View.VISIBLE);
    }

    private boolean checkFiltersStateChanged(int distanceFilterValue, boolean selectedApplyWorkingStatusFilter) {
        boolean applyWorkingStatusChanged = selectedApplyWorkingStatusFilter != PreselectedApplyWorkingStatusFilter
                || distanceFilterValue != PreselectedDistanceFilterValue;

        return applyWorkingStatusChanged;
    }

    @Click(R.id.filterVenues_GoButton)
    public void GoButton_Clicked(){
        VenuesFiltersActivity.this.finish();
    }

    @NonNull
    private String getDistanceFilterLabelText(int distanceFilterProgressMeters) {
        double distanceText = distanceFilterProgressMeters;
        String measurementTypeText = getString(R.string.activity_venuesFilters_distanceMeasureTypeMeters);
        if (distanceText >= 1000) {
            distanceText = distanceText / 1000;
            measurementTypeText = getString(R.string.activity_venuesFilters_distanceMeasureTypeKillometers);
        }
        return getResources().getString(R.string.activity_venuesFilters_distanceFilterTextView)
                + " " + String.valueOf(distanceText)
                + " " + measurementTypeText;
    }

    private int formatProgressDivideBy100(int progress) {
        progress = progress / distanceFilterStep;
        progress = progress * distanceFilterStep;
        return  progress;
    }

//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
////        mDistance.setText("" + AppConstants.getDistance() + " miles");
//        //Get the thumb bound and get its left value
//        int x = seekBar.getThumb().getBounds().left;
//        //set the left value to textview x value
////        mDistance.setX(x);
//    }

}
