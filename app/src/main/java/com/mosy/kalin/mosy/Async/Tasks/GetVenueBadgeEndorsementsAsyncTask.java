package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBadgeEndorsementsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by kkras on 8/9/2017.
 */

public class GetVenueBadgeEndorsementsAsyncTask extends AsyncTask<GetVenueBadgeEndorsementsBindingModel, String, ArrayList<VenueBadgeEndorsement>> {

    @Override
    protected ArrayList<VenueBadgeEndorsement> doInBackground(GetVenueBadgeEndorsementsBindingModel... models) {
        GetVenueBadgeEndorsementsBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBOEndorsements/ByFBOId");
        ArrayList<VenueBadgeEndorsement> badgeEndorsements = new ArrayList<VenueBadgeEndorsement>();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair param1 = new BasicNameValuePair("fboId", model.VenueId);
            params.add(param1);
            badgeEndorsements = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<VenueBadgeEndorsement>>(){}.getType(), StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            VenueBadgeEndorsement errResult = new VenueBadgeEndorsement();
            errResult.ErrorMessage = e.getMessage();
            badgeEndorsements.add(errResult);
            return badgeEndorsements;
        }
        return badgeEndorsements;
    }

    @Override
    protected void onPostExecute(final ArrayList<VenueBadgeEndorsement> result) {
        super.onPostExecute(result);
    }
}
