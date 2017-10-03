package com.mosy.kalin.mosy.Models.BindingModels;

/**
 * Created by kkras on 8/9/2017.
 */

public class GetMenuListItemImageBindingModel {

    public String MenuListItemId;
    public String getMenuListItemId() { return MenuListItemId; }
    public void setMenuListItemId(String menuListItemId) { this.MenuListItemId = menuListItemId; }

    public GetMenuListItemImageBindingModel() { }

    public GetMenuListItemImageBindingModel(String menuListItemId) {
        this.MenuListItemId = menuListItemId;
    }

}
