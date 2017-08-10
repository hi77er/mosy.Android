package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

/**
 * Created by kkras on 8/10/2017.
 */

public class Requestable extends ResultBase {

    @SerializedName("Id")
    public String Id;

    @SerializedName("BruchureId")
    public String BruchureId;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Summary")
    public String Summary;

    @SerializedName("PreparationEstimateSeconds")
    public int PreparationEstimateSeconds;

}
