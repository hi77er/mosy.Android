package com.mosy.kalin.mosy.DTOs.SignalR.SignalRResults;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.OrderMenuItemStatus;
import com.mosy.kalin.mosy.DTOs.Ingredient;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;
import com.mosy.kalin.mosy.DTOs.OrderMenuItem;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderItemStatusChangedResult
    implements Serializable {

    @SerializedName("Id")
    public String Id;
    @SerializedName("OrderId")
    public String OrderId;
    @SerializedName("OrderName")
    public String OrderName;
    @SerializedName("RequestableName")
    public String ItemName;
    @SerializedName("Status")
    public OrderMenuItemStatus Status;
    @SerializedName("DateCreated")
    public String DateCreated;
    @SerializedName("DateBeingProcessed")
    public String DateBeingProcessed;
    @SerializedName("DateReady")
    public String DateReady;

    @SerializedName("Requestable")
    public OrderMenuListItemResult MenuListItem;

    public OrderMenuItem toOrderMenuItem() {
        OrderMenuItem item = new OrderMenuItem();

        item.Id = this.Id;
        item.ItemName = this.ItemName;
        item.Status = this.Status;
        item.OrderId = this.OrderId;
        item.DateBeingProcessed = this.DateBeingProcessed;
        item.DateCreated = this.DateCreated;
        item.DateReady = this.DateReady;
        item.MenuListItem = new MenuListItem();

        if (this.MenuListItem != null) {
            item.MenuListItem.Id = this.MenuListItem.Id;
            item.MenuListItem.BrochureId = this.MenuListItem.Id;
            item.MenuListItem.CurrencyCode = this.MenuListItem.CurrencyCode;
            item.MenuListItem.DefaultMenuCulture = this.MenuListItem.DefaultMenuCulture;
            item.MenuListItem.SeenCount = this.MenuListItem.SeenCount;
            item.MenuListItem.IsPromoted = this.MenuListItem.IsPromoted;
            item.MenuListItem.IsPublic = this.MenuListItem.IsPublic;
            item.MenuListItem.PriceDisplayText = this.MenuListItem.PriceDisplayText;
            item.MenuListItem.Name = this.MenuListItem.Name;
            item.MenuListItem.Price = this.MenuListItem.Price;
            item.MenuListItem.UnitsOfMeasureType = this.MenuListItem.UnitsOfMeasureType;
            item.MenuListItem.QuantityDisplayText = this.MenuListItem.QuantityDisplayText;
            item.MenuListItem.MenuListItemCultures = new ArrayList<>();

            if (this.MenuListItem.MenuListItemCultures != null) {
                for (MenuListItemCultureResult cultureResult : this.MenuListItem.MenuListItemCultures) {
                    MenuListItemCulture culture = new MenuListItemCulture();
                    culture.Id = cultureResult.Id;
                    culture.Culture = cultureResult.Culture;
                    culture.MenuListItemId = cultureResult.MenuListItemId;
                    culture.MenuListItemName = cultureResult.MenuListItemName;
                    culture.MenuListItemDescription = cultureResult.MenuListItemDescription;
                    culture.Ingredients = new ArrayList<>();

                    if (cultureResult.Ingredients != null) {
                        for (IngredientResult ingredientResult : cultureResult.Ingredients) {
                            Ingredient ingredient = new Ingredient();
                            ingredient.Id = ingredientResult.Id;
                            ingredient.Name = ingredientResult.Name;
                            culture.Ingredients.add(ingredient);
                        }
                        item.MenuListItem.MenuListItemCultures.add(culture);
                    }
                }
            }
        }
        return item;
    }
}
