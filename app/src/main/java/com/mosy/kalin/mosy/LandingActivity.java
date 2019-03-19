package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Views.SpinnerLocale;
import com.mosy.kalin.mosy.Helpers.LocaleHelper;
import com.mosy.kalin.mosy.Services.Location.LocationResolver;
import com.mosy.kalin.mosy.Services.TableAccountsService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@SuppressLint("Registered")
@EActivity(R.layout.activity_landing)
public class LandingActivity
        extends BaseActivity
{
    int logoClicksCount = 0;
    boolean afterViewsFinished = false;
    boolean networkLost = false;


    @Bean
    TableAccountsService tableAccountsService;

    @ViewById(R.id.landing_btnDishes)
    Button btnDishes;
    @ViewById(R.id.landing_btnVenues)
    Button btnVenues;
    @ViewById(R.id.landing_lButtons)
    LinearLayout buttonsLayout;
    @ViewById(R.id.landing_llBottomBtns)
    LinearLayout bottomButtonsLayout;

    @ViewById(R.id.landing_tvLogo)
    TextView tvLogo;

    @ViewById(R.id.landing_llInitialLoadingProgress)
    LinearLayout centralProgressLayout;
    @ViewById(R.id.landing_rlInvalidHost)
    RelativeLayout invalidHostLayout;
    @ViewById(R.id.landing_rlTemporarilyDown)
    RelativeLayout temporarilyDownLayout;
    @ViewById(R.id.landing_spLanguage)
    Spinner languagesSpinner;
    @ViewById(R.id.landing_btnUserProfile)
    Button btnProfile;
    @ViewById(R.id.landing_btnLoginSignUp)
    Button btnLogin;
    @ViewById(R.id.btnWork)
    Button btnWork;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews(){
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/eurof35.ttf");
        tvLogo.setTypeface(typeface);

        if (ConnectivityHelper.isConnected(applicationContext)) {
            try {
                this.ensureHasAuthenticationToken();
            }
            catch (NullPointerException ex){
                this.showTemporarilyDownLayout();
            }

            networkLost = false;
        }
        else {
            this.showInvalidHostLayout();
            networkLost = true;
        }

        ArrayList<SpinnerLocale> spinnerLocalesList = new ArrayList<>();
        for (String supportedCultureId : LocaleHelper.SUPPORTED_LOCALES.keySet()) {
            int localeResourceId = LocaleHelper.SUPPORTED_LOCALES.get(supportedCultureId);
            spinnerLocalesList.add(new SpinnerLocale(supportedCultureId, constructLanguageSpinnerText(localeResourceId)));
        }
        if (spinnerLocalesList.size() > 0)
            setupLanguagesSpinner(spinnerLocalesList);

        afterViewsFinished = true;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.activityStopped = false;
    }

    @Override
    protected void onNetworkAvailable() {
        if (afterViewsFinished && networkLost){
            runOnUiThread(this::ensureHasAuthenticationToken);
            networkLost = false;
        }
    }

    @Override
    protected void onNetworkLost() {
        if (afterViewsFinished) {
            runOnUiThread(this::showInvalidHostLayout);
        }
        networkLost = true;
    }

    @Override
    public void onStop() {
        this.connectionStateMonitor.onAvailable = null;
        this.connectionStateMonitor.onLost = null;
        this.activityStopped = true;

        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onResume(){
        logoClicksCount = 0;
        super.onResume();
    }

    private void ensureHasAuthenticationToken() throws NullPointerException {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                this::showLoading,
                this::showButtonsLayout,
                this::showInvalidHostLayout);
    }

    private void setupLanguagesSpinner(ArrayList<SpinnerLocale> localesList) {
        //fill data in spinner
        ArrayAdapter<SpinnerLocale> adapter = new ArrayAdapter<>(this.applicationContext, R.layout.languages_spinner_activity_landing, localesList);
        adapter.setDropDownViewResource(R.layout.languages_spinner_activity_landing);
        this.languagesSpinner.setAdapter(adapter);

        String currentDefaultSpinnerLocale = LocaleHelper.getLanguage(applicationContext);

        //TODO: Linq-like syntax needed!
        //Stream.of(cuisineRegionFilters).filter(filter -> filter.Id.equals(filterId)).single();
        for(SpinnerLocale sLocale : localesList){
            if (sLocale.getId().equals(currentDefaultSpinnerLocale)){
                this.languagesSpinner.setSelection(adapter.getPosition(sLocale));
                break;
            }
        }

        this.languagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newLocaleId = localesList.get(i).getId();
                if (!currentDefaultSpinnerLocale.equals(newLocaleId)){
                    LocaleHelper.setLocale(applicationContext, newLocaleId);
                    recreate();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private String constructLanguageSpinnerText(int resourceId) {
        String applicationDefaultLocaleTranslation = StringHelper.getStringAppDefaultLocale(this, resourceId);
        String deviceDefaultLocaleTranslation = StringHelper.getStringDeviceDefaultLocale(this, resourceId);
        String label = applicationDefaultLocaleTranslation + " (" + deviceDefaultLocaleTranslation + ")";
        return label;
    }

    private void showLoading() {
        this.centralProgressLayout.setVisibility(View.VISIBLE);
        this.invalidHostLayout.setVisibility(View.GONE);
        this.buttonsLayout.setVisibility(View.GONE);
        this.bottomButtonsLayout.setVisibility(View.GONE);

        this.btnDishes.setEnabled(false);
        this.btnVenues.setEnabled(false);

        this.btnProfile.setVisibility(View.GONE);
        this.btnLogin.setVisibility(View.GONE);
        this.btnWork.setVisibility(View.GONE);
    }

    private void showButtonsLayout() {
        this.centralProgressLayout.setVisibility(View.GONE);
        this.invalidHostLayout.setVisibility(View.GONE);

        this.buttonsLayout.setVisibility(View.VISIBLE);
        this.bottomButtonsLayout.setVisibility(View.VISIBLE);

        this.btnDishes.setEnabled(true);
        this.btnVenues.setEnabled(true);

        this.btnProfile.setVisibility(isUserAuthenticated ? View.VISIBLE : View.GONE);
        this.btnLogin.setVisibility(isUserAuthenticated ? View.GONE : View.VISIBLE);

        if (isUserAuthenticated) publishManageTableAccountsBtn();

        //TODO: Delete before deploying to production!
//        Toast.makeText(applicationContext, "WebApi authToken refreshed!", Toast.LENGTH_LONG).show();
    }

    private void showInvalidHostLayout() {
        this.bottomButtonsLayout.setVisibility(View.GONE);
        this.buttonsLayout.setVisibility(View.GONE);
        this.invalidHostLayout.setVisibility(View.VISIBLE);
        this.centralProgressLayout.setVisibility(View.GONE);
        if (!this.activityStopped)
            new LocationResolver(this).showWifiSettingsDialog(applicationContext);
    }

    private void showTemporarilyDownLayout() {
        this.bottomButtonsLayout.setVisibility(View.GONE);
        this.buttonsLayout.setVisibility(View.GONE);
        this.temporarilyDownLayout.setVisibility(View.VISIBLE);
        this.centralProgressLayout.setVisibility(View.GONE);
    }

    private void loadManagedVenues() {
        AsyncTaskListener<ArrayList<Venue>> apiCallResultListener = new AsyncTaskListener<ArrayList<Venue>>() {
            @Override public void onPreExecute() {
                showLoading();
            }
            @Override public void onPostExecute(ArrayList<Venue> results) {
                if (results != null && results.size() > 0) {
                    if (results.size() == 1)
                        goToTableAccountsMonitor(results.get(0));
                    else
                        goToTableAccountVenues(results);

                    showButtonsLayout();
                }
            }
        };
        this.tableAccountsService.getTableAccountVenues(this.applicationContext, apiCallResultListener, this::onNetworkLost);
    }

    private void publishManageTableAccountsBtn() {
        boolean isTableAccountOperator = false;

        ArrayList<String> userRoles = this.accountService.getUserRoles(this.applicationContext);

        if (userRoles != null && userRoles.size() > 0){

            for (String role : userRoles) {
                if (role.toLowerCase().equals("tableaccountoperator")){
                    isTableAccountOperator = true;
                    break;
                }
            }

            if (isTableAccountOperator)
                this.btnWork.setVisibility(isUserAuthenticated ? View.VISIBLE : View.GONE);
        }
    }

    private void navigateToWallActivity(boolean isDishMode) {
        Intent intent = new Intent(LandingActivity.this, WallActivity_.class);
        intent.putExtra("DishesSearchModeActivated", isDishMode); //else find dishesWall
        startActivity(intent);
    }

    private void goToTableAccountVenues(ArrayList<Venue> venues) {
        Intent intent = new Intent(LandingActivity.this, OperatorTablesAccountsVenuesActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("tableAccountVenues", venues);
        startActivity(intent);
    }

    private void goToTableAccountsMonitor(Venue venue) {
        Intent intent = new Intent(LandingActivity.this, OperatorTablesAccountsActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("venue", venue);
        startActivity(intent);
    }

    @Click(R.id.btnWork)
    public void goToTableAccountsManagementActivity() {
        loadManagedVenues();
    }

    @Click(R.id.landing_tvLogo)
    public void click_ivLogo(){
        SharedPreferences preferences = applicationContext.getSharedPreferences(getString(R.string.pref_collectionName_developersMode), MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();

        if (logoClicksCount < 50)
            logoClicksCount ++;
        else if (logoClicksCount == 50) {
            logoClicksCount = 0;
            boolean devModeEnabledPrefValue = !preferences.getBoolean(getString(R.string.pref_developersModeEnabled), false);
            prefEditor.putBoolean(getString(R.string.pref_developersModeEnabled), devModeEnabledPrefValue);
            prefEditor.apply();

            Toast.makeText(applicationContext, "Developers' mode " + (devModeEnabledPrefValue ? "Enabled!" : "Disabled!"), Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.landing_btnVenues)
    public void navigateVenuesSearch(){
        navigateToWallActivity(false);
    }

    @Click(R.id.landing_btnDishes)
    public void navigateDishesSearch(){
        navigateToWallActivity(true);
    }

    @Click(R.id.landing_btnLoginSignUp)
    public void navigateLogin(){
        Intent intent = new Intent(LandingActivity.this, LoginActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.landing_btnUserProfile)
    public void navigateProfile(){
        Intent intent = new Intent(LandingActivity.this, UserProfileActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.landing_ibSocialFacebook)
    public void navigateFacebook(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(getString(R.string.link_social_facebook)));
        startActivity(intent);
    }

    @Click(R.id.landing_ibSocialInstagram)
    public void navigateInstagram(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(getString(R.string.link_social_instagram)));
        startActivity(intent);
    }

    @Click(R.id.landing_ibSocialTwitter)
    public void navigateTwitter(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(getString(R.string.link_social_twitter)));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
