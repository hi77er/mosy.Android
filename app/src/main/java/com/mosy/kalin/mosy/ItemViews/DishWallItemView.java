package com.mosy.kalin.mosy.ItemViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.DTOs.Enums.WorkingStatus;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.WallMenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.BusinessHoursHelper;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.MenuListItemHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.Services.AzureBlobService;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_dish)
public class DishWallItemView
        extends WallItemViewBase {

    private boolean IsUsingDefaultThumbnail;

    private Context baseContext;
    private WallMenuListItem WallMenuListItem;


    @ViewById(R.id.menuListItem_tvName)
    TextView nameTextView;
    @ViewById(R.id.menuListItem_tvVenueName)
    TextView venueNameTextView;
    @ViewById(R.id.menuListItem_ivThumbnail)
    ImageView imageThumbnail;

    @ViewById(R.id.menuListItem_tvWorkingStatusLabel)
    TextView workingStatusLabel;
    @ViewById(R.id.menuListItem_tvRecommendedLabel)
    TextView recommendedLabel;
    @ViewById(R.id.menuListItem_tvNewLabel)
    TextView newLabel;

    @ViewById(R.id.menuListItem_tvDistance)
    TextView distanceFromDeviceTextView;
    @ViewById(R.id.menuListItem_tvWalkingTime)
    TextView walkingTimeTextView;
    @ViewById(R.id.menuListItem_tvPriceTag)
    TextView priceTagTextVIew;
//    @ViewById(resName = "menuListItem_tvRatingTag")
//    TextView ratingTagTextView;

    public DishWallItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(WallMenuListItem wallMenuListItem) {
        this.imageThumbnail.setImageDrawable(null);

        if (wallMenuListItem != null) {
            this.WallMenuListItem = wallMenuListItem;

            MenuListItemCulture selectedCulture = MenuListItemHelper.getMenuListItemCulture(this.baseContext, wallMenuListItem);

            this.nameTextView.setText(selectedCulture.MenuListItemName);
            this.venueNameTextView.setText(wallMenuListItem.VenueName);

            if (wallMenuListItem.ImageThumbnail != null
                    && wallMenuListItem.ImageThumbnail.Bitmap != null) {
                this.imageThumbnail.setImageBitmap(wallMenuListItem.ImageThumbnail.Bitmap);
                IsUsingDefaultThumbnail = false;
            }
            else {
                this.setDefaultImageThumbnail();
            }

            this.distanceFromDeviceTextView.setVisibility(INVISIBLE);
            this.walkingTimeTextView.setVisibility(INVISIBLE);
            if (wallMenuListItem.DistanceToCurrentDeviceLocation > 0)
            {
                String distance = LocationHelper.buildDistanceText(wallMenuListItem.DistanceToCurrentDeviceLocation);
                if (StringHelper.isNotNullOrEmpty(distance)){
                    this.distanceFromDeviceTextView.setText(distance);
                    this.distanceFromDeviceTextView.setVisibility(VISIBLE);

                    String timeWalking = LocationHelper.buildMinutesWalkingText(wallMenuListItem.DistanceToCurrentDeviceLocation);
                    if (StringHelper.isNotNullOrEmpty(timeWalking)) {
                        this.walkingTimeTextView.setText(timeWalking);
                        this.walkingTimeTextView.setVisibility(VISIBLE);
                    }
                }
            }

            if (wallMenuListItem.PriceDisplayText != null) {
                this.priceTagTextVIew.setText(wallMenuListItem.PriceDisplayText);
                this.priceTagTextVIew.setVisibility(VISIBLE);
            }

//            WorkingStatus status = BusinessHoursHelper.getWorkingStatus(wallMenuListItem.VenueBusinessHours);
            WorkingStatus status = BusinessHoursHelper.getWorkingStatus(wallMenuListItem.VenueWorkingStatus);
            this.workingStatusLabel.setVisibility(VISIBLE);
            switch (status){
                case Open:
                    this.workingStatusLabel.setText(getResources().getString(R.string.item_dish_workingStatusOpenedLabelTextView));
                    this.workingStatusLabel.setBackgroundResource(R.color.colorTertiary);
                    break;
                case Open247:
                    this.workingStatusLabel.setText(getResources().getString(R.string.item_dish_workingStatus247LabelTextView));
                    this.workingStatusLabel.setBackgroundResource(R.color.colorTertiaryLight);
                    break;
                case Closed:
                    this.workingStatusLabel.setText(getResources().getString(R.string.item_dish_workingStatusClosedLabelTextView));
                    this.workingStatusLabel.setBackgroundResource(R.color.colorDarkRed);
                    break;
                case Unknown:
                    this.workingStatusLabel.setVisibility(GONE);
                    break;
            }
            this.recommendedLabel.setVisibility(wallMenuListItem.IsRecommended ? VISIBLE : GONE);
            this.newLabel.setVisibility(wallMenuListItem.IsNew ? VISIBLE : GONE);
        }
    }

    private void setDefaultImageThumbnail() {
        boolean isVector = false;
        int drawableId = R.drawable.eat_paprika_100x100;
        if (this.WallMenuListItem != null && this.WallMenuListItem.Filters != null){
            for (Filter filter : this.WallMenuListItem.Filters) {
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
            Bitmap defaultImageBitmap = BitmapFactory.decodeResource(this.baseContext.getResources(), drawableId);
            this.imageThumbnail.setImageBitmap(defaultImageBitmap);
        }
        IsUsingDefaultThumbnail = true;
    }

    @Click(R.id.menuListItem_ivThumbnail)
    public void ImageClick()
    {
        if (!this.IsUsingDefaultThumbnail
                && this.WallMenuListItem != null
                && StringHelper.isNotNullOrEmpty(this.WallMenuListItem.ImageThumbnail.Id)){

            final Dialog nagDialog = new Dialog(this.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);

            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override public void onPreExecute() {
                    ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                    ivPreview.setVisibility(GONE);
                    LinearLayout progressLayout = nagDialog.findViewById(R.id.llInitialLoadingProgress);
                    progressLayout.setVisibility(VISIBLE);
                }
                @Override public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)){
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        LinearLayout progressLayout = nagDialog.findViewById(R.id.llInitialLoadingProgress);
                        progressLayout.setVisibility(GONE);

                        ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                        ivPreview.setImageBitmap(bmp);
                        ivPreview.setVisibility(VISIBLE);

                        nagDialog.show();
                    } else
                        throw new NullPointerException("Image not found");
                }
            };

            new AzureBlobService().downloadMenuListItemThumbnail(this.baseContext, this.WallMenuListItem.ImageThumbnail.Id, ImageResolution.FormatOriginal, listener);
        }
    }

}
