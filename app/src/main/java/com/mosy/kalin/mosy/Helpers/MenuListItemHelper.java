package com.mosy.kalin.mosy.Helpers;

import android.content.Context;

import com.mosy.kalin.mosy.DTOs.WallMenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;

import java.util.List;

public class MenuListItemHelper {

    public static MenuListItemCulture getMenuListItemCulture(Context context, WallMenuListItem wallMenuListItem) {
        List<String> preferredLanguages = LocaleHelper.getPreferredLanguages(context);
        if (StringHelper.isNotNullOrEmpty(wallMenuListItem.DefaultMenuCulture)){
            preferredLanguages.add(preferredLanguages.size(), wallMenuListItem.DefaultMenuCulture);
        }

        MenuListItemCulture selectedCulture = null;
        for (String lang : preferredLanguages){
            boolean cultureSelected = false;
            for (MenuListItemCulture culture : wallMenuListItem.MenuListItemCultures){
                if (culture.Culture.contains(lang)){
                    selectedCulture = culture;
                    cultureSelected = true;
                    break;
                }
            }
            if (cultureSelected)
                break;
        }

        if (selectedCulture == null){
            selectedCulture = wallMenuListItem.MenuListItemCultures.get(0);
        }
        return selectedCulture;
    }

}