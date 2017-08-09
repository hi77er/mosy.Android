package com.mosy.kalin.mosy;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.Async.Tasks.TokenLoginAsyncTask;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.concurrent.ExecutionException;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    Button b1,b2;
    EditText ed1,ed2;

    TextView tx1;
    int counter = 3;

    @Click(R.id.login_btnLogin)
    public void Login() {
        String email = ((EditText)findViewById(R.id.login_etEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.login_etPassword)).getText().toString();
        Context applicationContext = getApplicationContext();

        if (!StringHelper.isNullOrWhitespace(email) && !StringHelper.isNullOrWhitespace(password)) {
            if (StringHelper.isEmailAddress(email)) {
                LoginBindingModel model = new LoginBindingModel(email, password);
                try {
                    new TokenLoginAsyncTask(applicationContext).execute(model).get();

                    Intent intent = new Intent(LoginActivity.this, VenuesActivity_.class);
                    startActivity(intent);
                }  catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = (Button)findViewById(R.id.login_btnLogin);
        ed1 = (EditText)findViewById(R.id.login_etEmail);
        ed2 = (EditText)findViewById(R.id.login_etPassword);

        b2 = (Button)findViewById(R.id.login_btnCancel);
        tx1 = (TextView)findViewById(R.id.login_tvAttemptsLeftPH);
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

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Click(R.id.login_btnRegister)
    public void goToRegisterActivity(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);
        startActivity(intent);
    }
}
