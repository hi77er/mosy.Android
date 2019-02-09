package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Services.Location.LocationResolver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@EActivity(R.layout.activity_promotions)
public class PromotionsActivity extends BaseActivity {

    private boolean afterViewsFinished = false;
    private boolean networkLost = false;

    private LocationResolver mLocationResolver;
    private Location lastKnownLocation;

    @ViewById(R.id.llInitialLoadingProgress)
    LinearLayout centralProgress;
    @ViewById(R.id.promotions_lPromotionsSwipeContainer)
    SwipeRefreshLayout promotionsSwipeContainer;
    @ViewById(R.id.promotions_rvPromotions)
    RecyclerView promotionsRecyclerView;
    @ViewById(R.id.promotions_llInvalidHost)
    LinearLayout invalidHostLayout;


    @AfterViews
    public void afterViews(){


        if (ConnectivityHelper.isConnected(applicationContext)) {
            networkLost = false;
            loadData();
        }
        else {
            networkLost = true;
            showInvalidHostLayout(); // For any case.. But should never happen because landing activity doesn't show links to this activity if no internet.
        }



        afterViewsFinished = true;
    }

    private void loadData() {
        this.invalidHostLayout.setVisibility(View.GONE);

        //
        this.mLocationResolver = new LocationResolver(this);
        this.mLocationResolver.onStart();
        refreshLastKnownLocation();

        //Intent intent = getIntent();
        //if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        //     query = intent.getStringExtra(SearchManager.QUERY);
        //    searchIsPromoted = null; //INFO: Normally get only promoted dishesWall, but when searched - search among all dishesWall
        //}

    }

    @Override
    protected void onNetworkAvailable() {
        if (afterViewsFinished && networkLost) {
            runOnUiThread(this::loadData);
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
    protected void onResume() {
        super.onResume();
        if (!ConnectivityHelper.isConnected(applicationContext)) {
            showInvalidHostLayout();
        }
    }

    private void showInvalidHostLayout() {
        this.invalidHostLayout.setVisibility(View.VISIBLE);
    }

    void refreshLastKnownLocation() {
        mLocationResolver.resolveLocation(this, location -> {
            this.lastKnownLocation = location;

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            Log.v("LOCATION CHANGE", timeStamp + " - IN ON_LOCATION_CHANGE, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
        });
    }


}
