package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueLocationBindingModel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by kkras on 8/9/2017.
 */

public class GetVenueLocationAsyncTask extends AsyncTask<GetVenueLocationBindingModel, String, VenueLocation> {

    @Override
    protected VenueLocation doInBackground(GetVenueLocationBindingModel... models) {
        GetVenueLocationBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBOLocation/ByFBOId");
        VenueLocation location = null;

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair param1 = new BasicNameValuePair("fboId", model.VenueId);
            params.add(param1);
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
