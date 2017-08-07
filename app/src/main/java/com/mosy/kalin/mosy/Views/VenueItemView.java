package com.mosy.kalin.mosy.Views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by kkras on 8/3/2017.
 */

@EViewGroup(R.layout.activity_venue_item)
public class VenueItemView extends LinearLayout {
    @ViewById
    TextView tv_venueName;

    @ViewById
    TextView tv_venueClass;

    public VenueItemView(Context context) {
        super(context);
    }

    public void bind(Venue venue) {
        tv_venueName.setText(venue.getName());
        tv_venueClass.setText(venue.getVenueClass());
    }
}
