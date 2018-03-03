package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.AccountRepository;
import com.mosy.kalin.mosy.DTOs.Enums.AuthenticationResultStatus;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;

public class AccountTokenLoginAsyncTask extends AsyncTask<LoginBindingModel, String, AuthenticationResultStatus> {

    private final AsyncTaskListener<AuthenticationResultStatus> asyncTaskListenerListener;

    public AccountTokenLoginAsyncTask(AsyncTaskListener<AuthenticationResultStatus> listener) {
        this.asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected AuthenticationResultStatus doInBackground(LoginBindingModel... models) {
        AuthenticationResultStatus results = null;

        try {
            LoginBindingModel model = models[0];
            results = new AccountRepository().tokenLogin(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final AuthenticationResultStatus result) {
        asyncTaskListenerListener.onPostExecute(result);
    }

}
