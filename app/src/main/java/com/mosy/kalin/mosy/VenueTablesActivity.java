package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.LinearLayout;

import com.mosy.kalin.mosy.Adapters.VenueTablesAdapter;
import com.mosy.kalin.mosy.CustomControls.Support.RecyclerViewItemsClickSupport;
import com.mosy.kalin.mosy.DTOs.Table;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.WallVenue;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Views.ItemModels.TableItem;
import com.mosy.kalin.mosy.Services.TableAccountsService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


@SuppressLint("Registered")
@EActivity(R.layout.activity_venue_tables)
public class VenueTablesActivity
        extends BaseActivity {

    private boolean afterViewsFinished = false;
    private boolean networkLost = false;
    private boolean tableAccountsLoadingStillInAction = false;

    boolean reloadAfterFinish = true;
    ArrayList<Table> venueTables;

    @Bean
    TableAccountsService tableAccountsService;
    @Bean
    VenueTablesAdapter venueTablesAdapter;

    @Extra
    String venueId;
    @Extra
    Table selectedTable;
    @Extra
    WallVenue wallVenue;
    @Extra
    String selectedMenuListId; //if the page is navigated via Dishes ListView or ClientTableAccountOrders, this should have value.
    @Extra
    TableAccount tableAccount; //being created the first time ClientTableAccountOrders activity is opened.
    @Extra
    ArrayList<String> newlySelectedMenuItemIds;

    @ViewById(R.id.llInitialLoadingProgress)
    LinearLayout centralProgress;

    @ViewById(R.id.venueTables_lVenueTablesContainer)
    SwipeRefreshLayout tablesLayout;
    @ViewById(R.id.venueTables_lvVenueTables)
    RecyclerView tablesView;
    @ViewById(R.id.venueTables_llEmptyMessageLayout)
    LinearLayout lEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels*.9), (int)(dm.heightPixels*.7));

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }

    @AfterViews
    public void afterViews(){
        RecyclerViewItemsClickSupport.addTo(this.tablesView).setOnItemClickListener((recyclerView, position, v) -> {
            TableItem itemClicked = (TableItem)venueTablesAdapter.getItemAt(position);
            if (this.selectedTable != null && this.selectedTable.id.equals(itemClicked.table.id))
                this.reloadAfterFinish = false;

            this.selectedTable = itemClicked.table;
            VenueTablesActivity.this.finish();
        });

        this.tablesView.setAdapter(venueTablesAdapter);
        this.tablesView.setLayoutManager(new GridLayoutManager(this.baseContext, 1));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.applicationContext, R.drawable.wall_divider_white)));
        this.tablesView.addItemDecoration(itemDecorator);
//          this.tablesView.addOnScrollListener(tableAccountsVenuesScrollListener);

        this.venueTablesAdapter.setSwipeRefreshLayout(tablesLayout);
        this.venueTablesAdapter.swipeContainer.setOnRefreshListener(() -> {
            if (!tableAccountsLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext)) {
                //INFO: REFRESH INITIAL LOAD
                if (StringHelper.isNotNullOrEmpty(this.venueId))
                    reloadTables();
            }
            tablesLayout.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
        });

        this.reloadTables();
    }

    @Override
    protected void onStart(){
        super.onStart();
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_activity_tables_venue);

    }

    @Override
    protected void onNetworkAvailable() {

    }

    @Override
    protected void onNetworkLost() {

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

        Intent intent = new Intent(VenueTablesActivity.this, VenueMenuActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        intent.putExtra("selectedTable", this.selectedTable);
        intent.putExtra("wallVenue", this.wallVenue);
        intent.putExtra("selectedMenuListId", this.selectedMenuListId);
        intent.putExtra("tableAccount", this.tableAccount);
        intent.putExtra("newlySelectedMenuItemIds", this.newlySelectedMenuItemIds);

        startActivity(intent);
    }

    private void reloadTables() {
        AsyncTaskListener<ArrayList<Table>> apiCallResultListener = new AsyncTaskListener<ArrayList<Table>>() {
            @Override public void onPreExecute() {
                centralProgress.setVisibility(VISIBLE);
                lEmptyList.setVisibility(GONE);
                tablesView.setVisibility(GONE);
            }
            @Override public void onPostExecute(ArrayList<Table> result) {
                if (result != null) {
                    venueTables = result;

                    for (Table table : result)
                        venueTablesAdapter.addTableAccountItem(table);
                }
                centralProgress.setVisibility(GONE);
                lEmptyList.setVisibility(VISIBLE);
                tablesView.setVisibility(VISIBLE);
                tableAccountsLoadingStillInAction = false;
            }
        };

        this.tableAccountsService.geTables(this.applicationContext, apiCallResultListener, this::onNetworkLost, this::goToLoginActivity, this.venueId);
    }

}
