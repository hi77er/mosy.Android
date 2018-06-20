package com.mosy.kalin.mosy.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.mosy.kalin.mosy.DTOs.Enums.FilterType;
import com.mosy.kalin.mosy.DTOs.Enums.FilteredType;

public class Filter
        implements Parcelable {

    public String Id;
    public String Name;
    public String I18nResourceName;
    public String Description;
    public String I18nResourceDescription;
    public FilteredType FilteredType;
    public FilterType FilterType;
    public int OrderIndex;
    public boolean IsChecked;

    protected Filter(Parcel in) {
        String[] data = new String[9];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.Id = data[0];
        this.Name = data[1];
        this.I18nResourceName = data[2];
        this.Description = data[3];
        this.I18nResourceDescription = data[4];
        this.FilteredType = com.mosy.kalin.mosy.DTOs.Enums.FilteredType.valueOf(data[5]);
        this.FilterType = com.mosy.kalin.mosy.DTOs.Enums.FilterType.valueOf(data[6]);
        this.OrderIndex = Integer.parseInt(data[7]);
        this.IsChecked = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(Name);
        parcel.writeString(I18nResourceName);
        parcel.writeString(Description);
        parcel.writeString(I18nResourceDescription);
        if (this.FilteredType != null) parcel.writeString(this.FilteredType.name());
        if (this.FilterType != null) parcel.writeString(this.FilterType.name());
        parcel.writeInt(this.OrderIndex);
        parcel.writeInt(IsChecked ? 1 : 0);
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}