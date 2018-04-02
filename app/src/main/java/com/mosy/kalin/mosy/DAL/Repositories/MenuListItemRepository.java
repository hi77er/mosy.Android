package com.mosy.kalin.mosy.DAL.Repositories;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DAL.Http.HttpParams;
import com.mosy.kalin.mosy.DAL.Http.JSONHttpClient;
import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.BindingModels.GetDishAllergensBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MenuListItemRepository {

    private static final String getMenuListItemsAllergensEndpointEnding = "Requestable/GetAllergens";

    public ArrayList<DishFilter> getAllergens(GetDishAllergensBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getMenuListItemsAllergensEndpointEnding);
        ArrayList<DishFilter> result = new ArrayList<>();
        try {
            HttpParams params = new HttpParams();
            params.put("requestableId", model.MenuListItemId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<ArrayList<DishFilter>>(){}.getType();
            result = jsonHttpClient.Get(endpoint, params, returnType, "HH:mm:ss", StringHelper.empty());
            return result;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
