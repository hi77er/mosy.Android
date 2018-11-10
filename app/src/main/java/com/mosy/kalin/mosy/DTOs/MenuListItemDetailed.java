package com.mosy.kalin.mosy.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DAL.Http.Results.ResultBase;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuListItemDetailed
        extends ResultBase
        implements Serializable {

    public String Id;
    public String BrochureId;
    public String Name;
    public String Summary;
    public int PreparationEstimateSeconds;
    public String VenueId;
    public String VenueName;
    public String VenueWorkingStatus;
    public boolean IsRecommended;
    public boolean IsNew;
    public int SeenCount;
    public MenuListItemImage ImageThumbnail;
    public double DistanceToCurrentDeviceLocation;
    public VenueBusinessHours VenueBusinessHours;
    public String PriceDisplayText;
    public String QuantityDisplayText;
    public String DefaultMenuCulture;
    public ArrayList<Ingredient> Ingredients;
    public ArrayList<Filter> Filters;
    public ArrayList<String> MatchingFiltersIds;
    public ArrayList<String> MismatchingFiltersIds;
    public ArrayList<MenuListItemCulture> MenuListItemCultures;

    public MenuListItemDetailed(String id
            ,String brochureId
            ,String name
            ,String summary
            ,String venueId
            ,String venueName
            ,String venueWorkingStatus
            ,boolean isRecommended
            ,boolean isNew
            ,int seenCount
            ,MenuListItemImage imageThumbnail
            ,double distanceToCurrentDeviceLocation
            ,VenueBusinessHours venueBusinessHours
            ,String priceDisplayText
            ,String quantityDisplayText
            ,String defaultMenuCulture
            ,ArrayList<Ingredient> ingredients
            ,ArrayList<Filter> filters
            ,ArrayList<String> matchingFiltersIds
            ,ArrayList<String> mismatchingFiltersIds
            ,ArrayList<MenuListItemCulture> menuListItemCultures){
        this.Id = id;
        this.Summary = summary;
        this.BrochureId = brochureId;
        this.Name = name;
        this.VenueId = venueId;
        this.VenueName = venueName;
        this.VenueWorkingStatus = venueWorkingStatus;
        this.IsRecommended = isRecommended;
        this.IsNew  = isNew;
        this.SeenCount  = seenCount;
        this.ImageThumbnail = imageThumbnail;
        this.DistanceToCurrentDeviceLocation = distanceToCurrentDeviceLocation;
        this.VenueBusinessHours = venueBusinessHours;
        this.PriceDisplayText = priceDisplayText;
        this.QuantityDisplayText= quantityDisplayText;
        this.DefaultMenuCulture = defaultMenuCulture;
        this.Ingredients = ingredients;
        this.Filters = filters;
        this.MatchingFiltersIds = matchingFiltersIds;
        this.MismatchingFiltersIds = mismatchingFiltersIds;
        this.MenuListItemCultures = menuListItemCultures;

    }
}