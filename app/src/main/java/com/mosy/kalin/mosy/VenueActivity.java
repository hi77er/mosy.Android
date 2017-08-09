package com.mosy.kalin.mosy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Results.VenueImageResult;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.ExecutionException;

@EActivity(R.layout.activity_venue)
public class VenueActivity extends AppCompatActivity {

    @ViewById(resName = "venue_tvName")
    TextView Name;

    @ViewById(resName = "venue_tvClass")
    TextView Class;

    @ViewById(resName = "venue_ivIndoor")
    ImageView IndoorImage;

    Venue Venue;

    @AfterViews
    void updateVenueWithData() {
        Name.setText(this.Venue.Name);
        Class.setText(this.Venue.Class);

        GetVenueIndoorImageBindingModel model = new GetVenueIndoorImageBindingModel(this.Venue.Id);
        Context context = getApplicationContext();
        try {
            VenueImageResult result = new GetVenueIndoorImageAsyncTask(context).execute(model).get();
            //byte[] asdasd = result.Bytes.getBytes()
            byte[] byteArray = Base64.decode(result.Bytes, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            this.IndoorImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
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