package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.AccountRepository;
import com.mosy.kalin.mosy.DAL.Http.Results.RegisterResult;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;

public class AccountRegisterAsyncTask extends AsyncTask<RegisterBindingModel, String, RegisterResult> {

    private final AsyncTaskListener<RegisterResult> asyncTaskListenerListener;

    public AccountRegisterAsyncTask(AsyncTaskListener<RegisterResult> listener) {
        this.asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected RegisterResult doInBackground(RegisterBindingModel... models) {
        RegisterResult results = null;
        try {
            RegisterBindingModel model = models[0];
            results = new AccountRepository().register(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final RegisterResult result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
