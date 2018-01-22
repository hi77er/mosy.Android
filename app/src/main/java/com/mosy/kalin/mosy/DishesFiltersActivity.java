package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mosy.kalin.mosy.DTOs.CuisinePhase;
import com.mosy.kalin.mosy.DTOs.CuisineRegion;
import com.mosy.kalin.mosy.DTOs.CuisineSpectrum;
import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.DTOs.Enums.DishFilterType;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResponse;
import com.mosy.kalin.mosy.Services.FiltersService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;


@SuppressLint("Registered")
@EActivity(R.layout.activity_filters_dishes)
public class DishesFiltersActivity
        extends AppCompatActivity {

    @Extra
    static ArrayList<String> CuisinePhaseFilterIds;
    @Extra
    static ArrayList<String> CuisineRegionFilterIds;
    @Extra
    static ArrayList<String> CuisineSpectrumFilterIds;

    @ViewById(resName = "filters_dishes_scWorkingTimeFilter")
    public SwitchCompat workingTimeFilter;
    @ViewById(resName = "filters_dishes_tvRatingLabel")
    public TextView ratingLabel;
    @ViewById(resName = "filters_dishes_sbRatingFilter")
    public SeekBar ratingFilter;

    @ViewById(resName = "filters_dishes_lPhases")
    public LinearLayout PhasesFiltersLayout;
    @ViewById(resName = "filters_dishes_lRegions")
    public LinearLayout RegionsFiltersLayout;
    @ViewById(resName = "filters_dishes_lSpectrums")
    public LinearLayout SpectrumFiltersLayout;

    private RequestableFiltersResponse Filters;

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
        this.ratingFilter.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimarySalmon), PorterDuff.Mode.SRC_IN);
        this.ratingFilter.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimarySalmon), PorterDuff.Mode.SRC_IN);
        this.ratingFilter.setProgress(5);
        this.ratingFilter.setMax(10);

        this.ratingFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ratingLabel.setText("Rating: higher than " + String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        this.Filters = new FiltersService().getRequestableFilters();
        this.InitializeFiltersLayouts(this.Filters);
    }

    @Override
    protected void onStart(){
        super.onStart();
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_activity_filters_dishes);

        if (CuisinePhaseFilterIds == null) CuisinePhaseFilterIds = new ArrayList<>();
        if (CuisineRegionFilterIds == null) CuisineRegionFilterIds = new ArrayList<>();
        if (CuisineSpectrumFilterIds == null) CuisineSpectrumFilterIds = new ArrayList<>();
        this.InitializePreselectedFilters();

    }

    @Override
    protected void onStop(){
        super.onStop();


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(DishesFiltersActivity.this, VenuesActivity_.class);
        intent.putExtra("CuisinePhaseFilterIds", CuisinePhaseFilterIds);
        intent.putExtra("CuisineRegionFilterIds", CuisineRegionFilterIds);
        intent.putExtra("CuisineSpectrumFilterIds", CuisineSpectrumFilterIds);
        startActivity(intent);
    }

    private void InitializeFiltersLayouts(RequestableFiltersResponse filters) {
        if (filters != null){
            for (CuisinePhase filter : filters.CuisinePhaseFilters )
            {
                LinearLayout filterView = BuildFilterLayout(filter, DishFilterType.CuisinePhaseFilter);
                this.PhasesFiltersLayout.addView(filterView);
            }

            for (CuisineRegion filter : filters.CuisineRegionFilters )
            {
                LinearLayout filterView = BuildFilterLayout(filter, DishFilterType.CuisineRegionFilter);
                this.RegionsFiltersLayout.addView(filterView);
            }

            for (CuisineSpectrum filter : filters.CuisineSpectrumFilters )
            {
                LinearLayout filterView = BuildFilterLayout(filter, DishFilterType.CuisineSpectrumFilter);
                this.SpectrumFiltersLayout.addView(filterView);
            }
        }
    }

    private LinearLayout BuildFilterLayout(DishFilter filter, DishFilterType filterType) {
        LinearLayout filterLayout = GenerateLayout();
        ToggleButton toggleButton = GenerateFilterToggleButton(filterType, filter.Id);
        TextView textView = GenerateFilterTextView(filter.I18nResourceName);
        textView.setOnClickListener(view -> toggleButton.setChecked(!toggleButton.isChecked()) );

        filterLayout.addView(toggleButton);
        filterLayout.addView(textView);
        filterLayout.setOnClickListener(view -> toggleButton.setChecked(!toggleButton.isChecked()));

        return filterLayout;
    }

    private LinearLayout GenerateLayout(){
        LinearLayout layout = new LinearLayout(this);
        layout.setId(View.generateViewId());
        layout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int dpInPxls = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, this.getResources().getDisplayMetrics());
        params.setMargins(0, 0, 0, dpInPxls);
        layout.setLayoutParams(params);

        return layout;
    }

    private ToggleButton GenerateFilterToggleButton(DishFilterType filterType, String tag) {
        ToggleButton toggleButton = new ToggleButton(this);
        toggleButton.setId(View.generateViewId());
        toggleButton.setTag(tag);

        toggleButton.setChecked(false);
        toggleButton.setOnCheckedChangeListener((compoundButton, b) -> AddRemoveToFiltersCollection(compoundButton.isChecked(), filterType, tag));
        toggleButton.setText(StringHelper.empty());
        toggleButton.setTextOn(StringHelper.empty());
        toggleButton.setTextOff(StringHelper.empty());

        toggleButton.setBackground(getResources().getDrawable(R.drawable.toggle_selector_filter));
        final float scale = this.getResources().getDisplayMetrics().density;
        int dp40InPixels = (int) (40 * scale + 0.5f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp40InPixels, dp40InPixels);
        int dp5InPixels = (int) (5 * scale + 0.5f);
        params.setMarginEnd(dp5InPixels);
        toggleButton.setLayoutParams(params);

        return toggleButton;
    }

    private TextView GenerateFilterTextView(String i18nResourceName) {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());

        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(getString(getResources().getIdentifier(i18nResourceName, "string", this.getPackageName())));
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
        textView.setTextSize(12);
        return textView;
    }

    private void AddRemoveToFiltersCollection(boolean add, DishFilterType filterType, String filterId) {
        switch (filterType){
            case CuisinePhaseFilter:
                if (add)
                    if (!CuisinePhaseFilterIds.contains(filterId))
                        CuisinePhaseFilterIds.add(filterId);
                if (!add)
                    if (CuisinePhaseFilterIds.contains(filterId))
                        CuisinePhaseFilterIds.remove(filterId);
                break;
            case CuisineRegionFilter:
                if (add)
                    if (!CuisineRegionFilterIds.contains(filterId))
                        CuisineRegionFilterIds.add(filterId);
                if (!add)
                    if (CuisineRegionFilterIds.contains(filterId))
                        CuisineRegionFilterIds.remove(filterId);
                break;
            case CuisineSpectrumFilter:
                if (add)
                    if (!CuisineSpectrumFilterIds.contains(filterId))
                        CuisineSpectrumFilterIds.add(filterId);
                if (!add)
                    if (CuisineSpectrumFilterIds.contains(filterId))
                        CuisineSpectrumFilterIds.remove(filterId);
                break;
        }
    }

    private void InitializePreselectedFilters() {
        for (String filter : CuisinePhaseFilterIds)
        {
            ToggleButton toggleButton = this.PhasesFiltersLayout.findViewWithTag(filter);
            if (toggleButton != null)
                toggleButton.setChecked(true);
        }
        for (String filter : CuisineRegionFilterIds)
        {
            ToggleButton toggleButton = this.RegionsFiltersLayout.findViewWithTag(filter);
            if (toggleButton != null)
                toggleButton.setChecked(true);
        }
        for (String filter : CuisineSpectrumFilterIds)
        {
            ToggleButton toggleButton = this.SpectrumFiltersLayout.findViewWithTag(filter);
            if (toggleButton != null)
                toggleButton.setChecked(true);
        }
    }

}
