package com.mosy.kalin.mosy.DTOs.Enums;

import com.google.gson.annotations.SerializedName;

public enum TableAccountStatus {

    @SerializedName("1")
    New(1),

    @SerializedName("2")
    AwaitingOperatorApprovement(2),

    @SerializedName("3")
    Idle(3),

    @SerializedName("4")
    Closed(4);

    private final int value;
    public int getValue() {
        return value;
    }

    private TableAccountStatus(int value) {
        this.value = value;
    }
}
