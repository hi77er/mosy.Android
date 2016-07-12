package com.mosy.kalin.mosy;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.Interfaces.IModelHelper;
import com.mosy.kalin.mosy.Models.Interfaces.IServiceEndpointFactory;
import com.mosy.kalin.mosy.Models.ModelHelper;
import com.mosy.kalin.mosy.Models.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Models.VenueModel;
import com.mosy.kalin.mosy.databinding.VenueBinding;

import cz.msebera.android.httpclient.Header;

public class VenueActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VenueBinding binding = DataBindingUtil.setContentView(this, R.layout.venue);

        IModelHelper helper = new ModelHelper();
        IServiceEndpointFactory factory = new ServiceEndpointFactory();
        VenueModel model = new VenueModel(helper, factory);

        //Venue venue = model.GetById("8B539D36-17F0-43F5-9B3E-E090213B746F");

        Venue venue = new Venue();
        venue.setName("Venue Name");
        venue.setTeamEmail("team@email.com");
        venue.setClass("Bar");

        binding.setVenue(venue);
    }
}
