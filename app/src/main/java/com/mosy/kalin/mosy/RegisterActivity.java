package com.mosy.kalin.mosy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.mosy.kalin.mosy.Async.Tasks.RegisterAsyncTask;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.concurrent.ExecutionException;

@EActivity(R.layout.activity_register)
public class RegisterActivity
        extends AppCompatActivity {

    @Click(R.id.btnRegister)
    public void Register() {
        String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.etPassword)).getText().toString();
        Context applicationContext = getApplicationContext();

        //TODO: Check if User with this Username already exists calling an Api method created for this purpose.

        if (!StringHelper.isNullOrWhitespace(email) && !StringHelper.isNullOrWhitespace(password)) {
            if (StringHelper.isEmailAddress(email)) {
                if (StringHelper.isMatch("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*(_|[^\\w])).{6,}$", password)) {
                    RegisterBindingModel model = new RegisterBindingModel(email, password, password);
                    try {
                        new RegisterAsyncTask(applicationContext).execute(model).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity_.class);
                    startActivity(intent);
                    Toast.makeText(applicationContext,
                            "Confirm email and login again. Register successful.",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(applicationContext,
                            "Passwords must have at least one non letter and digit character. " +
                            "Passwords must have at least one lowercase ('a'-'z'). " +
                            "Passwords must have at least one uppercase ('A'-'Z').",
                            Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(applicationContext,
                        "Invalid Email address.",
                        Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(applicationContext,
                    "Email and password are required.",
                    Toast.LENGTH_SHORT).show();
    }
}
