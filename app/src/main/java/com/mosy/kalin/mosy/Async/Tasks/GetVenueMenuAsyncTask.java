package com.mosy.kalin.mosy.Async.Tasks;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by kkras on 8/10/2017.
 */

public class GetVenueMenuAsyncTask extends AsyncTask<GetVenueMenuBindingModel, String, ArrayList<MenuList>> {

    @Override
    protected ArrayList<MenuList> doInBackground(GetVenueMenuBindingModel... models) {
        GetVenueMenuBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("Brochure/ByFBOIdWithRequestablesWithIngredients");
        ArrayList<MenuList> brochuresResult = new ArrayList<MenuList>();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair param1 = new BasicNameValuePair("Id", model.VenueId);
            params.add(param1);
            brochuresResult = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<MenuList>>(){}.getType(), StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            MenuList errResult = new MenuList();
            errResult.ErrorMessage = e.getMessage();
            brochuresResult.add(errResult);
            errResult.ErrorMessage = e.getMessage();
            return brochuresResult;
        }
        return brochuresResult;
    }

    @Override
    protected void onPostExecute(final ArrayList<MenuList> result) {
        super.onPostExecute(result);
    }
}

