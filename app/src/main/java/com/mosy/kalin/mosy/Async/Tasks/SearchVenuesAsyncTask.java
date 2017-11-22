package com.mosy.kalin.mosy.Async.Tasks;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import java.util.ArrayList;
public class SearchVenuesAsyncTask extends AsyncTask<SearchVenuesBindingModel, String, ArrayList<Venue>> {

    @Override
    protected ArrayList<Venue> doInBackground(SearchVenuesBindingModel... models) {
        SearchVenuesBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("Search/QueryFBOs");
        ArrayList<Venue> venuesResult = new ArrayList<>();

        try {
            ContentValues params = new ContentValues();
            params.put("query", model.Query);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            venuesResult = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<Venue>>(){}.getType(), StringHelper.empty());
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