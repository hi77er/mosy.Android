package com.mosy.kalin.mosy.Async.Tasks;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageMetaBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageThumbnailBindingModel;

public class GetVenueIndoorImageMetaAsyncTask extends AsyncTask<GetVenueIndoorImageMetaBindingModel, String, VenueImage> {

    @Override
    protected VenueImage doInBackground(GetVenueIndoorImageMetaBindingModel... models) {
        GetVenueIndoorImageMetaBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBOFiles/IndoorMetaByFBOId");
        VenueImage imageResult;

        try {
            ContentValues params = new ContentValues();
            params.put("Id", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            imageResult = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueImage>(){}.getType(), StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            VenueImage errResult = new VenueImage();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return imageResult;
    }

    @Override
    protected void onPostExecute(final VenueImage result) {
        super.onPostExecute(result);
    }
}