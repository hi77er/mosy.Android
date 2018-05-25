package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mosy.kalin.mosy.Models.Views.SpinnerLocale;
import com.mosy.kalin.mosy.Helpers.LocaleHelper;
import com.mosy.kalin.mosy.Services.AccountService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import static android.content.pm.PackageManager.GET_META_DATA;

@SuppressLint("Registered")
@EActivity(R.layout.activity_landing)
public class LandingActivity
        extends BaseActivity
{
    private Context applicationContext;

    @Bean
    AccountService accountService;

    @ViewById(resName = "landing_btnDishes")
    Button buttonDishes;
    @ViewById(resName = "landing_btnVenues")
    Button buttonVenues;
    @ViewById(resName = "landing_llInitialLoadingProgress")
    LinearLayout centralProgress;
    @ViewById(resName = "landing_spLanguage")
    Spinner languagesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.applicationContext = getApplicationContext();
    }

    @AfterViews
    public void afterViews(){
        startActivityLoading();

        boolean authTokenExistsAndIsValid = this.accountService.refreshApiAuthenticationTokenExists(applicationContext, () -> {
            //TODO: Delete before deploying to production!
            endActivityLoading();
            Toast.makeText(applicationContext, "WebApi authToken refreshed!", Toast.LENGTH_LONG).show();
        });
        if (authTokenExistsAndIsValid)
            endActivityLoading();

        setupLanguagesSpinner();
    }

    private void setupLanguagesSpinner() {
        ArrayList<SpinnerLocale> spinnerLocaleList = new ArrayList<>();

        spinnerLocaleList.add(new SpinnerLocale("bg", "Bulgarian"));
        spinnerLocaleList.add(new SpinnerLocale("en", "English"));
        spinnerLocaleList.add(new SpinnerLocale("de", "German"));
        spinnerLocaleList.add(new SpinnerLocale("el", "Greek"));
        spinnerLocaleList.add(new SpinnerLocale("ru", "Russian"));
        spinnerLocaleList.add(new SpinnerLocale("es", "Spanish"));

        //fill data in spinner
        ArrayAdapter<SpinnerLocale> adapter = new ArrayAdapter<>(this.applicationContext, android.R.layout.simple_spinner_dropdown_item, spinnerLocaleList);
        this.languagesSpinner.setAdapter(adapter);

        String currentDefaultSpinnerLocale = LocaleHelper.getLanguage(applicationContext);
        //TODO: Linq-like syntax needed!
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

    private void startActivityLoading() {
        this.buttonDishes.setEnabled(false);
        this.buttonVenues.setEnabled(false);
        this.centralProgress.setVisibility(View.VISIBLE);
    }

    private void endActivityLoading() {

        this.buttonDishes.setEnabled(true);
        this.buttonVenues.setEnabled(true);
        this.centralProgress.setVisibility(View.INVISIBLE);
    }

    @Click(resName = "landing_btnVenues")
    public void NavigateVenuesSearch(){
        Intent intent = new Intent(LandingActivity.this, VenuesActivity_.class);
        intent.putExtra("DishesSearchModeActivated", false); //else find dishesWall
        startActivity(intent);
    }

    @Click(resName = "landing_btnDishes")
    public void NavigateDishesSearch(){
        Intent intent = new Intent(LandingActivity.this, VenuesActivity_.class);
        intent.putExtra("DishesSearchModeActivated", true); //else find dishesWall
        startActivity(intent);
    }
}
