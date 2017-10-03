package com.mosy.kalin.mosy.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.Fragments.MenuListFragment_;


import java.util.ArrayList;

public class MenuListsAdapter
        extends FragmentStatePagerAdapter {

    ArrayList<MenuList> menuLists;
    String VenueId;
    public void setVenueId(String venueId){ this.VenueId = venueId; }

    public MenuListsAdapter(FragmentManager fm, ArrayList<MenuList> menuLists) {
        super(fm);
        this.menuLists = menuLists;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new MenuListFragment_();
        MenuList menuList = this.menuLists.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("MenuListName", menuList.Name);
        bundle.putParcelableArrayList("MenuListItems", menuList.menuListItems);

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
