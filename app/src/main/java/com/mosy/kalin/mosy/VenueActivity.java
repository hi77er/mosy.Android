package com.mosy.kalin.mosy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.MenuListsAdapter;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueMenuAsyncTask;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageThumbnailBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@EActivity(R.layout.activity_venue)
public class VenueActivity
        extends AppCompatActivity {

    @Extra
    Venue Venue;
    @ViewById(resName = "venue_tvName")
    TextView Name;
    @ViewById(resName = "venue_tvClass")
    TextView Class;
    @ViewById(resName = "venue_ivIndoor")
    ImageView IndoorImage;
    @ViewById(resName = "venue_vpMenu")
    ViewPager Menu;

    @AfterViews
    void updateVenueWithData() {
        Name.setText(this.Venue.Name);
        Class.setText(this.Venue.Class);

        GetVenueIndoorImageThumbnailBindingModel indoorImageModel = new GetVenueIndoorImageThumbnailBindingModel(this.Venue.Id);
        try {
            //INFO: TryGet Venue Image
            VenueImage result = new GetVenueIndoorImageThumbnailAsyncTask().execute(indoorImageModel).get();
            if (result.Bytes != null) {
                byte[] byteArray = Base64.decode(result.Bytes, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                this.IndoorImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
            }

            GetVenueMenuBindingModel model = new GetVenueMenuBindingModel(this.Venue.Id);
            ArrayList<MenuList> menuLists = new GetVenueMenuAsyncTask().execute(model).get();

            MenuListsAdapter adapter = new MenuListsAdapter(getSupportFragmentManager(), menuLists);
            this.Menu.setAdapter(adapter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click(R.id.venue_lVenueTitle)
    void venueTitle_Click() {
        Intent intent = new Intent(VenueActivity.this, VenueDetailsActivity_.class);
        this.Venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.Location = null;
        intent.putExtra("Venue", this.Venue);
        startActivity(intent);
        overridePendingTransition( R.transition.slide_in_up, R.transition.slide_out_up );
    }

}