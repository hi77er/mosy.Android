package com.mosy.kalin.mosy;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.DTOs.Http.HttpResults.RegisterHttpResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.CheckEmailAvailableResult;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Helpers.StringHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

@EActivity(R.layout.activity_register)
public class RegisterActivity
        extends BaseActivity {

    private String email;

    @Bean
    AccountService accountService;

    @ViewById(R.id.etPassword)
    EditText etPassword;
    @ViewById(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @ViewById(R.id.etEmail)
    EditText etEmail;

    @ViewById(R.id.tvInfoMessage)
    TextView infoMessage;
    @ViewById(R.id.register_llInitialLoadingProgress)
    LinearLayout centralProgress;

    @ViewById(R.id.tvConditions)
    TextView tvConditions;
    @ViewById(R.id.cbConditions)
    CheckBox cbConditions;

    @ViewById(R.id.btnRegister)
    Button btnRegister;


    @AfterViews
    public void afterViews(){
        cbConditions.setText(StringHelper.empty());

        tvConditions.setText(
                Html.fromHtml(
                        "I have read and agree to the " +
                        "<a href='https://www.treatspark.com/legal/termsweb'>Terms and Conditions</a>" +
                        " and " +
                        "<a href='https://www.treatspark.com/legal/privacyweb'>Privacy Policy</a>" +
                        " including the " +
                        "<a href='https://www.treatspark.com/legal/disclaimer'>Disclaimer</a>" +
                        " and our" +
                        "<a href='https://www.treatspark.com/legal/cookies'>Cookies Policy</a>" +
                        ".")
        );
        tvConditions.setClickable(true);
        tvConditions.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void onPreCheckEmailAvailable() {
        centralProgress.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.GONE);
        infoMessage.setVisibility(View.GONE);
    }

    private void onCheckEmailAvailableFinished(CheckEmailAvailableResult result, String email, String password) {
        if (result.IsAvailable) {
            AsyncTaskListener<RegisterHttpResult> listener = new AsyncTaskListener<RegisterHttpResult>() {
                @Override public void onPreExecute() {
                    // onPreCheckEmailAvailable still active here
                }
                @Override public void onPostExecute(final RegisterHttpResult result) {
                    onRegistrationFinished(result);
                }
            };
            new AccountService().register(applicationContext, email, password, password, listener, null);
        }
        else {
            this.setInfoMessageColor(R.color.colorPrimaryApricot);
            this.toggleInfoMessage("Email is being used");
            centralProgress.setVisibility(View.GONE);
            btnRegister.setVisibility(View.VISIBLE);
        }
    }

    private void onRegistrationFinished(RegisterHttpResult result) {
        centralProgress.setVisibility(View.GONE);

        if (result.isSuccessful){
            this.toggleInfoMessage(StringHelper.empty());
            toLoginActivity();
        }
        else{
            this.setInfoMessageColor(R.color.colorPrimaryApricot);
            this.toggleInfoMessage("Registration unsuccessful. Please try again.");
        }

    }

    private void onInvalidHost() {
        this.setInfoMessageColor(R.color.colorPrimaryApricot);
        this.toggleInfoMessage("No internet.");
    }

    private void toLoginActivity()
    {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity_.class);
        intent.putExtra("confirmEmailNeeded", true);
        startActivity(intent);
    }

    private void setInfoMessageColor(int colorResId){
        this.infoMessage.setTextColor(getResources().getColor(colorResId));
    }
    private void toggleInfoMessage(String infoMessage){
        this.infoMessage.setVisibility(StringHelper.isNotNullOrEmpty(infoMessage) ? View.VISIBLE : View.GONE);
        this.infoMessage.setText(StringHelper.isNotNullOrEmpty(infoMessage) ? infoMessage : StringHelper.empty());
    }

    @Click(R.id.btnRegister)
    public void Register() {
        this.email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String repeatPassword = etPassword.getText().toString();
        Context applicationContext = getApplicationContext();

        if (StringHelper.isNullOrWhitespace(email) || StringHelper.isNullOrWhitespace(password)){
            this.setInfoMessageColor(R.color.colorPrimaryApricot);
            this.toggleInfoMessage("Email and password are required fields.");
        }
        else if (!StringHelper.isEmailAddress(email)){
            this.setInfoMessageColor(R.color.colorPrimaryApricot);
            this.toggleInfoMessage("Invalid Email address.");
        }
        else if (!StringHelper.isMatch("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*(_|[^\\w])).{6,}$", password)) {
            this.setInfoMessageColor(R.color.colorPrimaryApricot);
            this.toggleInfoMessage("Passwords must have at least one non letter and digit character. " +
                                   "Passwords must have at least one lowercase ('a'-'z'). " +
                                   "Passwords must have at least one uppercase ('A'-'Z')." +
                                   "Passwords must have at least one digit.");
        } else if (!password.equals(repeatPassword)) {
            this.setInfoMessageColor(R.color.colorPrimaryApricot);
            this.toggleInfoMessage("Repeat password does not match Password");
        } else if (!cbConditions.isChecked()) {
            this.setInfoMessageColor(R.color.colorPrimaryApricot);
            this.toggleInfoMessage("Please accept our Terms and Conditions");
        } else {
            AsyncTaskListener<CheckEmailAvailableResult> isEmailValidListener = new AsyncTaskListener<CheckEmailAvailableResult>() {
                @Override public void onPreExecute() {
                    onPreCheckEmailAvailable();
                }
                @Override public void onPostExecute(CheckEmailAvailableResult result) {
                    onCheckEmailAvailableFinished(result, email, password);
                }
            };
            this.accountService.checkEmailAvailable(applicationContext, email, isEmailValidListener, this::onInvalidHost);
        }
    }

}
