package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.Toast;

import com.mosy.kalin.mosy.DTOs.Requestable;
import com.mosy.kalin.mosy.Views.RequestableItemDetailsView;
import com.mosy.kalin.mosy.Views.RequestableItemDetailsView_;
import com.mosy.kalin.mosy.Views.RequestableItemView;
import com.mosy.kalin.mosy.Views.RequestableItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kkras on 7/31/2017.
 */

@EBean
public class RequestablesAdapter implements ExpandableListAdapter {

    ArrayList<Requestable> Requestables;
    public void setRequestables(ArrayList<Requestable> requestables){
        this.Requestables = requestables;
        this.setDetails(this.Requestables);
    }

    HashMap<String, Requestable> Details;
    public void setDetails(ArrayList<Requestable> requestables){
        this.Details = new HashMap<>();
        for (Requestable req: requestables)
            this.Details.put(req.Name, req);
    }

    @RootContext
    Context context;

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.Requestables.get(listPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return 1;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean b, View convertView, ViewGroup viewGroup) {
        RequestableItemDetailsView requestableItemDetailsView = null;
        if (convertView == null)
            requestableItemDetailsView = RequestableItemDetailsView_.build(context);
        else
            requestableItemDetailsView = (RequestableItemDetailsView) convertView;

        Requestable requestable = this.Requestables.get(listPosition);
        requestableItemDetailsView.bind(requestable);

        return requestableItemDetailsView;
    }

    @Override
    public int getChildrenCount(int i) {
        if (this.Requestables.get(i).Ingredients.size() < 1)
            return 0;
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.Requestables.get(listPosition).Name;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return this.Requestables.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        RequestableItemView requestableItemView = null;
        if (convertView == null)
            requestableItemView = RequestableItemView_.build(context);
        else
            requestableItemView = (RequestableItemView) convertView;

        Requestable requestable = this.Requestables.get(listPosition);
        requestableItemView.bind(requestable);

        return requestableItemView;
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