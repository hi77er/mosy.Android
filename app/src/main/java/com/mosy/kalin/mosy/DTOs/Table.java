package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class Table
        implements Serializable {

    @SerializedName("id")
    public String Id;
    @SerializedName("name")
    public String Name;
    @SerializedName("fboId")
    public String VenueId;
    @SerializedName("tableRegionId")
    public String TableRegionId;
    @SerializedName("defaultSeatsCount")
    public int DefaultSeatsCount;
    @SerializedName("tableAccounts")
    public ArrayList<TableAccount> TableAccounts;


}
