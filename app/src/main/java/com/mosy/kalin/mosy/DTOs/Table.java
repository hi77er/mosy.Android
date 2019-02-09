package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class Table
        implements Serializable {

    @SerializedName("Id")
    public String id;
    @SerializedName("Name")
    public String name;
    @SerializedName("FBOId")
    public String venueId;
    @SerializedName("TableRegionId")
    public String tableRegionId;
    @SerializedName("DefaultSeatsCount")
    public int defaultSeatsCount;

}
