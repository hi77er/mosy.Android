package com.mosy.kalin.mosy;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Services.UserProfileService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Arrays;

@SuppressLint("Registered")
@EActivity(R.layout.activity_login)
public class LoginActivity
        extends BaseActivity {

    private CallbackManager callbackManager;

    @Bean
    UserProfileService userProfileService;

    @Extra
    boolean confirmEmailNeeded;

    @ViewById(R.id.login_llInitialLoadingProgress)
    LinearLayout centralProgress;

    @ViewById(R.id.landing_lButtons)
    LinearLayout layoutButtons;

    @ViewById(R.id.login_etEmail)
    EditText etEmail;
    @ViewById(R.id.login_etPassword)
    EditText etPassword;

    @ViewById(R.id.login_btnLogin)
    Button btnLogin;

    @ViewById(R.id.fbLogin_Btn)
    LoginButton fbLoginBtn;
    @ViewById(R.id.tvInfoMessage)
    TextView infoMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
    }

    @AfterViews
    public void afterViews(){
        if (confirmEmailNeeded){
            this.onFinishLogin("Confirm your email and come back to Login.");
            Toast.makeText(applicationContext, "Registration successful", Toast.LENGTH_SHORT).show();
        }

        this.setFacebookLoginCallback();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void setFacebookLoginCallback() {
        LoginManager.getInstance().logOut();
        fbLoginBtn.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            // String email = object.getString("email");
                            String fbAccessToken = loginResult.getAccessToken().getToken();

//                          FirstNameSocial = object.optString("first_name");
//                          LastNameSocial = object.optString("last_name");
//                          GenderSocial = object.optString("gender");
//                          String emailSocial = object.optString("email", "");
//                          Id = object.optString("Id");

                            accountService.facebookLogin(
                                    applicationContext,
                                    fbAccessToken,
                                    StringHelper.empty(),
                                    LoginActivity.this::onPreLogin,
                                    LoginActivity.this::onAuthenticationSucceeded,
                                    LoginActivity.this::onInvalidHostMessage,
                                    LoginActivity.this::onEmailAlreadyExists,
                                    LoginActivity.this::onFacebookAccountNotFound,
                                    LoginActivity.this::onFail);
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "Id,Name,email");
//                parameters.putString("fields", "Id,Name,email,birthday,gender,first_name,last_name,picture,education,work");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override public void onCancel() {
                onFail();
            }
            @Override public void onError(FacebookException exception) {
                onFail();
            }
        });
    }

    @Click(R.id.login_btnLogin)
    public void Login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        Context applicationContext = getApplicationContext();

        if (!StringHelper.isNullOrWhitespace(email) && !StringHelper.isNullOrWhitespace(password)) {
            if (StringHelper.isEmailAddress(email)) {

                this.accountService.userLogin(
                        this.applicationContext,
                        new LoginBindingModel(email, password),
                        this::onPreLogin,
                        this::onAuthenticationSucceeded,
                        this::onWrongUserOrPass,
                        this::onEmailNotConfirmed,
                        this::onFail,
                        this::onInvalidHostMessage);
            } else
                Toast.makeText(applicationContext,
                        "Invalid Email address.",
                        Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(applicationContext,
                    "Email and password are required.",
                    Toast.LENGTH_SHORT).show();
    }

    private void onPreLogin() {
        this.onStartLogin();
    }
    private void onAuthenticationSucceeded() {
        this.onFinishLogin(null);

        AsyncTaskListener<User> apiCallListener = new AsyncTaskListener<User>() {
            @Override public void onPreExecute() {

            }
            @Override public void onPostExecute(User user) {
                if (user != null) {
                    accountService.addUserDataToSharedPreferences(applicationContext, user);

                    Toast.makeText(applicationContext, "Login successful.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, LandingActivity_.class);
                    startActivity(intent);
                } else {
                    LoginManager.getInstance().logOut();
                    onFinishLogin("Something went wrong.");
                }
            }
        };

        this.userProfileService.getUserProfile(this.applicationContext, apiCallListener, this::onFail);
    }

    private void onWrongUserOrPass() {
        this.onFinishLogin("Wrong username or password.");
    }
    private void onEmailNotConfirmed() {
        this.onFinishLogin("Email not confirmed.");
    }
    private void onFacebookAccountNotFound() {
        LoginManager.getInstance().logOut();
        this.onFinishLogin("No such Facebook user was found.");
    }
    private void onEmailAlreadyExists() {
        LoginManager.getInstance().logOut();
        this.onFinishLogin("There is already a local account associated to the email of this Facebook account. You can login and then associate this Facebook profile to it in 'My profile' section.");
    }
    private void onInvalidHostMessage() {
        LoginManager.getInstance().logOut();
        this.onFinishLogin("No internet.");
    }
    private void onFail() {
        LoginManager.getInstance().logOut();
        this.onFinishLogin("Something went wrong.");
    }

    private void onStartLogin(){
        this.infoMessageTextView.setVisibility(View.GONE);
        this.infoMessageTextView.setText(StringHelper.empty());

        this.centralProgress.setVisibility(View.VISIBLE);
        this.layoutButtons.setVisibility(View.GONE);
    }
    private void onFinishLogin(String infoMessage){
        this.infoMessageTextView.setVisibility(StringHelper.isNotNullOrEmpty(infoMessage) ? View.VISIBLE : View.GONE);
        this.infoMessageTextView.setText(StringHelper.isNotNullOrEmpty(infoMessage) ? infoMessage : StringHelper.empty());

        this.centralProgress.setVisibility(View.GONE);
        this.layoutButtons.setVisibility(View.VISIBLE);
    }

    @Click(R.id.login_btnForgotPassword)
    public void forgotPassword() {
        String url = "https://www.treatspark.com/Account/ForgotPassword";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Click(R.id.login_btnRegister)
    public void goToRegisterActivity(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);
        startActivity(intent);
    }
}
