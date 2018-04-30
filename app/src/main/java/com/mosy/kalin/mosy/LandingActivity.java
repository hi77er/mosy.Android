package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mosy.kalin.mosy.Services.AccountService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_landing)
public class LandingActivity
        extends AppCompatActivity
{

    private Context applicationContext;

    @Bean
    AccountService accountService;

    @ViewById(resName = "landing_llInitialLoadingProgress")
    LinearLayout centralProgress;
    @ViewById(resName = "landing_btnDishes")
    Button buttonDishes;
    @ViewById(resName = "landing_btnVenues")
    Button buttonVenues;

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
