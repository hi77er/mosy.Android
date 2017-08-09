package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Enums.AuthenticationResultStatus;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkras on 8/7/2017.
 */

public class GetVenuesAsyncTask extends AsyncTask<GetVenuesBindingModel, String, ArrayList<Venue>> {
    private Context context;

    public GetVenuesAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Venue> doInBackground(GetVenuesBindingModel... models) {
//        GetVenuesBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("FBO/AllBasic");
        ArrayList<Venue> venuesResult; // = new ArrayList<Venue>();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            venuesResult = jsonHttpClient.Get(endpoint, null, new TypeToken<ArrayList<Venue>>(){}.getType());
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return venuesResult;
    }

    @Override
    protected void onPostExecute(final ArrayList<Venue> result) {
        super.onPostExecute(result);
    }
}