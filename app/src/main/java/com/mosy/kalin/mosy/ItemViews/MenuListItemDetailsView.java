package com.mosy.kalin.mosy.ItemViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.LruCache;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.ClientTableAccountOrdersActivity_;
import com.mosy.kalin.mosy.DTOs.Enums.FilterType;
import com.mosy.kalin.mosy.DTOs.Enums.FilteredType;
import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.Ingredient;
import com.mosy.kalin.mosy.DTOs.WallMenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.MenuListItemHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.Services.AzureBlobService;
import com.mosy.kalin.mosy.VenueMenuActivity;
import com.mosy.kalin.mosy.VenueMenuActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EViewGroup(R.layout.activity_item_menulistitem_details)
public class MenuListItemDetailsView
        extends RelativeLayout {

    private static final String thumbnailBlobStorageContainerPath = "userimages\\requestablealbums\\100x100";
    private static final String originalBlobStorageContainerPath = "userimages\\requestablealbums\\original";
    private LruCache<String, Bitmap> inMemoryCache;
    private boolean IsUsingDefaultImageThumbnail = true;
    private String ImageId;
    private ArrayList<Filter> Allergens;
    private ArrayList<Filter> DishTypes;
    private Context context;

    public WallMenuListItem menuListItem;

    @ViewById(R.id.menuListItemDetails_ivMainImage)
    ImageView imageThumbnail;
    @ViewById(R.id.menuListItemDetails_tvQuantityLabel)
    TextView quantityLabel;
    @ViewById(R.id.menuListItemDetails_tvIngredients)
    TextView ingredients;

    @ViewById(R.id.menuListItemDetails_btnAddItemToAccount)
    Button btnAddItemToAccount;
    @ViewById(R.id.menuListItemDetails_btnRemoveItemFromAccount)
    Button btnRemoveItemFromAccount;
    @ViewById(R.id.menuListItemDetails_btnAllergens)
    ImageButton btnOpenAllergensModal;

    boolean venueHasOrdersManagementSubscription = false;
    boolean itemExistsInNewlySelectedItems = false;

    public MenuListItemDetailsView(Context context) {
        super(context);
        this.context = context;
    }

    ///Add more controls here

    public void bind(WallMenuListItem wallMenuListItem,
                     LruCache<String, Bitmap> cache,
                     boolean venueHasOrdersManagementSubscription) {
        this.menuListItem = wallMenuListItem;
        this.inMemoryCache = cache;
        this.venueHasOrdersManagementSubscription = venueHasOrdersManagementSubscription;

        this.btnAddItemToAccount.setVisibility(this.venueHasOrdersManagementSubscription ? VISIBLE : INVISIBLE);
        this.btnRemoveItemFromAccount.setVisibility(this.venueHasOrdersManagementSubscription && itemExistsInNewlySelectedItems ? VISIBLE : INVISIBLE);

        MenuListItemCulture selectedCulture = MenuListItemHelper.getMenuListItemCulture(getContext(), wallMenuListItem);

        this.Allergens = new ArrayList<>(com.annimon.stream.Stream
                .of(wallMenuListItem.Filters)
                .filter(filter -> filter.FilteredType == FilteredType.Dishes && filter.FilterType == FilterType.DishAllergens)
                .toList());
        this.btnOpenAllergensModal.setVisibility(this.Allergens.size() > 0 ? VISIBLE : INVISIBLE);

        this.DishTypes = new ArrayList<>(com.annimon.stream.Stream
                .of(wallMenuListItem.Filters)
                .filter(filter -> filter.FilteredType == FilteredType.Dishes && filter.FilterType == FilterType.DishType)
                .toList());

        if (StringHelper.isNotNullOrEmpty(wallMenuListItem.QuantityDisplayText)){
            this.quantityLabel.setText(wallMenuListItem.QuantityDisplayText);
            this.quantityLabel.setVisibility(VISIBLE);
        }

        ArrayList<String> toJoin = new ArrayList<String>();
        String joined = StringHelper.empty();
        for (Ingredient ingredient: selectedCulture.Ingredients)
            toJoin.add(ingredient.Name);
        joined = StringHelper.join(", ", toJoin);
        if (StringHelper.isNotNullOrEmpty(joined)){
            this.ingredients.setText(joined);
            this.ingredients.setVisibility(VISIBLE);
        }

        final String imageKey = wallMenuListItem.ImageThumbnail != null ? wallMenuListItem.ImageThumbnail.Id : "default";
        if (imageKey.equals("default")){
            this.setDefaultImageThumbnail();
        } else {
            final Bitmap bitmap = getBitmapFromMemCache(imageKey);
            if (bitmap != null) {
                this.imageThumbnail.setImageBitmap(bitmap);
            } else {
                this.imageThumbnail.setImageResource(R.drawable.eat_paprika_100x100);
                this.downloadMenuListItemThumbnail(imageKey);
            }
        }

    }

    private void downloadMenuListItemThumbnail(String thumbnailId) {
        AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
            @Override
            public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(byte[] bytes) {
                if (ArrayHelper.hasValidBitmapContent(bytes)){
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageThumbnail.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                    IsUsingDefaultImageThumbnail = false;
                    ImageId = thumbnailId;
                    addBitmapToMemoryCache(thumbnailId, bmp);
                }
                else {
                    IsUsingDefaultImageThumbnail = true;
                    imageThumbnail.setImageResource(R.drawable.eat_paprika_100x100);
                }
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };

        DownloadBlobModel model = new DownloadBlobModel(thumbnailId, thumbnailBlobStorageContainerPath);
        new LoadAzureBlobAsyncTask(listener).execute(model);
    }

    private void setDefaultImageThumbnail() {
        boolean isVector = false;
        int drawableId = R.drawable.eat_paprika_100x100;
        if (this.DishTypes != null) {
            for (Filter filter : this.DishTypes) {
                switch (filter.Id.toUpperCase()) {
                    case "A9C13F74-219A-4FC5-8D81-0A60D7A1173B": // salads
                        drawableId = R.drawable.ic_salad_96;
                        break;
                    case "6A87D0EA-1503-4B99-8998-22D24C17307E": // pasta
                        drawableId = R.drawable.ic_spaghetti_96;
                        break;
                    case "E48B981A-5146-44AB-A78D-2A0029880286": // alcoholic drinks
                        drawableId = R.drawable.ic_wine_glass_96;
                        break;
                    case "84AD4941-A8FA-45A7-9A9B-3C2D9F4278F5": // sushi
                        drawableId = R.drawable.ic_sushi_96;
                        break;
                    case "E8ACF3CB-2C7A-4C8C-A142-5286388EB2AE": // appetizers & snacks
                        drawableId = R.drawable.ic_tapas_96;
                        break;
                    case "71539C9A-7DC6-4C55-860B-528C54DBF80D": // burgers
                        drawableId = R.drawable.ic_hamburger_96;
                        break;
                    case "9F704161-7D3B-4397-A68A-60412FB6A941": // non-alcoholic drinks
                        drawableId = R.drawable.ic_cup_water_24;
                        isVector = true;
                        break;
                    case "A47E2E84-E190-4B45-92B9-787669050168": // pizza
                        drawableId = R.drawable.ic_pizza_24;
                        isVector = true;
                        break;
                    case "09BC29A1-A475-4780-85D8-840DFCD3B18D": // breakfast
                        drawableId = R.drawable.ic_croissant_24;
                        isVector = true;
                        break;
                    case "48CC81A4-C2DE-40E8-A698-B9FE4C15B307": // buffet & brunch
                        drawableId = R.drawable.eat_paprika_100x100;
                        break;
                    case "9412FB28-4236-4202-A817-C35F25CE3B30": // mains
                        drawableId = R.drawable.eat_paprika_100x100;
                        break;
                    case "672A7F77-8087-4B62-BCC3-ECCDE5774B60": // sandwiches & wraps
                        drawableId = R.drawable.ic_taco_96;
                        break;
                    case "58B436F4-7280-4135-8A9B-F87A940B5C7A": // deserts
                        drawableId = R.drawable.ic_cupcake_24;
                        isVector = true;
                        break;
                }
            }
        }
        if (isVector){
            this.imageThumbnail.setImageResource(drawableId);
        }
        else{
            Bitmap defaultImageBitmap = BitmapFactory.decodeResource( this.context.getResources(), drawableId);
            this.imageThumbnail.setImageBitmap(defaultImageBitmap);
        }
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            inMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return inMemoryCache.get(key);
    }

    @Click(R.id.menuListItemDetails_ivMainImage)
    public void itemImageClick()
    {
        if (!IsUsingDefaultImageThumbnail && StringHelper.isNotNullOrEmpty(this.ImageId)){

            final Dialog nagDialog = new Dialog(this.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);
            nagDialog.show();

            if (this.ImageId != null && this.ImageId.length() > 0) {
                AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                    @Override
                    public void onPreExecute() {
//                        progressBar.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onPostExecute(byte[] bytes) {
                        if (ArrayHelper.hasValidBitmapContent(bytes)){
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                            ivPreview.setImageBitmap(bmp);
                        } else
                            throw new NullPointerException("Image not found");
                    }
                };

                new AzureBlobService().downloadMenuListItemThumbnail(this.context, this.ImageId, ImageResolution.FormatOriginal, listener);
            }
        }
    }

    @Click(R.id.menuListItemDetails_btnAllergens)
    public void btnOpenAllergensModalClick()
    {
        if (this.Allergens != null && this.Allergens.size() > 0)
        {
            StringBuilder allergensMessage = new StringBuilder(StringHelper.empty());
            String delimiter = StringHelper.empty();
            for (Filter filter : this.Allergens) {
                allergensMessage.append(delimiter).append(filter.Name);
                delimiter = ", ";
            }
            new AlertDialog.Builder(getContext())
                    .setTitle("Allergens")
                    .setMessage(allergensMessage.toString())
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->  dialog.cancel())
                    .show();
        }
    }

    @Click(R.id.menuListItemDetails_btnAddItemToAccount)
    public void addToAccountClick()
    {
        ((VenueMenuActivity_) context).addToNewlySelectedMenuItems(this.menuListItem.Id);
        this.reevaluateDropButtonVisibility();
    }

    @Click(R.id.menuListItemDetails_btnRemoveItemFromAccount)
    public void removeFromAccountClick()
    {
        ((VenueMenuActivity) context).removeFromNewlySelectedMenuItems(this.menuListItem.Id);
        this.reevaluateDropButtonVisibility();
    }

    public void reevaluateDropButtonVisibility(){
        ArrayList<String> newlySelectedMenuItemIds = ((VenueMenuActivity) context).newlySelectedMenuItemIds;

        boolean exists = false;

        for (int i = 0; i < newlySelectedMenuItemIds.size(); i++) {
            if (newlySelectedMenuItemIds.get(i).equals(this.menuListItem.Id)){
                exists = true;
                break;
            }
        }

        this.itemExistsInNewlySelectedItems = exists;
        this.btnRemoveItemFromAccount.setVisibility(this.venueHasOrdersManagementSubscription && exists ? VISIBLE : INVISIBLE);
    }
}
