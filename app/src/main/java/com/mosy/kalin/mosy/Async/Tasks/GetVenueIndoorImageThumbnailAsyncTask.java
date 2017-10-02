package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageThumbnailBindingModel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by kkras on 8/9/2017.
 */

public class GetVenueIndoorImageThumbnailAsyncTask extends AsyncTask<GetVenueIndoorImageThumbnailBindingModel, String, VenueImage> {

    @Override
    protected VenueImage doInBackground(GetVenueIndoorImageThumbnailBindingModel... models) {
        GetVenueIndoorImageThumbnailBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBOFiles/IndoorThumbnailById");
        VenueImage imageResult = null;

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair param1 = new BasicNameValuePair("Id", model.VenueId);
            params.add(param1);
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
