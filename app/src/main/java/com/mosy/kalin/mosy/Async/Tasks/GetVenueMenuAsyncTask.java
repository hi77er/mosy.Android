package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Brochure;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by kkras on 8/10/2017.
 */

public class GetVenueMenuAsyncTask extends AsyncTask<GetVenueMenuBindingModel, String, ArrayList<Brochure>> {

    private Context context;

    public GetVenueMenuAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Brochure> doInBackground(GetVenueMenuBindingModel... models) {
        GetVenueMenuBindingModel model = models[0];
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("Brochure/ByFBOId");
        ArrayList<Brochure> brochuresResult = new ArrayList<Brochure>();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair param1 = new BasicNameValuePair("Id", model.VenueId);
            params.add(param1);
            brochuresResult = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<Brochure>>(){}.getType());
        } catch(Exception e) {
            e.printStackTrace();
            Brochure errResult = new Brochure();
            errResult.ErrorMessage = e.getMessage();
            brochuresResult.add(errResult);
            errResult.ErrorMessage = e.getMessage();
            return brochuresResult;
        }
        return brochuresResult;
    }

    @Override
    protected void onPostExecute(final ArrayList<Brochure> result) {
        super.onPostExecute(result);
    }
}

