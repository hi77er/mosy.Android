package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.mosy.kalin.mosy.Adapters.FilterDishesPagerAdapter;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.ListHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Responses.DishFiltersResult;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FilterItem;
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
    private int distanceFilterStep = 100;
    private int distanceFilterMinValue = 100;
    private int distanceFilterMaxValue = 10000;
    private int distanceFilterFormattedValue;
    private boolean selectedApplyWorkingStatusFilter;

    private FilterDishesPagerAdapter dishFiltersAdapter;

    @Bean
    DishesService dishesService;

    @Extra
    static int PreselectedDistanceFilterValue;
    @Extra
    static boolean PreselectedApplyWorkingStatusFilter;
    @Extra
    static ArrayList<String> PreselectedDishTypeFilterIds;
    @Extra
    static ArrayList<String> PreselectedDishRegionFilterIds;
    @Extra
    static ArrayList<String> PreselectedDishMainIngredientFilterIds;
    @Extra
    static ArrayList<String> PreselectedDishAllergenFilterIds;

    @ViewById(resName = "filtersDishes_llInitialLoadingProgress")
    LinearLayout centralProgress;

    @ViewById(resName = "tl_filters_dishes")
    TabLayout dishesFiltersTabs;
    @ViewById(resName = "vp_filters_dishes")
    ViewPager dishesFiltersPager;

    @ViewById(resName = "filters_dishes_scWorkingTimeFilter")
    public Switch workingStatusFilter;
    @ViewById(resName = "filters_dishes_tvDistanceLabel")
    public TextView distanceLabel;
    @ViewById(resName = "filters_dishes_sbDistanceFilter")
    public SeekBar distanceSeekBar;


    @ViewById(resName = "filterDishes_GoButton")
    Button goButton;

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
            loadMenuListItemFilters();
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
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_activity_filters_dishes);

        if (PreselectedDishTypeFilterIds == null) PreselectedDishTypeFilterIds = new ArrayList<>();
        if (PreselectedDishRegionFilterIds == null) PreselectedDishRegionFilterIds = new ArrayList<>();
        if (PreselectedDishMainIngredientFilterIds == null) PreselectedDishMainIngredientFilterIds = new ArrayList<>();
        if (PreselectedDishAllergenFilterIds == null) PreselectedDishAllergenFilterIds = new ArrayList<>();
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
            ArrayList<String> selectedDishTypeFilterIds = new ArrayList<>();
            ArrayList<String> selectedDishRegionFilterIds = new ArrayList<>();
            ArrayList<String> selectedDishMainIngredientFilterIds = new ArrayList<>();
            ArrayList<String> selectedDishAllergenFilterIds = new ArrayList<>();

            selectedApplyWorkingStatusFilter = this.workingStatusFilter.isChecked();

            if (this.dishFiltersAdapter != null && this.dishFiltersAdapter.DishTypeFilterItems != null) {
                selectedDishTypeFilterIds = new ArrayList<>(Stream
                        .of(dishFiltersAdapter.DishTypeFilterItems)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }
            if (this.dishFiltersAdapter != null && this.dishFiltersAdapter.DishRegionFilterItems != null) {
                selectedDishRegionFilterIds = new ArrayList<>(Stream
                        .of(dishFiltersAdapter.DishRegionFilterItems)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList()
                );
            }
            if (this.dishFiltersAdapter != null && this.dishFiltersAdapter.DishMainIngredientFilterItems != null) {
                selectedDishMainIngredientFilterIds = new ArrayList<>(Stream
                        .of(dishFiltersAdapter.DishMainIngredientFilterItems)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }
            if (this.dishFiltersAdapter != null && this.dishFiltersAdapter.DishAllergenFilterItems != null) {
                selectedDishAllergenFilterIds = new ArrayList<>(Stream
                        .of(dishFiltersAdapter.DishAllergenFilterItems)
                        .filter(x -> x.IsChecked)
                        .map(x -> x.Id)
                        .toList());
            }

            boolean filtersStateChanged = checkFiltersStateChanged(distanceFilterFormattedValue,
                                                                   selectedApplyWorkingStatusFilter,
                                                                   selectedDishTypeFilterIds,
                                                                   selectedDishRegionFilterIds,
                                                                   selectedDishMainIngredientFilterIds,
                                                                   selectedDishAllergenFilterIds);

            if (filtersStateChanged) {
                intent.putExtra("ApplyDistanceFilterToDishes", distanceFilterFormattedValue);
                intent.putExtra("ApplyWorkingStatusFilterToDishes", selectedApplyWorkingStatusFilter);
                intent.putExtra("SelectedDishTypeFilterIds", selectedDishTypeFilterIds);
                intent.putExtra("SelectedDishRegionFilterIds", selectedDishRegionFilterIds);
                intent.putExtra("SelectedDishMainIngredientFilterIds", selectedDishMainIngredientFilterIds);
                intent.putExtra("SelectedDishAllergenFilterIds", selectedDishAllergenFilterIds);
                startActivity(intent);
            }
        }// In both "else"s do nothing. Simply close this activity without passing any values or initiating a "start" of the Wall activity.
    }

    private void loadMenuListItemFilters() {

        AsyncTaskListener<DishFiltersResult> listener = new AsyncTaskListener<DishFiltersResult>() {
            @Override public void onPreExecute() {
                showFiltersLoadingLayout();
            }

            @Override public void onPostExecute(DishFiltersResult result) {
                if (result != null) {
                    populateAlreadySelectedFilters(
                            result.DishTypeFilters,
                            result.DishRegionFilters,
                            result.DishMainIngredientFilters,
                            result.DishAllergenFilters);

                    ArrayList<FilterItem> dishTypeFilters = toFilterItems(result.DishTypeFilters);
                    ArrayList<FilterItem> dishRegionFilters = toFilterItems(result.DishRegionFilters);
                    ArrayList<FilterItem> dishMainIngredientFilters = toFilterItems(result.DishMainIngredientFilters);
                    ArrayList<FilterItem> dishAllergenFilters = toFilterItems(result.DishAllergenFilters);

                    dishFiltersAdapter = new FilterDishesPagerAdapter(applicationContext,
                                                                      getSupportFragmentManager(),
                                                                      dishTypeFilters,
                                                                      dishRegionFilters,
                                                                      dishMainIngredientFilters,
                                                                      dishAllergenFilters);

                    dishesFiltersPager.setAdapter(dishFiltersAdapter);
                    dishesFiltersTabs.setupWithViewPager(dishesFiltersPager);
                }
                onFiltersLoaded();
            }
        };

        this.dishesService.getFilters(this.applicationContext, listener);
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
            ArrayList<Filter> dishTypeFilters,
            ArrayList<Filter> dishRegionFilters,
            ArrayList<Filter> dishMainIngredientFilters,
            ArrayList<Filter> dishAllergenFilters) {

        Stream.of(PreselectedDishTypeFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(dishTypeFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedDishRegionFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(dishRegionFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedDishMainIngredientFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(dishMainIngredientFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });

        Stream.of(PreselectedDishAllergenFilterIds).forEach(filterId -> {
            Filter matchingFilter = Stream.of(dishAllergenFilters).filter(filter -> filter.Id.equals(filterId)).single();
            matchingFilter.IsChecked = true;
        });
    }

    private boolean checkFiltersStateChanged(int distanceFilterValue,
                                             boolean selectedApplyWorkingStatusFilter,
                                             ArrayList<String> selectedDishTypeFilterIds,
                                             ArrayList<String> selectedDishRegionFilterIds,
                                             ArrayList<String> selectedDishMainIngredientFilterIds,
                                             ArrayList<String> selectedDishAllergenFilterIds) {

        boolean searchedDistanceChanged = distanceFilterValue != PreselectedDistanceFilterValue;
        boolean applyWorkingStatusChanged = selectedApplyWorkingStatusFilter != PreselectedApplyWorkingStatusFilter;
        boolean dishTypeFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedDishTypeFilterIds, selectedDishTypeFilterIds);
        boolean dishRegionFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedDishRegionFilterIds, selectedDishRegionFilterIds);
        boolean dishMainIngredientFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedDishMainIngredientFilterIds, selectedDishMainIngredientFilterIds);
        boolean dishAllergenFiltersChanged = !ListHelper.listEqualsIgnoreOrder(PreselectedDishAllergenFilterIds, selectedDishAllergenFilterIds);

        return searchedDistanceChanged || applyWorkingStatusChanged || dishTypeFiltersChanged || dishRegionFiltersChanged || dishMainIngredientFiltersChanged || dishAllergenFiltersChanged;
    }

    @NonNull
    private String getDistanceFilterLabelText(int distanceFilterProgressMeters) {
        double distanceText = distanceFilterProgressMeters;
        String measurementTypeText = getString(R.string.activity_dishesFilters_distanceMeasureTypeMeters);
        if (distanceText >= 1000) {
            distanceText = distanceText / 1000;
            measurementTypeText = getString(R.string.activity_dishesFilters_distanceMeasureTypeKilometers);
        }
        return getResources().getString(R.string.activity_dishesFilters_distanceFilterTextView)
                + " " + String.valueOf(distanceText)
                + " " + measurementTypeText;
    }

    private int formatProgressDivideBy100(int progress) {
        progress = progress / distanceFilterStep;
        progress = progress * distanceFilterStep;
        return  progress;
    }

    @Click(R.id.filterDishes_GoButton)
    public void GoButton_Clicked(){
        DishesFiltersActivity.this.finish();
    }

}
