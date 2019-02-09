package com.mosy.kalin.mosy.DTOs.Enums;

import com.google.gson.annotations.SerializedName;

public enum OrderMenuItemStatus {

    @SerializedName("1")
    New(1),

    @SerializedName("2")
    AwaitingAccountApproval(2),

    @SerializedName("3")
    Received(3),

    @SerializedName("4")
    BeingPrepared(4),

    @SerializedName("5")
    ReadyForDelivery(5),

    @SerializedName("6")
    Delivered(6);

    private final int value;
    public int getValue() {
        return value;
    }

    private OrderMenuItemStatus(int value) {
        this.value = value;
    }
}
