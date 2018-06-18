package com.mosy.kalin.mosy;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.mosy.kalin.mosy.DAL.Http.Results.RegisterResult;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.AsyncTasks.AccountRegisterAsyncTask;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_register)
public class RegisterActivity
        extends BaseActivity {

    @Click(R.id.btnRegister)
    public void Register() {
        String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.etPassword)).getText().toString();
        Context applicationContext = getApplicationContext();

        //TODO: Check if User with this Username already exists calling an Api method created for this purpose.

        if (!StringHelper.isNullOrWhitespace(email) && !StringHelper.isNullOrWhitespace(password)) {
            if (StringHelper.isEmailAddress(email)) {
                if (StringHelper.isMatch("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*(_|[^\\w])).{6,}$", password)) {
                    AsyncTaskListener<RegisterResult> listener = new AsyncTaskListener<RegisterResult>() {
                        @Override
                        public void onPreExecute() {
                            //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPostExecute(final RegisterResult result) {
                            publishRegisterResult(result);
                        }
                    };
                    RegisterBindingModel model = new RegisterBindingModel(email, password, password);
                    new AccountRegisterAsyncTask(listener).execute(model);
                } else
                    Toast.makeText(applicationContext,
                            "Passwords must have at least one non letter and digit character. " +
                            "Passwords must have at least one lowercase ('a'-'z'). " +
                            "Passwords must have at least one uppercase ('A'-'Z').",
                            Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(applicationContext, "Invalid Email address.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(applicationContext, "Email and password are required.", Toast.LENGTH_SHORT).show();
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
