package com.mosy.kalin.mosy;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.Services.AccountService;

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

//    TextView tx1;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews(){
//        tx1.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().equals("admin") &&
                        ed2.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

//                    tx1.setVisibility(View.VISIBLE);
//                    tx1.setBackgroundColor(Color.RED);
                    counter--;
//                    tx1.setText(Integer.toString(counter));

                    if (counter == 0) {
                        b1.setEnabled(false);
                    }
                }
            }
        });

        b2.setOnClickListener(v -> finish());
    }

    @Click(R.id.login_btnLogin)
    public void Login() {
        String email = ((EditText)findViewById(R.id.login_etEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.login_etPassword)).getText().toString();
        Context applicationContext = getApplicationContext();

        if (!StringHelper.isNullOrWhitespace(email) && !StringHelper.isNullOrWhitespace(password)) {
            if (StringHelper.isEmailAddress(email)) {

                LoginBindingModel model = new LoginBindingModel(email, password);
                new AccountService().executeAssuredUserTokenValidOrRefreshed(
                        this.applicationContext, model, null,
                        this::userAuthenticationSucceeded,
                        this::showInvalidHostMessage);
            } else
                Toast.makeText(applicationContext,
                        "Invalid Email address.",
                        Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(applicationContext,
                    "Email and password are required.",
                    Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.login_btnRegister)
    public void goToRegisterActivity(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);
        startActivity(intent);
    }

    private void userAuthenticationSucceeded() {
        Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, WallActivity_.class);
        startActivity(intent);
    }

    private void userAuthenticationFailed() {
        Toast.makeText(this, "Wrong username or password.", Toast.LENGTH_SHORT).show();
    }

    private void showInvalidHostMessage() {
        Toast.makeText(this, "Wrong username or password.", Toast.LENGTH_SHORT).show();
    }
}
