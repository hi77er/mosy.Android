package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.MenuListsAdapter;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueMenuAsyncTask;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;
import com.mosy.kalin.mosy.Services.AzureBlobService;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@SuppressLint("Registered")
@EActivity(R.layout.activity_venue)
public class VenueActivity extends AppCompatActivity {

    @Extra
    Venue Venue;

    @Extra
    String SelectedMenuListId; //if the page is navigated via Dishes ListView, this should have value

    @ViewById(resName = "venue_tvName")
    TextView Name;
    @ViewById(resName = "venue_tvClass")
    TextView Class;
    @ViewById(resName = "venue_ivIndoor")
    ImageView IndoorImage;
    @ViewById(resName = "venue_vpMenu")
    ViewPager Menu;

    ArrayList<MenuList> menuLists = null;

    @AfterViews
    void updateVenueWithData() {
        try {
            Name.setText(this.Venue.Name);
            Class.setText(this.Venue.Class);

            this.Venue.IndoorImage = new VenuesService().downloadVenueIndoorImageMeta(this.Venue.Id);

            if (this.Venue != null && this.Venue.IndoorImage != null && this.Venue.IndoorImage.Id != null && this.Venue.IndoorImage.Id.length() > 0) {
                //INFO: TryGet Venue Image
                byte[] indoorImageBytes = new AzureBlobService().GetBlob(this.Venue.IndoorImage.Id, "userimages\\fboalbums\\200x200");
                if (indoorImageBytes != null && indoorImageBytes.length > 0) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(indoorImageBytes, 0, indoorImageBytes.length);
                    this.IndoorImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                }
            }
            GetVenueMenuBindingModel model = new GetVenueMenuBindingModel(this.Venue.Id);
            ArrayList<MenuList> menuLists = new GetVenueMenuAsyncTask().execute(model).get();

            MenuListsAdapter adapter = new MenuListsAdapter(getSupportFragmentManager(), menuLists, SelectedMenuListId);
            this.Menu.setAdapter(adapter);

            if (SelectedMenuListId != null && !SelectedMenuListId.equals(StringHelper.empty())) {
                int selectedMenuListIndex = 0;
                for (MenuList list : menuLists) {
                    if (this.SelectedMenuListId.equals(list.Id))
                        selectedMenuListIndex = menuLists.indexOf(list);
                }
                this.Menu.setCurrentItem(selectedMenuListIndex, false);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Click(R.id.venue_lVenueTitle)
    void venueTitle_Click() {
        Intent intent = new Intent(VenueActivity.this, VenueDetailsActivity_.class);
        this.Venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.Location = null;
        intent.putExtra("Venue", this.Venue);
        startActivity(intent);
        overridePendingTransition( R.transition.slide_in_right, R.transition.slide_out_right );
    }

}