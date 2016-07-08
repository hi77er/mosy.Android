package com.mosy.kalin.mosy;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class VenueActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue);
    }

    public void testMosyWS(View view){

        RequestParams params = new RequestParams();
        params.put("Id", "8B539D36-17F0-43F5-9B3E-E090213B746F");
        invokeMosyWS(params);

    }

    public void invokeMosyWS(RequestParams params) {
// Show Progress Dialog
//        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.1.14/api/fbo/Get/",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
//                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject json = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    //JSONObject json2 = json.getJSONObject("fbo");

                    if (json != null)
                    {
//                        venue.Name = (String) venue.get("Name");
//                        Toast.makeText(getApplicationContext(), "Venue name is: " + fboName, Toast.LENGTH_LONG).show();
                    }
                    else{
//                        errorMsg.setText(json.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), json.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), e.getMessage() + " JSON response might be invalid!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }


            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
//                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 400){
                    Toast.makeText(getApplicationContext(), "Sorry! Bad request..", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Status code: " + statusCode + "." + error.getMessage(), Toast.LENGTH_LONG).show();
                    // + " Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]"

                }
            }
        });

    }
}
