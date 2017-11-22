package com.mosy.kalin.mosy.Async.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Http.JSONHttpClient;
import com.mosy.kalin.mosy.DTOs.Results.RegisterResult;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;

public class RegisterAsyncTask extends AsyncTask<RegisterBindingModel, String, RegisterResult> {
    private Context context;

    public RegisterAsyncTask(Context context) { this.context = context; }

    @Override
    protected RegisterResult doInBackground(RegisterBindingModel... models) {
        String registerEndpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint("Account/Register");
        RegisterBindingModel model = models[0];
        JSONHttpClient jsonHttpClient = new JSONHttpClient();

        try {
            String result = jsonHttpClient.PostObject(registerEndpoint, model, String.class, StringHelper.empty());
            return new RegisterResult(true, result);
        }
        catch(Exception e)
        {
            return new RegisterResult(false, e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(final RegisterResult result) {
        if (!result.isSuccessful())
            Toast.makeText(this.context,
                    "Register unsuccessful.",
                    Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this.context,
                    "Register successfully.",
                    Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }
    }
}
