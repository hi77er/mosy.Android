package com.mosy.kalin.mosy.Services;

import com.mosy.kalin.mosy.Async.Tasks.GetMenuListItemImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetMenuListItemImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.Models.BindingModels.GetMenuListItemImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetMenuListItemImageThumbnailBindingModel;

import org.androidannotations.annotations.EBean;

/**
 * Created by kkras on 8/25/2017.
 */

@EBean
public class MenuListItemsService {

    public MenuListItemImage downloadMenuListItemImageThumbnail(String dishId) {
        MenuListItemImage image = null;
        try {
            GetMenuListItemImageThumbnailBindingModel menuListItemImageModel = new GetMenuListItemImageThumbnailBindingModel(dishId);
            image = new GetMenuListItemImageThumbnailAsyncTask().execute(menuListItemImageModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public MenuListItemImage downloadMenuListItemImage(String dishId) {
        MenuListItemImage image = null;
        try {
            GetMenuListItemImageBindingModel menuListItemImageModel = new GetMenuListItemImageBindingModel(dishId);
            image = new GetMenuListItemImageAsyncTask().execute(menuListItemImageModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

}
