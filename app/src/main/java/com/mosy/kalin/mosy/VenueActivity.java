package com.mosy.kalin.mosy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.BrochuresAdapter;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueMenuAsyncTask;
import com.mosy.kalin.mosy.DTOs.Brochure;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@EActivity(R.layout.activity_venue)
public class VenueActivity extends AppCompatActivity /*, FragmentActivity*/ {

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

    BrochuresAdapter Adapter;

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
                this.IndoorImage.setVisibility(View.VISIBLE);
            }
            else
                this.IndoorImage.setVisibility(View.GONE);

            GetVenueMenuBindingModel model = new GetVenueMenuBindingModel(this.Venue.Id);
            ArrayList<Brochure> brochures = new GetVenueMenuAsyncTask(context).execute(model).get();

            BrochuresAdapter adapter = new BrochuresAdapter(getSupportFragmentManager(), brochures);
            this.Menu.setAdapter(adapter);
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