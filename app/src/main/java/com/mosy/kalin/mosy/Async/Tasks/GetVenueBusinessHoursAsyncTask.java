package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by kkras on 8/9/2017.
 */

public class GetVenueBusinessHoursAsyncTask extends AsyncTask<GetVenueBusinessHoursBindingModel, String, VenueBusinessHours> {

    private Context context;

    public GetVenueBusinessHoursAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected VenueBusinessHours doInBackground(GetVenueBusinessHoursBindingModel... models) {
        GetVenueBusinessHoursBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBOBusinessHours/ByFBOId");
        VenueBusinessHours businessHours = null;

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair param1 = new BasicNameValuePair("fboId", model.VenueId);
            params.add(param1);
            businessHours = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueBusinessHours>(){}.getType());
        } catch(Exception e) {
            e.printStackTrace();
            VenueBusinessHours errResult = new VenueBusinessHours();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return businessHours;
    }

    @Override
    protected void onPostExecute(final VenueBusinessHours result) {
        super.onPostExecute(result);
    }
}
