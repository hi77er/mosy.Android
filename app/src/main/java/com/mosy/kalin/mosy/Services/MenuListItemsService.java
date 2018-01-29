package com.mosy.kalin.mosy.Services;

import com.mosy.kalin.mosy.Async.Tasks.GetMenuListItemImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetMenuListItemImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.SearchMenuListItemsAsyncTask;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.GetMenuListItemImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetMenuListItemImageThumbnailBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@EBean
public class MenuListItemsService {

    public ArrayList<MenuListItem> searchMenuListItems(
            int maxResultsCount,
            double lastKnownLatitude,
            double lastKnownLongitude,
            Boolean isPromoted,
            String query,
            ArrayList<String> phaseFilterIds,
            ArrayList<String> regionFilterIds,
            ArrayList<String> spectrumFilterIds) {
        ArrayList<MenuListItem> result = null;

        try {
            SearchMenuListItemsBindingModel model = new SearchMenuListItemsBindingModel(
                    8,
                    lastKnownLatitude,
                    lastKnownLongitude,
                    isPromoted,
                    query,
                    phaseFilterIds,
                    regionFilterIds,
                    spectrumFilterIds);
            result = new SearchMenuListItemsAsyncTask().execute(model).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

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

    public void LoadMenuListItemImageThumbnails(ArrayList<MenuListItem> menuListItems) {
        for (MenuListItem item: menuListItems) {
            if (item.ImageThumbnail != null && item.ImageThumbnail.Id != null && item.ImageThumbnail.Id.length() > 0)
                item.ImageThumbnail.Bytes = new AzureBlobService().GetBlob(item.ImageThumbnail.Id, "userimages\\requestablealbums\\100x100");
        }
    }

//    public void sortMenuListItemsByDistanceToDevice(ArrayList<MenuListItem> items) {
//        for (MenuListItem venue: items)
//            if (venue.DistanceToCurrentDeviceLocation == 0) venue.DistanceToCurrentDeviceLocation = 999999999;
//
//        Collections.sort(items, new Comparator<MenuListItem>() {
//            @Override
//            public int compare(MenuListItem v1, MenuListItem v2) {
//                return Double.compare(v1.DistanceToCurrentDeviceLocation, v2.DistanceToCurrentDeviceLocation);
//            }
//        });
//    }

}
