package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mosy.kalin.mosy.DTOs.AuthenticationResult;
import com.mosy.kalin.mosy.DTOs.JSONHttpClient;
import com.mosy.kalin.mosy.DTOs.TokenModel;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;
import com.mosy.kalin.mosy.Models.ServiceEndpointFactory;
import com.mosy.kalin.mosy.VenueActivity;

/**
 * Created by kkras on 7/25/2017.
 */

public class RegisterAsyncTask extends AsyncTask<RegisterBindingModel, String, AuthenticationResult> {
    private Context context;

    public RegisterAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected AuthenticationResult doInBackground(RegisterBindingModel... models) {
        String registerEndpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("Register");
        RegisterBindingModel model = models[0];
        JSONHttpClient jsonHttpClient = new JSONHttpClient();

        String result = jsonHttpClient.PostObject(registerEndpoint, model, String.class);

        if (StringHelper.isNullOrWhitespace(result)) {
            String object = String.format("grant_type=password&username=%s&password=%s", model.getEmail(), model.getPassword());
            String tokenEndpoint = new ServiceEndpointFactory().getMosyWebAPITokenDevEndpoint();
            TokenModel tokenModel = jsonHttpClient.PostObject(tokenEndpoint, object, TokenModel.class);
            if (tokenModel != null && tokenModel.AccessToken != null) {
                return new AuthenticationResult(true, tokenModel.AccessToken, null);
            } else
                return new AuthenticationResult(false, null, "Get Token failed");
        } else
            return new AuthenticationResult(false, null, result);
    }

    @Override
    protected void onPostExecute(final AuthenticationResult result) {
        if (!result.isSuccessful())
            Toast.makeText(this.context,
                    "Error.",
                    Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this.context,
                    "Register successfully. Your access token is: " + result.getAccessToken(),
                    Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }
    }
}
