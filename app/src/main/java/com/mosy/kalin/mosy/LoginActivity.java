package com.mosy.kalin.mosy;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.DTOs.Enums.TokenResultStatus;
import com.mosy.kalin.mosy.DTOs.Results.TokenResult;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.AccountTokenLoginAsyncTask;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity
        extends BaseActivity {
    Button b1,b2;
    EditText ed1,ed2;

    TextView tx1;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b1 = findViewById(R.id.login_btnLogin);
        ed1 = findViewById(R.id.login_etEmail);
        ed2 = findViewById(R.id.login_etPassword);

        b2 = findViewById(R.id.login_btnCancel);
        tx1.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().equals("admin") &&
                        ed2.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                    counter--;
                    tx1.setText(Integer.toString(counter));

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
                try {
                    AsyncTaskListener<TokenResult> listener = new AsyncTaskListener<TokenResult>() {
                        @Override
                        public void onPreExecute() {
                            //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPostExecute(final TokenResult result) {
                            //TODO: Handle the case when no Internet connection
                            //TODO: Handle the case when Server does not respond
                            populateAuthenticationResult(result);
                        }
                    };
                    LoginBindingModel model = new LoginBindingModel(email, password);
                    new AccountTokenLoginAsyncTask(listener).execute(model);

                    Intent intent = new Intent(LoginActivity.this, WallActivity_.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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


    private void populateAuthenticationResult(TokenResult result) {
        if (result.Status == TokenResultStatus.Fail ||
                result.Status  == TokenResultStatus.Unknown ||
                result.Status  == TokenResultStatus.InvalidHostName)
            Toast.makeText(this, "Please try later. We are currently experiencing some troubles to connect you.", Toast.LENGTH_SHORT).show();
        else if (result.Status  == TokenResultStatus.Unauthorized)
            Toast.makeText(this, "Wrong username or password.", Toast.LENGTH_SHORT).show();
        else if (result.Status  == TokenResultStatus.Success)
            Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show();
    }
}
