package com.mosy.kalin.mosy.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

public class DishFilter
        implements Parcelable {

    public String Id;
    public String Name;
    public String I18nResourceName;
    public boolean IsChecked;

    protected DishFilter(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.Id = data[0];
        this.Name = data[1];
        this.I18nResourceName = data[2];
        this.IsChecked = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(Name);
        parcel.writeString(I18nResourceName);
        parcel.writeInt(IsChecked ? 1 : 0);
    }

    public static final Creator<DishFilter> CREATOR = new Creator<DishFilter>() {
        @Override
        public DishFilter createFromParcel(Parcel in) {
            return new DishFilter(in);
        }

        @Override
        public DishFilter[] newArray(int size) {
            return new DishFilter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}