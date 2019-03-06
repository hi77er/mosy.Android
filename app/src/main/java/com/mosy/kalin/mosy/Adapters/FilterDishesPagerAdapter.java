package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.mosy.kalin.mosy.CustomControls.DynamicHeightViewPager;
import com.mosy.kalin.mosy.Fragments.FiltersPage_;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FilterItem;
import com.mosy.kalin.mosy.R;

import java.util.ArrayList;

public class FilterDishesPagerAdapter
        extends FragmentStatePagerAdapter {

    private int mCurrentPosition = -1;

    public ArrayList<FilterItem> DishTypeFilterItems;
    public ArrayList<FilterItem> DishRegionFilterItems;
    public ArrayList<FilterItem> DishMainIngredientFilterItems;
    public ArrayList<FilterItem> DishAllergenFilterItems;
    private Context context;

    public FilterDishesPagerAdapter(Context context,
                                    FragmentManager manager,
                                    ArrayList<FilterItem> dishTypeFilterItems,
                                    ArrayList<FilterItem> dishRegionFilterItems,
                                    ArrayList<FilterItem> dishMainIngredientFilterItems,
                                    ArrayList<FilterItem> dishAllergenFilterItems) {
        super(manager);
        this.context = context;
        this.DishTypeFilterItems = dishTypeFilterItems;
        this.DishRegionFilterItems = dishRegionFilterItems;
        this.DishMainIngredientFilterItems = dishMainIngredientFilterItems;
        this.DishAllergenFilterItems = dishAllergenFilterItems;
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
                filterItems = this.DishTypeFilterItems;
                break;
            case 1:
                filterItems = this.DishMainIngredientFilterItems;
                break;
            case 2:
                filterItems = this.DishRegionFilterItems;
                break;
            case 3:
                filterItems = this.DishAllergenFilterItems;
                String note = StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_allergensFiltersNote);
                fragment.setNote(note);
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
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_typeFiltersTitle);
        if (position == 1)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_ingredientsFiltersTitle);
        if (position == 2)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_regionFiltersTitle);
        if (position == 3)
            return StringHelper.getStringAppDefaultLocale(context, R.string.activity_dishesFilters_allergensFiltersTitle);
        return null;
    }

}