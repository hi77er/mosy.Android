package com.mosy.kalin.mosy.Fragments;

import android.support.v4.app.Fragment;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.MenuListItemsAdapter;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_menulist)
public class MenuListFragment extends Fragment {

    @FragmentArg("MenuListName")
    String MenuListName;

    @FragmentArg("MenuListItems")
    ArrayList<MenuListItem> menuListItems;

    @FragmentArg("SelectedMenuListItemId")
    String selectedMenuListItemId;

    @FragmentArg("IsSelected")
    Boolean isSelected;

    @ViewById(R.id.menuList_lvItems)
    ExpandableListView menuList_lvItems;

    @Bean
    MenuListItemsAdapter menuListItemsAdapter;

    @AfterViews
    void init() {

        if (this.menuListItems != null) {
            menuListItemsAdapter.setMenuListItems(this.menuListItems);

            menuList_lvItems.setAdapter(menuListItemsAdapter);
        }
    }

    public MenuListFragment() { }

}
