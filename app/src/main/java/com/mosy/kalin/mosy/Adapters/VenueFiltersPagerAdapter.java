package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.Fragments.FiltersPage_;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.R;

import java.util.ArrayList;

public class VenueFiltersPagerAdapter
        extends FragmentStatePagerAdapter {

    public ArrayList<Filter> VenueBadgeFilters;
    public ArrayList<Filter> VenueCultureFilters;
    private Context context;

    public VenueFiltersPagerAdapter(Context context,
                                    FragmentManager manager,
                                    ArrayList<Filter> venueBadgeFilters,
                                    ArrayList<Filter> venueCultureFilters) {
        super(manager);
        this.context = context;
        this.VenueBadgeFilters = venueBadgeFilters;
        this.VenueCultureFilters = venueCultureFilters;
    }

    @Override
    public Fragment getItem(int position) {
        FiltersPage_ fragment = new FiltersPage_();

        Bundle bundle = new Bundle();
        ArrayList<Filter> filters = new ArrayList<>();
        switch (position){
            case 0:
                filters = this.VenueBadgeFilters;
                break;
            case 1:
                filters = this.VenueCultureFilters;
                break;
        }

        bundle.putParcelableArrayList("Filters", filters);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_venuesFilters_badgesFiltersTitle);
        if (position == 1)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_venuesFilters_cultureFiltersTitle);

        return null;
    }

}