package com.mosy.kalin.mosy.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

import java.util.ArrayList;

/**
 * Created by kkras on 8/10/2017.
 */

public class MenuListItem
        extends ResultBase
        implements Parcelable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("BruchureId")
    public String BrochureId;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Summary")
    public String Summary;

    @SerializedName("PreparationEstimateSeconds")
    public int PreparationEstimateSeconds;
//
//    @SerializedName("RequestableIngredients")
//    public ArrayList<MenuListItemIngredient> RequestableIngredients;

    @SerializedName("Ingredients")
    public ArrayList<Ingredient> Ingredients;

    @SuppressWarnings("unchecked")
    protected MenuListItem(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.Id = data[0];
        this.BrochureId = data[1];
        this.Name = data[2];
        this.Summary = data[3];
        this.PreparationEstimateSeconds = Integer.parseInt(data[4]);
//        this.RequestableIngredients = in.readArrayList(null);
        this.Ingredients = in.readArrayList(null);
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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(BrochureId);
        parcel.writeString(Name);
        parcel.writeString(Summary);
        parcel.writeInt(PreparationEstimateSeconds);
//        parcel.writeList(RequestableIngredients);
        parcel.writeList(Ingredients);

    }
}