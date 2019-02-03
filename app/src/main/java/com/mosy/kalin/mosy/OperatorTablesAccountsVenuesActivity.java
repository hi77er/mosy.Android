package com.mosy.kalin.mosy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.mosy.kalin.mosy.Adapters.OperatorTableAccountsVenuesAdapter;
import com.mosy.kalin.mosy.CustomControls.Support.RecyclerViewItemsClickSupport;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Views.ItemModels.OperatorTableAccountsVenueItem;
import com.mosy.kalin.mosy.Services.TableAccountsService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Objects;

@EActivity(R.layout.activity_operator_table_accounts_venues)
public class OperatorTablesAccountsVenuesActivity
        extends BaseActivity {

    private boolean afterViewsFinished = false;
    private boolean networkLost = false;
    private boolean managedVenuesLoadingStillInAction = false;

    @Extra
    public String username;
    @Extra
    public ArrayList<Venue> tableAccountVenues;

    @Bean
    TableAccountsService tableAccountsService;
    @Bean
    OperatorTableAccountsVenuesAdapter operatorTableAccountsVenuesAdapter;

    @ViewById(R.id.llInitialLoadingProgress)
    LinearLayout progressLayout;
    @ViewById(R.id.venuesMonitors_lVenuesSwipeContainer)
    SwipeRefreshLayout venuesLayout;
    @ViewById(R.id.venuesMonitors_lvTableAccountsVenues)
    RecyclerView tableAccountsVenuesView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews(){

        if (ConnectivityHelper.isConnected(applicationContext)) {
            networkLost = false;

            RecyclerViewItemsClickSupport.addTo(this.tableAccountsVenuesView).setOnItemClickListener((recyclerView, position, v) -> {
                OperatorTableAccountsVenueItem itemClicked = (OperatorTableAccountsVenueItem) operatorTableAccountsVenuesAdapter.getItemAt(position);

                Intent intent = new Intent(OperatorTablesAccountsVenuesActivity.this, OperatorTablesAccountsActivity_.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("username", username);
                intent.putExtra("venue", itemClicked.Venue);
                startActivity(intent);
            });

            this.tableAccountsVenuesView.setAdapter(operatorTableAccountsVenuesAdapter);
            this.tableAccountsVenuesView.setLayoutManager(new GridLayoutManager(this.baseContext, 1));
            DividerItemDecoration itemDecorator = new DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.applicationContext, R.drawable.wall_divider)));
            this.tableAccountsVenuesView.addItemDecoration(itemDecorator);
//            this.tableAccountsVenuesView.addOnScrollListener(tableAccountsVenuesScrollListener);

            this.operatorTableAccountsVenuesAdapter.setSwipeRefreshLayout(venuesLayout);
            this.operatorTableAccountsVenuesAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (!managedVenuesLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext)) {
                    operatorTableAccountsVenuesAdapter.clearItems();

                    //INFO: REFRESH INITIAL LOAD
                    loadManagedVenues();
                }
                venuesLayout.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            if (tableAccountVenues != null && tableAccountVenues.size() > 0){
                progressLayout.setVisibility(View.GONE);
                venuesLayout.setVisibility(View.VISIBLE);

                for (Venue item : tableAccountVenues) {
                    operatorTableAccountsVenuesAdapter.addTableAccountsVenueItem(item);
                }
                managedVenuesLoadingStillInAction = false;
            }
        }
        else {
            networkLost = true;
            showNoInternetLayout();
        }

        afterViewsFinished = true;
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onNetworkAvailable() {
        if (afterViewsFinished && networkLost) {
            runOnUiThread(this::loadManagedVenues);
            networkLost = false;
        }
    }

    @Override
    protected void onNetworkLost() {
        if (afterViewsFinished) {
            runOnUiThread(this::showNoInternetLayout);
        }
        networkLost = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.connectionStateMonitor.onAvailable = null;
        this.connectionStateMonitor.onLost = null;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    private void showNoInternetLayout() {

    }

    private void loadManagedVenues() {
        AsyncTaskListener<ArrayList<Venue>> apiCallResultListener = new AsyncTaskListener<ArrayList<Venue>>() {
            @Override public void onPreExecute() {
                venuesLayout.setVisibility(View.GONE);
                progressLayout.setVisibility(View.VISIBLE);
            }
            @Override public void onPostExecute(ArrayList<Venue> results) {
                progressLayout.setVisibility(View.GONE);
                venuesLayout.setVisibility(View.VISIBLE);

                if (results != null && results.size() > 0) {
                    tableAccountVenues = results;

                    for (Venue item : results) {
                        operatorTableAccountsVenuesAdapter.addTableAccountsVenueItem(item);
                    }

                    managedVenuesLoadingStillInAction = false;
                }
            }
        };
        this.tableAccountsService.getTableAccountVenues(this.applicationContext, apiCallResultListener, this::onNetworkLost);
    }

}
