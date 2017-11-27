package com.mosy.kalin.mosy.Async.Tasks;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
public class SearchVenuesAsyncTask extends AsyncTask<SearchVenuesBindingModel, String, ArrayList<Venue>> {

    @Override
    protected ArrayList<Venue> doInBackground(SearchVenuesBindingModel... models) {
        SearchVenuesBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("Search/QueryClosestFBOIds");
        ArrayList<Venue> venuesResult = new ArrayList<>();

        try {
            ContentValues params = new ContentValues();
            params.put("maxResultsCount", model.MaxResultsCount);
            params.put("latitude", model.Latitude);
            params.put("longitude", model.Longitude);
            if (model.Query != StringHelper.empty()) params.put("query", model.Query);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<ArrayList<Venue>>(){}.getType();
            venuesResult = jsonHttpClient.Get(endpoint, params, returnType, "yyyy-MM-dd'T'HH:mm:ss.");
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