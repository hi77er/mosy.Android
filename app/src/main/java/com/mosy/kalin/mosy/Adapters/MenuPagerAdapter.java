package com.mosy.kalin.mosy.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.Fragments.MenuListPage_;


import java.util.ArrayList;

public class MenuPagerAdapter
        extends FragmentStatePagerAdapter {

    private ArrayList<MenuList> menuLists;

    private String venueId;
    public void setVenueId(String venueId){ this.venueId = venueId; }

    private String selectedMenuListId;

    private boolean venueHasOrdersManagementSubscription;
    public void setVenueHasOrdersManagementSubscription(boolean value){ this.venueHasOrdersManagementSubscription = value; }


    public MenuPagerAdapter(FragmentManager fm, ArrayList<MenuList> menuLists, String menuListId) {
        super(fm);
        this.menuLists = menuLists;
        this.selectedMenuListId = menuListId;
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return this.menuLists.get(position).Name;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new MenuListPage_();
        MenuList menuList = this.menuLists.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("MenuListName", menuList.Name);
        bundle.putParcelableArrayList("wallMenuListItems", menuList.wallMenuListItems);
        bundle.putBoolean("venueHasOrdersManagementSubscription", venueHasOrdersManagementSubscription);

        if (menuList.Id.equals(selectedMenuListId)) {
            if (!selectedMenuListId.equals(""))
                bundle.putBoolean("IsSelected", true);
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        if (this.menuLists != null)
            return this.menuLists.size();
        return 0;
    }
}
