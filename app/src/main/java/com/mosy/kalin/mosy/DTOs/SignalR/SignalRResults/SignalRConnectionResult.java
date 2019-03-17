package com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignalRConnectionResult implements Serializable {

    @SerializedName("ConnectionId")
    public String ConnectionId;

    @SerializedName("Username")
    public String Username;

    @SerializedName("HubName")
    public String HubName;

}
