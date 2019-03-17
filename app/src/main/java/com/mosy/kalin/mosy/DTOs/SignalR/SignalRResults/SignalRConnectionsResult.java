package com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SignalRConnectionsResult {

    @SerializedName("RecentConnection")
    public SignalRConnectionResult RecentConnection;

    @SerializedName("AllConnections")
    public ArrayList<SignalRConnectionResult> AllConnections;

}
