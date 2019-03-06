package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Venue
        implements Serializable {

    @SerializedName("id")
    public String Id;

    @SerializedName("locationId")
    public String LocationId;

    @SerializedName("name")
    public String Name;

    @SerializedName("class")
    public String Class;

    @SerializedName("description")
    public String Description;

    @SerializedName("seenCount")
    public String SeenCount;

}
