package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by kkras on 8/7/2017.
 */

public class SearchVenuesAsyncTask extends AsyncTask<SearchVenuesBindingModel, String, ArrayList<Venue>> {
    private Context context;

    public SearchVenuesAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Venue> doInBackground(SearchVenuesBindingModel... models) {
        SearchVenuesBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("Search/QueryFBOs");
        ArrayList<Venue> venuesResult = new ArrayList<Venue>();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair param1 = new BasicNameValuePair("query", model.Query);
            params.add(param1);
            venuesResult = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<Venue>>(){}.getType());
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