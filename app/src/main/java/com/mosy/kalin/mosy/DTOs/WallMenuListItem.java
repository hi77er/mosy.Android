package com.mosy.kalin.mosy.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.io.Serializable;
import java.util.ArrayList;

public class WallMenuListItem
        extends HttpResult
        implements Parcelable, Serializable {

    @SerializedName("id")
    public String Id;

    @SerializedName("brochureId")
    public String BrochureId;

    @SerializedName("name")
    public String Name;

    @SerializedName("summary")
    public String Summary;

    @SerializedName("preparationEstimateSeconds")
    public int PreparationEstimateSeconds;

    @SerializedName("fboId")
    public String VenueId;

    @SerializedName("fboName")
    public String VenueName;

    @SerializedName("workingStatus")
    public String VenueWorkingStatus;

    @SerializedName("isRecommended")
    public boolean IsRecommended;

    @SerializedName("isNew")
    public boolean IsNew;

    @SerializedName("seenCount")
    public int SeenCount;

    @SerializedName("requestableImagesMetas")
    public ArrayList<MenuListItemImage> ImageThumbnails;

    @SerializedName("requestableImageMeta")
    public MenuListItemImage ImageThumbnail;

    @SerializedName("distanceToDevice")
    public double DistanceToCurrentDeviceLocation;

    @SerializedName("businessHours")
    public VenueBusinessHours VenueBusinessHours;

    @SerializedName("priceDisplayText")
    public String PriceDisplayText;

    @SerializedName("quantityDisplayText")
    public String QuantityDisplayText;

    @SerializedName("defaultMenuCulture")
    public String DefaultMenuCulture;

    @SerializedName("ingredients")
    public ArrayList<Ingredient> Ingredients;

    @SerializedName("filters")
    public ArrayList<Filter> Filters;

    @SerializedName("matchingFiltersIds")
    public ArrayList<String> MatchingFiltersIds;

    @SerializedName("mismatchingFiltersIds")
    public ArrayList<String> MismatchingFiltersIds;

    @SerializedName("requestableCultures")
    public ArrayList<MenuListItemCulture> MenuListItemCultures;

    @SuppressWarnings("unchecked")
    protected WallMenuListItem(Parcel in) {
        String[] data = new String[8];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.Id = data[0];
        this.BrochureId = data[1];
        this.Name = data[2];
        this.Summary = data[3];
        this.PreparationEstimateSeconds = Integer.parseInt(data[4]);
        this.VenueId = data[5];
        this.VenueName = data[6];
        this.Ingredients = in.readArrayList(null);
        this.Filters = in.readArrayList(null);
        this.MatchingFiltersIds = in.readArrayList(null);
        this.MismatchingFiltersIds = in.readArrayList(null);
        this.MenuListItemCultures = in.readArrayList(null);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(BrochureId);
        parcel.writeString(Name);
        parcel.writeString(Summary);
        parcel.writeInt(PreparationEstimateSeconds);
        parcel.writeString(VenueId);
        parcel.writeString(VenueName);
        parcel.writeList(Ingredients);
        parcel.writeList(Filters);
        parcel.writeList(MatchingFiltersIds);
        parcel.writeList(MismatchingFiltersIds);
        parcel.writeList(MenuListItemCultures);

    }

    public static final Creator<WallMenuListItem> CREATOR = new Creator<WallMenuListItem>() {
        @Override
        public WallMenuListItem createFromParcel(Parcel in) {
            return new WallMenuListItem(in);
        }

        @Override
        public WallMenuListItem[] newArray(int size) {
            return new WallMenuListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public MenuListItemDetailed toDetailed() {
        return new MenuListItemDetailed(
                this.Id,
                this.BrochureId,
                this.Name,
                this.Summary,
                this.VenueId,
                this.VenueName,
                this.VenueWorkingStatus,
                this.IsRecommended,
                this.IsNew,
                this.SeenCount,
                this.ImageThumbnails,
                this.DistanceToCurrentDeviceLocation,
                this.VenueBusinessHours,
                this.PriceDisplayText,
                this.QuantityDisplayText,
                this.DefaultMenuCulture,
                this.Ingredients,
                this.Filters,
                this.MatchingFiltersIds,
                this.MismatchingFiltersIds,
                this.MenuListItemCultures);
    }
}