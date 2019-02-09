package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.mosy.kalin.mosy.CustomControls.DynamicHeightViewPager;
import com.mosy.kalin.mosy.Fragments.FiltersPage_;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FilterItem;
import com.mosy.kalin.mosy.R;

import java.util.ArrayList;

public class FilterVenuesPagerAdapter
        extends FragmentStatePagerAdapter {

    private int mCurrentPosition = -1;

    public ArrayList<FilterItem> VenueAccessibilityFilterItems;
    public ArrayList<FilterItem> VenueAvailabilityFilterItems;
    public ArrayList<FilterItem> VenueAtmosphereFilterItems;
    public ArrayList<FilterItem> VenueCultureFilterItems;
    private Context context;

    public FilterVenuesPagerAdapter(Context context,
                                    FragmentManager manager,
                                    ArrayList<FilterItem> venueAccessibilityFilterItems,
                                    ArrayList<FilterItem> venueAvailabilityFilterItems,
                                    ArrayList<FilterItem> venueAtmosphereFilterItems,
                                    ArrayList<FilterItem> venueCultureFilterItems) {
        super(manager);
        this.context = context;
        this.VenueAccessibilityFilterItems = venueAccessibilityFilterItems;
        this.VenueAvailabilityFilterItems = venueAvailabilityFilterItems;
        this.VenueAtmosphereFilterItems = venueAtmosphereFilterItems;
        this.VenueCultureFilterItems = venueCultureFilterItems;

//        this.VenueAccessibilityFilterItems = new ArrayList<>(Stream.of(venueAccessibilityFilterItems).sortBy(x -> x.OrderIndex).toList());
////        this.VenueAvailabilityFilterItems = new ArrayList<>(Stream.of(venueAvailabilityFilterItems).sortBy(x -> x.OrderIndex).toList());
////        this.VenueAtmosphereFilterItems = new ArrayList<>(Stream.of(venueAtmosphereFilterItems).sortBy(x -> x.OrderIndex).toList());
////        this.VenueCultureFilterItems = new ArrayList<>(Stream.of(venueCultureFilterItems).sortBy(x -> x.OrderIndex).toList());
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            FiltersPage_ filtersPage = (FiltersPage_) object;
            DynamicHeightViewPager pager = (DynamicHeightViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView(), filtersPage.getItemsCount(), filtersPage.getExcessHeight());
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        FiltersPage_ fragment = new FiltersPage_();

        Bundle bundle = new Bundle();
        ArrayList<FilterItem> filterItems = new ArrayList<>();
        switch (position){
            case 0:
                filterItems = this.VenueAccessibilityFilterItems;
                break;
            case 1:
                filterItems = this.VenueAvailabilityFilterItems;
                break;
            case 2:
                filterItems = this.VenueAtmosphereFilterItems;
                break;
            case 3:
                filterItems = this.VenueCultureFilterItems;
                break;
        }

        bundle.putParcelableArrayList("Filters", filterItems);
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
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_venuesFilters_accessibilityFiltersTitle);
        if (position == 1)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_venuesFilters_availabilityFiltersTitle);
        if (position == 2)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_venuesFilters_atmosphereFiltersTitle);
        if (position == 3)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_venuesFilters_cultureFiltersTitle);

        return null;
    }

}