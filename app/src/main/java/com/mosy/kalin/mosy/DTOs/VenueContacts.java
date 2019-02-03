package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

public class VenueContacts extends HttpResult {

    @SerializedName("Id")
    public String Id;

    @SerializedName("Address")
    public String Address;

    @SerializedName("Phone")
    public String Phone;

    @SerializedName("Facebook")
    public String FacebookUrl;

    @SerializedName("Forsquare")
    public String Foursquare;

    @SerializedName("Email")
    public String Email;

    @SerializedName("Instagram")
    public String Instagram;

    @SerializedName("WebPage")
    public String WebPage;


}
