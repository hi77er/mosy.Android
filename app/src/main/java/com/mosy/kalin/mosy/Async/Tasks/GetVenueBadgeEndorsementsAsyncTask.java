package com.mosy.kalin.mosy.Async.Tasks;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBadgeEndorsementsBindingModel;

import java.util.ArrayList;

public class GetVenueBadgeEndorsementsAsyncTask extends AsyncTask<GetVenueBadgeEndorsementsBindingModel, String, ArrayList<VenueBadgeEndorsement>> {

    @Override
    protected ArrayList<VenueBadgeEndorsement> doInBackground(GetVenueBadgeEndorsementsBindingModel... models) {
        GetVenueBadgeEndorsementsBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBOEndorsements/ByFBOId");
        ArrayList<VenueBadgeEndorsement> badgeEndorsements = new ArrayList<VenueBadgeEndorsement>();

        try {ContentValues params = new ContentValues();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
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
