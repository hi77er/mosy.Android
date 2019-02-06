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
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.ClientTableAccountOrdersAdapter;
import com.mosy.kalin.mosy.DTOs.Order;
import com.mosy.kalin.mosy.DTOs.OrderMenuItem;
import com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults.TableAccountStatusResult;
import com.mosy.kalin.mosy.DTOs.Table;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.WallVenue;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Views.ItemModels.ClientTableAccountItem;
import com.mosy.kalin.mosy.Models.Views.ItemModels.OperatorTableAccountOrderItem;
import com.mosy.kalin.mosy.Services.SignalR.SignalRService;
import com.mosy.kalin.mosy.Services.SignalR.SignalRService_;
import com.mosy.kalin.mosy.Services.TableAccountsService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Objects;


@EActivity(R.layout.activity_client_table_account_orders)
public class ClientTableAccountOrdersActivity
        extends BaseActivity {

    private boolean afterViewsFinished = false;
    private boolean networkLost = false;
    private boolean tableAccountsLoadingStillInAction = false;


    public ArrayList<Order> orders;

    @Extra
    public TableAccount tableAccount;
    @Extra
    public ArrayList<String> newlySelectedMenuItemIds;
    @Extra
    WallVenue wallVenue;
    @Extra
    String selectedMenuListId;
    @Extra
    Table selectedTable;

    @Bean
    TableAccountsService tableAccountsService;
    @Bean
    ClientTableAccountOrdersAdapter clientTableAccountOrdersAdapter;

    @ViewById(R.id.clientTableAccountOrders_tvAccountStatus)
    TextView accountStatus;

    @ViewById(R.id.llInitialLoadingProgress)
    LinearLayout progressLayout;
    @ViewById(R.id.clientTableAccountOrders_llEmptyMessageLayout)
    LinearLayout emptyLayout;
    @ViewById(R.id.clientTableAccountOrders_lOrdersSwipeContainer)
    SwipeRefreshLayout tableAccountOrdersLayout;
    @ViewById(R.id.clientTableAccountOrders_lvOrders)
    RecyclerView ordersView;
    @ViewById(R.id.clientTableAccountOrders_tvAccountStatus)
    TextView tvAccountStatus;

    SignalRService mSignalRService;
    /** Defines callbacks for service binding, passed to bindService() */
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mSignalRService = binder.getService();
            setBound(true);
        }

        @Override public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private boolean mBound = false;
    private void setBound(boolean value){
        this.mBound = value;
        if (value){
            // Call a method from the SignalRService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            // mSignalRService.pingOrdersHub("ping Test123"); // testing the connection

            mSignalRService.setEventListeners(super.username);

            mSignalRService.setOnTAStatusChangedClient(new AsyncTaskListener<TableAccountStatusResult>() {
                @Override public void onPreExecute() { }
                @Override public void onPostExecute(TableAccountStatusResult result) {
                    tableAccount = new TableAccount();
                    tableAccount.Id = result.TableAccountId;
                    tableAccount.Status = result.Status;

                    tvAccountStatus.setText(result.Status.name());

                    mSignalRService.updateOrderRequestablesStatusAfterAccountStatusChanged(result.TableAccountId);

                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && v != null) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else if (v != null){ // Vibrate for 500 milliseconds
                        v.vibrate(500); //deprecated in API 26
                    }
                }
            });

            mSignalRService.setOnOrderItemStatusClientChanged(new AsyncTaskListener<OrderMenuItem>() {
                @Override public void onPreExecute() { }
                @Override public void onPostExecute(OrderMenuItem orderMenuItem) {
                    if (clientTableAccountOrdersAdapter != null){
                        ClientTableAccountItem item = clientTableAccountOrdersAdapter.getItemById(orderMenuItem.Id);

                        ClientTableAccountOrdersActivity.this.runOnUiThread(
                                () -> {
                                    if (item != null)
                                        clientTableAccountOrdersAdapter.changeItemStatus(orderMenuItem.Id, orderMenuItem.Status);
                                    else
                                        clientTableAccountOrdersAdapter.addTableAccountItem(orderMenuItem);
                                }
                        );
                    }
                }
            });

            clientTableAccountOrdersAdapter.setSignalRService(mSignalRService);
            clientTableAccountOrdersAdapter.clearItems();

            if (this.mSignalRService != null &&
                    this.tableAccount == null &&
                    this.selectedTable != null &&
                    StringHelper.isNotNullOrEmpty(super.username)){

                this.mSignalRService.createTableAccount(super.username, this.selectedTable.id, this.newlySelectedMenuItemIds);
            } else if (this.mSignalRService != null &&
                    this.tableAccount != null &&
                    this.selectedTable != null &&
                    StringHelper.isNotNullOrEmpty(super.username)){

                //this.mSignalRService.createOrder(super.username, this.tableAccount.Id, this.newlySelectedMenuItemIds);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if (!mBound) {
            this.bindService(SignalRService_.intent(this).get(), mConnection, Context.BIND_AUTO_CREATE);
        }

        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews(){

        if (ConnectivityHelper.isConnected(applicationContext)) {
            networkLost = false;

//            RecyclerViewItemsClickSupport.addTo(this.ordersView).setOnItemClickListener((recyclerView, position, v) -> {
//                ClientTableAccountItem itemClicked = (ClientTableAccountItem)clientTableAccountOrdersAdapter.getItemAt(position);

//            });

            this.ordersView.setAdapter(clientTableAccountOrdersAdapter);
            this.ordersView.setLayoutManager(new GridLayoutManager(this.baseContext, 1));
            DividerItemDecoration itemDecorator = new DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.applicationContext, R.drawable.wall_divider_primary)));
            this.ordersView.addItemDecoration(itemDecorator);
