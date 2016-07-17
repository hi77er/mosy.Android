package com.mosy.kalin.mosy.Models;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.Interfaces.IServiceEndpointFactory;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kkras on 16.7.2016 г..
 */
public class HttpGetAsyncTask extends AsyncTask<String, Void, JSONObject> {
    private IServiceEndpointFactory theFactory = null;
    private JSONObject json = null;

    public HttpGetAsyncTask(IServiceEndpointFactory factory){
        theFactory = factory;
    }


    @Override
    protected JSONObject doInBackground(String[] serviceUrl) {
        AsyncHttpClient client = new AsyncHttpClient();
        String endpoint = theFactory.getMosyEndpoint("fbo") + "/Get/";
        RequestParams params = new RequestParams();
        params.put("Id", serviceUrl[0]);

        try {
            client.get(endpoint, params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    try {
                        json = response;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }


}
