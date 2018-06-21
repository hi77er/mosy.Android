package com.mosy.kalin.mosy.DTOs.Enums;

import com.google.gson.annotations.SerializedName;

public enum FilterType {
    @SerializedName("1")
    VenueBadge(1),

    @SerializedName("2")
    VenueCulture(2),

    @SerializedName("3")
    DishType(3),

    @SerializedName("4")
    DishRegion(4),

    @SerializedName("5")
    DishMainIngredient(5),

    @SerializedName("6")
    DishAllergens(6);

    private final int value;
    public int getValue() {
        return value;
    }

    private FilterType(int value) {
        this.value = value;
    }
}
