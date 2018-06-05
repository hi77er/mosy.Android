package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;

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
    private boolean selectedApplyWorkingStatusFilter;

    @Extra
    static boolean PreselectedApplyWorkingStatusFilter;

    @ViewById(resName = "filters_venues_sbWorkingTimeFilter")
    public Switch workingStatusFilter;
    @ViewById(resName = "filterVenues_GoButton")
    public Button goButton;


//    @ViewById(resName = "filters_venues_tvRatingLabel")
//    public TextView ratingLabel;
//    @ViewById(resName = "filters_venues_sbRatingFilter")
//    public SeekBar ratingFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels*.9), (int)(dm.heightPixels*.7));

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }

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

        afterViewsFinished = true;

//        this.ratingFilter.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimarySalmon), PorterDuff.Mode.SRC_IN);
//        this.ratingFilter.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimarySalmon), PorterDuff.Mode.SRC_IN);
//        this.ratingFilter.setProgress(5);
//        this.ratingFilter.setMax(10);
//        this.ratingFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                ratingLabel.setText("Rating: higher than " + String.valueOf(progress));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) { }
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) { }
//        });
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
        Intent intent = new Intent(VenuesFiltersActivity.this, VenuesActivity_.class);

        selectedApplyWorkingStatusFilter = this.workingStatusFilter.isChecked();
        if (ConnectivityHelper.isConnected(applicationContext)) {
            if (checkFiltersStateChanged(selectedApplyWorkingStatusFilter)) {
                intent.putExtra("ApplyWorkingStatusFilterToVenues", selectedApplyWorkingStatusFilter);
                startActivity(intent);
            }
            else{
                // Do nothing. Simply close this activity.
            }
        }
        else {
            // Do nothing. Simply close this activity.
        }
    }

    private void showLoading() {
        goButton.setVisibility(View.GONE);
    }

    private void showLoaded() {
        goButton.setVisibility(View.VISIBLE);
    }

    private boolean checkFiltersStateChanged(boolean selectedApplyWorkingStatusFilter) {
        boolean applyWorkingStatusChanged = selectedApplyWorkingStatusFilter != PreselectedApplyWorkingStatusFilter;

        return applyWorkingStatusChanged;
    }

    @Click(R.id.filterVenues_GoButton)
    public void GoButton_Clicked(){
        VenuesFiltersActivity.this.finish();
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
