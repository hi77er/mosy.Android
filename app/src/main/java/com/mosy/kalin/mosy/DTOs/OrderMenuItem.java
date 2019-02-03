package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.OrderMenuItemStatus;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

public class OrderMenuItem extends HttpResult {

    @SerializedName("Id")
    public String Id;
    @SerializedName("OrderId")
    public String OrderId;
    @SerializedName("RequestableId")
    public String RequestableId;
    @SerializedName("Status")
    public OrderMenuItemStatus Status;
    @SerializedName("DateCreated")
    public String DateCreated;
    @SerializedName("DateBeingProcessed")
    public String DateBeingProcessed;
    @SerializedName("DateDelivered")
    public String DateDelivered;
    @SerializedName("DateReady")
    public String DateReady;

    @SerializedName("Requestable")
    public MenuListItem MenuListItem;

}
