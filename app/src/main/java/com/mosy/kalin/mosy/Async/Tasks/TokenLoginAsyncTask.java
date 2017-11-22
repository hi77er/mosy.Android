package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mosy.kalin.mosy.DTOs.Enums.AuthenticationResultStatus;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.DTOs.Results.TokenResult;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;

public class TokenLoginAsyncTask extends AsyncTask<LoginBindingModel, String, AuthenticationResultStatus> {
    private Context context;

    public TokenLoginAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected AuthenticationResultStatus doInBackground(LoginBindingModel... models) {
        LoginBindingModel model = models[0];
        String tokenEndpoint = new ServiceEndpointFactory().getMosyWebAPIDevTokenEndpoint();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            TokenResult tokenResult = jsonHttpClient.GetToken(tokenEndpoint, model.getEmail(), model.getPassword());

            //TODO: Handle the case when no Internet connection
            //TODO: Handle the case when Server does not respond
            switch (tokenResult.Status){
                case Unknown:
                    return AuthenticationResultStatus.Unknown;
                case Unauthorized:
                    return AuthenticationResultStatus.Unauthorized;
                case Success:
                    return AuthenticationResultStatus.Authorized;
                case Fail:
                    return AuthenticationResultStatus.Failed;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return AuthenticationResultStatus.Failed;
        }
        return AuthenticationResultStatus.Failed;
    }

    @Override
    protected void onPostExecute(final AuthenticationResultStatus result) {
        //TODO: Handle the case when no Internet connection
        //TODO: Handle the case when Server does not respond
        if (result == AuthenticationResultStatus.Failed ||
                result == AuthenticationResultStatus.Unknown ||
                result == AuthenticationResultStatus.ServerDoesNotRespond)
            Toast.makeText(this.context, "Please try later. We are currently experiencing some troubles to connect you.", Toast.LENGTH_SHORT).show();
        else if (result == AuthenticationResultStatus.Unauthorized)
            Toast.makeText(this.context, "Wrong username or password.", Toast.LENGTH_SHORT).show();
        else if (result == AuthenticationResultStatus.Authorized)
            Toast.makeText(this.context, "Login successful.", Toast.LENGTH_SHORT).show();

        super.onPostExecute(result);
    }
}
