package com.mosy.kalin.mosy.Services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.AddExternalLoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RegisterExternalBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RemoveLoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.ExternalLoginResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.FacebookLoginResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.RegisterHttpResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.TokenHttpResult;
import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IAccountRepository;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IUserRepository;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.CheckEmailAvailableResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.UserInfoResult;
import com.mosy.kalin.mosy.DTOs.Role;
import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.CheckEmailAvailableBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RegisterBindingModel;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@EBean
public class AccountService {

    private IAccountRepository accountRepository;

    public AccountService(){
        this.accountRepository = RetrofitAPIClientFactory.getClient().create(IAccountRepository.class);
    }

    public void executeAssuredWebApiTokenValidOrRefreshed(Context applicationContext, Runnable preExecute, @NonNull Runnable onSuccess, Runnable onInvalidHost) {
        boolean tokenExistsAndIsValid = this.checkWebApiTokenValid(applicationContext);

        if (!tokenExistsAndIsValid) {
            this.webApiLogin(applicationContext, preExecute, onSuccess, onInvalidHost);
        }
        else {
            onSuccess.run();
        }
    }
    public void executeAssuredUserTokenValidOrRefreshed(Context applicationContext, Runnable onTokenValid, Runnable onUserTokenInvalidOrExpired) {
        if (this.checkUserTokenValid(applicationContext))
            onTokenValid.run();
        else
            onUserTokenInvalidOrExpired.run();
    }

