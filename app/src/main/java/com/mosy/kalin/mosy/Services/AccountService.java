package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.mosy.kalin.mosy.DAL.Http.Results.RegisterResult;
import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IAccountRepository;
import com.mosy.kalin.mosy.DAL.Http.Results.TokenResult;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
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
public class AccountService {

    public String getWebApiAuthTokenHeader(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_webApi), StringHelper.empty());
        String type = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_webApi), StringHelper.empty());
        return type + " " + token;
    }

    public String getUserAuthTokenHeader(Context applicationContext){
//        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_user), Context.MODE_PRIVATE);
//        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_user), StringHelper.empty());
//        String type = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_user), StringHelper.empty());
//        return type + " " + token;
        return null;
    }

    public void executeAssuredWebApiTokenValidOrRefreshed(Context applicationContext, Runnable preExecute, Runnable onSuccess, Runnable onInvalidHost) {
        boolean tokenExistsAndIsValid = this.checkWebApiTokenValid(applicationContext);

        if (!tokenExistsAndIsValid) {
            IAccountRepository accountRepo = RetrofitAPIClientFactory.getClient().create(IAccountRepository.class);
            Call<TokenResult> call = accountRepo.tokenLogin("webapiadmin@mosy.com", "!23Qwe", "password");

            if (preExecute != null) preExecute.run();

            call.enqueue(new Callback<TokenResult>() {
                @Override public void onResponse(Call<TokenResult> call, Response<TokenResult> response) {
                    if (response.code() == 400){
                        if (onInvalidHost != null)
                            onInvalidHost.run();
                    }
                    else {
                        TokenResult result = response.body();
                        if (result == null) throw new NullPointerException("Must have Authentication Token response");

                        if (!StringHelper.isNullOrEmpty(result.AccessToken) && !StringHelper.isNullOrEmpty(result.AccessToken)){
                            refreshWebApiAuthTokenSettings(applicationContext, result.AccessToken, result.TokenType, result.IssuedAt, result.ExpiresIn);

                            if (onSuccess != null)
                                onSuccess.run();
                        }
                    }
                }
                @Override public void onFailure(Call<TokenResult> call, Throwable t) {
                    t.printStackTrace();
                    executeAssuredWebApiTokenValidOrRefreshed(applicationContext, preExecute, onSuccess, onInvalidHost);
                }
            });
        }
        else if (onSuccess != null) {
            onSuccess.run();
        }
    }

    public void executeAssuredUserTokenValidOrRefreshed(Context applicationContext, LoginBindingModel userLoginModel, Runnable preExecute, Runnable onSuccess, Runnable onInvalidHost) {
        boolean tokenExistsAndIsValid = this.checkUserTokenValid(applicationContext);

        if (!tokenExistsAndIsValid) {
            IAccountRepository accountRepo = RetrofitAPIClientFactory.getClient().create(IAccountRepository.class);
            Call<TokenResult> call = accountRepo.tokenLogin(userLoginModel.Email, userLoginModel.Password, "password");

            if (preExecute != null) preExecute.run();

            call.enqueue(new Callback<TokenResult>() {
                @Override public void onResponse(Call<TokenResult> call, Response<TokenResult> response) {
                    if (response.code() == 400){
                        if (onInvalidHost != null)
                            onInvalidHost.run();
                    }
                    else {
                        TokenResult result = response.body();
                        if (result == null) throw new NullPointerException("Must have Authentication Token response");

                        if (!StringHelper.isNullOrEmpty(result.AccessToken) && !StringHelper.isNullOrEmpty(result.AccessToken)){
                            refreshUserAuthTokenSettings(applicationContext, result.AccessToken, result.TokenType, result.IssuedAt, result.ExpiresIn);

                            if (onSuccess != null)
                                onSuccess.run();
                        }
                    }
                }
                @Override public void onFailure(Call<TokenResult> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else if (onSuccess != null) {
            onSuccess.run();
        }
    }

    private boolean checkWebApiTokenValid(Context applicationContext) {
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_webApi), StringHelper.empty());
        String tokenType = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_webApi), StringHelper.empty());
        String expiresAt = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenExpiresAt_webApi), StringHelper.empty());

        return checkTokenValid(applicationContext, token, tokenType, expiresAt);
    }

    private boolean checkUserTokenValid(Context applicationContext) {
//        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_user), Context.MODE_PRIVATE);
//        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_user), StringHelper.empty());
//        String tokenType = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_user), StringHelper.empty());
//        String expiresAt = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenExpiresAt_user), StringHelper.empty());
//
//        return checkTokenValid(applicationContext, token, tokenType, expiresAt);
        return false;
    }

    private boolean checkTokenValid(Context applicationContext, String token, String tokenType, String expiresAt) {
        if (StringHelper.isNullOrEmpty(token) || StringHelper.isNullOrEmpty(tokenType) || StringHelper.isNullOrEmpty(expiresAt))
            return false;

        try {
            Date now = Calendar.getInstance().getTime();
            Date expirationDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US).parse(expiresAt);
            return now.before(expirationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void refreshWebApiAuthTokenSettings(Context applicationContext, String token, String tokenType, String issuedAt, int expiresIn){
           SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString(applicationContext.getString(R.string.pref_authToken_webApi), token);
            editor.putString(applicationContext.getString(R.string.pref_authTokenType_webApi), tokenType);
            editor.putInt(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_webApi), expiresIn);
            //We calc expiresAt here to avoid tracking Server time zone difference.

            try {
                Date issuedAtDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).parse(issuedAt);
                Long expiresAtMilliseconds = issuedAtDate.getTime() + expiresIn * 1000;
                Date expiresAtDate = new Date(expiresAtMilliseconds);
                String expiresAt = expiresAtDate.toString();
                editor.putString(applicationContext.getString(R.string.pref_authTokenExpiresAt_webApi), expiresAt);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            editor.apply();
    }

    private void refreshUserAuthTokenSettings(Context applicationContext, String token, String tokenType, String issuedAt, int expiresIn){
//        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_user), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mPreferences.edit();
//        editor.putString(applicationContext.getString(R.string.pref_authToken_user), token);
//        editor.putString(applicationContext.getString(R.string.pref_authTokenType_user), tokenType);
//        editor.putInt(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_user), expiresIn);
//        //We calc expiresAt here to avoid tracking Server time zone difference.
//
//        try {
//            Date issuedAtDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).parse(issuedAt);
//            Long expiresAtMilliseconds = issuedAtDate.getTime() + expiresIn * 1000;
//            Date expiresAtDate = new Date(expiresAtMilliseconds);
//            String expiresAt = expiresAtDate.toString();
//            editor.putString(applicationContext.getString(R.string.pref_authTokenExpiresAt_user), expiresAt);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        editor.apply();
    }

    public void register(Context applicationContext,
                         String Email,
                         String Password,
                         String ConfirmPassword,
                         AsyncTaskListener<RegisterResult> apiCallResultListener,
                         Runnable onInvalidHost)
    {
        RegisterBindingModel model = new RegisterBindingModel(Email, Password, ConfirmPassword);
        IAccountRepository repository = RetrofitAPIClientFactory.getClient().create(IAccountRepository.class);
        Call<RegisterResult> callRegResult = repository.register(model);

        callRegResult.enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                if(response.code() == 400)
                    if(onInvalidHost != null)
                        onInvalidHost.run();
                RegisterResult result = response.body();
                if(result != null && result.isSuccessful() && apiCallResultListener != null)
                    apiCallResultListener.onPostExecute(result);
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                call.cancel();
            }
        });

    }

}