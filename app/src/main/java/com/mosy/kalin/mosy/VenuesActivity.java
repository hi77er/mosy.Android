package com.mosy.kalin.mosy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.Venue;

import java.util.ArrayList;



public class VenuesActivity extends Activity {
    Venue[] venues = new Venue[31];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);

        this.venues[0] = new Venue("The Godfather", "Bar");
        this.venues[1] = new Venue("Happy", "Bar");
        this.venues[2] = new Venue("Mc Donalds", "Fast Food");
        this.venues[3] = new Venue("Raffy", "Restaurant & Bar");
        this.venues[4] = new Venue("Donut", "Restaurant");
        this.venues[5] = new Venue("Pizza Hut", "Pizzerria");
        this.venues[6] = new Venue("Lovin Hut", "Snack Bar");
        this.venues[7] = new Venue("Spaghetti Kitchen", "Restaurant");
        this.venues[8] = new Venue("Venue9", "Bar");
        this.venues[9] = new Venue("Venue10", "Bar");
        this.venues[10] = new Venue("Venue11", "Bar");
        this.venues[11] = new Venue("Happy", "Bar");
        this.venues[12] = new Venue("Mc Donalds", "Fast Food");
        this.venues[13] = new Venue("Raffy", "Restaurant & Bar");
        this.venues[14] = new Venue("Donut", "Restaurant");
        this.venues[15] = new Venue("Pizza Hut", "Pizzerria");
        this.venues[16] = new Venue("Lovin Hut", "Snack Bar");
        this.venues[17] = new Venue("Spaghetti Kitchen", "Restaurant");
        this.venues[18] = new Venue("Venue9", "Bar");
        this.venues[19] = new Venue("Venue10", "Bar");
        this.venues[20] = new Venue("Venue11", "Bar");
        this.venues[21] = new Venue("Happy", "Bar");
        this.venues[22] = new Venue("Mc Donalds", "Fast Food");
        this.venues[23] = new Venue("Raffy", "Restaurant & Bar");
        this.venues[24] = new Venue("Donut", "Restaurant");
        this.venues[25] = new Venue("Pizza Hut", "Pizzerria");
        this.venues[26] = new Venue("Lovin Hut", "Snack Bar");
        this.venues[27] = new Venue("Spaghetti Kitchen", "Restaurant");
        this.venues[28] = new Venue("Venue9", "Bar");
        this.venues[29] = new Venue("Venue10", "Bar");
        this.venues[30] = new Venue("Venue11", "Bar");

        VenuesAdapter cus = new VenuesAdapter(this, venues);
        ListView lv = (ListView)findViewById(R.id.venues_lvVenues);
        lv.setAdapter(cus);
    }

}
