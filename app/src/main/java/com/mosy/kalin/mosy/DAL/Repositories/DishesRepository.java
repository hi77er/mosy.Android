package com.mosy.kalin.mosy.DAL.Repositories;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.DAL.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResult;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DishesRepository {

    private static final String searchMenuListItemsEndpointEnding = "dishes/closest";
    private static final String getMenuListItemFiltersEndpointEnding = "dishes/filters/all";


    public ArrayList<MenuListItem> loadDishes(SearchMenuListItemsBindingModel model){
        String endpoint = new ServiceEndpointFactory().constructMosyWebAPIDevEndpoint(searchMenuListItemsEndpointEnding);
        ArrayList<MenuListItem> menuItemsResult = new ArrayList<>();
        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<ArrayList<MenuListItem>>(){}.getType();
            menuItemsResult = jsonHttpClient.PostObject(endpoint, model, returnType, "yyyy-MM-dd'T'HH:mm:ss.", model.AuthTokenHeader);
            return menuItemsResult;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return menuItemsResult;
    }

    public RequestableFiltersResult getFilters(GetRequestableFiltersBindingModel model){
        String endpoint = new ServiceEndpointFactory().constructMosyWebAPIDevEndpoint(getMenuListItemFiltersEndpointEnding);
        RequestableFiltersResult response;

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<RequestableFiltersResult>(){}.getType();
            response = jsonHttpClient.Get(endpoint, null, returnType, StringHelper.empty(), model.AuthTokenHeader);
        } catch(Exception e) {
            e.printStackTrace();
            RequestableFiltersResult errResult = new RequestableFiltersResult();
            errResult.ErrorMessage = e.getMessage();
            return errResult ;
        }
        return response;
    }
}
