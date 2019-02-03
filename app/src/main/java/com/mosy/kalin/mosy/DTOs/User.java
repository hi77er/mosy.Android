package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class User
        implements Serializable {

    @SerializedName("Username")
    public String Username;

    @SerializedName("FirstName")
    public String FirstName;

    @SerializedName("LastName")
    public String LastName;

    @SerializedName("ProfileImage")
    public UserImage ProfileImage;

    @SerializedName("UserLogins")
    public ArrayList<UserExternalLogin> ExternalLogins;

    @SerializedName("Roles")
    public ArrayList<Role> Roles;

}