//          this.ordersView.addOnScrollListener(tableAccountsVenuesScrollListener);

            this.clientTableAccountOrdersAdapter.setSwipeRefreshLayout(tableAccountOrdersLayout);
            this.clientTableAccountOrdersAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (!tableAccountsLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext)) {
                    //INFO: REFRESH INITIAL LOAD
                    if (tableAccount != null)
                        reloadTableAccountOrders();
                }
                tableAccountOrdersLayout.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            if (this.tableAccount != null  &&
                this.selectedTable != null &&
                StringHelper.isNotNullOrEmpty(super.username)) {

                this.reloadTableAccountOrders();
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
    protected void onResume(){
        super.onResume();

        if (!mBound) {
            this.bindService(SignalRService_.intent(this).get(), mConnection, Context.BIND_AUTO_CREATE);
        }
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

        Intent intent = new Intent(ClientTableAccountOrdersActivity.this, VenueMenuActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        intent.putExtra("tableAccount", this.tableAccount);
        intent.putExtra("wallVenue", this.wallVenue);
        intent.putExtra("selectedTable", this.selectedTable);
        intent.putExtra("selectedMenuListId", this.selectedMenuListId);

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
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void showNoInternetLayout() {

    }

    private void reloadTableAccountOrders() {
        if (this.tableAccount != null){
            clientTableAccountOrdersAdapter.clearItems();

            AsyncTaskListener<ArrayList<Order>> apiCallResultListener = new AsyncTaskListener<ArrayList<Order>>() {
                @Override public void onPreExecute() {
                    tableAccountOrdersLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.VISIBLE);
                    tableAccountsLoadingStillInAction = true;
                }
                @Override public void onPostExecute(ArrayList<Order> results) {
                    progressLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    tableAccountOrdersLayout.setVisibility(View.VISIBLE);

                    if (results != null && results.size() > 0) {
                        orders = results;

                        for (Order order : results) {
                            for (OrderMenuItem item : order.orderMenuItems) {
                                clientTableAccountOrdersAdapter.addTableAccountItem(item);
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
