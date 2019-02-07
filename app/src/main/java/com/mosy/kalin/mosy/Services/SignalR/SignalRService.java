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
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

@EService
public class SignalRService extends IntentService {

    //When testing on Device - localhost be like:
    //private static final String SIGNALR_SERVER_URL = "https://localhost:44384";

    //Public Web App url be like:
    private static final String SIGNALR_SERVER_URL = "https://mosy.azurewebsites.net";

    private static final String ORDERS_SERVER_HUB_NAME = "ordersHub";
    private static final String TABLE_ACCOUNTS_SERVER_HUB_NAME = "tableAccountsHub";
    private static final String DELIVERIES_SERVER_HUB_NAME = "deliveriesHub";

    private microsoft.aspnet.signalr.client.hubs.HubConnection mHubConnection;
    private microsoft.aspnet.signalr.client.hubs.HubProxy mTableAccountsHubProxy;
    private microsoft.aspnet.signalr.client.hubs.HubProxy mOrdersHubProxy;
    private microsoft.aspnet.signalr.client.hubs.HubProxy mDeliveriesHubProxy;

    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients

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

    private AsyncTaskListener<TableAccountStatusResult> onTAStatusChangedClientTask;
    public void setOnTAStatusChangedClient(AsyncTaskListener<TableAccountStatusResult> task){
        this.onTAStatusChangedClientTask = task;
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
    public void onDestroy() {
        mHubConnection.stop();
        super.onDestroy();
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

    /**
     * method for clients (activities)
     */

    private void startSignalR() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        mHubConnection = new microsoft.aspnet.signalr.client.hubs.HubConnection(SIGNALR_SERVER_URL);
        mOrdersHubProxy = mHubConnection.createHubProxy(ORDERS_SERVER_HUB_NAME);
        mTableAccountsHubProxy = mHubConnection.createHubProxy(TABLE_ACCOUNTS_SERVER_HUB_NAME);
        mDeliveriesHubProxy = mHubConnection.createHubProxy(DELIVERIES_SERVER_HUB_NAME);

        ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
        SignalRFuture<Void> signalRFuture = mHubConnection.start(clientTransport);

        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("SimpleSignalR", e.toString());
            return;
        }

    }

    public void setEventListeners(String taOperatorUsername){

        // OPERATOR !
        String updatedTAStatusOperatorReceiverMethodName = "TAOperatorHubProxy-" + taOperatorUsername + "-TAUpdatedStatusReceiverMethod";
        mTableAccountsHubProxy.on(
                updatedTAStatusOperatorReceiverMethodName,
                result -> onTAStatusChangedOperatorTask.onPostExecute(result),
                TableAccountStatusResult.class);

        String updatedTAStatusClientReceiverMethodName = "TAClientHubProxy-" + taOperatorUsername + "-TAUpdatedStatusReceiverMethod";
        mTableAccountsHubProxy.on(
                updatedTAStatusClientReceiverMethodName,
                result -> onTAStatusChangedClientTask.onPostExecute(result),
                TableAccountStatusResult.class);



        // CLIENT !
        String updatedOrderItemStatusOperatorReceiverMethodName = "TAOperatorHubProxy-" + taOperatorUsername + "-OrderUpdatedStatusReceiverMethod";
        mOrdersHubProxy.on(
                updatedOrderItemStatusOperatorReceiverMethodName,
                orderItem -> onOrderItemStatusChangedOperatorTask.onPostExecute(orderItem),
                OrderMenuItem.class);

        String updatedOrderItemStatusClientReceiverMethodName = "TAClientHubProxy-" + taOperatorUsername + "-OrderUpdatedStatusReceiverMethod";
        mOrdersHubProxy.on(
                updatedOrderItemStatusClientReceiverMethodName,
                orderItem -> onOrderItemStatusChangedClientTask.onPostExecute(orderItem),
                OrderMenuItem.class);
    }

    public void createOrder(String creatorUsername, String tableAccountId, ArrayList<String> menuItemIds){
        CreateOrderBindingModel model = new CreateOrderBindingModel();
        model.CreatorUsername = creatorUsername;
        model.TableAccountId = tableAccountId;
        model.RequestableIds = menuItemIds;
        mOrdersHubProxy.invoke("CreateOrderRequest", model);
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
        mTableAccountsHubProxy.invoke("CreateTableAccountRequest", model);
    }

    public void updateTableAccountStatus(String tableAccountId, TableAccountStatus newStatus){
        TableAccountStatusBindingModel model = new TableAccountStatusBindingModel();
        model.TableAccountId = tableAccountId;
        model.NewStatus = newStatus;
        mTableAccountsHubProxy.invoke("UpdateTableAccountStatus", model);
    }

    public void updateOrderRequestablesStatusAfterAccountStatusChanged(String tableAccountId){
        mOrdersHubProxy.invoke("UpdateOrderRequestablesStatusAfterAccountStatusChanged", tableAccountId);
    }

    public void updateOrderRequestablesStatus(String orderRequestableId){
        mOrdersHubProxy.invoke("UpdateOrderRequestableStatus", orderRequestableId);
    }

    public void pingOrdersHub(String pingStartingMessageToMirror){
        mOrdersHubProxy.invoke("PingHub", pingStartingMessageToMirror);
    }

}
