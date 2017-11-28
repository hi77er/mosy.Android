package com.mosy.kalin.mosy.Async.Tasks;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class SearchMenuListItemsAsyncTask extends AsyncTask<SearchMenuListItemsBindingModel, String, ArrayList<MenuListItem>> {

    @Override
    protected ArrayList<MenuListItem> doInBackground(SearchMenuListItemsBindingModel... models) {
        SearchMenuListItemsBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("Requestable/QueryClosestRequestables");
        ArrayList<MenuListItem> venuesResult = new ArrayList<>();

        try {
            ContentValues params = new ContentValues();
            params.put("maxResultsCount", model.MaxResultsCount);
            params.put("latitude", model.Latitude);
            params.put("longitude", model.Longitude);
            if (!model.Query.equals(StringHelper.empty())) params.put("query", model.Query);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<ArrayList<MenuListItem>>(){}.getType();
            venuesResult = jsonHttpClient.Get(endpoint, params, returnType, StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return venuesResult;
    }

    @Override
    protected void onPostExecute(final ArrayList<MenuListItem> result) {
        super.onPostExecute(result);
    }
}