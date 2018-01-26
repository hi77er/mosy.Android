package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Views.MenuListItemDetailsView;
import com.mosy.kalin.mosy.Views.MenuListItemDetailsView_;
import com.mosy.kalin.mosy.Views.MenuListItemView;
import com.mosy.kalin.mosy.Views.MenuListItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@EBean
public class MenuListItemsAdapter
        implements ExpandableListAdapter {

    ArrayList<MenuListItem> menuListItems;
    public void setMenuListItems(ArrayList<MenuListItem> menuListItems){
        this.menuListItems = menuListItems;
        this.setDetails(this.menuListItems);
    }


    HashMap<String, MenuListItem> Details;
    public void setDetails(ArrayList<MenuListItem> menuListItems){
        this.Details = new HashMap<>();
        for (MenuListItem req: menuListItems)
            this.Details.put(req.Name, req);
    }

    @RootContext
    Context context;

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.menuListItems.get(listPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return 1;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean b, View convertView, ViewGroup viewGroup) {
        MenuListItemDetailsView menuListItemDetailsView = null;
        if (convertView == null)
            menuListItemDetailsView = MenuListItemDetailsView_.build(context);
        else
            menuListItemDetailsView = (MenuListItemDetailsView) convertView;

        MenuListItem menuListItem = this.menuListItems.get(listPosition);
        menuListItemDetailsView.bind(menuListItem);

        return menuListItemDetailsView;
    }

    @Override
    public int getChildrenCount(int i) {
        if (this.menuListItems.get(i).Ingredients.size() < 1)
            return 0;
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.menuListItems.get(listPosition).Name;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return this.menuListItems.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        MenuListItemView menuListItemView = null;
        if (convertView == null)
            menuListItemView = MenuListItemView_.build(context);
        else
            menuListItemView = (MenuListItemView) convertView;

        MenuListItem menuListItem = this.menuListItems.get(listPosition);

        String listItemName = menuListItem.getName();
        menuListItemView.bind(menuListItem);

        return menuListItemView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) { }
    @Override
    public void onGroupCollapsed(int i) { }
    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }
    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
}