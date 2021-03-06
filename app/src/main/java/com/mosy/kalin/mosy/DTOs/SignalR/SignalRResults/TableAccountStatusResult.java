package com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.TableAccount;

import java.io.Serializable;

public class TableAccountStatusResult implements Serializable {

    @SerializedName("TableAccountId")
    public String TableAccountId;

    @SerializedName("Status")
    public TableAccountStatus Status;

    @SerializedName("AssignedOperatorUsername")
    public String AssignedOperatorUsername;

    @SerializedName("NeedsItemsStatusUpdate")
    public boolean NeedsItemsStatusUpdate;

    @SerializedName("TableAccount")
    public TableAccount TableAccount;
}
