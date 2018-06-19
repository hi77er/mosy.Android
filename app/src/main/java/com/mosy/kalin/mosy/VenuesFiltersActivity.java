package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.mosy.kalin.mosy.Adapters.VenueFiltersPagerAdapter;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.ListHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Responses.VenueFiltersResult;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

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

    private VenueFiltersPagerAdapter venueFiltersAdapter;

    @Bean
    VenuesService venuesService;

    @Extra
    static boolean PreselectedApplyWorkingStatusFilter;
    @Extra
    static int PreselectedDistanceFilterValue;
    @Extra
    static ArrayList<String> PreselectedVenueBadgeFilterIds;
    @Extra
    static ArrayList<String> PreselectedVenueCultureFilterIds;

    @ViewById(resName = "filtersVenues_llInitialLoadingProgress")
    LinearLayout centralProgress;

    @ViewById(resName = "tl_filters_venues")
    TabLayout venuesFiltersTabs;
    @ViewById(resName = "vp_filters_venues")
    ViewPager venuesFiltersPager;

    @ViewById(resName = "filters_venues_sbWorkingTimeFilter")
    public Switch workingStatusFilter;
    @ViewById(resName = "filters_venues_tvDistanceLabel")
    public TextView distanceLabel;
    @ViewById(resName = "filters_venues_sbDistanceFilter")
    public SeekBar distanceSeekBar;

    @ViewById(resName = "filterVenues_GoButton")
    public Button goButton;


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
            loadVenueFilters();
        }
        else {
            networkLost = true;
            showFiltersLoadingLayout();
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

        if (PreselectedVenueBadgeFilterIds == null) PreselectedVenueBadgeFilterIds = new ArrayList<>();
        if (PreselectedVenueCultureFilterIds == null) PreselectedVenueCultureFilterIds = new ArrayList<>();
    }

    @Override
    protected void onNetworkAvailable() {
        if (afterViewsFinished && networkLost) {
            runOnUiThread(this::loadVenueFilters);
            networkLost = false;
        }
    }

    @Override
    protected void onNetworkLost() {
        if (afterViewsFinished) {
            runOnUiThread(this::showFiltersLoadingLayout);
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

        if (ConnectivityHelper.isConnected(applicationContext)) {
            Intent intent = new Intent(VenuesFiltersActivity.this, WallActivity_.class);
            ArrayList<String> selectedVenueBadgeFilterIds = new ArrayList<>();
            ArrayList<String> selectedVenueCultureFilterIds = new ArrayList<>();

            selectedApplyWorkingStatusFilter = this.workingStatusFilter.isChecked();

            if (this.venueFiltersAdapter != null && this.venueFiltersAdapter.VenueBadgeFilters != null) {
                selectedVenueBadgeFilterIds = new ArrayList<>(Stream
                        .of(venueFiltersAdapter.VenueBadgeFilters)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }
            if (this.venueFiltersAdapter != null && this.venueFiltersAdapter.VenueCultureFilters != null) {
                selectedVenueCultureFilterIds = new ArrayList<>(Stream
                        .of(venueFiltersAdapter.VenueCultureFilters)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }

            boolean filtersStateChanged = checkFiltersStateChanged(distanceFilterFormattedValue,
                                                                   selectedApplyWorkingStatusFilter,
                                                                   selectedVenueBadgeFilterIds,
                                                                   selectedVenueCultureFilterIds);

            if (filtersStateChanged) {
                intent.putExtra("ApplyDistanceFilterToVenues", distanceFilterFormattedValue);
                intent.putExtra("ApplyWorkingStatusFilterToVenues", selectedApplyWorkingStatusFilter);
                intent.putExtra("SelectedVenuesBadgeFilterIds", selectedVenueBadgeFilterIds);
                intent.putExtra("SelectedVenuesCultureFilterIds", selectedVenueCultureFilterIds);
                startActivity(intent);
            }
        } // In both "else"s do nothing. Simply close this activity without passing any values or initiating a "start" of the Wall activity.
    }

    private void loadVenueFilters() {

        AsyncTaskListener<VenueFiltersResult> listener = new AsyncTaskListener<VenueFiltersResult>() {
            @Override public void onPreExecute() {
                showFiltersLoadingLayout();
            }

            @Override public void onPostExecute(VenueFiltersResult result) {
                if (result != null) {
                    populateAlreadySelectedFilters(
                            result.VenueBadgeFilters,
                            result.VenueCultureFilters);

                    venueFiltersAdapter = new VenueFiltersPagerAdapter(applicationContext,
                            getSupportFragmentManager(),
                            result.VenueBadgeFilters,
                            result.VenueCultureFilters);

                    venuesFiltersPager.setAdapter(venueFiltersAdapter);
                    venuesFiltersTabs.setupWithViewPager(venuesFiltersPager);
                }
                onFiltersLoaded();
            }
        };

        this.venuesService.getFilters(this.applicationContext, listener);
    }

    private void showFiltersLoadingLayout() {
        venuesFiltersPager.setVisibility(View.GONE);
        goButton.setVisibility(View.GONE);
        centralProgress.setVisibility(View.VISIBLE);
    }

    private void onFiltersLoaded() {
        centralProgress.setVisibility(View.GONE);
        venuesFiltersPager.setVisibility(View.VISIBLE);
        goButton.setVisibility(View.VISIBLE);
    }

    private void populateAlreadySelectedFilters(
            ArrayList<Filter> venueBadgeFilters,
            ArrayList<Filter> venueCultureFilters) {

        Stream.of(PreselectedVenueBadgeFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(venueBadgeFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedVenueCultureFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(venueCultureFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });

    }

    private boolean checkFiltersStateChanged(int distanceFilterValue,
                                             boolean selectedApplyWorkingStatusFilter,
                                             ArrayList<String> selectedVenueBadgeFilterIds,
                                             ArrayList<String> selectedVenueCultureFilterIds) {
        boolean applyWorkingStatusChanged = selectedApplyWorkingStatusFilter != PreselectedApplyWorkingStatusFilter;
        boolean searchedDistanceChanged = distanceFilterValue != PreselectedDistanceFilterValue;
        boolean dishMainIngredientFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedVenueBadgeFilterIds, selectedVenueBadgeFilterIds);
        boolean dishAllergenFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedVenueCultureFilterIds, selectedVenueCultureFilterIds);

        return applyWorkingStatusChanged || searchedDistanceChanged || dishMainIngredientFiltersChanged || dishAllergenFiltersChanged;
    }

    @NonNull
    private String getDistanceFilterLabelText(int distanceFilterProgressMeters) {
        double distanceText = distanceFilterProgressMeters;
        String measurementTypeText = getString(R.string.activity_venuesFilters_distanceMeasureTypeMeters);
        if (distanceText >= 1000) {
            distanceText = distanceText / 1000;
            measurementTypeText = getString(R.string.activity_venuesFilters_distanceMeasureTypeKilometers);
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

    @Click(R.id.filterVenues_GoButton)
    public void GoButton_Clicked(){
        VenuesFiltersActivity.this.finish();
    }

}
