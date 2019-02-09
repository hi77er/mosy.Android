package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Role
        implements Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("Name")
    public String Name;

}