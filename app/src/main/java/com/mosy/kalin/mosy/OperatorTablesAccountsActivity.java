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

import com.mosy.kalin.mosy.Adapters.OperatorTableAccountsAdapter;
import com.mosy.kalin.mosy.CustomControls.Support.RecyclerViewItemsClickSupport;
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults.TableAccountStatusResult;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Views.ItemModels.OperatorTableAccountItem;
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


@EActivity(R.layout.activity_operator_tables_accounts)
public class OperatorTablesAccountsActivity
        extends BaseActivity {

    private boolean afterViewsFinished = false;
    private boolean networkLost = false;
    private boolean tableAccountsLoadingStillInAction = false;

    public ArrayList<TableAccount> tableAccounts;

    @Extra
    public Venue venue;

    @Bean
    TableAccountsService tableAccountsService;
    @Bean
    OperatorTableAccountsAdapter operatorTableAccountsAdapter;

    @ViewById(R.id.llInitialLoadingProgress)
    LinearLayout progressLayout;
    @ViewById(R.id.operatorTableAccounts_llEmptyMessageLayout)
    LinearLayout emptyLayout;
    @ViewById(R.id.operatorTableAccounts_lTableAccountsSwipeContainer)
    SwipeRefreshLayout tableAccountsLayout;
    @ViewById(R.id.operatorTableAccounts_lvTableAccounts)
    RecyclerView tableAccountsView;

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

            mSignalRService.setOnTAStatusChangedOperator(new AsyncTaskListener<TableAccountStatusResult>() {
                @Override public void onPreExecute() { }
                @Override public void onPostExecute(TableAccountStatusResult result) {
                    if (operatorTableAccountsAdapter != null){
                        operatorTableAccountsAdapter.changeItemStatus(result.TableAccountId, result.Status);

                        mSignalRService.updateOrderRequestablesStatusAfterAccountStatusChanged(result.TableAccountId);

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && v != null) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else if (v != null){ // Vibrate for 500 milliseconds
                            v.vibrate(500); //deprecated in API 26
                        }
                    }
                }
            });
            operatorTableAccountsAdapter.setSignalRService(mSignalRService);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if (!mBound) {
            ComponentName myService = startService(new Intent(this, SignalRService.class));
            this.bindService(SignalRService_.intent(this).get(), mConnection, Context.BIND_AUTO_CREATE);
        }

        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews(){

        if (ConnectivityHelper.isConnected(applicationContext)) {
            networkLost = false;

            RecyclerViewItemsClickSupport.addTo(this.tableAccountsView).setOnItemClickListener((recyclerView, position, v) -> {
                OperatorTableAccountItem itemClicked = (OperatorTableAccountItem) operatorTableAccountsAdapter.getItemAt(position);

                if (itemClicked.tableAccount.Status != TableAccountStatus.AwaitingOperatorApprovement){
                    Intent intent = new Intent(OperatorTablesAccountsActivity.this, OperatorTableAccountOrdersActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("venue", venue);
                    intent.putExtra("tableAccount", itemClicked.tableAccount);
                    startActivity(intent);
                }
            });

            this.tableAccountsView.setAdapter(operatorTableAccountsAdapter);
            this.tableAccountsView.setLayoutManager(new GridLayoutManager(this.baseContext, 1));
            DividerItemDecoration itemDecorator = new DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.applicationContext, R.drawable.wall_divider_primary)));
            this.tableAccountsView.addItemDecoration(itemDecorator);
//            this.tableAccountsVenuesView.addOnScrollListener(tableAccountsVenuesScrollListener);

            this.operatorTableAccountsAdapter.setSwipeRefreshLayout(tableAccountsLayout);
            this.operatorTableAccountsAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (!tableAccountsLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext)) {
                    operatorTableAccountsAdapter.clearItems();

                    //INFO: REFRESH INITIAL LOAD
                    loadTablesAccounts();
                }
                tableAccountsLayout.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            loadTablesAccounts();
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
            runOnUiThread(this::loadTablesAccounts);
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

        Intent intent = new Intent(OperatorTablesAccountsActivity.this, UserProfileActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }

    @Override
    protected void onPause() {

        super.onPause();
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

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void showNoInternetLayout() {

    }

    private void loadTablesAccounts() {
        if (this.venue != null){
            AsyncTaskListener<ArrayList<TableAccount>> apiCallResultListener = new AsyncTaskListener<ArrayList<TableAccount>>() {
                @Override public void onPreExecute() {
                    tableAccountsLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.VISIBLE);
                }
                @Override public void onPostExecute(ArrayList<TableAccount> results) {
                    progressLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    tableAccountsLayout.setVisibility(View.VISIBLE);

                    if (results != null && results.size() > 0) {
                        tableAccounts = results;

                        for (TableAccount item : results) {
                            operatorTableAccountsAdapter.addTableAccountItem(item);
                        }

                        tableAccountsLoadingStillInAction = false;
                    }
                }
            };
            this.tableAccountsService.getTableAccounts(this.applicationContext, apiCallResultListener, this::onNetworkLost, venue.Id);
        }
    }

}
