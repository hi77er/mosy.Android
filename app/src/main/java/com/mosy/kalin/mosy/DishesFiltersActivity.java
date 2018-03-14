package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mosy.kalin.mosy.Adapters.DishFiltersPagerAdapter;
import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.DTOs.Enums.DishFilterType;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResponse;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadMenuListFiltersAsyncTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SuppressLint("Registered")
@EActivity(R.layout.activity_filters_dishes)
public class DishesFiltersActivity
        extends AppCompatActivity {

    private DishFiltersPagerAdapter DFAdapter;

    @Extra
    static boolean ApplyWorkingStatusFilter;
    @Extra
    static ArrayList<String> SelectedPhaseFilterIds;
    @Extra
    static ArrayList<String> SelectedRegionFilterIds;
    @Extra
    static ArrayList<String> SelectedSpectrumFilterIds;

    @ViewById(resName = "venues_llInitialLoadingProgress")
    LinearLayout centralProgress;

    @ViewById(resName = "vp_filters_dishes")
    ViewPager DishesFiltersPager;
    @ViewById(resName = "tl_filters_dishes")
    TabLayout DishesFiltersTabs;

    @ViewById(resName = "filters_dishes_scWorkingTimeFilter")
    public Switch workingStatusFilter;
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
    public void InitializeComponents(){
        this.workingStatusFilter.setChecked(ApplyWorkingStatusFilter);
        this.workingStatusFilter.setOnCheckedChangeListener(
                (compoundButton, b) -> {
                    ApplyWorkingStatusFilter = compoundButton.isChecked();
                }
        );

        AsyncTaskListener<RequestableFiltersResponse> listener = new AsyncTaskListener<RequestableFiltersResponse>() {
            @Override
            public void onPreExecute() {
                centralProgress.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPostExecute(RequestableFiltersResponse result) {
                RequestableFiltersResponse filters = result;

                DFAdapter = new DishFiltersPagerAdapter(
                        getSupportFragmentManager(),
                        filters.CuisinePhaseFilters,
                        filters.CuisineRegionFilters,
                        filters.CuisineSpectrumFilters);

                DishesFiltersPager.setAdapter(DFAdapter);
                DishesFiltersTabs.setupWithViewPager(DishesFiltersPager);

                centralProgress.setVisibility(View.GONE);
            }
        };
        new LoadMenuListFiltersAsyncTask(listener).execute(new GetRequestableFiltersBindingModel());




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

        if (SelectedPhaseFilterIds == null) SelectedPhaseFilterIds = new ArrayList<>();
        if (SelectedRegionFilterIds == null) SelectedRegionFilterIds = new ArrayList<>();
        if (SelectedSpectrumFilterIds == null) SelectedSpectrumFilterIds = new ArrayList<>();

    }

    @Override
    protected void onStop(){
        super.onStop();


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(DishesFiltersActivity.this, VenuesActivity_.class);
        intent.putExtra("ApplyWorkingStatusFilterToDishes", ApplyWorkingStatusFilter);
//        intent.putExtra("CuisinePhaseFilterIds", CuisinePhaseFilterIds);
//        intent.putExtra("CuisineRegionFilterIds", CuisineRegionFilterIds);
//        intent.putExtra("CuisineSpectrumFilterIds", CuisineSpectrumFilterIds);

        List<String> selectedPhasesFilterIds = Stream
                .of(DFAdapter.PhasesFilters)
                .filter(x -> x.IsChecked)
                .map(x -> x.Id)
                .toList();
        List<String> selectedRegionsFilterIds = Stream
                .of(DFAdapter.RegionsFilters)
                .filter(x -> x.IsChecked)
                .map(x -> x.Id)
                .toList();
        List<String> selectedSpectrumFilterIds = Stream
                .of(DFAdapter.SpectrumFilters)
                .filter(x -> x.IsChecked)
                .map(x -> x.Id)
                .toList();

        intent.putExtra("SelectedPhaseFilterIds", new ArrayList<>(selectedPhasesFilterIds));
        intent.putExtra("SelectedRegionFilterIds", new ArrayList<>(selectedRegionsFilterIds));
        intent.putExtra("SelectedSpectrumFilterIds", new ArrayList<>(selectedSpectrumFilterIds));

        startActivity(intent);
    }

}
