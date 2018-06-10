package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.annimon.stream.Stream;
import com.mosy.kalin.mosy.Adapters.DishFiltersPagerAdapter;
import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.ListHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResult;
import com.mosy.kalin.mosy.Services.DishesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;


@SuppressLint("Registered")
@EActivity(R.layout.activity_filters_dishes)
public class DishesFiltersActivity
        extends BaseActivity {

    private boolean afterViewsFinished = false;
    private boolean networkLost = false;
    private boolean selectedApplyWorkingStatusFilter;
    private DishFiltersPagerAdapter DFAdapter;

    @Bean
    DishesService dishesService;

    @Extra
    static boolean PreselectedApplyWorkingStatusFilter;
    @Extra
    static ArrayList<String> PreselectedPhaseFilterIds;
    @Extra
    static ArrayList<String> PreselectedRegionFilterIds;
    @Extra
    static ArrayList<String> PreselectedSpectrumFilterIds;
    @Extra
    static ArrayList<String> PreselectedAllergensFilterIds;

    @ViewById(resName = "filters_dishes_scWorkingTimeFilter")
    public Switch workingStatusFilter;

    @ViewById(resName = "vp_filters_dishes")
    ViewPager dishesFiltersPager;
    @ViewById(resName = "venues_llInitialLoadingProgress")
    LinearLayout centralProgress;
    @ViewById(resName = "tl_filters_dishes")
    TabLayout dishesFiltersTabs;
    @ViewById(resName = "filterDishes_GoButton")
    Button goButton;

    //    @ViewById(resName = "filters_dishes_tvRatingLabel")
//    public TextView ratingLabel;
//    @ViewById(resName = "filters_dishes_sbRatingFilter")
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
                (compoundButton, b) -> {
                    selectedApplyWorkingStatusFilter =  compoundButton.isChecked();
                }
        );

        if (ConnectivityHelper.isConnected(applicationContext)) {
            networkLost = false;
            loadMenuListItemFilters();
        }
        else {
            networkLost = true;
            showFiltersLoadingLayout();
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
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_activity_filters_dishes);

        if (PreselectedPhaseFilterIds == null) PreselectedPhaseFilterIds = new ArrayList<>();
        if (PreselectedRegionFilterIds == null) PreselectedRegionFilterIds = new ArrayList<>();
        if (PreselectedSpectrumFilterIds == null) PreselectedSpectrumFilterIds = new ArrayList<>();
        if (PreselectedAllergensFilterIds == null) PreselectedAllergensFilterIds = new ArrayList<>();

    }

    @Override
    protected void onNetworkAvailable() {
        if (afterViewsFinished && networkLost) {
            runOnUiThread(this::loadMenuListItemFilters);
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
            Intent intent = new Intent(DishesFiltersActivity.this, WallActivity_.class);
            ArrayList<String> selectedPhasesFilterIds = new ArrayList<>();
            ArrayList<String> selectedRegionsFilterIds = new ArrayList<>();
            ArrayList<String> selectedSpectrumFilterIds = new ArrayList<>();
            ArrayList<String> selectedAllergensFilterIds = new ArrayList<>();

            selectedApplyWorkingStatusFilter = this.workingStatusFilter.isChecked();

            if (DFAdapter != null && DFAdapter.PhasesFilters != null) {
                selectedPhasesFilterIds = new ArrayList<>(Stream
                        .of(DFAdapter.PhasesFilters)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }
            if (DFAdapter != null && DFAdapter.RegionsFilters != null) {
                selectedRegionsFilterIds = new ArrayList<>(Stream
                        .of(DFAdapter.RegionsFilters)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }
            if (DFAdapter != null && DFAdapter.SpectrumFilters != null) {
                selectedSpectrumFilterIds = new ArrayList<>(Stream
                        .of(DFAdapter.SpectrumFilters)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }
            if (DFAdapter != null && DFAdapter.AllergensFilters != null) {
                selectedAllergensFilterIds = new ArrayList<>(Stream
                        .of(DFAdapter.AllergensFilters)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }

            boolean filtersStateChanged = checkFiltersStateChanged(
                    selectedApplyWorkingStatusFilter,
                    selectedPhasesFilterIds,
                    selectedRegionsFilterIds,
                    selectedSpectrumFilterIds,
                    selectedAllergensFilterIds);

            if (filtersStateChanged) {
                intent.putExtra("ApplyWorkingStatusFilterToDishes", selectedApplyWorkingStatusFilter);
                intent.putExtra("SelectedPhaseFilterIds", selectedPhasesFilterIds);
                intent.putExtra("SelectedRegionFilterIds", selectedRegionsFilterIds);
                intent.putExtra("SelectedSpectrumFilterIds", selectedSpectrumFilterIds);
                intent.putExtra("SelectedAllergensFilterIds", selectedAllergensFilterIds);
                startActivity(intent);
            }
            else {
                // Do nothing and.
            }
        }
        else {
            // Do nothing and.
        }
    }

    private void loadMenuListItemFilters() {

        AsyncTaskListener<RequestableFiltersResult> listener = new AsyncTaskListener<RequestableFiltersResult>() {
            @Override public void onPreExecute() {
                showFiltersLoadingLayout();
            }

            @Override public void onPostExecute(RequestableFiltersResult result) {
                if (result != null) {
                    populateAlreadySelectedFilters(
                            result.CuisinePhaseFilters,
                            result.CuisineRegionFilters,
                            result.CuisineSpectrumFilters,
                            result.CuisineAllergensFilters);

                    DFAdapter = new DishFiltersPagerAdapter(
                            getSupportFragmentManager(),
                            result.CuisinePhaseFilters,
                            result.CuisineRegionFilters,
                            result.CuisineSpectrumFilters,
                            result.CuisineAllergensFilters,
                            applicationContext);

                    dishesFiltersPager.setAdapter(DFAdapter);
                    dishesFiltersTabs.setupWithViewPager(dishesFiltersPager);
                }
                onFiltersLoaded();
            }
        };

        this.dishesService.getFilters(this.applicationContext, listener);
    }

    private void showFiltersLoadingLayout() {
        dishesFiltersPager.setVisibility(View.GONE);
        goButton.setVisibility(View.GONE);
        centralProgress.setVisibility(View.VISIBLE);
    }

    private void onFiltersLoaded() {
        centralProgress.setVisibility(View.GONE);
        dishesFiltersPager.setVisibility(View.VISIBLE);
        goButton.setVisibility(View.VISIBLE);
    }

    private void populateAlreadySelectedFilters(
            ArrayList<DishFilter> cuisinePhaseFilters,
            ArrayList<DishFilter> cuisineRegionFilters,
            ArrayList<DishFilter> cuisineSpectrumFilters,
            ArrayList<DishFilter> cuisineAllergensFilters) {

        Stream.of(PreselectedPhaseFilterIds).forEach(filterId -> {
            DishFilter matchingFilter = Stream.of(cuisinePhaseFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedRegionFilterIds).forEach(filterId -> {
            DishFilter matchingFilter = Stream.of(cuisineRegionFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedSpectrumFilterIds).forEach(filterId -> {
            DishFilter matchingFilter = Stream.of(cuisineSpectrumFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedAllergensFilterIds).forEach(filterId -> {
            DishFilter matchingFilter = Stream.of(cuisineAllergensFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });
    }

    private boolean checkFiltersStateChanged(
            boolean selectedApplyWorkingStatusFilter,
            ArrayList<String> selectedPhasesFilterIds,
            ArrayList<String> selectedRegionsFilterIds,
            ArrayList<String> selectedSpectrumFilterIds,
            ArrayList<String> selectedAllergensFilterIds) {

        boolean applyWorkingStatusChanged = selectedApplyWorkingStatusFilter != PreselectedApplyWorkingStatusFilter;
        boolean phasesChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedPhaseFilterIds, selectedPhasesFilterIds);
        boolean regionsChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedRegionFilterIds, selectedRegionsFilterIds);
        boolean spectrumChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedSpectrumFilterIds, selectedSpectrumFilterIds);
        boolean allergensChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedAllergensFilterIds, selectedAllergensFilterIds);

        return applyWorkingStatusChanged || phasesChanged || regionsChanged || spectrumChanged || allergensChanged;
    }

    @Click(R.id.filterDishes_GoButton)
    public void GoButton_Clicked(){
        DishesFiltersActivity.this.finish();
    }

}
