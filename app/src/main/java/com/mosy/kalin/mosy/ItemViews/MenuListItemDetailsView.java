package com.mosy.kalin.mosy.ItemViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.LruCache;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Enums.FilterType;
import com.mosy.kalin.mosy.DTOs.Enums.FilteredType;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.Ingredient;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.stream.Stream;

@EViewGroup(R.layout.activity_item_menulistitem_details)
public class MenuListItemDetailsView
        extends RelativeLayout {

    private static final String thumbnailBlobStorageContainerPath = "userimages\\requestablealbums\\100x100";
    private static final String originalBlobStorageContainerPath = "userimages\\requestablealbums\\original";
    private LruCache<String, Bitmap> inMemoryCache;
    private boolean IsUsingDefaultImageThumbnail = true;
    private String ImageId;
    private ArrayList<Filter> Allergens;

    public MenuListItemDetailsView(Context context) {
        super(context);
    }

    @ViewById(resName = "menuListItemDetails_ivMainImage")
    ImageView ImageThumbnail;

    @ViewById(resName = "menuListItemDetails_tvQuantityLabel")
    TextView QuantityLabel;

    @ViewById(resName = "menuListItemDetails_tvIngredients")
    TextView Ingredients;

    @ViewById(resName = "menuListItemDetails_btnAllergens")
    ImageButton AllergensButton;

    ///Add more controls here

    public void bind(MenuListItem menuListItem, LruCache<String, Bitmap> cache) {
        this.inMemoryCache = cache;
        this.Allergens = new ArrayList<>(com.annimon.stream.Stream
                .of(menuListItem.Filters)
                .filter(filter -> filter.FilteredType == FilteredType.Dishes && filter.FilterType == FilterType.DishAllergens)
                .toList());

        if (menuListItem.ImageThumbnail != null) {
            this.ImageId = menuListItem.ImageThumbnail.Id;
        }
        if (StringHelper.isNotNullOrEmpty(menuListItem.QuantityDisplayText)){
            this.QuantityLabel.setText(menuListItem.QuantityDisplayText);
            this.QuantityLabel.setVisibility(VISIBLE);
        }

        ArrayList<String> toJoin = new ArrayList<String>();
        String joined = StringHelper.empty();
        for (Ingredient ingredient: menuListItem.Ingredients)
            toJoin.add(ingredient.Name);
        joined = StringHelper.join(", ", toJoin);
        if (StringHelper.isNotNullOrEmpty(joined)){
            this.Ingredients.setText(joined);
            this.Ingredients.setVisibility(VISIBLE);
        }

        final String imageKey = menuListItem.ImageThumbnail != null ? menuListItem.ImageThumbnail.Id : "default";
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);

        if (bitmap != null) {
            ImageThumbnail.setImageBitmap(bitmap);
        } else {
            ImageThumbnail.setImageResource(R.drawable.eat_paprika_100x100);
            this.downloadMenuListItemThumbnail(imageKey);
        }

        if (this.Allergens != null && this.Allergens.size() > 0)
            AllergensButton.setVisibility(VISIBLE);
        else
            AllergensButton.setVisibility(GONE);
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
                    ImageThumbnail.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                    IsUsingDefaultImageThumbnail = false;
                    addBitmapToMemoryCache(thumbnailId, bmp);
                }
                else {
                    IsUsingDefaultImageThumbnail = true;
                    ImageThumbnail.setImageResource(R.drawable.eat_paprika_100x100);
                }
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };

        DownloadBlobModel model = new DownloadBlobModel(thumbnailId, thumbnailBlobStorageContainerPath);
        new LoadAzureBlobAsyncTask(listener).execute(model);
    }

    @Click(resName = "menuListItemDetails_ivMainImage")
    public void ItemClick()
    {
        if (!IsUsingDefaultImageThumbnail && this.ImageId != null && !this.ImageId.equals(StringHelper.empty())){
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

                DownloadBlobModel model = new DownloadBlobModel(this.ImageId, originalBlobStorageContainerPath);
                new LoadAzureBlobAsyncTask(listener).execute(model);
            }
        }
    }

    @Click(resName = "menuListItemDetails_btnAllergens")
    public void allergensButtonClick()
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

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            inMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return inMemoryCache.get(key);
    }

}
