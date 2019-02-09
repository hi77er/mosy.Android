package com.mosy.kalin.mosy.DTOs.Enums;

import com.google.gson.annotations.SerializedName;

public enum OrderStatus {

    @SerializedName("1")
    New(1),

    @SerializedName("2")
    Received(2),

    @SerializedName("3")
    RequestablesBeingPrepared(3),

    @SerializedName("4")
    Ready(4);

    private final int value;
    public int getValue() {
        return value;
    }

    private OrderStatus(int value) {
        this.value = value;
    }
}
