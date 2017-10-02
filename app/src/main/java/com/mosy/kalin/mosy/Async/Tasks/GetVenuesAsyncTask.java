package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;

import java.util.ArrayList;

/**
 * Created by kkras on 8/7/2017.
 */

public class GetVenuesAsyncTask extends AsyncTask<GetVenuesBindingModel, String, ArrayList<Venue>> {

    @Override
    protected ArrayList<Venue> doInBackground(GetVenuesBindingModel... models) {
//        GetVenuesBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBO/AllBasic");
        ArrayList<Venue> venuesResult = new ArrayList<Venue>();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            venuesResult = jsonHttpClient.Get(endpoint, null, new TypeToken<ArrayList<Venue>>(){}.getType(), StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            Venue errResult = new Venue();
            errResult.ErrorMessage = e.getMessage();
            venuesResult.add(errResult);
            return venuesResult;
        }
        return venuesResult;
    }

    @Override
    protected void onPostExecute(final ArrayList<Venue> result) {
        super.onPostExecute(result);
    }
}