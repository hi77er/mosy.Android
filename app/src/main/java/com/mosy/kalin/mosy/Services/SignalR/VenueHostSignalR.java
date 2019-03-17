package com.mosy.kalin.mosy.Services.SignalR;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.OrderMenuItem;
import com.mosy.kalin.mosy.DTOs.SignalR.SignalRBindingModels.CreateOrderBindingModel;
import com.mosy.kalin.mosy.DTOs.SignalR.SignalRBindingModels.CreateTableAccountBindingModel;
import com.mosy.kalin.mosy.DTOs.SignalR.SignalRBindingModels.TableAccountStatusBindingModel;
import com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults.TableAccountStatusResult;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Services.AccountService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

import java.util.ArrayList;

import io.reactivex.Single;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;

@EService
public class VenueHostSignalR extends IntentService {

    private com.microsoft.signalr.HubConnection accountsHubConnection;
    private com.microsoft.signalr.HubConnection ordersHubConnection;

    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public VenueHostSignalR getService() {
            // Return this instance of AccountOpenerSignalR so clients can call public methods
            return VenueHostSignalR.this;
        }
    }

    @Bean
    AccountService accountService;

    private AsyncTaskListener<OrderMenuItem> onOrderItemStatusChangedOperatorTask;
    public void setOnOrderItemStatusOperatorChanged(AsyncTaskListener<OrderMenuItem> task){
        this.onOrderItemStatusChangedOperatorTask = task;
    }

    private AsyncTaskListener<TableAccountStatusResult> onTAStatusChangedOperatorTask;
    public void setOnTAStatusChangedOperator(AsyncTaskListener<TableAccountStatusResult> task){
        this.onTAStatusChangedOperatorTask = task;
    }

    //called when THE STATUS of ITEM FROM AN ACCOUNT is changed
    private AsyncTaskListener<TableAccountStatusResult> onTAOrderItemStatusChangedTask;
    public void setOnTAOrderItemStatusChanged(AsyncTaskListener<TableAccountStatusResult> task){
        this.onTAOrderItemStatusChangedTask = task;
    }

    public VenueHostSignalR() {
        super(VenueHostSignalR.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        startSignalR();
        return result;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        startSignalR();
        return mBinder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onDestroy() {
        this.accountsHubConnection.stop();
        this.ordersHubConnection.stop();
        super.onDestroy();
    }


    private void startSignalR() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        String userAuthToken = this.accountService.getUserAuthToken(getApplicationContext());

        this.ordersHubConnection = com.microsoft.signalr.HubConnectionBuilder
                .create("https://wsmosy.azurewebsites.net/hubs/orders")
                .withAccessTokenProvider(
                        Single.defer(() -> {
                            // Your logic here.
                            return Single.just(userAuthToken);
                        }))
                .build();

        this.accountsHubConnection = com.microsoft.signalr.HubConnectionBuilder
                .create("https://wsmosy.azurewebsites.net/hubs/accounts")
                .withAccessTokenProvider(
                        Single.defer(() -> {
                            // Your logic here.
                            return Single.just(userAuthToken);
                        }))
                .build();
    }

    boolean listenersAlreadySet = false;
    public void setEventListeners(String venueId){
        if (!listenersAlreadySet){
            listenersAlreadySet = true;

            // After connections building !!
            // Before connections started !!
            // TEST
            accountsHubConnection.on("PongClient", result -> Log.d("Signalr", result), String.class);
            ordersHubConnection.on("PongClient", result -> Log.d("Signalr", result), String.class);

            // ACTUAL
            accountsHubConnection.on("AccountStatusChanged",
                    result -> onTAStatusChangedOperatorTask.onPostExecute(result),
                    TableAccountStatusResult.class);

            ordersHubConnection.on("AccountHasItemStatusChanged",
                    result -> onTAOrderItemStatusChangedTask.onPostExecute(result),
                    TableAccountStatusResult.class);


            // After all .on methods !!
            try {
                this.ordersHubConnection.start().blockingAwait();
                this.ordersHubConnection.send("ConnectAsVenueHost", venueId);
                //this.pingOrdersHub("Ping Orders 213");

                this.accountsHubConnection.start().blockingAwait();
                this.accountsHubConnection.send("ConnectAsVenueHost", venueId);
                //this.pingAccountsHub("Ping Accounts 213");
            }
            catch(NullPointerException ex) {
                ex.printStackTrace();
            }
            catch(Error e2) {
                Log.e("", "ROME parse error2: " + e2.toString());
            }

        }
    }

    public void updateTableAccountStatus(String tableAccountId, TableAccountStatus newStatus, String updaterUsername){
        TableAccountStatusBindingModel model = new TableAccountStatusBindingModel();
        model.TableAccountId = tableAccountId;
        model.NewStatus = newStatus;
        model.UpdaterUsername = updaterUsername;
        //mTableAccountsHubProxy.invoke("UpdateTableAccountStatus", model);
        accountsHubConnection.send("UpdateTableAccountStatus", model);
    }

    public void updateOrderRequestablesStatusAfterAccountStatusChanged(String tableAccountId){
        //mOrdersHubProxy.invoke("UpdateOrderRequestablesStatusAfterAccountStatusChanged", tableAccountId);
        accountsHubConnection.send("UpdateOrderRequestablesStatusAfterAccountStatusChanged", tableAccountId);
    }

    //TODO: UNCOMMENT WHEN IMPLEMENTING FEATURE: HOST CREATING AN ACCOUNT
    //
    //public void createTableAccount(String openerUsername, String tableId, ArrayList<String> menuItemIds){
    //    this.createTableAccount(openerUsername, tableId, menuItemIds, StringHelper.empty());
    //}
    //
    //public void createTableAccount(String openerUsername, String tableId, ArrayList<String> menuItemIds, String assignedOperatorUsername){
    //    CreateTableAccountBindingModel model = new CreateTableAccountBindingModel();
    //    model.OpenerUsername = openerUsername;
    //    model.FBOTableId = tableId;
    //    model.RequestableIds = menuItemIds;
    //    model.AssignedOperatorUsername = assignedOperatorUsername;
    //    //mTableAccountsHubProxy.invoke("CreateTableAccountRequest", model);
    //    accountsHubConnection.send("CreateTableAccountRequest", model);
    //}

    // TEST
    public void pingAccountsHub(String pingStartingMessageToMirror){
        //mOrdersHubProxy.invoke("PingHub", pingStartingMessageToMirror);
        this.accountsHubConnection.send("PingHub", pingStartingMessageToMirror);
    }

    public void pingOrdersHub(String pingStartingMessageToMirror){
        //mOrdersHubProxy.invoke("PingHub", pingStartingMessageToMirror);
        this.ordersHubConnection.send("PingHub", pingStartingMessageToMirror);
    }


}
