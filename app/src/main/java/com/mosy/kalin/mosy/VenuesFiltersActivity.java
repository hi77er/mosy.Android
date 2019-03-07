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
import com.mosy.kalin.mosy.Adapters.FilterVenuesPagerAdapter;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.ListHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Responses.VenueFiltersHttpResult;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FilterItem;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
    private boolean stateHasBeenReset;

    private FilterVenuesPagerAdapter venueFiltersAdapter;

    @Bean
    VenuesService venuesService;

    @Extra
    static boolean PreselectedApplyWorkingStatusFilter;
    @Extra
    static int PreselectedDistanceFilterValue;
    @Extra
    static ArrayList<String> PreselectedVenueAccessibilityFilterIds;
    @Extra
    static ArrayList<String> PreselectedVenueAvailabilityFilterIds;
    @Extra
    static ArrayList<String> PreselectedVenueAtmosphereFilterIds;
    @Extra
    static ArrayList<String> PreselectedVenueCultureFilterIds;

    @ViewById(R.id.filtersVenues_llInitialLoadingProgress)
    LinearLayout centralProgress;

    @ViewById(R.id.tl_filters_venues)
    TabLayout venuesFiltersTabs;
    @ViewById(R.id.vp_filters_venues)
    ViewPager venuesFiltersPager;

    @ViewById(R.id.filters_venues_sbWorkingTimeFilter)
    public Switch workingStatusFilter;
    @ViewById(R.id.filters_venues_tvDistanceLabel)
    public TextView distanceLabel;
    @ViewById(R.id.filters_venues_sbDistanceFilter)
    public SeekBar distanceSeekBar;
    @ViewById(R.id.filters_venues_tvClosedLabel)
    public TextView closedLabel;

    @ViewById(R.id.filterVenues_GoButton)
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
        distanceFilterMaxValue = this.isDevelopersModeActivated ? 10000000 : 10000;

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

        this.setWorkingStatusLabels();

        afterViewsFinished = true;
    }

    @Override
    protected void onStart(){
        super.onStart();
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_activity_filters_venues);

        if (PreselectedVenueAccessibilityFilterIds == null) PreselectedVenueAccessibilityFilterIds = new ArrayList<>();
        if (PreselectedVenueAvailabilityFilterIds == null) PreselectedVenueAvailabilityFilterIds = new ArrayList<>();
        if (PreselectedVenueAtmosphereFilterIds == null) PreselectedVenueAtmosphereFilterIds = new ArrayList<>();
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
            ArrayList<String> selectedVenueAccessibilityFilterIds = new ArrayList<>();
            ArrayList<String> selectedVenueAvailabilityFilterIds = new ArrayList<>();
            ArrayList<String> selectedVenueAtmosphereFilterIds = new ArrayList<>();
            ArrayList<String> selectedVenueCultureFilterIds = new ArrayList<>();

            selectedApplyWorkingStatusFilter = this.workingStatusFilter.isChecked();

            if (this.venueFiltersAdapter != null && this.venueFiltersAdapter.VenueAccessibilityFilterItems != null) {
                selectedVenueAccessibilityFilterIds =  new ArrayList<>(Stream
                        .of(venueFiltersAdapter.VenueAccessibilityFilterItems)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }
            if (this.venueFiltersAdapter != null && this.venueFiltersAdapter.VenueAccessibilityFilterItems != null) {
                selectedVenueAvailabilityFilterIds =  new ArrayList<>(Stream
                        .of(venueFiltersAdapter.VenueAvailabilityFilterItems)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }
            if (this.venueFiltersAdapter != null && this.venueFiltersAdapter.VenueAccessibilityFilterItems != null) {
                selectedVenueAtmosphereFilterIds =  new ArrayList<>(Stream
                        .of(venueFiltersAdapter.VenueAtmosphereFilterItems)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }
            if (this.venueFiltersAdapter != null && this.venueFiltersAdapter.VenueCultureFilterItems != null) {
                selectedVenueCultureFilterIds = new ArrayList<>(Stream
                        .of(venueFiltersAdapter.VenueCultureFilterItems)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }

            boolean filtersStateChanged = checkFiltersStateChanged(distanceFilterFormattedValue,
                                                                   selectedApplyWorkingStatusFilter,
                                                                    selectedVenueAccessibilityFilterIds,
                                                                    selectedVenueAvailabilityFilterIds,
                                                                   selectedVenueAtmosphereFilterIds,
                                                                   selectedVenueCultureFilterIds);

            if (filtersStateChanged) {
                intent.putExtra("ApplyDistanceFilterToVenues", distanceFilterFormattedValue);
                intent.putExtra("ApplyWorkingStatusFilterToVenues", selectedApplyWorkingStatusFilter);
                intent.putExtra("SelectedVenueAccessibilityFilterIds", selectedVenueAccessibilityFilterIds);
                intent.putExtra("SelectedVenueAvailabilityFilterIds", selectedVenueAvailabilityFilterIds);
                intent.putExtra("SelectedVenueAtmosphereFilterIds", selectedVenueAtmosphereFilterIds);
                intent.putExtra("SelectedVenueCultureFilterIds", selectedVenueCultureFilterIds);
                intent.putExtra("NavigatedFromFilters", true);
                startActivity(intent);
            }
        } // In both "else"s do nothing. Simply close this activity without passing any values or initiating a "start" of the Wall activity.
    }

    private void loadVenueFilters() {

        AsyncTaskListener<VenueFiltersHttpResult> listener = new AsyncTaskListener<VenueFiltersHttpResult>() {
            @Override public void onPreExecute() {
                showFiltersLoadingLayout();
            }

            @Override public void onPostExecute(VenueFiltersHttpResult result) {
                if (result != null) {
                    populateAlreadySelectedFilters(
                            result.VenueAccessibilityFilters,
                            result.VenueAvailabilityFilters,
                            result.VenueAtmosphereFilters,
                            result.VenueCultureFilters);

                    ArrayList<FilterItem> accessibilityFilterItems = toFilterItems(result.VenueAccessibilityFilters);
                    ArrayList<FilterItem> availabilityFilterItems = toFilterItems(result.VenueAvailabilityFilters);
                    ArrayList<FilterItem> atmosphereFilterItems = toFilterItems(result.VenueAtmosphereFilters);
                    ArrayList<FilterItem> cultureFilterItems = toFilterItems(result.VenueCultureFilters);

                    venueFiltersAdapter = new FilterVenuesPagerAdapter(applicationContext, getSupportFragmentManager(),
                            accessibilityFilterItems, availabilityFilterItems, atmosphereFilterItems, cultureFilterItems);

                    venuesFiltersPager.setAdapter(venueFiltersAdapter);
                    venuesFiltersTabs.setupWithViewPager(venuesFiltersPager);
                }
                onFiltersLoaded();
            }
        };

        this.venuesService.getFilters(this.applicationContext, listener, this.isDevelopersModeActivated);
    }

    private ArrayList<FilterItem> toFilterItems(ArrayList<Filter> filters) {
        ArrayList<FilterItem> items = new ArrayList<>();
        if (filters != null) {
            for (Filter filter : filters) {
                FilterItem item = new FilterItem();
                item.Id = filter.Id;
                item.Name = filter.Name;
                item.Description = filter.Description;
                item.I18nResourceName = filter.I18nResourceName;
                item.I18nResourceDescription = filter.I18nResourceDescription;
                item.FilteredType = filter.FilteredType;
                item.FilterType = filter.FilterType;
                item.IsChecked = filter.IsChecked;
                item.Icon = filter.Icon;
                items.add(item);
            }
        }
        return items;
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
            ArrayList<Filter> venueAccessibilityFilters,
            ArrayList<Filter> venueAvailabilityFilters,
            ArrayList<Filter> venueAtmosphereFilters,
            ArrayList<Filter> venueCultureFilters) {

        Stream.of(PreselectedVenueAccessibilityFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(venueAccessibilityFilters).filter(filter -> filter.Id.equals(filterId)).findFirst().orElse(null);
            //preselectedFilters might deffer depending on whether devMode enabled.
            if (matchingFilter != null) matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedVenueAvailabilityFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(venueAvailabilityFilters).filter(filter -> filter.Id.equals(filterId)).findFirst().orElse(null);
            //preselectedFilters might deffer depending on whether devMode enabled.
            if (matchingFilter != null) matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedVenueAtmosphereFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(venueAtmosphereFilters).filter(filter -> filter.Id.equals(filterId)).findFirst().orElse(null);
            //preselectedFilters might deffer depending on whether devMode enabled.
            if (matchingFilter != null) matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedVenueCultureFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(venueCultureFilters).filter(filter -> filter.Id.equals(filterId)).findFirst().orElse(null);
            //preselectedFilters might deffer depending on whether devMode enabled.
            if (matchingFilter != null) matchingFilter.IsChecked = true;
        });

    }

    private boolean checkFiltersStateChanged(int distanceFilterValue,
                                             boolean selectedApplyWorkingStatusFilter,
                                             ArrayList<String> selectedVenueAccessibilityFilterIds,
                                             ArrayList<String> selectedVenueAvailabilityFilterIds,
                                             ArrayList<String> selectedVenueAtmosphereFilterIds,
                                             ArrayList<String> selectedVenueCultureFilterIds) {
        boolean applyWorkingStatusChanged = selectedApplyWorkingStatusFilter != PreselectedApplyWorkingStatusFilter;
        boolean searchedDistanceChanged = distanceFilterValue != PreselectedDistanceFilterValue;
        boolean venueAccessibilityFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedVenueAccessibilityFilterIds, selectedVenueAccessibilityFilterIds);
        boolean venueAvailabilityFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedVenueAvailabilityFilterIds, selectedVenueAvailabilityFilterIds);
        boolean venueAtmosphereFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedVenueAtmosphereFilterIds, selectedVenueAtmosphereFilterIds);
        boolean venueCultureFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedVenueCultureFilterIds, selectedVenueCultureFilterIds);

        return this.stateHasBeenReset ||
                applyWorkingStatusChanged ||
                searchedDistanceChanged ||
                venueAccessibilityFiltersChanged ||
                venueAvailabilityFiltersChanged ||
                venueAtmosphereFiltersChanged ||
                venueCultureFiltersChanged;
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

    private void setWorkingStatusLabels() {
        this.closedLabel.setVisibility(this.workingStatusFilter.isChecked() ? GONE : VISIBLE);
    }

    @Click(R.id.filterVenues_ResetFiltersButton)
    public void resetButton_Clicked(){
        PreselectedVenueAccessibilityFilterIds = new ArrayList<>();
        PreselectedVenueAtmosphereFilterIds = new ArrayList<>();
        PreselectedVenueAvailabilityFilterIds = new ArrayList<>();
        PreselectedVenueCultureFilterIds = new ArrayList<>();
        PreselectedDistanceFilterValue = DEFAULT_MINIMAL_DISTANCE_FILTER_METERS;
        PreselectedApplyWorkingStatusFilter = DEFAULT_APPLY_WORKING_STATUS_FILTER;
        this.workingStatusFilter.setChecked(DEFAULT_APPLY_WORKING_STATUS_FILTER);
        this.setProgress(DEFAULT_MINIMAL_DISTANCE_FILTER_METERS);
        this.stateHasBeenReset = true;

        afterViews();
    }

    @Click(R.id.filters_venues_sbWorkingTimeFilter)
    public void workingStatusSwitch_Clicked(){
        this.closedLabel.setVisibility(this.workingStatusFilter.isChecked() ? GONE : VISIBLE);
    }

    @Click(R.id.filterVenues_GoButton)
    public void goButton_Clicked(){
        VenuesFiltersActivity.this.finish();
    }

}
