package com.mosy.kalin.mosy.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.mosy.kalin.mosy.Adapters.Base.RecyclerViewAdapterBase;
import com.mosy.kalin.mosy.Adapters.Base.ViewWrapper;
import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.Table;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.ItemViews.TableItemView;
import com.mosy.kalin.mosy.ItemViews.TableItemView_;
import com.mosy.kalin.mosy.Models.Views.ItemModels.TableItem;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class VenueTablesAdapter
        extends RecyclerViewAdapterBase<WallItemBase, WallItemViewBase> {

    public SwipeRefreshLayout swipeContainer;
    public void setSwipeRefreshLayout(SwipeRefreshLayout layout) {
        if (layout != null) {
            this.swipeContainer = layout;
            // Configure the refreshing colors
            this.swipeContainer.setColorScheme(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            this.swipeContainer.setVisibility(View.VISIBLE);
        }
    }

    @RootContext
    Context context;

    @RootContext
    Activity activity;

    @AfterInject
    void afterInject() {
        if (this.items == null) this.items = new ArrayList<>();
        // final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // final int cacheSize = maxMemory / 8; // Use 1/8th of the available memory for this memory cache.

    }

    @Override
    protected WallItemViewBase onCreateItemView(ViewGroup parent, int viewType) {

        return TableItemView_.build(activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<WallItemViewBase> viewHolder, int position) {
        TableItemView view = (TableItemView)viewHolder.getView();
        TableItem tableItem = (TableItem) this.items.get(position);
        Table table = tableItem.table;
        view.bind(table);
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).getType();
    }

    public WallItemBase getItemAt(int position) {
        return this.items.get(position);
    }

    private int getItemPosition(WallItemBase item) {
        return this.items.indexOf(item);
    }

    public TableItem getItemById(String tableId) {
        for (WallItemBase item : this.items) {
            TableItem casted = (TableItem) item;
            if (tableId.equals(casted.table.Id))
                return casted;
        }
        return null;
    }

    public void addTableAccountItem(Table table) {
        if (this.items != null){
            TableItem item = new TableItem();
            item.table = table;
            this.items.add(item);
            this.notifyDataSetChanged();
        }
    }

    public void onItemChanged(WallItemBase item) {
        int position = this.getItemPosition(item);
        activity.runOnUiThread(() -> this.notifyItemChanged(position));
    }

}