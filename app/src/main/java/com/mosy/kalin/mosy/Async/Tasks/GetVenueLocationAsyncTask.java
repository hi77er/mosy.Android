package com.mosy.kalin.mosy.Async.Tasks;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.HttpParams;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueLocationBindingModel;

public class GetVenueLocationAsyncTask extends AsyncTask<GetVenueLocationBindingModel, String, VenueLocation> {

    @Override
    protected VenueLocation doInBackground(GetVenueLocationBindingModel... models) {
        GetVenueLocationBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBOLocation/ByFBOId");
        VenueLocation location;

        try {
            HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            location = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueLocation>(){}.getType(), StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            VenueLocation errResult = new VenueLocation();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return location;
    }

    @Override
    protected void onPostExecute(final VenueLocation result) {
        super.onPostExecute(result);
    }
}
