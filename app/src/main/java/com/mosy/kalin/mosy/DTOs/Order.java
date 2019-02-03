package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.OrderStatus;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.util.ArrayList;

public class Order extends HttpResult {

    @SerializedName("Id")
    public String Id;
    @SerializedName("TableAccountId")
    public String TableAccountId;
    @SerializedName("Status")
    public OrderStatus Status;
    @SerializedName("Name")
    public String Name;
    @SerializedName("DateCreated")
    public String DateCreated;
    @SerializedName("DateReady")
    public String DateReady;
    @SerializedName("DateDelivered")
    public String DateDelivered;

    @SerializedName("OrderRequestables")
    public ArrayList<OrderMenuItem> orderMenuItems;


}
