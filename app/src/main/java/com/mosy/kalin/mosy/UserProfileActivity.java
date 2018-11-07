package com.mosy.kalin.mosy;

import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.AccountService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static com.mosy.kalin.mosy.WallActivity.DishesSearchModeActivated;

@EActivity(R.layout.activity_userprofile)
public class UserProfileActivity extends BaseActivity {

    @ViewById(R.id.userprofile_name)
    TextView tvUsername;

    @Click(R.id.btnLogout)
    public void logOut () {

        new AccountService().logoutUser(this.applicationContext);
        navigateToWallActivity();
        Toast.makeText(this.baseContext, "Logged out.", Toast.LENGTH_SHORT).show();
    }

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

    private void navigateToWallActivity() {
        Intent intent = new Intent(UserProfileActivity.this, LandingActivity_.class);
        startActivity(intent);
    }
}


