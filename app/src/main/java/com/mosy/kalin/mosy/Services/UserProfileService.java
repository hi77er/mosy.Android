package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IAccountRepository;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IUserRepository;
import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;

import org.androidannotations.annotations.EBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@EBean
public class UserProfileService {

    private IAccountRepository accountRepository;

    private AccountService accountService;

    public UserProfileService(){
        this.accountRepository = RetrofitAPIClientFactory.getClient().create(IAccountRepository.class);
        this.accountService = new AccountService();
    }

    public void getUserProfile(@NonNull Context applicationContext, @NonNull AsyncTaskListener<User> apiCallResultListener, @NonNull Runnable onUserTokenInvalidOrExpired) {
        this.accountService.executeAssuredUserTokenValidOrRefreshed(applicationContext,
                () -> {
                    String authTokenHeader = this.accountService.getUserAuthTokenHeader(applicationContext);
                    IUserRepository repository = RetrofitAPIClientFactory.getClient().create(IUserRepository.class);
                    try {
                        Call<User> call = repository.getUserProfile(authTokenHeader);
                        apiCallResultListener.onPreExecute();
                        call.enqueue(new Callback<User>() {
                            @Override public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                if (response.raw().message().toLowerCase().contains("unauthorized")){
                                    onUserTokenInvalidOrExpired.run();
                                }
                                else{
                                    User result = response.body();
                                    apiCallResultListener.onPostExecute(result);
                                }
                            }
                            @Override public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                },
                onUserTokenInvalidOrExpired);
    }

}