    private boolean checkWebApiTokenValid(Context applicationContext) {
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_webApi), StringHelper.empty());
        String tokenType = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_webApi), StringHelper.empty());
        String expiresAt = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenExpiresAt_webApi), StringHelper.empty());

        return checkTokenValid(applicationContext, token, tokenType, expiresAt);
    }
    public boolean checkUserTokenValid(Context applicationContext) {
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_user), StringHelper.empty());
        String tokenType = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_user), StringHelper.empty());
        String expiresAt = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenExpiresAt_user), StringHelper.empty());

        return checkTokenValid(applicationContext, token, tokenType, expiresAt);
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

    public String getWebApiAuthTokenHeader(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_webApi), StringHelper.empty());
        String type = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_webApi), StringHelper.empty());
        return type + " " + token;
    }
    public String getUserAuthTokenHeader(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_user), StringHelper.empty());
        String type = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_user), StringHelper.empty());
        return type + " " + token;
    }
    private boolean isFacebookLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
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
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(applicationContext.getString(R.string.pref_authToken_user), token);
        editor.putString(applicationContext.getString(R.string.pref_authTokenType_user), tokenType);
        editor.putInt(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_user), expiresIn);
        //We calc expiresAt here to avoid tracking Server time zone difference.

        try {
            Date issuedAtDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).parse(issuedAt);
            Long expiresAtMilliseconds = issuedAtDate.getTime() + expiresIn * 1000;
            Date expiresAtDate = new Date(expiresAtMilliseconds);
            String expiresAt = expiresAtDate.toString();
            editor.putString(applicationContext.getString(R.string.pref_authTokenExpiresAt_user), expiresAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        editor.apply();
    }

    public void addUserDataToSharedPreferences(Context applicationContext, User userData){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_user), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(applicationContext.getString(R.string.pref_user_username), userData.Username);
        editor.putString(applicationContext.getString(R.string.pref_user_firstName), userData.FirstName);
        editor.putString(applicationContext.getString(R.string.pref_user_lastName), userData.LastName);

        StringBuilder rolesString = new StringBuilder(StringHelper.empty());
        for (Role role : userData.Roles) {
            rolesString.append(role.Name);
        }
        editor.putString(applicationContext.getString(R.string.pref_user_roles), rolesString.toString());

        editor.apply();
    }

    public String getUsername(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_user), Context.MODE_PRIVATE);
        String username = mPreferences.getString(applicationContext.getString(R.string.pref_user_username), StringHelper.empty());

        return username;
    }

    private void webApiLogin(Context applicationContext, Runnable preExecute, @NonNull Runnable onSuccess, Runnable onInvalidHost) {
        Call<TokenHttpResult> call = this.accountRepository.tokenLogin(
                applicationContext.getString(R.string.webAPI_username),
                applicationContext.getString(R.string.webAPI_pass),
                applicationContext.getString(R.string.webAPI_grant_type));

        if (preExecute != null)
            preExecute.run();

        call.enqueue(new Callback<TokenHttpResult>() {
            @Override public void onResponse(Call<TokenHttpResult> call, Response<TokenHttpResult> response) {
                if (response.code() == 400){
                    if (onInvalidHost != null)
                        onInvalidHost.run();
                }
                else {
                    TokenHttpResult result = response.body();
                    if (result == null) throw new NullPointerException("Must have Authentication Token response");

                    if (!StringHelper.isNullOrEmpty(result.AccessToken) && !StringHelper.isNullOrEmpty(result.TokenType)){
                        refreshWebApiAuthTokenSettings(applicationContext, result.AccessToken, result.TokenType, result.IssuedAt, result.ExpiresIn);

                        onSuccess.run();
                    }
                }
            }
            @Override public void onFailure(Call<TokenHttpResult> call, Throwable t) {
                t.printStackTrace();
                executeAssuredWebApiTokenValidOrRefreshed(applicationContext, preExecute, onSuccess, onInvalidHost);
            }
        });
    }

    public void userLogin(Context applicationContext, LoginBindingModel userLoginModel,
                          @NonNull Runnable preExecute,
                          @NonNull Runnable onSuccess,
                          @NonNull Runnable onWrongUserOrPass,
                          @NonNull Runnable onEmailNotConfirmed,
                          @NonNull Runnable onFail,
                          @NonNull Runnable onInvalidHost) {
        try {
            preExecute.run();
            Call<TokenHttpResult> call = this.accountRepository.tokenLogin(userLoginModel.Email, userLoginModel.Password, "password");

            call.enqueue(new Callback<TokenHttpResult>() {
                @Override public void onFailure(Call<TokenHttpResult> call, Throwable t) {
                    t.printStackTrace();
                }
                @Override public void onResponse(Call<TokenHttpResult> call, Response<TokenHttpResult> response) {
                    try {
                        if (response.code() == 400){
                                if (response.errorBody() != null) {
                                    String errorBody = response.errorBody().string().toLowerCase();

                                    if (errorBody.contains("email is not confirmed"))
                                        onEmailNotConfirmed.run();
                                    else if(errorBody.contains("user name or password is incorrect"))
                                        onWrongUserOrPass.run();
                                    else
                                        onInvalidHost.run();
                                }
                        }
                        else {
                            TokenHttpResult result = response.body();
                            if (result == null) onFail.run();

                            if (!StringHelper.isNullOrEmpty(result.AccessToken) && !StringHelper.isNullOrEmpty(result.TokenType)){
                                refreshUserAuthTokenSettings(applicationContext, result.AccessToken, result.TokenType, result.IssuedAt, result.ExpiresIn);

                                onSuccess.run();
                            }
                            onFail.run();
                        }
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                        onFail.run();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void facebookLogin(Context applicationContext, String facebookAccessToken, String password,
                              @NonNull Runnable onPreExecute,
                              @NonNull Runnable onSuccess,
                              @NonNull Runnable onInvalidHost,
                              @NonNull Runnable onEmailAlreadyExists,
                              @NonNull Runnable onFacebookAccountNotFound,
                              @NonNull Runnable onFail) {
        try {
            onPreExecute.run();
            Call<FacebookLoginResult> callResult =  this.accountRepository.facebookLogin(facebookAccessToken, password);

            callResult.enqueue(new Callback<FacebookLoginResult>() {
                @Override public void onFailure(@NonNull Call<FacebookLoginResult> call, @NonNull Throwable t) {
                    call.cancel();
                }
                @Override public void onResponse(@NonNull Call<FacebookLoginResult> call, @NonNull Response<FacebookLoginResult> response) {
                    try {
                        if (response.code() == 400){
                            if (response.errorBody() != null) {
                                assert response.errorBody() != null;
                                String errorBody = response.errorBody().string().toLowerCase();

                                onInvalidHost.run();
                            }
                        }
                        else {
                            FacebookLoginResult result = response.body();
                            if (result != null) {
                                if (StringHelper.isNotNullOrEmpty(result.ResultMessage))
                                {
                                    if (result.ResultMessage.toLowerCase().contains("email already exists"))
                                        onEmailAlreadyExists.run();
                                    if (result.ResultMessage.toLowerCase().contains("facebook account not fount"))
                                        onFacebookAccountNotFound.run();
                                }
                                else if(result.AccessToken != null &&
                                        !StringHelper.isNullOrEmpty(result.AccessToken.AccessToken) &&
                                        !StringHelper.isNullOrEmpty(result.AccessToken.TokenType)) {

                                    refreshUserAuthTokenSettings(applicationContext, result.AccessToken.AccessToken, result.AccessToken.TokenType, result.AccessToken.IssuedAt, result.AccessToken.ExpiresIn);
                                    onSuccess.run();
                                }
                            }
                            else
                                onFail.run();
                        }
                    }
                    catch (Exception ex){ ex.printStackTrace(); }
                }
            });
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void register(Context applicationContext, String Email, String Password, String ConfirmPassword, AsyncTaskListener<RegisterHttpResult> apiCallResultListener, Runnable onInvalidHost) {
        this.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    apiCallResultListener.onPreExecute();

                    String authToken = this.getWebApiAuthTokenHeader(applicationContext);

                    RegisterBindingModel model = new RegisterBindingModel(Email, Password, ConfirmPassword, true);
                    IAccountRepository repository = RetrofitAPIClientFactory.getClient().create(IAccountRepository.class);
                    Call<RegisterHttpResult> callRegResult = repository.register(authToken, model);

                    callRegResult.enqueue(new Callback<RegisterHttpResult>() {
                        @Override public void onFailure(Call<RegisterHttpResult> call, Throwable t) {
                            call.cancel();
                        }
                        @Override public void onResponse(Call<RegisterHttpResult> call, Response<RegisterHttpResult> response) {
                            if(response.code() == 400)
                                if(onInvalidHost != null)
                                    onInvalidHost.run();
                            RegisterHttpResult result = response.body();
                            if(apiCallResultListener != null)
                                apiCallResultListener.onPostExecute(result);
                        }
                    });
                },
                onInvalidHost);
    }

    public void localLogoutUser(Context applicationContext) {
        //First log out from Facebook
        LoginManager.getInstance().logOut();

        //Then delete all access token information for the user
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString(applicationContext.getString(R.string.pref_authToken_user), StringHelper.empty());
        editor.putString(applicationContext.getString(R.string.pref_authTokenType_user), StringHelper.empty());
        editor.putInt(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_user), 0);
        editor.apply();


        mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_user), Context.MODE_PRIVATE);
        editor = mPreferences.edit();

        editor.putString(applicationContext.getString(R.string.pref_user_username), StringHelper.empty());
        editor.putString(applicationContext.getString(R.string.pref_user_firstName), StringHelper.empty());
        editor.putString(applicationContext.getString(R.string.pref_user_lastName), StringHelper.empty());
        editor.putString(applicationContext.getString(R.string.pref_user_roles), StringHelper.empty());
        editor.apply();
    }



    public void getExternalLogins(Context applicationContext, AsyncTaskListener<ArrayList<ExternalLoginResult>> apiCallResultListener, String returnUrl, boolean generateState) {
        try {
            Call<ArrayList<ExternalLoginResult>> callResult =  this.accountRepository.getExternalLogins(returnUrl, generateState);
            apiCallResultListener.onPreExecute();
            callResult.enqueue(new Callback<ArrayList<ExternalLoginResult>>() {
                @Override public void onFailure(@NonNull Call<ArrayList<ExternalLoginResult>> call, @NonNull Throwable t) {
                    call.cancel();
                }
                @Override public void onResponse(@NonNull Call<ArrayList<ExternalLoginResult>> call, @NonNull Response<ArrayList<ExternalLoginResult>> response) {
                    ArrayList<ExternalLoginResult> result = response.body();
                    apiCallResultListener.onPostExecute(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void externalLogin(Context applicationContext, AsyncTaskListener<HttpResult> apiCallResultListener, String provider, String error) {
        try {
            Call<Void> callResult =  this.accountRepository.externalLogin(provider, error);
            apiCallResultListener.onPreExecute();
            callResult.enqueue(new Callback<Void>() {
                @Override public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    call.cancel();
                }
                @Override public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    HttpResult result = new HttpResult(response.isSuccessful());
                    apiCallResultListener.onPostExecute(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getUserInfo(Context applicationContext, AsyncTaskListener<UserInfoResult> apiCallResultListener) {
        try {
            String authTokenHeader = this.getUserAuthTokenHeader(applicationContext);

            Call<UserInfoResult> callResult =  this.accountRepository.getUserInfo(authTokenHeader);

            apiCallResultListener.onPreExecute();
            callResult.enqueue(new Callback<UserInfoResult>() {
                @Override public void onFailure(@NonNull Call<UserInfoResult> call, @NonNull Throwable t) {
                    call.cancel();
                }
                @Override public void onResponse(@NonNull Call<UserInfoResult> call, @NonNull Response<UserInfoResult> response) {
                    UserInfoResult result = response.body();
                    apiCallResultListener.onPostExecute(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void registerExternal(Context applicationContext, AsyncTaskListener<HttpResult> apiCallResultListener, String email) {
        try {
            String authTokenHeader = this.getUserAuthTokenHeader(applicationContext);

            RegisterExternalBindingModel model = new RegisterExternalBindingModel(email);
            Call<Void> callResult =  this.accountRepository.registerExternal(authTokenHeader, model);

            apiCallResultListener.onPreExecute();
            callResult.enqueue(new Callback<Void>() {
                @Override public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    call.cancel();
                }
                @Override public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    HttpResult result = new HttpResult(response.isSuccessful());
                    apiCallResultListener.onPostExecute(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addExternalLogin(Context applicationContext, AsyncTaskListener<HttpResult> apiCallResultListener) {
        try {
            String authTokenHeader = this.getUserAuthTokenHeader(applicationContext);

            AddExternalLoginBindingModel model = new AddExternalLoginBindingModel(authTokenHeader);
            Call<Void> callResult =  this.accountRepository.addExternalLogin(model);

            apiCallResultListener.onPreExecute();
            callResult.enqueue(new Callback<Void>() {
                @Override public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    call.cancel();
                }
                @Override public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    HttpResult result = new HttpResult(response.isSuccessful());
                    apiCallResultListener.onPostExecute(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeExternalLogin(Context applicationContext, AsyncTaskListener<HttpResult> apiCallResultListener, String loginProvider, String providerKey) {
        try {
            RemoveLoginBindingModel model = new RemoveLoginBindingModel(loginProvider, providerKey);
            Call<Void> callResult =  this.accountRepository.removeExternalLogin(model);

            apiCallResultListener.onPreExecute();
            callResult.enqueue(new Callback<Void>() {
                @Override public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    call.cancel();
                }
                @Override public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    HttpResult result = new HttpResult(response.isSuccessful());
                    apiCallResultListener.onPostExecute(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkEmailAvailable (Context applicationContext, String email, AsyncTaskListener<CheckEmailAvailableResult> apiCallResultListener, Runnable onInvalidHost) {
        this.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    apiCallResultListener.onPreExecute();
                    String authToken = this.getWebApiAuthTokenHeader(applicationContext);

                    CheckEmailAvailableBindingModel model = new CheckEmailAvailableBindingModel(email);
                    IAccountRepository repository = RetrofitAPIClientFactory.getClient().create(IAccountRepository.class);
                    Call<CheckEmailAvailableResult> callRegResult = repository.checkEmailAvailable(authToken, model);

                    callRegResult.enqueue(new Callback<CheckEmailAvailableResult>() {
                        @Override
                        public void onResponse(Call<CheckEmailAvailableResult> call, Response<CheckEmailAvailableResult> response) {
                            if(response.code() == 400)
                                if(onInvalidHost != null)
                                    onInvalidHost.run();

                            CheckEmailAvailableResult result = response.body();
                            apiCallResultListener.onPostExecute(result);
                        }
                        @Override public void onFailure(Call<CheckEmailAvailableResult> call, Throwable t) {
                            call.cancel();
                        }
                    });
                },
                onInvalidHost);
    }

}