package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.MenuList;

import java.io.Serializable;
import java.util.ArrayList;

public class PublicMenuResult implements Serializable {


    @SerializedName("defaultMenuCulture")
    public String DefaultMenuCulture;

    @SerializedName("menuCultures")
    public ArrayList<String> MenuCultures;

    //Collections
    @SerializedName("brochures")
    public ArrayList<MenuList> MenuLists;

}
