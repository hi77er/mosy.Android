package com.mosy.kalin.mosy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Services.AzureBlobService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_userprofile)
public class UserProfileActivity extends BaseActivity {

    @ViewById(R.id.userprofile_name)
    TextView tvUsername;

    @ViewById(R.id.userProfile_image)
    ImageView userProfileImage;

    User user;

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
            public void onPostExecute(User localUser) {
                if(localUser != null) {
                    String usernameText = localUser.FirstName + " " + localUser.LastName;
                    if (StringHelper.isNullOrWhitespace(usernameText))
                        usernameText = localUser.Email;
                    tvUsername.setText(usernameText);
                    tvUsername.setVisibility(View.VISIBLE);
                    user = localUser;
                    populateUserImageThumbnail();
                }
            }
        };
        new AccountService().getUserProfile(applicationContext,listener);
    }


    private void populateUserImageThumbnail() {
        if (this.user!= null &&
                this.user.ProfileImage != null &&
                StringHelper.isNotNullOrEmpty(this.user.ProfileImage.Id)) {

            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }
                @Override public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        userProfileImage.setImageTintList(null);
                        userProfileImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
//                        isUsingDefaultImageThumbnail = false;
                    } else {
//                        isUsingDefaultImageThumbnail = true;
                    }
//
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                }
            };

            new AzureBlobService().downloadUserProfileThumbnail(this.baseContext, this.user.ProfileImage.Id, ImageResolution.Format200x200, listener);
        } //IN ALL 3 'ELSE'S DO NOTHING. The Item has already been set the default image
    }

    @Click(R.id.userProfile_image)
    public void ImageClick() {
        if (    this.user!= null &&
                this.user.ProfileImage != null &&
                StringHelper.isNotNullOrEmpty(this.user.ProfileImage.Id)) {

            final Dialog nagDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);

            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }
                @Override public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                        ivPreview.setImageBitmap(bmp);
                        nagDialog.show();
                    } else
                        throw new NullPointerException("Image not found");
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                }
            };

            new AzureBlobService().downloadUserProfileThumbnail(this.baseContext, this.user.ProfileImage.Id, ImageResolution.FormatOriginal, listener);
        } //IN ALL 3 'ELSE'S DO NOTHING. The Item has already been set the default image

    }


    private void navigateToWallActivity() {
        Intent intent = new Intent(UserProfileActivity.this, LandingActivity_.class);
        startActivity(intent);
    }
}


