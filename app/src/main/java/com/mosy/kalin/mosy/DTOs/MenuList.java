package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.util.ArrayList;

/**
 * Created by kkras on 8/10/2017.
 */

public class MenuList extends HttpResult {

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
    public ArrayList<WallMenuListItem> wallMenuListItems;


}
