package com.mosy.kalin.mosy.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuListItemCulture
        extends HttpResult
        implements Parcelable, Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("RequestableId")
    public String MenuListItemId;

    @SerializedName("Culture")
    public String Culture;

    @SerializedName("RequestableName")
    public String MenuListItemName;

    @SerializedName("RequestableDescription")
    public String MenuListItemDescription;

    @SerializedName("Ingredients")
    public ArrayList<Ingredient> Ingredients;

    @SuppressWarnings("unchecked")
    protected MenuListItemCulture(Parcel in) {
        String[] data = new String[8];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.Id = data[0];
        this.MenuListItemId = data[1];
        this.Culture = data[2];
        this.MenuListItemName = data[3];
        this.MenuListItemDescription = data[4];
        this.Ingredients = in.readArrayList(null);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(MenuListItemId);
        parcel.writeString(Culture);
        parcel.writeString(MenuListItemName);
        parcel.writeString(MenuListItemDescription);
        parcel.writeList(Ingredients);
    }

    public static final Creator<MenuListItemCulture> CREATOR = new Creator<MenuListItemCulture>() {
        @Override
        public MenuListItemCulture createFromParcel(Parcel in) {
            return new MenuListItemCulture(in);
        }

        @Override
        public MenuListItemCulture[] newArray(int size) {
            return new MenuListItemCulture[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}