package com.mosy.kalin.mosy;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_login)
public class LoginActivity
        extends BaseActivity {

    @ViewById(R.id.login_btnLogin)
    Button b1;
    @ViewById(R.id.login_btnCancel)
    Button b2;
    @ViewById(R.id.login_etEmail)
    EditText ed1;
    @ViewById(R.id.login_etPassword)
    EditText ed2;
    @ViewById(R.id.login_llInitialLoadingProgress)
    LinearLayout centralProgress;

    //    TextView tx1;
    int counter = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews(){

    }

    @Click(R.id.login_btnLogin)
    public void Login() {
        String email = ed1.getText().toString().trim();
        String password = ed2.getText().toString();
        Context applicationContext = getApplicationContext();

        if (!StringHelper.isNullOrWhitespace(email) && !StringHelper.isNullOrWhitespace(password)) {
            if (StringHelper.isEmailAddress(email)) {

                LoginBindingModel model = new LoginBindingModel(email, password);
                Toast emailNotConfirmedToast = Toast.makeText(this.applicationContext, "Email not confirmed", Toast.LENGTH_LONG);
                this.accountService.executeAssuredUserTokenValidOrRefreshed(
                        this.applicationContext, model,
                        this::showProgress,
                        this::userAuthenticationSucceeded,
                        this::showInvalidHostMessage,
                        emailNotConfirmedToast);
            } else
                Toast.makeText(applicationContext,
                        "Invalid Email address.",
                        Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(applicationContext,
                    "Email and password are required.",
                    Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        centralProgress.setVisibility(View.VISIBLE);
    }

    @Click(R.id.login_btnRegister)
    public void goToRegisterActivity(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);
        startActivity(intent);
    }

    private void userAuthenticationSucceeded() {
        Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show();
        centralProgress.setVisibility(View.GONE);

        Intent intent = new Intent(LoginActivity.this, LandingActivity_.class);
        startActivity(intent);
    }

    private void userAuthenticationFailed() {
        Toast.makeText(this, "Wrong username or password.", Toast.LENGTH_SHORT).show();
    }

    private void showInvalidHostMessage() {
        Toast.makeText(this, "Wrong username or password.", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.login_btnForgotPassword)
    public void forgotPassword() {
        String url = "https://tastespot.app/Account/ForgotPassword";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
