package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

import java.util.ArrayList;

/**
 * Created by kkras on 8/10/2017.
 */

public class MenuList extends ResultBase {

    @SerializedName("Id")
    public String Id;

    @SerializedName("FBOId")
    public String VenueId;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Summary")
    public String Summary;

    @SerializedName("IsPublic")
    public boolean IsPublic;

    //Collections
    @SerializedName("Requestables")
    public ArrayList<MenuListItem> menuListItems;

}