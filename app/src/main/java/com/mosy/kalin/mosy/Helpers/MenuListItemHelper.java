package com.mosy.kalin.mosy.Helpers;

import android.content.Context;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;

import java.util.List;

public class MenuListItemHelper {

    public static MenuListItemCulture getMenuListItemCulture(Context context, MenuListItem menuListItem) {
        List<String> preferredLanguages = LocaleHelper.getPreferredLanguages(context);
        if (StringHelper.isNotNullOrEmpty(menuListItem.DefaultMenuCulture)){
            preferredLanguages.add(preferredLanguages.size(), menuListItem.DefaultMenuCulture);
        }

        MenuListItemCulture selectedCulture = null;
        for (String lang : preferredLanguages){
            boolean cultureSelected = false;
            for (MenuListItemCulture culture : menuListItem.MenuListItemCultures){
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
            selectedCulture = menuListItem.MenuListItemCultures.get(0);
        }
        return selectedCulture;
    }

}