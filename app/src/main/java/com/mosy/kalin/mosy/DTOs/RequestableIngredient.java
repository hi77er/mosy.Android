package com.mosy.kalin.mosy.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

/**
 * Created by kkras on 8/22/2017.
 */

public class RequestableIngredient
        extends ResultBase
        implements Parcelable {

    @SerializedName("IngredientId")
    public String IngredientId;

    @SerializedName("RequestableId")
    public String RequestableId;

    @SerializedName("Removable")
    public boolean Removable;


    protected RequestableIngredient(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.IngredientId = data[0];
        this.RequestableId = data[1];
        this.Removable = Boolean.parseBoolean(data[2]);
    }

    public static final Creator<RequestableIngredient> CREATOR = new Creator<RequestableIngredient>() {
        @Override
        public RequestableIngredient createFromParcel(Parcel in) {
            return new RequestableIngredient(in);
        }

        @Override
        public RequestableIngredient[] newArray(int size) {
            return new RequestableIngredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(IngredientId);
        parcel.writeString(RequestableId);
        parcel.writeByte((byte) (Removable ? 1 : 0));
    }
}
