package com.mosy.kalin.mosy.Services;

import android.content.Context;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.ISecurityRepository;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;

import org.androidannotations.annotations.EBean;

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