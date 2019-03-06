package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.util.ArrayList;

/**
 * Created by kkras on 8/10/2017.
 */

public class MenuList extends HttpResult {

    @SerializedName("id")
    public String Id;

    @SerializedName("fboId")
    public String VenueId;

    @SerializedName("name")
    public String Name;

    @SerializedName("summary")
    public String Summary;

    @SerializedName("isPublic")
    public boolean IsPublic;

    //Collections
    @SerializedName("requestables")
    public ArrayList<WallMenuListItem> wallMenuListItems;


}
