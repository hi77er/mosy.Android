package com.mosy.kalin.mosy.DTOs.Enums;

import com.google.gson.annotations.SerializedName;

public enum ContactType {

    @SerializedName("0")
    Undefined(0),

    @SerializedName("1")
    Email(1),

    @SerializedName("2")
    Telephone(2),

    @SerializedName("3")
    Facebook(3),

    @SerializedName("4")
    Skype(4),

    @SerializedName("5")
    Twitter(5),

    @SerializedName("6")
    Foursquare(6),

    @SerializedName("7")
    GooglePlus(7),

    @SerializedName("8")
    Address(8);

    private final int value;
    public int getValue() {
        return value;
    }

    private ContactType(int value) {
        this.value = value;
    }
}
