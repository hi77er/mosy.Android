package com.mosy.kalin.mosy.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.Fragments.FiltersPage_;

import java.util.ArrayList;

public class DishFiltersPagerAdapter
        extends FragmentStatePagerAdapter {

    public ArrayList<DishFilter> PhasesFilters;
    public ArrayList<DishFilter> RegionsFilters;
    public ArrayList<DishFilter> SpectrumFilters;
    public ArrayList<DishFilter> AllergensFilters;

    public DishFiltersPagerAdapter(FragmentManager manager,
                                   ArrayList<DishFilter> phases,
                                   ArrayList<DishFilter> regions,
                                   ArrayList<DishFilter> spectrums,
                                   ArrayList<DishFilter> allergens) {
        super(manager);
        PhasesFilters = phases;
        RegionsFilters = regions;
        SpectrumFilters = spectrums;
        AllergensFilters = allergens;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FiltersPage_();

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
            return "Phases";
        if (position == 1)
            return "Regions";
        if (position == 2)
            return "Spectrums";
        if (position == 3)
            return "Allergens";
        return null;
    }

}