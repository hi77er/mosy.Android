package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.mosy.kalin.mosy.DAL.Http.Results.RegisterResult;
import com.mosy.kalin.mosy.DAL.Http.Results.TokenResult;
import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IAccountRepository;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.ISecurityRepository;
import com.mosy.kalin.mosy.DTOs.HttpResponses.CheckEmailAvailableResponse;
import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.CheckEmailAvailableBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@EBean
public class SecurityService {

    public void pingServer(Context applicationContext, AsyncTaskListener<String> apiCallResultListener, Runnable onInvalidHost)
    {
        AccountService accountService = new AccountService();
        accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = accountService.getWebApiAuthTokenHeader(applicationContext);

                    ISecurityRepository repository = RetrofitAPIClientFactory.getClient().create(ISecurityRepository.class);
                    Call<String> callRegResult = repository.ping(authToken);

                    callRegResult.enqueue(new Callback<String>() {
                        @Override public void onResponse(Call<String> call, Response<String> response) {
                            if(response.code() == 400)
                                if(onInvalidHost != null)
                                    onInvalidHost.run();
                            String result = response.body();
                            apiCallResultListener.onPostExecute(result);
                        }
                        @Override public void onFailure(Call<String> call, Throwable t) {
                            call.cancel();
                        }
                    });
                },
                onInvalidHost);
    }

}