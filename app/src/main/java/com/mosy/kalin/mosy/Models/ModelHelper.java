package com.mosy.kalin.mosy.Models;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.mosy.kalin.mosy.Models.Interfaces.IModelHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by User on 7/7/2016.
 * For setting up a working Debug Web Service Client:
 * 1. Publish the service ot IIS or other web server;
 * 2. Set permissions to Web Service deployment folder;
 * 3. Set permissions in the SQL server if the services are getting or putting data to one;
 * 4. Use 10.0.2(3).2 instead of localhost or 127.0.0.1 for a root Url;
 */
public class ModelHelper implements IModelHelper {

    GsonBuilder GsonBuilder = new GsonBuilder();
    Gson Gson = GsonBuilder.create();

    JSONObject jsonResponse = null;

    @Override
    public JSONObject Get(String rootUrl, String action, Map<String, String> params) {
        AsyncHttpClient client = AsyncHttpClientFactory();
        RequestParams requestParams = new RequestParams(params);
        String serviceUrl = rootUrl + (rootUrl.endsWith("/") ? "" : "/") + action + "/";

        client.get(serviceUrl,requestParams ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, JSONObject response) {
              try {
                  if(response.getBoolean("status")){
                      jsonResponse = response;
                  }
              } catch (JSONException ex) {

              }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                jsonResponse = error;
            }
        });
        return jsonResponse;
    }

    @Override
    public JSONObject Post(String rootUrl, String action,  Map<String, String> params) {
//        AsyncHttpClient client = AsyncHttpClientFactory();
//        RequestParams requestParams = new RequestParams(params);
//        String serviceUrl = rootUrl + action;
//
//        client.post(serviceUrl,requestParams ,new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    JSONObject obj = new JSONObject(response);
//                    if(obj.getBoolean("status")){
//                        jsonResponse = response;
//                    }
//                    else{
//                        jsonResponse = obj.getString("error_msg");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Throwable error,String content) {
//                jsonResponse = error.getMessage();
//            }
//        });
        return jsonResponse;
    }

    @Override
    public <T> T Deserialize(JSONObject json, Class<T> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("MM/dd/yy HH:mm:ss");

        Gson gson = builder.create();
        return gson.fromJson(json.toString(), clazz);
    }

    AsyncHttpClient AsyncHttpClientFactory(){
        return new AsyncHttpClient();
    }

}
