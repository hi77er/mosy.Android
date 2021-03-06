package com.mosy.kalin.mosy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.mosy.kalin.mosy.Adapters.OperatorTableAccountOrdersAdapter;
import com.mosy.kalin.mosy.DTOs.Order;
import com.mosy.kalin.mosy.DTOs.OrderMenuItem;
import com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults.OrderItemStatusChangedResult;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.SignalR.AccountOpenerSignalR_;
import com.mosy.kalin.mosy.Services.SignalR.AccountOperatorSignalR;
import com.mosy.kalin.mosy.Services.SignalR.AccountOperatorSignalR_;
import com.mosy.kalin.mosy.Services.SignalR.VenueHostSignalR_;
import com.mosy.kalin.mosy.Services.TableAccountsService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Objects;


@EActivity(R.layout.activity_operator_table_account_orders)
public class OperatorTableAccountOrdersActivity
        extends BaseActivity {

    private boolean afterViewsFinished = false;
    private boolean networkLost = false;
    private boolean tableAccountsLoadingStillInAction = false;

    public ArrayList<Order> orders;

    @Extra
    public Venue venue;
    @Extra
    public TableAccount tableAccount;

    @Bean
    TableAccountsService tableAccountsService;
    @Bean
    OperatorTableAccountOrdersAdapter operatorTableAccountOrdersAdapter;

    @ViewById(R.id.llInitialLoadingProgress)
    LinearLayout progressLayout;
    @ViewById(R.id.tableAccountOrders_llEmptyMessageLayout)
    LinearLayout emptyLayout;
    @ViewById(R.id.tableAccountOrders_lOrdersSwipeContainer)
    SwipeRefreshLayout tableAccountOrdersLayout;
    @ViewById(R.id.operatorTableAccountOrders_lvOrders)
    RecyclerView ordersView;

    AccountOperatorSignalR mSignalRService;
    /** Defines callbacks for service binding, passed to bindService() */
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to AccountOpenerSignalR, cast the IBinder and get AccountOpenerSignalR instance
            AccountOperatorSignalR.LocalBinder binder = (AccountOperatorSignalR.LocalBinder) service;
            mSignalRService = binder.getService();

            setupSignalRService();
            setBound(true);
        }

        @Override public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void setupSignalRService() {
        mSignalRService.setEventListeners(venue.Id);

        mSignalRService.setOnOrderItemStatusOperatorChanged(new AsyncTaskListener<OrderItemStatusChangedResult>() {
            @Override public void onPreExecute() { }
            @Override public void onPostExecute(OrderItemStatusChangedResult itemStatusChangedResult) {
                if (operatorTableAccountOrdersAdapter != null){
                    operatorTableAccountOrdersAdapter.changeItemStatus(itemStatusChangedResult.Id, itemStatusChangedResult.Status);

                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && v != null) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else if (v != null){
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                }
            }
        });

        operatorTableAccountOrdersAdapter.setSignalRService(mSignalRService);
    }

    private boolean mBound = false;
    private void setBound(boolean value){
        this.mBound = value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if (!mBound) {
            Intent intent = AccountOperatorSignalR_.intent(this).get();
            this.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            //this.startService(intent);
        }
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews(){

        if (ConnectivityHelper.isConnected(applicationContext)) {
            networkLost = false;

//            RecyclerViewItemsClickSupport.addTo(this.ordersView).setOnItemClickListener((recyclerView, position, v) -> {
//                OperatorTableAccountItem itemClicked = (OperatorTableAccountItem)operatorTableAccountOrdersAdapter.getItemAt(position);
//
//                Intent intent = new Intent(OperatorTablesAccountsActivity.this, OperatorTableAccountOrdersActivity_.class);
//                intent.putExtra("tableAccount", itemClicked.tableAccount);
//                startActivity(intent);
//            });

            this.ordersView.setAdapter(operatorTableAccountOrdersAdapter);
            this.ordersView.setLayoutManager(new GridLayoutManager(this.baseContext, 1));
            DividerItemDecoration itemDecorator = new DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.applicationContext, R.drawable.wall_divider_primary)));
            this.ordersView.addItemDecoration(itemDecorator);
//            this.tableAccountsVenuesView.addOnScrollListener(tableAccountsVenuesScrollListener);

            this.operatorTableAccountOrdersAdapter.setSwipeRefreshLayout(tableAccountOrdersLayout);
            this.operatorTableAccountOrdersAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (!tableAccountsLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext)) {
                    //INFO: REFRESH INITIAL LOAD
                    reloadTableAccountOrders();
                }
                tableAccountOrdersLayout.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            this.reloadTableAccountOrders();
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
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    @Override
    protected void onNetworkAvailable() {
        if (afterViewsFinished && networkLost) {
            runOnUiThread(this::reloadTableAccountOrders);
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
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(OperatorTableAccountOrdersActivity.this, OperatorTablesAccountsActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        intent.putExtra("venue", this.venue);

        startActivity(intent);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {
        this.connectionStateMonitor.onAvailable = null;
        this.connectionStateMonitor.onLost = null;

        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mBound) {
            stopService(AccountOperatorSignalR_.intent(this).get());
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void showNoInternetLayout() {

    }

    private void reloadTableAccountOrders() {
        operatorTableAccountOrdersAdapter.clearItems();

        if (this.tableAccount != null){
            AsyncTaskListener<ArrayList<Order>> apiCallResultListener = new AsyncTaskListener<ArrayList<Order>>() {
                @Override public void onPreExecute() {
                    tableAccountOrdersLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.VISIBLE);
                }
                @Override public void onPostExecute(ArrayList<Order> results) {
                    progressLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    tableAccountOrdersLayout.setVisibility(View.VISIBLE);

                    if (results != null && results.size() > 0) {
                        orders = results;

                        for (Order order : results) {
                            for (OrderMenuItem item : order.orderMenuItems) {
                                operatorTableAccountOrdersAdapter.addTableAccountItem(item);
                            }
                        }

                        tableAccountsLoadingStillInAction = false;
                    }
                }
            };
            this.tableAccountsService.getOrders(this.applicationContext, apiCallResultListener, this::onNetworkLost, tableAccount.Id);
        }
    }








}
