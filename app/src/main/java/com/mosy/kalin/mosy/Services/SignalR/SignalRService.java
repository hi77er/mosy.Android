package com.mosy.kalin.mosy.Services.SignalR;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;

import io.reactivex.Single;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;

@EService
public class SignalRService extends IntentService {

    private com.microsoft.signalr.HubConnection newAccountsHubConnection;
    private com.microsoft.signalr.HubConnection newOrdersHubConnection;

    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return SignalRService.this;
        }
    }

    //called when THE STATUS of ITEM FROM AN ACCOUNT is changed
    @Bean
    AccountService accountService;

    private AsyncTaskListener<OrderMenuItem> onOrderItemStatusChangedOperatorTask;
    public void setOnOrderItemStatusOperatorChanged(AsyncTaskListener<OrderMenuItem> task){
        this.onOrderItemStatusChangedOperatorTask = task;
    }

    private AsyncTaskListener<OrderMenuItem> onOrderItemStatusChangedClientTask;
    public void setOnOrderItemStatusClientChanged(AsyncTaskListener<OrderMenuItem> task){
        this.onOrderItemStatusChangedClientTask = task;
    }

    private AsyncTaskListener<TableAccountStatusResult> onTAStatusChangedOperatorTask;
    public void setOnTAStatusChangedOperator(AsyncTaskListener<TableAccountStatusResult> task){
        this.onTAStatusChangedOperatorTask = task;
    }

    //called when the STATUS of AN ACCOUNT is changed
    private AsyncTaskListener<TableAccountStatusResult> onTAStatusChangedClientTask;
    public void setOnTAStatusChangedClient(AsyncTaskListener<TableAccountStatusResult> task){
        this.onTAStatusChangedClientTask = task;
    }

    //called when THE STATUS of ITEM FROM AN ACCOUNT is changed
    private AsyncTaskListener<TableAccountStatusResult> onTAOrderItemStatusChangedTask;
    public void setOnTAOrderItemStatusChanged(AsyncTaskListener<TableAccountStatusResult> task){
        this.onTAOrderItemStatusChangedTask = task;
    }

    public SignalRService() {
        super(SignalRService.class.getSimpleName());

    }

    @UiThread
    void showToast() {
        Toast.makeText(getApplicationContext(), "Hello World!", Toast.LENGTH_LONG).show();
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
        this.newAccountsHubConnection.stop();
        this.newOrdersHubConnection.stop();
        super.onDestroy();
    }


    /**
     * method for clients (activities)
     */

    private void startSignalR() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        //String webApiAuthToken = this.accountService.getWebApiAuthToken(getApplicationContext());

        String userAuthToken = this.accountService.getUserAuthToken(getApplicationContext());

        this.newOrdersHubConnection = com.microsoft.signalr.HubConnectionBuilder
                .create("https://wsmosy.azurewebsites.net/hubs/orders")
                .withAccessTokenProvider(
                        Single.defer(() -> {
                            // Your logic here.
                            return Single.just(userAuthToken);
                        }))
                .build();


        this.newAccountsHubConnection = com.microsoft.signalr.HubConnectionBuilder
                .create("https://wsmosy.azurewebsites.net/hubs/accounts")
                .withAccessTokenProvider(
                        Single.defer(() -> {
                            // Your logic here.
                            return Single.just(userAuthToken);
                        }))
                .build();
    }

    boolean listenersAlreadySet = false;
    public void setEventListeners(String username){
        if (!listenersAlreadySet){
            listenersAlreadySet = true;

            // After connections building !!
            // Before connections started !!
            //
            // OPERATOR SIDE !
            String updatedTAStatusOperatorReceiverMethodName = "TAOperatorHubProxy-" + username + "-TAUpdatedStatusReceiverMethod";
            newAccountsHubConnection.on(
                    updatedTAStatusOperatorReceiverMethodName,
                    result -> onTAStatusChangedOperatorTask.onPostExecute(result),
                    TableAccountStatusResult.class);

            String updatedOrderItemStatusOperatorReceiverMethodName = "TAOperatorHubProxy-" + username + "-OrderUpdatedStatusReceiverMethod";
            newOrdersHubConnection.on(
                    updatedOrderItemStatusOperatorReceiverMethodName,
                    result -> onOrderItemStatusChangedOperatorTask.onPostExecute(result),
                    OrderMenuItem.class);

            String updatedTAOrderItemStatusOperatorReceiverMethodName = "TAOperatorHubProxy-" + username + "-TAHasUpdatedOrderStatusReceiverMethod";
            newOrdersHubConnection.on(
                    updatedTAOrderItemStatusOperatorReceiverMethodName,
                    result -> onTAOrderItemStatusChangedTask.onPostExecute(result),
                    TableAccountStatusResult.class);

            // CLIENT SIDE!
            String updatedTAStatusClientReceiverMethodName = "TAClientHubProxy-" + username + "-TAUpdatedStatusReceiverMethod";
            newAccountsHubConnection.on(
                    updatedTAStatusClientReceiverMethodName,
                    result -> onTAStatusChangedClientTask.onPostExecute(result),
                    TableAccountStatusResult.class);

            String updatedOrderItemStatusClientReceiverMethodName = "TAClientHubProxy-" + username + "-OrderUpdatedStatusReceiverMethod";
            newOrdersHubConnection.on(
                    updatedOrderItemStatusClientReceiverMethodName,
                    result -> onOrderItemStatusChangedClientTask.onPostExecute(result),
                    OrderMenuItem.class);

            // TEST
            String accountsHubPongReceiverMethodName = "PongClient";
            newAccountsHubConnection.on(
                    accountsHubPongReceiverMethodName,
                    result -> Log.d("Signalr", result),
                    String.class);

            String ordersHubPongReceiverMethodName = "PongClient";
            newOrdersHubConnection.on(
                    ordersHubPongReceiverMethodName,
                    result -> Log.d("Signalr", result),
                    String.class);

            // After all .on methods !!
            try { this.newOrdersHubConnection.start().blockingAwait(); }
            catch(Exception ex) { ex.printStackTrace(); }//RuntimeExceptions are being thrown when Unauthorized Request is sent or SignalR Connection wasn't started!

            try { this.newAccountsHubConnection.start().blockingAwait(); }
            catch(Exception ex) { ex.printStackTrace(); }//RuntimeExceptions are being thrown when Unauthorized Request is sent or SignalR Connection wasn't started!

            try { this.pingOrdersHub("Ping Orders 213"); }
            catch (Exception ex){ ex.printStackTrace(); }

            try { this.pingAccountsHub("Ping Accounts 213"); }
            catch (Exception ex){ ex.printStackTrace(); }

        }
    }

    public void createOrder(String creatorUsername, String tableAccountId, ArrayList<String> menuItemIds){
        CreateOrderBindingModel model = new CreateOrderBindingModel();
        model.CreatorUsername = creatorUsername;
        model.TableAccountId = tableAccountId;
        model.RequestableIds = menuItemIds;
        //mOrdersHubProxy.invoke("CreateOrderRequest", model);
        newOrdersHubConnection.send("CreateOrderRequest", model);
    }

    public void createTableAccount(String openerUsername, String tableId, ArrayList<String> menuItemIds){
        this.createTableAccount(openerUsername, tableId, menuItemIds, StringHelper.empty());
    }

    public void createTableAccount(String openerUsername, String tableId, ArrayList<String> menuItemIds, String assignedOperatorUsername){
        CreateTableAccountBindingModel model = new CreateTableAccountBindingModel();
        model.OpenerUsername = openerUsername;
        model.FBOTableId = tableId;
        model.RequestableIds = menuItemIds;
        model.AssignedOperatorUsername = assignedOperatorUsername;
        //mTableAccountsHubProxy.invoke("CreateTableAccountRequest", model);
        newAccountsHubConnection.send("CreateTableAccountRequest", model);
    }

    public void updateTableAccountStatus(String tableAccountId, TableAccountStatus newStatus, String updaterUsername){
        TableAccountStatusBindingModel model = new TableAccountStatusBindingModel();
        model.TableAccountId = tableAccountId;
        model.NewStatus = newStatus;
        model.UpdaterUsername = updaterUsername;
        //mTableAccountsHubProxy.invoke("UpdateTableAccountStatus", model);
        newAccountsHubConnection.send("UpdateTableAccountStatus", model);
    }

    public void updateOrderRequestablesStatusAfterAccountStatusChanged(String tableAccountId){
        //mOrdersHubProxy.invoke("UpdateOrderRequestablesStatusAfterAccountStatusChanged", tableAccountId);
        newAccountsHubConnection.send("UpdateOrderRequestablesStatusAfterAccountStatusChanged", tableAccountId);
    }

    public void updateOrderRequestablesStatus(String orderRequestableId){
        //mOrdersHubProxy.invoke("UpdateOrderRequestableStatus", orderRequestableId);
        newOrdersHubConnection.send("UpdateOrderRequestableStatus", orderRequestableId);
    }

    public void pingAccountsHub(String pingStartingMessageToMirror){
        //mOrdersHubProxy.invoke("PingHub", pingStartingMessageToMirror);
        this.newAccountsHubConnection.send("PingHub", pingStartingMessageToMirror);
    }

    public void pingOrdersHub(String pingStartingMessageToMirror){
        //mOrdersHubProxy.invoke("PingHub", pingStartingMessageToMirror);
        this.newOrdersHubConnection.send("PingHub", pingStartingMessageToMirror);
    }

}
