package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.VenuesActivity;

/**
 * Created by kkras on 7/31/2017.
 */

public class VenuesAdapter extends BaseAdapter {
    Context context;
    Venue[] venues;
    private static LayoutInflater inflater = null;

    public VenuesAdapter(Context context, Venue[] venues) {
        this.context = context;
        this.venues = venues;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return venues.length;//ListView items count.
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.activity_venue_item, null);

        TextView tvName = (TextView) vi.findViewById(R.id.tv_venueName);
        tvName.setText(venues[position].getName());

        TextView tvClass = (TextView) vi.findViewById(R.id.tv_venueClass);
        tvName.setText(venues[position].getVenueClass());

        return vi;
    }

    class ViewHolder
    {
        TextView tvVenuename;
        TextView tvVenueClass;
    }
}