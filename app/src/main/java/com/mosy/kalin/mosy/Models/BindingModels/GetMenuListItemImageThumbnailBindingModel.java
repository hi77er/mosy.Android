package com.mosy.kalin.mosy.Models.BindingModels;

/**
 * Created by kkras on 8/9/2017.
 */

public class GetMenuListItemImageThumbnailBindingModel {

    public String MenuListItemId;
    public String getVenueId() { return MenuListItemId; }
    public void setVenueId(String menuListItemId) { this.MenuListItemId = menuListItemId; }

    public GetMenuListItemImageThumbnailBindingModel() { }

    public GetMenuListItemImageThumbnailBindingModel(String menuListItemId) {
        this.MenuListItemId = menuListItemId;
    }

}
