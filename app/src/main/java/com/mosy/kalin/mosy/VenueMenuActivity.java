package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.MenuPagerAdapter;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.PublicMenuResult;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Table;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.WallVenue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.LocaleHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Models.Views.SpinnerLocale;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.Services.TableAccountsService;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

@SuppressLint("Registered")
@EActivity(R.layout.activity_venue_menu)
public class VenueMenuActivity
        extends BaseActivity {

    private static final String storageContainer = "userimages\\fboalbums\\200x200";

    @Bean
    VenuesService venueService;
    @Bean
    AccountService accountService;
    @Bean
    TableAccountsService tableAccountsService;


    @Extra
    Table selectedTable;
    @Extra
    WallVenue wallVenue;
    @Extra
    String selectedMenuListId; //if the page is navigated via Dishes ListView or ClientTableAccountOrders, this should have value.
    @Extra
    TableAccount tableAccount; //being created the first time ClientTableAccountOrders activity is opened.
    @Extra
    public ArrayList<String> newlySelectedMenuItemIds; // needs to stay public - accessed from menu item details

    @ViewById(R.id.llInitialLoadingProgress)
    LinearLayout CentralProgress;
    @ViewById(R.id.venueMenu_llToolbox)
    LinearLayout MenuToolboxLayout;
    @ViewById(R.id.venue_tvName)
    TextView NameTextView;
    @ViewById(R.id.venue_tvClass)
    TextView ClassTextView;
    @ViewById(R.id.venueMenu_tvNewlySelectedItemsText)
    TextView tvNewlySelectedItemsText;
    @ViewById(R.id.venueMenu_tvTableName)
    TextView tvTableName;

    @ViewById(R.id.venue_ivIndoor)
    ImageView IndoorImageView;
    @ViewById(R.id.venueMenu_btnOpenAccount)
    Button buttonOpenAccount;
    @ViewById(R.id.venue_vpMenu)
    ViewPager MenuViewPager;
    @ViewById(R.id.venue_llNoMenu)
    RelativeLayout NoMenuLayout;
    @ViewById(R.id.venueMenu_llTableAndSelectedItems)
    RelativeLayout llTableAndSelectedItems;

    @ViewById(R.id.venueMenu_spLanguage)
    Spinner LanguagesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.applicationContext = getApplicationContext();
    }

    @AfterViews
    void afterViews() {
        try {
            this.NameTextView.setText(this.wallVenue.Name);
            this.ClassTextView.setText(this.wallVenue.Class);
            this.MenuViewPager.setVisibility(GONE);
            this.NoMenuLayout.setVisibility(VISIBLE);

            loadMenu();

            if (this.newlySelectedMenuItemIds == null){
                this.newlySelectedMenuItemIds = new ArrayList<>();
            }

            this.reevaluateOrderLabelsVisibility();

            if (this.wallVenue.HasOrdersManagementSubscription) {
                this.getExistingTableAccount();
            }
            else {
                this.loadIndoorImageMeta();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //now getIntent() should always return the last received intent

        this.reevaluateOrderLabelsVisibility();
    }

    private void getExistingTableAccount() {
        AsyncTaskListener<TableAccount> apiCallResultListener = new AsyncTaskListener<TableAccount>() {
            @Override public void onPreExecute() {

            }
            @Override public void onPostExecute(TableAccount result) {
                tableAccount = result;
                if (tableAccount != null){
                    selectedTable = tableAccount.Table;
                    tvTableName.setText(selectedTable.Name);
                }
                reevaluateOrderLabelsVisibility();
            }
        };
        this.tableAccountsService.getTableAccount(this.applicationContext, apiCallResultListener, null, null, this.wallVenue.Id, super.username);
    }

    private void loadIndoorImageMeta() {
        AsyncTaskListener<VenueImage> apiCallResultListener = new AsyncTaskListener<VenueImage>() {
            @Override public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }
            @Override public void onPostExecute(VenueImage result) {
                wallVenue.IndoorImage = result;
                populateIndoorImageThumbnail();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.venueService.getImageMeta(this.applicationContext, apiCallResultListener, null, this.wallVenue.Id, false);
    }

    private void loadMenu(){
        AsyncTaskListener<PublicMenuResult> apiCallResultListener = new AsyncTaskListener<PublicMenuResult>() {
            @Override public void onPreExecute() {
                CentralProgress.setVisibility(VISIBLE);
            }

            @Override public void onPostExecute(PublicMenuResult result) {
                PublicMenuResult publicMenu = result;
                if (publicMenu != null && publicMenu.MenuLists != null && publicMenu.MenuLists.size() > 0) {
                    MenuViewPager.setVisibility(VISIBLE);
                    NoMenuLayout.setVisibility(GONE);

                    if (publicMenu.MenuCultures != null && publicMenu.MenuCultures.size() > 1)
                    {
                        ArrayList<SpinnerLocale> spinnerLocalesList = new ArrayList<>();
                        for (String menuCulture : result.MenuCultures) {
                            String localeId = menuCulture.length() > 2 ? menuCulture.substring(0,2) : StringHelper.empty();
                            if (StringHelper.isNotNullOrEmpty(localeId)){
                                int localeResourceId = 0;

                                try { localeResourceId = LocaleHelper.SUPPORTED_LOCALES.get(localeId); }
                                catch(Exception e) { /* Do nothing. */ }

                                if (localeResourceId != 0)
                                    spinnerLocalesList.add(new SpinnerLocale(localeId, constructLanguageSpinnerText(localeResourceId)));
                            }
                        }
                        if (spinnerLocalesList.size() > 1) {
                            setupLanguagesSpinner(spinnerLocalesList);
                            MenuToolboxLayout.setVisibility(VISIBLE);
                        }
                    }

                    MenuPagerAdapter adapter = new MenuPagerAdapter(getSupportFragmentManager(), publicMenu.MenuLists, selectedMenuListId);
                    adapter.setVenueHasOrdersManagementSubscription(wallVenue.HasOrdersManagementSubscription);
                    MenuViewPager.setAdapter(adapter);

                    if (selectedMenuListId != null && !selectedMenuListId.equals(StringHelper.empty())) {
                        int selectedMenuListIndex = 0;
                        for (MenuList list : publicMenu.MenuLists) {
                            if (selectedMenuListId.equals(list.Id))
                                selectedMenuListIndex = publicMenu.MenuLists.indexOf(list);
                        }
                        MenuViewPager.setCurrentItem(selectedMenuListIndex, false);
                    }
                }
                CentralProgress.setVisibility(GONE);
            }
        };
        this.venueService.getMenu(this.applicationContext, apiCallResultListener, null, this.wallVenue.Id);
    }

    private void populateIndoorImageThumbnail() {
        if (this.wallVenue.IndoorImage != null && this.wallVenue.IndoorImage.Id != null && this.wallVenue.IndoorImage.Id.length() > 0) {
            //INFO: TryGet WallVenue Image
            AsyncTaskListener<byte[]> apiCallResultListener = new AsyncTaskListener<byte[]>() {
                @Override public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }

                @Override public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)){
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        IndoorImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                    }
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                }
            };

            DownloadBlobModel model = new DownloadBlobModel(this.wallVenue.IndoorImage.Id, storageContainer);
            new LoadAzureBlobAsyncTask(apiCallResultListener).execute(model);
        }
    }

    private void setupLanguagesSpinner(ArrayList<SpinnerLocale> localesList) {
        //fill data in spinner
        ArrayAdapter<SpinnerLocale> adapter = new ArrayAdapter<>(this.applicationContext, R.layout.languages_spinner_activity_venue_menu, localesList);
        adapter.setDropDownViewResource(R.layout.languages_spinner_activity_venue_menu);
        this.LanguagesSpinner.setAdapter(adapter);

        String currentDefaultSpinnerLocale = LocaleHelper.getLanguage(applicationContext);

        //TODO: Linq-like syntax needed!
        //Stream.of(cuisineRegionFilters).filter(filter -> filter.Id.equals(filterId)).single();
        for(SpinnerLocale sLocale : localesList){
            if (sLocale.getId().equals(currentDefaultSpinnerLocale)){
                this.LanguagesSpinner.setSelection(adapter.getPosition(sLocale));
                break;
            }
        }

        this.LanguagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newLocaleId = localesList.get(i).getId();
                if (!currentDefaultSpinnerLocale.equals(newLocaleId)){
                    LocaleHelper.setLocale(applicationContext, newLocaleId);
                    recreate();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private String constructLanguageSpinnerText(int resourceId) {
        String applicationDefaultLocaleTranslation = StringHelper.getStringAppDefaultLocale(this, resourceId);
        String deviceDefaultLocaleTranslation = StringHelper.getStringDeviceDefaultLocale(this, resourceId);
        String label = applicationDefaultLocaleTranslation + " (" + deviceDefaultLocaleTranslation + ")";
        return label;
    }

    public void addToNewlySelectedMenuItems(String itemToAddId){
        this.newlySelectedMenuItemIds.add(itemToAddId);
        this.reevaluateOrderLabelsVisibility();
    }

    public void removeFromNewlySelectedMenuItems(String itemToRemoveId){
        boolean exists = false;
        int indexOfItemToRemove = 0;

        for (int i = 0; i < this.newlySelectedMenuItemIds.size(); i++) {
            if (this.newlySelectedMenuItemIds.get(i).equals(itemToRemoveId)){
                exists = true;
                indexOfItemToRemove = i;
                break;
            }
        }

        if (exists){
            this.newlySelectedMenuItemIds.remove(indexOfItemToRemove);
        }

        this.reevaluateOrderLabelsVisibility();
    }

    private void reevaluateOrderLabelsVisibility(){
        if (this.newlySelectedMenuItemIds != null) {
            String value = String.valueOf(this.newlySelectedMenuItemIds.size()) + " item" + (this.newlySelectedMenuItemIds.size() > 1 ? "s " : " ") + "selected";
            this.tvNewlySelectedItemsText.setText(value);
            this.tvNewlySelectedItemsText.setVisibility(this.newlySelectedMenuItemIds.size() > 0 ? VISIBLE : INVISIBLE);
        }

        if (this.selectedTable != null){
            this.tvTableName.setText(this.selectedTable.Name);
        }

        this.llTableAndSelectedItems.setVisibility(
                this.wallVenue.HasOrdersManagementSubscription &&
                        (this.selectedTable != null || (this.newlySelectedMenuItemIds != null && this.newlySelectedMenuItemIds.size() > 0))? VISIBLE : GONE);

        this.IndoorImageView.setVisibility(this.wallVenue.HasOrdersManagementSubscription ? GONE : VISIBLE);
        this.buttonOpenAccount.setVisibility(this.wallVenue.HasOrdersManagementSubscription ? VISIBLE : GONE);
        this.buttonOpenAccount.setEnabled(this.wallVenue.HasOrdersManagementSubscription);

        this.tvTableName.setVisibility(selectedTable != null ? VISIBLE : GONE);

        String btnOpenAccountText = this.selectedTable == null ? "Select a table" : "Send Order (Open Account)";
        this.buttonOpenAccount.setText(btnOpenAccountText);

    }

    @Click(R.id.venue_lVenueTitle)
    void venueTitle_Click() {
        Intent intent = new Intent(VenueMenuActivity.this, DetailsVenueActivity_.class);
        this.wallVenue.OutdoorImage = null; // Don't need these one in the WallVenue page. If needed should implement Serializable or Parcelable
        this.wallVenue.IndoorImage = null; // Don't need these one in the WallVenue page. If needed should implement Serializable or Parcelable
        this.wallVenue.Location = null;
        intent.putExtra("wallVenue", this.wallVenue);
        startActivity(intent);
        overridePendingTransition( R.transition.slide_in_right, R.transition.slide_out_right );
    }

    @Click(R.id.venueMenu_btnOpenAccount)
    void openAccount_Click() {
        if (this.tableAccount == null && this.selectedTable == null){
            Intent intent = new Intent(VenueMenuActivity.this, VenueTablesActivity_.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            intent.putExtra("VenueId", this.wallVenue.Id);
            intent.putExtra("selectedTable", this.selectedTable);
            intent.putExtra("wallVenue", this.wallVenue);
            intent.putExtra("selectedMenuListId", this.selectedMenuListId);
            intent.putExtra("tableAccount", this.tableAccount);
            intent.putExtra("newlySelectedMenuItemIds", this.newlySelectedMenuItemIds);

            startActivity(intent);
        }
        else { // && this.selectedTable != null
            accountService.executeAssuredUserTokenValidOrRefreshed(
                    this.baseContext,
                    () -> {
                        Intent intent = new Intent(VenueMenuActivity.this, ClientTableAccountOrdersActivity_.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        this.wallVenue.OutdoorImage = null; // Don't need these one in the WallVenue page. If needed should implement Serializable or Parcelable
                        this.wallVenue.IndoorImage = null; // Don't need these one in the WallVenue page. If needed should implement Serializable or Parcelable
                        this.wallVenue.Location = null;
                        this.wallVenue.VenueBusinessHours = null;
                        this.wallVenue.VenueContacts = null;
                        this.wallVenue.Filters = null;
                        intent.putExtra("selectedTable", this.selectedTable);
                        intent.putExtra("tableAccount", this.tableAccount);
                        intent.putExtra("wallVenue", this.wallVenue);
                        intent.putExtra("selectedMenuListId", this.selectedMenuListId);
                        intent.putExtra("newlySelectedMenuItemIds", this.newlySelectedMenuItemIds);

                        startActivity(intent);
                    },
                    this::goToLoginActivity);
        }
    }
}
