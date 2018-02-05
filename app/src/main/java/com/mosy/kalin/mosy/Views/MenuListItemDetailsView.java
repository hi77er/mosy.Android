package com.mosy.kalin.mosy.Views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Ingredient;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadMenuListItemThumbnailAsyncTask;
import com.mosy.kalin.mosy.Services.AzureBlobService;

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

    public MenuListItemDetailsView(Context context) {
        super(context);
    }

    @ViewById(resName = "menuListItemDetails_ivMainImage")
    ImageView ImageThumbnail;

    @ViewById(resName = "menuListItemDetails_tvIngredients")
    TextView Ingredients;
    ///Add more controls here

    public void bind(MenuListItem menuListItem, LruCache<String, Bitmap> cache) {
        this.inMemoryCache = cache;
        if (menuListItem.ImageThumbnail != null)
            this.ImageId = menuListItem.ImageThumbnail.Id;

        ArrayList<String> toJoin = new ArrayList<String>();
        String joined = StringHelper.empty();
        for (Ingredient ingredient: menuListItem.Ingredients)
            toJoin.add(ingredient.Name);
        joined = StringHelper.join(", ", toJoin);
        this.Ingredients.setText(joined);

        final String imageKey = menuListItem.ImageThumbnail != null ? menuListItem.ImageThumbnail.Id : "default";
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);

        if (bitmap != null) {
            ImageThumbnail.setImageBitmap(bitmap);
        } else {
            ImageThumbnail.setImageResource(R.drawable.eat_paprika);
            this.downloadMenuListItemThumbnail(imageKey);
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
                    ImageThumbnail.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                    IsUsingDefaultImageThumbnail = false;
                    addBitmapToMemoryCache(thumbnailId, bmp);
                }
                else {
                    IsUsingDefaultImageThumbnail = true;
                    ImageThumbnail.setImageResource(R.drawable.eat_paprika);
                }
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };

        DownloadBlobModel model = new DownloadBlobModel(thumbnailId, thumbnailBlobStorageContainerPath);
        new LoadMenuListItemThumbnailAsyncTask(listener).execute(model);
    }

    @Click(resName = "menuListItemDetails_ivMainImage")
    public void ItemClick()
    {
        if (!IsUsingDefaultImageThumbnail){
            final Dialog nagDialog = new Dialog(this.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);

            if (this.ImageId != null && this.ImageId.length() > 0) {
                byte[] byteArray = new AzureBlobService().GetBlob(this.ImageId, originalBlobStorageContainerPath);

                if (byteArray != null && byteArray.length > 0) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                    ivPreview.setImageBitmap(bmp);
                    nagDialog.show();
                }
            }
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
