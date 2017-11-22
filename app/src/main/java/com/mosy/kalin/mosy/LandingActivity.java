package com.mosy.kalin.mosy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_landing)
public class LandingActivity
        extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_landing);
    }

    @Click(resName = "landing_btnVenues")
    public void NavigateVenuesSearch(){
        Intent intent = new Intent(LandingActivity.this, VenuesActivity_.class);
        intent.putExtra("DishesSearchModeActivated", false); //else find dishes
        startActivity(intent);
    }

    @Click(resName = "landing_btnDishes")
    public void NavigateDishesSearch(){
        Intent intent = new Intent(LandingActivity.this, VenuesActivity_.class);
        intent.putExtra("DishesSearchModeActivated", true); //else find dishes
        startActivity(intent);
    }
}
