package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.annimon.stream.Stream;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.Fragments.FiltersPage_;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FilterItem;
import com.mosy.kalin.mosy.R;

import java.nio.file.DirectoryStream;
import java.util.ArrayList;

public class FilterVenuesPagerAdapter
        extends FragmentStatePagerAdapter {

    public ArrayList<FilterItem> VenueBadgeFilterItems;
    public ArrayList<FilterItem> VenueCultureFilterItems;
    private Context context;

    public FilterVenuesPagerAdapter(Context context,
                                    FragmentManager manager,
                                    ArrayList<FilterItem> venueBadgeFilterItems,
                                    ArrayList<FilterItem> venueCultureFilterItems) {
        super(manager);
        this.context = context;
        this.VenueBadgeFilterItems = venueBadgeFilterItems;
        this.VenueCultureFilterItems = venueCultureFilterItems;
    }

    @Override
    public Fragment getItem(int position) {
        FiltersPage_ fragment = new FiltersPage_();

        Bundle bundle = new Bundle();
        ArrayList<FilterItem> filterItems = new ArrayList<>();
        switch (position){
            case 0:
                filterItems = this.VenueBadgeFilterItems;
                break;
            case 1:
                filterItems = this.VenueCultureFilterItems;
                break;
        }

        bundle.putParcelableArrayList("Filters", filterItems);
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