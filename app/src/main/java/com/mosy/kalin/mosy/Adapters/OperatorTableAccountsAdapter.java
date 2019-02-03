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
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.ItemViews.OperatorTableAccountItemView;
import com.mosy.kalin.mosy.ItemViews.OperatorTableAccountItemView_;
import com.mosy.kalin.mosy.Models.Views.ItemModels.OperatorTableAccountItem;
import com.mosy.kalin.mosy.Services.SignalR.SignalRService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class OperatorTableAccountsAdapter
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

    private SignalRService signalRService;
    public void setSignalRService(SignalRService signalRService) {
        this.signalRService = signalRService;
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

        return OperatorTableAccountItemView_.build(activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<WallItemViewBase> viewHolder, int position) {
        OperatorTableAccountItemView view = (OperatorTableAccountItemView)viewHolder.getView();
        OperatorTableAccountItem wallItemBase = (OperatorTableAccountItem) this.items.get(position);
        TableAccount tableAccount = wallItemBase.tableAccount;
        view.bind(tableAccount, signalRService);
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

    public OperatorTableAccountItem getItemById(String tableAccountid) {
        for (WallItemBase item : this.items) {
            OperatorTableAccountItem casted = (OperatorTableAccountItem) item;
            if (tableAccountid.equals(casted.tableAccount.Id))
                return casted;
        }
        return null;
    }

    public void addTableAccountItem(TableAccount tableAccount) {
        if (this.items != null){
            OperatorTableAccountItem item = new OperatorTableAccountItem();
            item.tableAccount = tableAccount;
            this.items.add(item);
            this.notifyDataSetChanged();
        }
    }

    public void changeItemStatus(String itemId, TableAccountStatus newStatus) {
        if (this.items != null){
            OperatorTableAccountItem item = this.getItemById(itemId);
            item.tableAccount.Status = newStatus;

            this.onItemChanged(item);
        }
    }

    public void onItemChanged(WallItemBase item) {
        int position = this.getItemPosition(item);
        activity.runOnUiThread(() -> this.notifyItemChanged(position));
    }

}