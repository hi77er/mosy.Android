package com.mosy.kalin.mosy.Async.Tasks;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;

public class GetVenueBusinessHoursAsyncTask extends AsyncTask<GetVenueBusinessHoursBindingModel, String, VenueBusinessHours> {

    @Override
    protected VenueBusinessHours doInBackground(GetVenueBusinessHoursBindingModel... models) {
        GetVenueBusinessHoursBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBOBusinessHours/ByFBOId");
        VenueBusinessHours businessHours = null;

        try {
            ContentValues params = new ContentValues();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            businessHours = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueBusinessHours>(){}.getType(), "HH:mm:ss");
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
