package com.mosy.kalin.mosy.DTOs.Enums;

import com.google.gson.annotations.SerializedName;

public enum FilterType {

    @SerializedName("101")
    VenueAccessibility(101),

    @SerializedName("102")
    VenueAvailability(102),

    @SerializedName("103")
    VenueAtmosphere(103),

    @SerializedName("104")
    VenueCulture(104),



    @SerializedName("201")
    DishType(201),

    @SerializedName("202")
    DishRegion(202),

    @SerializedName("203")
    DishMainIngredient(203),

    @SerializedName("204")
    DishAllergens(204);



    private final int value;
    public int getValue() {
        return value;
    }

    private FilterType(int value) {
        this.value = value;
    }
}
