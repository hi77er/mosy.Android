package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Venue
        implements Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("LocationId")
    public String LocationId;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Class")
    public String Class;

    @SerializedName("Description")
    public String Description;

    @SerializedName("SeenCount")
    public String SeenCount;

}
