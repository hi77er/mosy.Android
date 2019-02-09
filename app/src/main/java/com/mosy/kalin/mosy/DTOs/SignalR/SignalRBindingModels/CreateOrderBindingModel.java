package com.mosy.kalin.mosy.DTOs.SignalR.SignalRBindingModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateOrderBindingModel {


    @SerializedName("CreatorUsername")
    public String CreatorUsername;

    @SerializedName("TableAccountId")
    public String TableAccountId;

    @SerializedName("RequestableIds")
    public ArrayList<String> RequestableIds;

}
