package com.mosy.kalin.mosy;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mosy.kalin.mosy.DAL.Http.Results.RegisterResult;
import com.mosy.kalin.mosy.DTOs.HttpResponses.CheckEmailAvailableResponse;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Helpers.StringHelper;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_register)
public class RegisterActivity
        extends BaseActivity {

    @ViewById(R.id.etPassword)
    EditText etPassword;
    @ViewById(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @ViewById(R.id.etEmail)
    EditText etEmail;
    @ViewById(R.id.register_llInitialLoadingProgress)
    LinearLayout centralProgress;

    @Click(R.id.btnCancel)
    public void goBack() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.btnRegister)
    public void Register() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String repeatPassword = etPassword.getText().toString();
        Context applicationContext = getApplicationContext();

        if (StringHelper.isNullOrWhitespace(email) || StringHelper.isNullOrWhitespace(password)) {
            Toast.makeText(applicationContext, "Email and password are required.", Toast.LENGTH_LONG).show();
        } else if (!StringHelper.isEmailAddress(email)) {
            Toast.makeText(applicationContext, "Invalid Email address.", Toast.LENGTH_LONG).show();
        } else if (!StringHelper.isMatch("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*(_|[^\\w])).{6,}$", password)) {
            Toast.makeText(applicationContext,
                    "Passwords must have at least one non letter and digit character. " +
                            "Passwords must have at least one lowercase ('a'-'z'). " +
                            "Passwords must have at least one uppercase ('A'-'Z').",
                    Toast.LENGTH_LONG).show();
        } else if (!password.equals(repeatPassword)) {
            Toast.makeText(applicationContext,
                    "Repeat password does not match Password",
                    Toast.LENGTH_LONG).show();
        } else {
            AsyncTaskListener<CheckEmailAvailableResponse> isEmailValidListener = new AsyncTaskListener<CheckEmailAvailableResponse>() {
                @Override public void onPreExecute() {
                    centralProgress.setVisibility(View.VISIBLE);
                }
                @Override public void onPostExecute(CheckEmailAvailableResponse result) {
                    if(result.IsAvailable) {
                        AsyncTaskListener<RegisterResult> listener = new AsyncTaskListener<RegisterResult>() {
                            @Override public void onPreExecute() {
                                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                            }
                            @Override public void onPostExecute(final RegisterResult result) {
                                publishRegisterResult(result);
                                navigateToLoginActivity();
                                Toast.makeText(applicationContext,"We've sent you an email. Please verify your registration.", Toast.LENGTH_LONG).show();
                            }
                        };
                        new AccountService().register(applicationContext, email, password, password, listener, null);
                    }
                    else {
                        Toast.makeText(applicationContext,"Email is being used", Toast.LENGTH_LONG).show();
                    }
                    centralProgress.setVisibility(View.GONE);
                }
            };
            new AccountService().checkEmailAvailable(applicationContext, email, null, isEmailValidListener);
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity_.class);
        startActivity(intent);
    }

    private void publishRegisterResult(RegisterResult result) {
        Context applicationContext = getApplicationContext();
        Intent intent;

        if (!result.isSuccessful()) {
            intent = new Intent(RegisterActivity.this, RegisterActivity_.class);
            Toast.makeText(applicationContext, "Register unsuccessful ... please try again.", Toast.LENGTH_SHORT).show();
        } else {
            intent = new Intent(RegisterActivity.this, LoginActivity_.class);
            startActivity(intent);
            Toast.makeText(applicationContext, "Confirm email and login. Register successful.", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
    }
}
