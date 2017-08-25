package com.mosy.kalin.mosy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.BrochuresAdapter;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueMenuAsyncTask;
import com.mosy.kalin.mosy.DTOs.Brochure;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


@EActivity(R.layout.activity_venue_details)
public class VenueDetailsActivity extends AppCompatActivity {

    @Extra
    public Venue Venue;
    @ViewById(resName = "venueDetails_tvName")
    TextView Name;
    @ViewById(resName = "venueDetails_tvClass")
    TextView Class;
    @ViewById(resName = "venueDetails_ivIndoor")
    ImageView IndoorImage;

    @AfterViews
    void updateVenueWithData() {
        Context context = getApplicationContext();

        Name.setText(this.Venue.Name);
        Class.setText(this.Venue.Class);

        GetVenueIndoorImageBindingModel indorImageModel = new GetVenueIndoorImageBindingModel(this.Venue.Id);
        try {
            //INFO: TryGet Venue Image
            VenueImage result = new GetVenueIndoorImageAsyncTask(context).execute(indorImageModel).get();
            if (result.Bytes != null) {
                byte[] byteArray = Base64.decode(result.Bytes, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                this.IndoorImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

}
