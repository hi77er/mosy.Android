package com.mosy.kalin.mosy;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mosy.kalin.mosy.DAL.Http.Results.RegisterResult;
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

    @Click(R.id.btnRegister)
    public void Register() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String repeatPassword = etPassword.getText().toString();
        Context applicationContext = getApplicationContext();

        //TODO: Check if User with this Username already exists calling an Api method created for this purpose.

        boolean emailOccupied = false;
        if (StringHelper.isNullOrWhitespace(email) || StringHelper.isNullOrWhitespace(password)) {
            Toast.makeText(applicationContext, "Email and password are required.", Toast.LENGTH_SHORT).show();
        } else if (!StringHelper.isEmailAddress(email)) {
            Toast.makeText(applicationContext, "Invalid Email address.", Toast.LENGTH_SHORT).show();
        } else if (emailOccupied) {
            Toast.makeText(applicationContext,"Email is being used", Toast.LENGTH_LONG).show();
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
            AsyncTaskListener<RegisterResult> listener = new AsyncTaskListener<RegisterResult>() {
                @Override
                public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPostExecute(final RegisterResult result) {
                    publishRegisterResult(result);
                    navigateToLoginActivity();
                    Toast.makeText(applicationContext,"We've sent you an email. Please verify your registration.", Toast.LENGTH_LONG).show();
                }
            };
            new AccountService().register(applicationContext, email, password, password, listener, null);
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity_.class);
        startActivity(intent);
    }

    private void publishRegisterResult(RegisterResult result) {
        if (!result.isSuccessful())
            Toast.makeText(this, "Register unsuccessful.", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this, "Register successfully.", Toast.LENGTH_SHORT).show();
        }

        Context applicationContext = getApplicationContext();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity_.class);
        startActivity(intent);
        Toast.makeText(applicationContext, "Confirm email and login again. Register successful.", Toast.LENGTH_SHORT).show();
    }
}
