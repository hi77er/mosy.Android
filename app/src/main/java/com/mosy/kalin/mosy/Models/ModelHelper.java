package com.mosy.kalin.mosy.Models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mosy.kalin.mosy.Models.Inderfaces.IModelHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by User on 7/7/2016.
 */
public class ModelHelper implements IModelHelper {

    GsonBuilder GsonBuilder = new GsonBuilder();
    Gson Gson = GsonBuilder.create();
    String jsonResponse = "";

    @Override
    public String Get(String rootUrl, String action, Map<String, String> params) {
        AsyncHttpClient client = AsyncHttpClientFactory();
        RequestParams requestParams = new RequestParams();

        if (!params.isEmpty()){
            Set keys = params.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                String value = (String) params.get(key);
                requestParams.put(key, value);
            }
        }
        String serviceUrl = rootUrl + action;

        client.get(serviceUrl,requestParams ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("status")){
                        jsonResponse = response;
                    }
                    else{
                        jsonResponse = obj.getString("error_msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content) {
                jsonResponse = error.getMessage();
            }
        });

        return jsonResponse;
    }

    @Override
    public String Post(String rootUrl, String action, Object Content) {
        //add headers
//        for(NameValuePair h : headers)
//        {
//            request.addHeader(h.getName(), h.getValue());
//        }
//
//        if(!params.isEmpty()){
//            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//        }
//
//        executeRequest(request, url);
        return "";
    }

    @Override
    public <T> void Deserialize(String jsonString) {

    }

    AsyncHttpClient AsyncHttpClientFactory(){
        return new AsyncHttpClient();
    }

}
