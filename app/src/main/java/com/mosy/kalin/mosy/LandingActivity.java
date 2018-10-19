package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.Views.SpinnerLocale;
import com.mosy.kalin.mosy.Helpers.LocaleHelper;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Services.Location.LocationResolver;

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

    @ViewById(R.id.landing_btnDishes)
    Button btnDishes;
    @ViewById(R.id.landing_btnVenues)
    Button btnVenues;
    @ViewById(R.id.landing_lButtons)
    LinearLayout buttonsLayout;
    @ViewById(R.id.landing_llInitialLoadingProgress)
    LinearLayout centralProgressLayout;
    @ViewById(R.id.landing_llInvalidHost)
    LinearLayout invalidHostLayout;
    @ViewById(R.id.landing_spLanguage)
    Spinner languagesSpinner;
    @ViewById(R.id.landing_btnUserProfile)
    Button btnProfile;
    @ViewById(R.id.landing_btnLoginSignUp)
    Button btnLogin;

    @AfterViews
    public void afterViews(){

        if (ConnectivityHelper.isConnected(applicationContext)) {
            ensureHasAuthenticationToken();
            networkLost = false;
        }
        else {
            showInvalidHostLayout();
            networkLost = true;
        }

        showProfileButton(this.isUserAuthenticated);

        setupLanguagesSpinner();
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
        super.onStop();
        this.connectionStateMonitor.onAvailable = null;
        this.connectionStateMonitor.onLost = null;
        this.activityStopped = true;
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

    private void ensureHasAuthenticationToken() {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
            this::showLoading,
            this::showButtonsLayout,
            this::showInvalidHostLayout);

    }

    private void setupLanguagesSpinner() {
        ArrayList<SpinnerLocale> spinnerLocaleList = new ArrayList<>();

        spinnerLocaleList.add(new SpinnerLocale("bg", constructLanguageSpinnerText(R.string.activity_landing_languageBgSpinner)));
        spinnerLocaleList.add(new SpinnerLocale("en", constructLanguageSpinnerText(R.string.activity_landing_languageEnUSSpinner)));
        spinnerLocaleList.add(new SpinnerLocale("de", constructLanguageSpinnerText(R.string.activity_landing_languageDeSpinner)));
        spinnerLocaleList.add(new SpinnerLocale("el", constructLanguageSpinnerText(R.string.activity_landing_languageElSpinner)));
        spinnerLocaleList.add(new SpinnerLocale("ru", constructLanguageSpinnerText(R.string.activity_landing_languageRuSpinner)));
        spinnerLocaleList.add(new SpinnerLocale("es", constructLanguageSpinnerText(R.string.activity_landing_languageEsSpinner)));

        //fill data in spinner
        ArrayAdapter<SpinnerLocale> adapter = new ArrayAdapter<>(this.applicationContext, R.layout.languages_spinner_activity_landing, spinnerLocaleList);
        adapter.setDropDownViewResource(R.layout.languages_spinner_activity_landing);
        this.languagesSpinner.setAdapter(adapter);

        String currentDefaultSpinnerLocale = LocaleHelper.getLanguage(applicationContext);

        //TODO: Linq-like syntax needed!
        //Stream.of(cuisineRegionFilters).filter(filter -> filter.Id.equals(filterId)).single();
        for(SpinnerLocale sLocale : spinnerLocaleList){
            if (sLocale.getId().equals(currentDefaultSpinnerLocale)){
                this.languagesSpinner.setSelection(adapter.getPosition(sLocale));
                break;
            }
        }

        this.languagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newLocaleId = spinnerLocaleList.get(i).getId();
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
        this.buttonsLayout.setVisibility(View.GONE);
        this.invalidHostLayout.setVisibility(View.GONE);
        this.btnDishes.setEnabled(false);
        this.btnVenues.setEnabled(false);
        this.centralProgressLayout.setVisibility(View.VISIBLE);
    }

    private void showButtonsLayout() {
        this.buttonsLayout.setVisibility(View.VISIBLE);
        this.invalidHostLayout.setVisibility(View.GONE);
        this.btnDishes.setEnabled(true);
        this.btnVenues.setEnabled(true);
        this.centralProgressLayout.setVisibility(View.GONE);
        //TODO: Delete before deploying to production!
//        Toast.makeText(applicationContext, "WebApi authToken refreshed!", Toast.LENGTH_LONG).show();
    }

    private void showInvalidHostLayout() {
        this.buttonsLayout.setVisibility(View.GONE);
        this.invalidHostLayout.setVisibility(View.VISIBLE);
        this.centralProgressLayout.setVisibility(View.GONE);
        if (!this.activityStopped)
            new LocationResolver(this).showWifiSettingsDialog(applicationContext);
        //TODO: Delete before deploying to production!
        Toast.makeText(applicationContext, "No internet!", Toast.LENGTH_LONG).show();
    }

    private void showProfileButton(boolean isUserAuthenticated) {
        this.btnProfile.setVisibility(isUserAuthenticated ? View.VISIBLE : View.GONE);
        this.btnLogin.setVisibility(isUserAuthenticated ? View.GONE : View.VISIBLE);
    }

    @Click(R.id.landing_ivLogo)
    public void click_ivLogo(){
        SharedPreferences preferences = applicationContext.getSharedPreferences(getString(R.string.pref_collectionName_developersMode), MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();

        if (logoClicksCount < 50)
            logoClicksCount ++;
        else if (logoClicksCount == 50) {
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

    private void navigateToWallActivity(boolean isDishMode) {
        Intent intent = new Intent(LandingActivity.this, WallActivity_.class);
        intent.putExtra("DishesSearchModeActivated", isDishMode); //else find dishesWall
        startActivity(intent);
    }

}
