package com.mosy.kalin.mosy;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.AccountService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_userprofile)
public class UserProfileActivity extends BaseActivity {

    @ViewById(R.id.userprofile_name)
    TextView tvUsername;


    @AfterViews
    public void afterViews(){
        populateUserProfile();
    }

    private void populateUserProfile() {
        AsyncTaskListener<User> listener = new AsyncTaskListener<User>() {
            @Override
            public void onPreExecute() {
            }

            @Override
            public void onPostExecute(User user) {
                if(user != null) {
                    String usernameText = user.FirstName + " " + user.LastName;
                    if (StringHelper.isNullOrWhitespace(usernameText))
                        usernameText = user.Email;
                    tvUsername.setText(usernameText);
                    tvUsername.setVisibility(View.VISIBLE);
                }
            }
        };
        new AccountService().getUserProfile(applicationContext,listener);
    }
}


