package com.mosy.kalin.mosy.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by kkras on 8/3/2017.
 */

@EViewGroup(R.layout.activity_item_venue)
public class VenueItemView extends RelativeLayout {

    @ViewById(resName = "venueItem_tvName")
    TextView Name;
    @ViewById(resName = "venueItem_tvClass")
    TextView Class;
    @ViewById(resName = "venueItem_tvDistance")
    TextView DistanceFromDevice;
    @ViewById(resName = "venueItem_ivOutdoor")
    ImageView OutdoorImage;

    public VenueItemView(Context context) {
        super(context);
    }

    public void bind(Venue venue) {
        this.Name.setText(venue.Name);
        this.Class.setText(venue.Class);
        if (venue.Location != null) {
            String distance = LocationHelper.buildDistanceText(venue.Location.DistanceToCurrentLocationMeters);
            String timeWalking = LocationHelper.buildMinutesWalkingText(venue.Location.DistanceToCurrentLocationMeters);
            String text = distance + (timeWalking.length() > 0 ? " | " + timeWalking : StringHelper.empty());
            this.DistanceFromDevice.setText(text);
            // String timeText = LocationHelper.buildDistanceText(venue.Location.DistanceToCurrentLocationMeters);
            // this.WalkingTime.setText(timeText);
        }
        if (venue.OutdoorImage != null && venue.OutdoorImage.Bytes != null){
            byte[] byteArray = Base64.decode(venue.OutdoorImage.Bytes, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            this.OutdoorImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
        }
        else
            this.OutdoorImage.setImageResource(R.drawable.ic_venue_default_thumbnail);
    }

}
