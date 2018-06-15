package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IAccountRepository;
import com.mosy.kalin.mosy.DTOs.Enums.TokenResultStatus;
import com.mosy.kalin.mosy.DTOs.Results.TokenResult;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AsyncTasks.AccountTokenLoginAsyncTask;

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

    public void executeAssuredTokenValidOrRefreshed(Context applicationContext, Runnable onSuccess, Runnable onInvalidHost){
        executeAssuredTokenValidOrRefreshed(applicationContext, null, onSuccess, onInvalidHost);
    }

    public void executeAssuredTokenValidOrRefreshed(Context applicationContext, Runnable preExecute, Runnable onSuccess, Runnable onInvalidHost) {
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        LoginBindingModel model = new LoginBindingModel("webapiadmin@mosy.com", "!23Qwe");
        boolean tokenExistsAndIsValid = this.checkTokenValid(applicationContext);

        if (!tokenExistsAndIsValid) {
            IAccountRepository accountRepo = RetrofitAPIClientFactory.getClient().create(IAccountRepository.class);
            Call<TokenResult> call = accountRepo.tokenLogin(model.Email, model.Password, "password");

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
                }
            });

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.apply();
        }
        else if (onSuccess != null) {
            onSuccess.run();
        }
    }

    private boolean checkTokenValid(Context applicationContext) {
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_webApi), StringHelper.empty());
        String tokenType = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_webApi), StringHelper.empty());
        String expiresAt = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenExpiresAt_webApi), StringHelper.empty());
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

    public String getAuthTokenHeader(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_webApi), StringHelper.empty());
        String type = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_webApi), StringHelper.empty());
        return type + " " + token;
    }
}