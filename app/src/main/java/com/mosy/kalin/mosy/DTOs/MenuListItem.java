package com.mosy.kalin.mosy.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DAL.Http.Results.ResultBase;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuListItem
        extends ResultBase
        implements Parcelable, Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("BrochureId")
    public String BrochureId;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Summary")
    public String Summary;

    @SerializedName("PreparationEstimateSeconds")
    public int PreparationEstimateSeconds;

    @SerializedName("FBOId")
    public String VenueId;

    @SerializedName("FBOName")
    public String VenueName;

    @SerializedName("WorkingStatus")
    public String VenueWorkingStatus;

    @SerializedName("RequestableImageMeta")
    public MenuListItemImage ImageThumbnail;

    @SerializedName("DistanceToDevice")
    public double DistanceToCurrentDeviceLocation;

    @SerializedName("BusinessHours")
    public VenueBusinessHours VenueBusinessHours;

    @SerializedName("PriceDisplayText")
    public String PriceDisplayText;

    @SerializedName("QuantityDisplayText")
    public String QuantityDisplayText;

    @SerializedName("DefaultMenuCulture")
    public String DefaultMenuCulture;

    @SerializedName("Ingredients")
    public ArrayList<Ingredient> Ingredients;

    @SerializedName("Filters")
    public ArrayList<Filter> Filters;

    @SerializedName("MatchingFiltersIds")
    public ArrayList<String> MatchingFiltersIds;

    @SerializedName("MismatchingFiltersIds")
    public ArrayList<String> MismatchingFiltersIds;

    @SerializedName("RequestableCultures")
    public ArrayList<MenuListItemCulture> MenuListItemCultures;

    @SuppressWarnings("unchecked")
    protected MenuListItem(Parcel in) {
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

    public static final Creator<MenuListItem> CREATOR = new Creator<MenuListItem>() {
        @Override
        public MenuListItem createFromParcel(Parcel in) {
            return new MenuListItem(in);
        }

        @Override
        public MenuListItem[] newArray(int size) {
            return new MenuListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}