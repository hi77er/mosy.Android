package com.mosy.kalin.mosy.DTOs.HttpResponses;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.MenuList;

import java.io.Serializable;
import java.util.ArrayList;

public class PublicMenuResponse implements Serializable {

    @SerializedName("MenuCultures")
    public ArrayList<String> MenuCultures;

    //Collections
    @SerializedName("Brochures")
    public ArrayList<MenuList> MenuLists;

}
