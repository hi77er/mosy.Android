package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.Fragments.FiltersPage_;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.R;

import java.util.ArrayList;

public class DishFiltersPagerAdapter
        extends FragmentStatePagerAdapter {

    public ArrayList<DishFilter> PhasesFilters;
    public ArrayList<DishFilter> RegionsFilters;
    public ArrayList<DishFilter> SpectrumFilters;
    public ArrayList<DishFilter> AllergensFilters;
    private Context context;

    public DishFiltersPagerAdapter(Context context,
                                   FragmentManager manager,
                                   ArrayList<DishFilter> phases,
                                   ArrayList<DishFilter> regions,
                                   ArrayList<DishFilter> spectrums,
                                   ArrayList<DishFilter> allergens) {
        super(manager);
        PhasesFilters = phases;
        RegionsFilters = regions;
        SpectrumFilters = spectrums;
        AllergensFilters = allergens;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        FiltersPage_ fragment = new FiltersPage_();

        Bundle bundle = new Bundle();
        ArrayList<DishFilter> filters = new ArrayList<>();
        switch (position){
            case 0:
                filters = this.PhasesFilters;
                break;
            case 1:
                filters = this.RegionsFilters;
                break;
            case 2:
                filters = this.SpectrumFilters;
                break;
            case 3:
                filters = this.AllergensFilters;
                String note = StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_allergensFiltersNote);
                fragment.setNote(note);
                break;
        }

        bundle.putParcelableArrayList("Filters", filters);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_typeFiltersTitle);
        if (position == 1)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_regionFiltersTitle);
        if (position == 2)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_ingredientsFiltersTitle);
        if (position == 3)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_allergensFiltersTitle);
        return null;
    }

}