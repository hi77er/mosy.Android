package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Results.VenueImageResult;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by kkras on 8/9/2017.
 */

public class GetVenueIndoorImageAsyncTask extends AsyncTask<GetVenueIndoorImageBindingModel, String, VenueImageResult> {

    private Context context;

    public GetVenueIndoorImageAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected VenueImageResult doInBackground(GetVenueIndoorImageBindingModel... models) {
        GetVenueIndoorImageBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBOFiles/IndoorById");
        VenueImageResult imageResult = null;

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair param1 = new BasicNameValuePair("Id", model.VenueId);
            params.add(param1);
            imageResult = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueImageResult>(){}.getType());
        } catch(Exception e) {
            e.printStackTrace();
            VenueImageResult errResult = new VenueImageResult();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return imageResult;
    }

    @Override
    protected void onPostExecute(final VenueImageResult result) {
        super.onPostExecute(result);
    }
}
