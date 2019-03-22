package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

public class VenueContacts extends HttpResult {

    @SerializedName("id")
    public String Id;

    @SerializedName("address")
    public String Address;

    @SerializedName("phoneCountryCode")
    public String PhoneCountryCode;

    @SerializedName("phone")
    public String Phone;

    @SerializedName("facebook")
    public String FacebookUrl;

    @SerializedName("forsquare")
    public String Foursquare;

    @SerializedName("email")
    public String Email;

    @SerializedName("instagram")
    public String Instagram;

    @SerializedName("webPage")
    public String WebPage;


}
