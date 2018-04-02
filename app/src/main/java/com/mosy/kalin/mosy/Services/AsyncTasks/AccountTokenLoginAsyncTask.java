package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.AccountRepository;
import com.mosy.kalin.mosy.DTOs.Enums.AuthenticationResultStatus;
import com.mosy.kalin.mosy.DTOs.Results.TokenResult;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;

public class AccountTokenLoginAsyncTask extends AsyncTask<LoginBindingModel, String, TokenResult> {

    private final AsyncTaskListener<TokenResult> asyncTaskListenerListener;

    public AccountTokenLoginAsyncTask(AsyncTaskListener<TokenResult> listener) {
        this.asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected TokenResult doInBackground(LoginBindingModel... models) {
        TokenResult results = null;

        try {
            LoginBindingModel model = models[0];
            results = new AccountRepository().tokenLogin(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final TokenResult result) {
        asyncTaskListenerListener.onPostExecute(result);
    }

}
