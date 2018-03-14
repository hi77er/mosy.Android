package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.mosy.kalin.mosy.Helpers.StringHelper;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_filters_venues)
public class VenuesFiltersActivity
        extends AppCompatActivity {


    @Extra
    static boolean ApplyWorkingStatusFilter;

    @ViewById(resName = "filters_venues_sbWorkingTimeFilter")
    public Switch workingStatusFilter;

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
    public void InitializeComponents(){
        this.workingStatusFilter.setChecked(ApplyWorkingStatusFilter);
        this.workingStatusFilter.setOnCheckedChangeListener(
            (compoundButton, b) -> {
                ApplyWorkingStatusFilter = compoundButton.isChecked();
            }
        );

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
    protected void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(VenuesFiltersActivity.this, VenuesActivity_.class);
        intent.putExtra("ApplyWorkingStatusFilterToVenues", ApplyWorkingStatusFilter);
        startActivity(intent);
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
