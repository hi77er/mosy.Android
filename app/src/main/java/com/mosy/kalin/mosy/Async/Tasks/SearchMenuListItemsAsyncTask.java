package com.mosy.kalin.mosy.Async.Tasks;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.HttpParams;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchMenuListItemsAsyncTask extends AsyncTask<SearchMenuListItemsBindingModel, String, ArrayList<MenuListItem>> {

    @Override
    protected ArrayList<MenuListItem> doInBackground(SearchMenuListItemsBindingModel... models) {
        SearchMenuListItemsBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("Requestable/QueryClosestRequestables");
        ArrayList<MenuListItem> venuesResult = new ArrayList<>();
        try {
            Calendar localCalendar = Calendar.getInstance();
            String localTimeParam = localCalendar.get(Calendar.HOUR_OF_DAY) + ":" + localCalendar.get(Calendar.MINUTE);
            HttpParams params = new HttpParams();
            params.put("maxResultsCount", String.valueOf(model.MaxResultsCount));
            params.put("totalItemsOffset", String.valueOf(model.TotalItemsOffset));
            params.put("latitude", String.valueOf(model.Latitude));
            params.put("longitude", String.valueOf(model.Longitude));
            params.put("localDayOfWeek", String.valueOf(localCalendar.get(Calendar.DAY_OF_WEEK)));
            params.put("localTime", String.valueOf(localTimeParam));

            if (model.IsPromoted != null) params.put("isPromoted", String.valueOf(model.IsPromoted));
            if (!model.Query.equals(StringHelper.empty())) params.put("query", model.Query);

            if (model.SelectedCuisinePhaseIds != null)
                for (String filter : model.SelectedCuisinePhaseIds)
                    params.put("cuisinePhaseIds", filter);

            if (model.SelectedCuisineRegionIds != null)
                for (String filter : model.SelectedCuisineRegionIds)
                    params.put("cuisineRegionIds", filter);

            if (model.SelectedCuisineSpectrumIds != null)
                for (String filter : model.SelectedCuisineSpectrumIds)
                    params.put("cuisineSpectrumIds", filter);

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