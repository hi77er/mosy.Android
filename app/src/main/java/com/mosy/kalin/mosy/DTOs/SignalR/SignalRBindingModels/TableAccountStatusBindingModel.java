package com.mosy.kalin.mosy.DTOs.SignalR.SignalRBindingModels;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;

import java.io.Serializable;

public class TableAccountStatusBindingModel implements Serializable {

    @SerializedName("TableAccountId")
    public String TableAccountId;

    @SerializedName("NewStatus")
    public TableAccountStatus NewStatus;

    @SerializedName("UpdaterUsername")
    public String UpdaterUsername;

}
