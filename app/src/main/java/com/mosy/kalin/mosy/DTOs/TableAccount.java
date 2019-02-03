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
    @SerializedName("HasOperatorApprovement")
    public boolean HasOperatorApprovement;
    @SerializedName("Status")
    public TableAccountStatus Status;

//    @SerializedName("OpenerUsername")
//    public String OpenerUsername;
//
    public ArrayList<Order> Orders;


}
