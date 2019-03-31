package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.ITokenRepository;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.AuthorizationTokensResource;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.FacebookLoginResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.RegisterHttpResult;
import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IAccountRepository;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.CheckEmailAvailableResult;
import com.mosy.kalin.mosy.DTOs.Role;
import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.CheckEmailAvailableBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RegisterBindingModel;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@EBean
public class AccountService {

    private ITokenRepository webApiTokenRepository;
    private IAccountRepository webApiAccountRepository;

    public AccountService(){
        this.webApiTokenRepository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint)
                .create(ITokenRepository.class);
        this.webApiAccountRepository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint)
                .create(IAccountRepository.class);
    }

    public void executeAssuredWebApiTokenValidOrRefreshed(Context applicationContext, Runnable preExecute, @NonNull Runnable onSuccess, Runnable onInvalidHost) throws NullPointerException {
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
        long expiresInSecondsAfter1970Utc = mPreferences.getLong(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_webApi), 0);

        Date expiresAt = new Date(expiresInSecondsAfter1970Utc * 1000);
        return checkTokenValid(applicationContext, token, tokenType, expiresAt);
    }
    public boolean checkUserTokenValid(Context applicationContext) {
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_user), StringHelper.empty());
        String tokenType = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_user), StringHelper.empty());
        long expiresInSecondsAfter1970Utc = 0;
        try {
            expiresInSecondsAfter1970Utc = mPreferences.getLong(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_user), 0);
        }
        catch (Exception ex)
        {
            expiresInSecondsAfter1970Utc = mPreferences.getInt(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_user), 0);
        }

        Date expiresAt = new Date(expiresInSecondsAfter1970Utc * 1000);
        return checkTokenValid(applicationContext, token, tokenType, expiresAt);
    }
    private boolean checkTokenValid(Context applicationContext, String token, String tokenType, Date expiresAt) {
        if (StringHelper.isNullOrEmpty(token) || StringHelper.isNullOrEmpty(tokenType) || expiresAt == null)
            return false;

        Date now = Calendar.getInstance().getTime();
        //Date expirationDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US).parse(expiresAt);
        return now.before(expiresAt);
    }

    public String getWebApiAuthToken(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        return mPreferences.getString(applicationContext.getString(R.string.pref_authToken_webApi), StringHelper.empty());
    }
    public String getWebApiAuthTokenHeader(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_webApi), StringHelper.empty());
        String type = mPreferences.getString(applicationContext.getString(R.string.pref_authTokenType_webApi), StringHelper.empty());
        return type + " " + token;
    }

    public String getUserAuthToken(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        String token = mPreferences.getString(applicationContext.getString(R.string.pref_authToken_user), StringHelper.empty());
        return token;
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

    private void refreshWebApiAuthTokenSettings(Context applicationContext, String token, String tokenType, String issuedAt, long expiresInSecondsAfter1970Utc){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(applicationContext.getString(R.string.pref_authToken_webApi), token);
        editor.putString(applicationContext.getString(R.string.pref_authTokenType_webApi), tokenType);
        editor.putLong(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_webApi), expiresInSecondsAfter1970Utc);

        // int rawOffset = Calendar.getInstance().getTimeZone().getRawOffset();
        Date expiresAtDate = new Date(expiresInSecondsAfter1970Utc * 1000); // + rawOffset or is it already automatically in the correct local timezone?
        editor.putString(applicationContext.getString(R.string.pref_authTokenExpiresAt_webApi), (expiresAtDate).toString());

        editor.apply();
    }
    private void refreshUserAuthTokenSettings(Context applicationContext, String token, String tokenType, String issuedAt, long expiresInSecondsAfter1970Utc){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(applicationContext.getString(R.string.pref_authToken_user), token);
        editor.putString(applicationContext.getString(R.string.pref_authTokenType_user), tokenType);
        editor.putLong(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_user), expiresInSecondsAfter1970Utc);

        // int rawOffset = Calendar.getInstance().getTimeZone().getRawOffset();
        Date expiresAtDate = new Date(expiresInSecondsAfter1970Utc * 1000); // + rawOffset or is it already automatically in the correct local timezone?
        editor.putString(applicationContext.getString(R.string.pref_authTokenExpiresAt_user), (expiresAtDate).toString());

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
            String roleName = role.Name;
            if (userData.Roles.iterator().hasNext())
                roleName += ",";
            rolesString.append(roleName);
        }
        editor.putString(applicationContext.getString(R.string.pref_user_roles), rolesString.toString());

        editor.apply();
    }

    public String getUsername(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_user), Context.MODE_PRIVATE);
        String username = mPreferences.getString(applicationContext.getString(R.string.pref_user_username), StringHelper.empty());

        return username;
    }

    public ArrayList<String> getUserRoles(Context applicationContext){
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_user), Context.MODE_PRIVATE);
        String userRoles = mPreferences.getString(applicationContext.getString(R.string.pref_user_roles), StringHelper.empty());

        return new ArrayList<>(Arrays.asList(userRoles.split(",")));
    }

    private void webApiLogin(Context applicationContext, Runnable preExecute, @NonNull Runnable onSuccess, Runnable onInvalidHost) throws NullPointerException {
        LoginBindingModel model = new LoginBindingModel(applicationContext.getString(R.string.webAPI_username), applicationContext.getString(R.string.webAPI_pass));
        Call<AuthorizationTokensResource> call = this.webApiTokenRepository.tokenLogin(model);

        if (preExecute != null)
            preExecute.run();

        call.enqueue(new Callback<AuthorizationTokensResource>() {
            @Override public void onResponse(Call<AuthorizationTokensResource> call, Response<AuthorizationTokensResource> response) {
                if (response.code() == 400){
                    if (onInvalidHost != null)
                        onInvalidHost.run();
                }
                else {
                    AuthorizationTokensResource result = response.body();
                    if (result != null && result.AccessToken != null && !StringHelper.isNullOrEmpty(result.AccessToken.AccessToken) && !StringHelper.isNullOrEmpty(result.AccessToken.TokenType)) {
                        refreshWebApiAuthTokenSettings(applicationContext, result.AccessToken.AccessToken, result.AccessToken.TokenType, String.valueOf(result.AccessToken.Issued), result.AccessToken.ExpiresIn);
                        onSuccess.run();
                    }
                    else
                    {
                        throw new NullPointerException("Must have Authentication Token response");
                    }
                }
            }
            @Override public void onFailure(Call<AuthorizationTokensResource> call, Throwable t) {
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

            //Deprecated
            //Call<TokenHttpResult> call = this.webApiAccountRepository.tokenLogin(userLoginModel.Email, userLoginModel.Password, "password");
            Call<AuthorizationTokensResource> call = this.webApiTokenRepository.tokenLogin(userLoginModel);

            call.enqueue(new Callback<AuthorizationTokensResource>() {
                @Override public void onFailure(Call<AuthorizationTokensResource> call, Throwable t) {
                    t.printStackTrace();
                    onFail.run();
                }
                @Override public void onResponse(Call<AuthorizationTokensResource> call, Response<AuthorizationTokensResource> response) {
                    try {
                        if (response.code() == 400){
                                if (response.errorBody() != null) {
                                    String errorBody = response.errorBody().string().toLowerCase();

                                    if (errorBody.contains("not confirmed"))
                                    {
                                        onEmailNotConfirmed.run();
                                    }
                                    else if(errorBody.contains("name or password is incorrect")
                                         || errorBody.contains("wrong user or pass"))
                                    {
                                        //INFO: MEANS THAT USER EMAIL WAS WRONG (NOT FOUND IN THE DB). USER SHOULDN'T KNOW THAT!
                                        onWrongUserOrPass.run();
                                    }
                                    else
                                    {
                                        onInvalidHost.run();
                                    }
                                }
                        }
                        else {
                            AuthorizationTokensResource result = response.body();

                            if (result != null && result.AccessToken != null && !StringHelper.isNullOrEmpty(result.AccessToken.AccessToken) && !StringHelper.isNullOrEmpty(result.AccessToken.TokenType)){
                                refreshUserAuthTokenSettings(applicationContext, result.AccessToken.AccessToken, result.AccessToken.TokenType, String.valueOf(result.AccessToken.Issued), result.AccessToken.ExpiresIn);

                                onSuccess.run();
                            }
                            else
                            {
                                onFail.run();
                            }
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
            onFail.run();
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
            Call<FacebookLoginResult> callResult =  this.webApiTokenRepository.facebookLogin(facebookAccessToken, password);

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
                    IAccountRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IAccountRepository.class);
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

        //Then delete all access token information for the user:
        SharedPreferences mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_webApi), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString(applicationContext.getString(R.string.pref_authToken_user), StringHelper.empty());
        editor.putString(applicationContext.getString(R.string.pref_authTokenType_user), StringHelper.empty());
        editor.putInt(applicationContext.getString(R.string.pref_authTokenExpiresInSeconds_user), 0);
        editor.apply();

        //Then delete all access token information for the user:
        mPreferences = applicationContext.getSharedPreferences(applicationContext.getString(R.string.pref_collectionName_user), Context.MODE_PRIVATE);
        editor = mPreferences.edit();

        editor.putString(applicationContext.getString(R.string.pref_user_username), StringHelper.empty());
        editor.putString(applicationContext.getString(R.string.pref_user_firstName), StringHelper.empty());
        editor.putString(applicationContext.getString(R.string.pref_user_lastName), StringHelper.empty());
        editor.putString(applicationContext.getString(R.string.pref_user_roles), StringHelper.empty());
        editor.apply();
    }

    public void checkEmailAvailable (Context applicationContext, String email, AsyncTaskListener<CheckEmailAvailableResult> apiCallResultListener, Runnable onInvalidHost) {
        this.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    apiCallResultListener.onPreExecute();
                    String authToken = this.getWebApiAuthTokenHeader(applicationContext);

                    CheckEmailAvailableBindingModel model = new CheckEmailAvailableBindingModel(email);
                    IAccountRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IAccountRepository.class);
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