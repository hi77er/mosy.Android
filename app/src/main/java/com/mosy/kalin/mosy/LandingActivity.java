package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mosy.kalin.mosy.Services.AccountService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@SuppressLint("Registered")
@EActivity(R.layout.activity_landing)
public class LandingActivity
        extends AppCompatActivity
{

    private Context applicationContext;

    @Bean
    AccountService accountService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.applicationContext = getApplicationContext();
        this.accountService.refreshApiAuthenticationToken(applicationContext, () -> {
            //TODO: Delete before deploying to production!
            Toast.makeText(applicationContext, "WebApi authToken refreshed!", Toast.LENGTH_LONG).show();
        });
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
