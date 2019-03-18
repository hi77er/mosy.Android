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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.Adapters.ClientTableAccountOrdersAdapter;
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.Order;
import com.mosy.kalin.mosy.DTOs.OrderMenuItem;
import com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults.OrderItemStatusChangedResult;
import com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults.TableAccountStatusResult;
import com.mosy.kalin.mosy.DTOs.Table;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.WallVenue;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Views.ItemModels.ClientTableAccountItem;
import com.mosy.kalin.mosy.Services.SignalR.AccountOpenerSignalR;
import com.mosy.kalin.mosy.Services.SignalR.AccountOpenerSignalR_;
import com.mosy.kalin.mosy.Services.TableAccountsService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


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


    @ViewById(R.id.clientTableAccountItem_llActions)
    LinearLayout layoutActions;
    @ViewById(R.id.clientTableAccountOrders_btnAttention)
    Button buttonCall;
    @ViewById(R.id.clientTableAccountOrders_btnBill)
    Button buttonBill;
    @ViewById(R.id.clientTableAccountOrders_ivAssignedOperator)
    ImageView ivAssignedOperator;

    @ViewById(R.id.clientTableAccountOrders_tvAccountStatus)
    TextView tvAccountStatus;

    AccountOpenerSignalR mSignalRService;
    /** Defines callbacks for service binding, passed to bindService() */
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to AccountOpenerSignalR, cast the IBinder and get AccountOpenerSignalR instance
            AccountOpenerSignalR.LocalBinder binder = (AccountOpenerSignalR.LocalBinder) service;
            mSignalRService = binder.getService();

            setupSignalRService();
            setBound(true);
        }

        @Override public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void setupSignalRService() {
        mSignalRService.setEventListeners(tableAccount != null ? tableAccount.Id : null);

        mSignalRService.setOnTAStatusChangedClient(new AsyncTaskListener<TableAccountStatusResult>() {
            @Override public void onPreExecute() { }
            @Override public void onPostExecute(TableAccountStatusResult result) {
                tableAccount = new TableAccount();
                tableAccount.Id = result.TableAccountId;
                tableAccount.Status = result.Status;
                tableAccount.AssignedOperatorUsername = result.AssignedOperatorUsername;

                ClientTableAccountOrdersActivity.this.runOnUiThread(
                        () -> setupActionLayout(tableAccount)
                );

                if (result.NeedsItemsStatusUpdate && result.Status == TableAccountStatus.AwaitingOperatorApprovement) //only after creating the account
                    mSignalRService.updateOrderRequestablesStatusAfterAccountStatusChanged(result.TableAccountId);

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && v != null) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else if (v != null){ // Vibrate for 500 milliseconds
                    v.vibrate(500); //deprecated in API 26
                }
            }
        });

        mSignalRService.setOnOrderItemStatusClientChanged(new AsyncTaskListener<OrderItemStatusChangedResult>() {
            @Override public void onPreExecute() { }
            @Override public void onPostExecute(OrderItemStatusChangedResult orderItemStatusChangedResult) {
                if (clientTableAccountOrdersAdapter != null){
                    ClientTableAccountItem item = clientTableAccountOrdersAdapter.getItemById(orderItemStatusChangedResult.Id);

                    ClientTableAccountOrdersActivity.this.runOnUiThread(
                            () -> {
                                if (item != null)
                                    clientTableAccountOrdersAdapter.changeItemStatus(orderItemStatusChangedResult.Id, orderItemStatusChangedResult.Status);
                                else
                                {
                                    OrderMenuItem itemToAdd = orderItemStatusChangedResult.toOrderMenuItem();
                                    clientTableAccountOrdersAdapter.addTableAccountItem(itemToAdd);
                                }
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
                StringHelper.isNotNullOrEmpty(super.username) &&
                this.newlySelectedMenuItemIds != null &&
                this.newlySelectedMenuItemIds.size() > 0){

            this.mSignalRService.createTableAccount(super.username, this.selectedTable.Id, this.newlySelectedMenuItemIds);
        }
        else if (this.mSignalRService != null &&
                this.tableAccount != null &&
                this.selectedTable != null &&
                StringHelper.isNotNullOrEmpty(super.username) &&
                this.newlySelectedMenuItemIds != null &&
                this.newlySelectedMenuItemIds.size() > 0){

            this.mSignalRService.createOrder(super.username, this.tableAccount.Id, this.newlySelectedMenuItemIds);
        }
    }

//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        assert manager != null;
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    private boolean mBound = false;
    private void setBound(boolean value){
        this.mBound = value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if (!mBound){
            Intent intent = AccountOpenerSignalR_.intent(this).get();
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
                    if (tableAccount != null){
                        reloadTableAccountOrders();
                        setupActionLayout(tableAccount);
                    }
                }
                tableAccountOrdersLayout.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            if (this.tableAccount != null  &&
                this.selectedTable != null &&
                StringHelper.isNotNullOrEmpty(super.username)) {

                setupActionLayout(tableAccount);
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
        intent.putExtra("newlySelectedMenuItemIds", new ArrayList<>());

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
            stopService(AccountOpenerSignalR_.intent(this).get());
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void showNoInternetLayout() {

    }

    private void setupActionLayout(TableAccount tableAccount) {
        switch (tableAccount.Status){
            case New:
                publishActionLayout("New account.", R.color.colorRed, false, false, false, StringHelper.isNotNullOrEmpty(tableAccount.AssignedOperatorUsername));
                break;
            case AwaitingOperatorApprovement:
                publishActionLayout("Awaiting approvement..", R.color.colorPrimaryApricot, false, false, false, StringHelper.isNotNullOrEmpty(tableAccount.AssignedOperatorUsername));
                break;
            case Idle:
                publishActionLayout("Everything is OK.", R.color.colorPrimary, true, true, true, StringHelper.isNotNullOrEmpty(tableAccount.AssignedOperatorUsername));
                break;
            case OrderReadyForDelivery:
                publishActionLayout("Order just got ready.", R.color.colorPrimary, true, true, true, StringHelper.isNotNullOrEmpty(tableAccount.AssignedOperatorUsername));
                break;
            case NeedAttention:
                publishActionLayout("Waiter called.", R.color.colorSecondaryAmber, true, false, true, StringHelper.isNotNullOrEmpty(tableAccount.AssignedOperatorUsername));
                break;
            case AskingToPay:
                publishActionLayout("Bill called.", R.color.colorSecondaryAmber, true, true, false, StringHelper.isNotNullOrEmpty(tableAccount.AssignedOperatorUsername));
                break;
            case Closed:
                publishActionLayout("Closed.", R.color.soft, false, false, true, StringHelper.isNotNullOrEmpty(tableAccount.AssignedOperatorUsername));
                break;
        }
    }

    public void publishActionLayout(String statusText, int statusColorResourceId, boolean actionVisible, boolean callWaiterEnabled, boolean callBillEnabled, boolean hasAssignedOperator){
        this.tvAccountStatus.setVisibility(VISIBLE);
        this.tvAccountStatus.setText(statusText);
        this.tvAccountStatus.setTextColor(getResources().getColor(statusColorResourceId));

        this.layoutActions.setVisibility(actionVisible ? VISIBLE : INVISIBLE);
        this.buttonCall.setEnabled(callWaiterEnabled);
        this.buttonBill.setEnabled(callBillEnabled);
        this.ivAssignedOperator.setVisibility(hasAssignedOperator ? VISIBLE : INVISIBLE);
    }

    private void reloadTableAccountOrders() {
        if (this.tableAccount != null){
            clientTableAccountOrdersAdapter.clearItems();

            AsyncTaskListener<ArrayList<Order>> apiCallResultListener = new AsyncTaskListener<ArrayList<Order>>() {
                @Override public void onPreExecute() {
                    tableAccountOrdersLayout.setVisibility(GONE);
                    emptyLayout.setVisibility(GONE);
                    progressLayout.setVisibility(VISIBLE);
                    tableAccountsLoadingStillInAction = true;
                }
                @Override public void onPostExecute(ArrayList<Order> results) {
                    progressLayout.setVisibility(GONE);
                    emptyLayout.setVisibility(VISIBLE);
                    tableAccountOrdersLayout.setVisibility(VISIBLE);

                    if (results != null && results.size() > 0) {
                        orders = results;

                        for (Order order : results) {
                            for (OrderMenuItem item : order.orderMenuItems) {
                                ClientTableAccountOrdersActivity.this.runOnUiThread(
                                        () -> clientTableAccountOrdersAdapter.addTableAccountItem(item)
                                );
                            }
                        }

                    }

                    tableAccountsLoadingStillInAction = false;
                }
            };
            this.tableAccountsService.getOrders(this.applicationContext, apiCallResultListener, this::onNetworkLost, tableAccount.Id);
        }
    }

    @Click(R.id.clientTableAccountOrders_ivAssignedOperator)
    public void ivAssignedOperator_Clicked(){
        Toast.makeText(this.applicationContext, "Account has assigned waiter!", Toast.LENGTH_LONG).show();
    }

    @Click(R.id.clientTableAccountOrders_btnAttention)
    public void btnAttention_Clicked(){
        Toast.makeText(this.applicationContext, "Your waiter was called!", Toast.LENGTH_LONG).show();
        mSignalRService.updateTableAccountStatus(tableAccount.Id, TableAccountStatus.NeedAttention, StringHelper.empty());
    }

    @Click(R.id.clientTableAccountOrders_btnBill)
    public void btnBill_Clicked(){
        mSignalRService.updateTableAccountStatus(tableAccount.Id, TableAccountStatus.AskingToPay, StringHelper.empty());
        Toast.makeText(this.applicationContext, "Bill was called!", Toast.LENGTH_LONG).show();
    }
}
