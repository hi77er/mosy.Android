package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Requestable;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueOutdoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;
import com.mosy.kalin.mosy.Views.RequestableItemView;
import com.mosy.kalin.mosy.Views.RequestableItemView_;
import com.mosy.kalin.mosy.Views.VenueItemView;
import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by kkras on 7/31/2017.
 */

@EBean
public class RequestablesAdapter extends BaseAdapter {

    ArrayList<Requestable> Requestables;
    public void setRequestables(ArrayList<Requestable> requestables){
        this.Requestables = requestables;
    }

    @RootContext
    Context context;

//    @AfterInject
//    void initAdapter() {
//
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestableItemView requestableItemView = null;
        if (convertView == null)
            requestableItemView = RequestableItemView_.build(context);
        else
            requestableItemView = (RequestableItemView) convertView;

        Requestable requestable = getItem(position);
        requestableItemView.bind(requestable);

        return requestableItemView;
    }

    @Override
    public int getCount() {
        if (this.Requestables != null)
            return this.Requestables.size();
        else return 0;
    }

    @Override
    public Requestable getItem(int position) {
        return (Requestable) this.Requestables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}