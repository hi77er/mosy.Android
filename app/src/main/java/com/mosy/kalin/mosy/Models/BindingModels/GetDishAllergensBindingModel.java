package com.mosy.kalin.mosy.Models.BindingModels;

public class GetDishAllergensBindingModel {

    public String MenuListItemId;
    public String getMenuListItemId() { return MenuListItemId; }
    public void setMenuListItemId(String menuListItemId) { this.MenuListItemId = menuListItemId; }

    public GetDishAllergensBindingModel() { }

    public GetDishAllergensBindingModel(String menuListItemId) {
        this.MenuListItemId = menuListItemId;
    }

}
