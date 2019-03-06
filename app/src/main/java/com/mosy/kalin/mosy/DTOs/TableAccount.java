package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.io.Serializable;
import java.util.ArrayList;

public class TableAccount
        implements Serializable {

    @SerializedName("id")
    public String Id;
    @SerializedName("tableName")
    public String TableName;
    @SerializedName("status")
    public TableAccountStatus Status;

    @SerializedName("openerUsername")
    public String OpenerUsername;
    @SerializedName("assignedOperatorUsername")
    public String AssignedOperatorUsername;

    @SerializedName("fboTable")
    public Table Table;
    @SerializedName("orders")
    public ArrayList<Order> Orders;


    //Custom props
    public boolean HasOrderItemStatusChanged;

}
