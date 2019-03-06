package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.OrderMenuItemStatus;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

public class OrderMenuItem extends HttpResult {

    @SerializedName("id")
    public String Id;
    @SerializedName("orderId")
    public String OrderId;
    @SerializedName("requestableId")
    public String RequestableId;
    @SerializedName("status")
    public OrderMenuItemStatus Status;
    @SerializedName("requestableName")
    public String ItemName;
    @SerializedName("dateCreated")
    public String DateCreated;
    @SerializedName("dateBeingProcessed")
    public String DateBeingProcessed;
    @SerializedName("dateDelivered")
    public String DateDelivered;
    @SerializedName("dateReady")
    public String DateReady;

    @SerializedName("requestable")
    public MenuListItem MenuListItem;

}
