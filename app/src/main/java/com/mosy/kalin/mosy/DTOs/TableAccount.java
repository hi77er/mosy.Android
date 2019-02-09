package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.io.Serializable;
import java.util.ArrayList;

public class TableAccount
        implements Serializable {

    @SerializedName("Id")
    public String Id;
    @SerializedName("TableName")
    public String TableName;
    @SerializedName("Status")
    public TableAccountStatus Status;

    @SerializedName("OpenerUsername")
    public String OpenerUsername;
    @SerializedName("AssignedOperatorUsername")
    public String AssignedOperatorUsername;

    @SerializedName("FBOTable")
    public Table Table;
    @SerializedName("Orders")
    public ArrayList<Order> Orders;


    //Custom props
    public boolean HasOrderItemStatusChanged;

}
