package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class User
        implements Serializable {

    @SerializedName("Email")
    public String Email;

    @SerializedName("FirstName")
    public String FirstName;

    @SerializedName("LastName")
    public String LastName;



}