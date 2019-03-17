package com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Ingredient;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuListItemCultureResult
    implements Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("RequestableId")
    public String MenuListItemId;

    @SerializedName("Culture")
    public String Culture;

    @SerializedName("RequestableName")
    public String MenuListItemName;

    @SerializedName("RequestableDescription")
    public String MenuListItemDescription;

    @SerializedName("Ingredients")
    public ArrayList<IngredientResult> Ingredients;
}
