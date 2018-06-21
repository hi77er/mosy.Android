package com.mosy.kalin.mosy.DTOs.Enums;

import com.google.gson.annotations.SerializedName;

public enum FilteredType {

    @SerializedName("1")
    Venues(1),

    @SerializedName("2")
    Dishes(2);

    private final int value;
    public int getValue() {
        return value;
    }

    private FilteredType(int value) {
        this.value = value;
    }
}
