package com.mosy.kalin.mosy.Async.Tasks;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Http.HttpParams;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueByIdBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GetVenueByIdAsyncTask extends AsyncTask<GetVenueByIdBindingModel, String, Venue> {

    @Override
    protected Venue doInBackground(GetVenueByIdBindingModel... models) {
        GetVenueByIdBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBO/ById");
        Venue venue = new Venue();

        try {
            HttpParams params = new HttpParams();
            params.put("id", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<Venue>(){}.getType();
            venue = jsonHttpClient.Get(endpoint, params, returnType, "yyyy-MM-dd'T'HH:mm:ss.");
        } catch(Exception e) {
            e.printStackTrace();
            Venue errResult = new Venue();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return venue;
    }

    @Override
    protected void onPostExecute(final Venue result) {
        super.onPostExecute(result);
    }
}