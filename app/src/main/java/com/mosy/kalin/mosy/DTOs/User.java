package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class User
        implements Serializable {

    @SerializedName("username")
    public String Username;

    @SerializedName("firstName")
    public String FirstName;

    @SerializedName("lastName")
    public String LastName;

    @SerializedName("profileImage")
    public UserImage ProfileImage;

    @SerializedName("userLogins")
    public ArrayList<UserExternalLogin> ExternalLogins;

    @SerializedName("roles")
    public ArrayList<Role> Roles;

}