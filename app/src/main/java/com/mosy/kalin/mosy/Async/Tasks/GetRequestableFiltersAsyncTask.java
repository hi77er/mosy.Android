package com.mosy.kalin.mosy.Async.Tasks;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GetRequestableFiltersAsyncTask extends AsyncTask<GetRequestableFiltersBindingModel, String, RequestableFiltersResponse> {

    @Override
    protected RequestableFiltersResponse doInBackground(GetRequestableFiltersBindingModel... models) {
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("RequestableFilters/All");
        RequestableFiltersResponse response = new RequestableFiltersResponse();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<RequestableFiltersResponse>(){}.getType();
            response = jsonHttpClient.Get(endpoint, null, returnType, StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            RequestableFiltersResponse errResult = new RequestableFiltersResponse ();
            errResult.ErrorMessage = e.getMessage();
            return errResult ;
        }
        return response;
    }

    @Override
    protected void onPostExecute(final RequestableFiltersResponse result) {
        super.onPostExecute(result);
    }
}