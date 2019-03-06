package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.OrderStatus;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.util.ArrayList;

public class Order extends HttpResult {

    @SerializedName("id")
    public String Id;
    @SerializedName("tableAccountId")
    public String TableAccountId;
    @SerializedName("status")
    public OrderStatus Status;
    @SerializedName("name")
    public String Name;
    @SerializedName("dateCreated")
    public String DateCreated;
    @SerializedName("dateReady")
    public String DateReady;
    @SerializedName("dateDelivered")
    public String DateDelivered;

    @SerializedName("orderRequestables")
    public ArrayList<OrderMenuItem> orderMenuItems;


}
