package com.mosy.kalin.mosy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.DTOs.Role;
import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.AzureBlobService;
import com.mosy.kalin.mosy.Services.TableAccountsService;
import com.mosy.kalin.mosy.Services.UserProfileService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_userprofile)
public class UserProfileActivity
        extends BaseActivity {

    User user;

    @Bean
    UserProfileService userProfileService;
    @Bean
    AzureBlobService azureBlobService;
    @Bean
    TableAccountsService tableAccountsService;

    @ViewById(R.id.userProfile_name)
    TextView tvName;
    @ViewById(R.id.userProfile_email)
    TextView tvEmail;
    @ViewById(R.id.userProfile_image)
    ImageView userProfileImage;

    @ViewById(R.id.btnLogout)
    Button btnLogout;
    @ViewById(R.id.btnLoginViaFacebook)
    Button btnLoginViaFacebook;
    @ViewById(R.id.btnManageTablesAccountsMonitor)
    Button btnManageTablesAccountsMonitor;


    @AfterViews
    public void afterViews(){
        populateUserProfile();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(UserProfileActivity.this, LandingActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void populateUserProfile() {
        AsyncTaskListener<User> listener = new AsyncTaskListener<User>() {
            @Override public void onPreExecute() {
                onPreInfoLoading();
            }
            @Override public void onPostExecute(User userResult) {
                if(userResult != null) {
                    user = userResult;

                    publishUserInfo();
                    publishUserImageThumbnail();
                    publishFacebookBtn();
                    publishManageTableAccountsBtn();
                    onInfoLoaded();
                } else {
                    Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_LONG).show();
                    accountService.localLogoutUser(applicationContext);
                }
            }
        };
        this.userProfileService.getUserProfile(applicationContext, listener, this::navigateToLogin);
    }

    private void publishUserInfo() {
        String emailText = user.Username; // Email
        String usernameText = user.FirstName + " " + user.LastName;

        if (!StringHelper.isNullOrWhitespace(usernameText)) {
            tvName.setText(usernameText);
            tvEmail.setText(String.format("(%s)", emailText));
            tvEmail.setVisibility(View.VISIBLE);
        }
        else {
            tvName.setText(emailText);
            tvEmail.setVisibility(View.GONE);
        }
    }

    private void publishFacebookBtn() {
        String labelBtnLoginViaFacebook = "Connect Facebook account";
        if (this.user != null && this.user.ExternalLogins != null && this.user.ExternalLogins.size() > 0){
            labelBtnLoginViaFacebook = "Disconnect Facebook account";
        }

        this.btnLoginViaFacebook.setText(labelBtnLoginViaFacebook);
        this.btnLoginViaFacebook.setVisibility(View.VISIBLE);
    }

    private void publishManageTableAccountsBtn() {
        boolean isTableAccountOperator = false;

        if (user != null
                && user.Roles != null
                && user.Roles.size() > 0){

            for (Role role : user.Roles) {
                if (role.Name.toLowerCase().equals("tableaccountoperator")){
                    isTableAccountOperator = true;
                    break;
                }
            }

            if (isTableAccountOperator)
                this.btnManageTablesAccountsMonitor.setVisibility(View.VISIBLE);
        }
    }

    private void publishUserImageThumbnail() {
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

            this.azureBlobService.downloadUserProfileThumbnail(this.baseContext, this.user.ProfileImage.Id, ImageResolution.Format200x200, listener);
        } //IN ALL 3 'ELSE'S DO NOTHING. The Item has already been set the default image
    }

    private void onPreInfoLoading() {
        this.btnLogout.setVisibility(View.GONE);
    }

    private void onInfoLoaded() {
        this.btnLogout.setVisibility(isUserAuthenticated ? View.VISIBLE : View.GONE);
    }

    private void navigateToWall() {
        Intent intent = new Intent(UserProfileActivity.this, LandingActivity_.class);
        startActivity(intent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity_.class);
        startActivity(intent);
    }

    private void loadManagedVenues() {
        AsyncTaskListener<ArrayList<Venue>> apiCallResultListener = new AsyncTaskListener<ArrayList<Venue>>() {
            @Override public void onPreExecute() {
                btnManageTablesAccountsMonitor.setVisibility(View.GONE);
            }
            @Override public void onPostExecute(ArrayList<Venue> results) {
                if (results != null && results.size() > 0) {
                    if (results.size() == 1)
                        goToTableAccountsMonitor(results.get(0));
                    else
                        goToTableAccountVenues(results);

                    btnManageTablesAccountsMonitor.setVisibility(View.VISIBLE);
                }
            }
        };
        this.tableAccountsService.getTableAccountVenues(this.applicationContext, apiCallResultListener, this::onNetworkLost);
    }

    private void goToTableAccountVenues(ArrayList<Venue> venues) {
        Intent intent = new Intent(UserProfileActivity.this, OperatorTablesAccountsVenuesActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("tableAccountVenues", venues);
        startActivity(intent);
    }

    private void goToTableAccountsMonitor(Venue venue) {
        Intent intent = new Intent(UserProfileActivity.this, OperatorTablesAccountsActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("venue", venue);
        startActivity(intent);
    }

    @Click(R.id.btnLogout)
    public void logOut () {
        accountService.localLogoutUser(this.applicationContext);
        this.navigateToWall();
        Toast.makeText(this.applicationContext, "Logged out.", Toast.LENGTH_SHORT).show();
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

    @Click(R.id.btnLoginViaFacebook)
    public void loginViaFacebook() {
        String url = "https://www.treatspark.com/manage/manageLogins";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Click(R.id.btnManageTablesAccountsMonitor)
    public void goToTableAccountsManagementActivity() {
        loadManagedVenues();
    }

}


