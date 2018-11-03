package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.ItemViews.FilterItemView;
import com.mosy.kalin.mosy.ItemViews.FilterItemView_;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FilterItem;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class FilterItemsListAdapter
        extends BaseAdapter {

    private List<FilterItem> Filters;
    public void setFilters(ArrayList<FilterItem> filters){
        this.Filters = filters;
    }

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        if (this.Filters == null) this.Filters = new ArrayList<>();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        FilterItemView filterItemView = null;
        if (view == null)
            filterItemView = FilterItemView_.build(context);
        else
            filterItemView = (FilterItemView) view;
        FilterItem filter = this.Filters.get(position);
        filterItemView.bind(filter);

        return filterItemView;
    }

    @Override
    public int getCount() {
        return this.Filters.size();
    }
    @Override
    public FilterItem getItem(int position) {
        return this.Filters.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


}