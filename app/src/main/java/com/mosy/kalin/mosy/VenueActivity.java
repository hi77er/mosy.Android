package com.mosy.kalin.mosy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.MenuAdapter;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueMenuAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Brochure;
import com.mosy.kalin.mosy.DTOs.Results.VenueImageResult;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@EActivity(R.layout.activity_venue)
public class VenueActivity extends AppCompatActivity /*, FragmentActivity*/ {

    Venue Venue;

    @ViewById(resName = "venue_tvName")
    TextView Name;

    @ViewById(resName = "venue_tvClass")
    TextView Class;

    @ViewById(resName = "venue_ivIndoor")
    ImageView IndoorImage;

    @ViewById(resName = "venue_vpMenu")
    ViewPager Menu;

    MenuAdapter Adapter;

    @AfterViews
    void updateVenueWithData() {
        Context context = getApplicationContext();

        Name.setText(this.Venue.Name);
        Class.setText(this.Venue.Class);

        GetVenueIndoorImageBindingModel indorImageModel = new GetVenueIndoorImageBindingModel(this.Venue.Id);
        try {
            //INFO: TryGet Venue Image
//            VenueImageResult result = new GetVenueIndoorImageAsyncTask(context).execute(indorImageModel).get();
//            if (result.Bytes != null) {
//                byte[] byteArray = Base64.decode(result.Bytes, Base64.DEFAULT);
//                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//                this.IndoorImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
//                this.IndoorImage.setVisibility(View.VISIBLE);
//            }
//            else
//                this.IndoorImage.setVisibility(View.GONE);

            GetVenueMenuBindingModel model = new GetVenueMenuBindingModel(this.Venue.Id);
            ArrayList<Brochure> brochures = new GetVenueMenuAsyncTask(context).execute(model).get();
            MenuAdapter adapter = new MenuAdapter(getSupportFragmentManager(), brochures);
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
        this.Venue = (Venue)getIntent().getSerializableExtra("Venue");
    }

}