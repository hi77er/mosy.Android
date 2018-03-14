package com.mosy.kalin.mosy.DAL.Repositories;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.DAL.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MenuRepository {

    private static final String searchMenuListItemsEndpointEnding = "Requestable/QueryClosestRequestables";
    private static final String getMenuListItemFiltersEndpointEnding = "filters/All";


    public ArrayList<MenuListItem> searchMenuListItems(SearchMenuListItemsBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(searchMenuListItemsEndpointEnding);
        ArrayList<MenuListItem> menuItemsResult = new ArrayList<>();
        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<ArrayList<MenuListItem>>(){}.getType();
            menuItemsResult = jsonHttpClient.PostObject(endpoint, model, returnType, "HH:mm:ss");
            return menuItemsResult;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return menuItemsResult;
    }

    public RequestableFiltersResponse getMenuListItemFilters(GetRequestableFiltersBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getMenuListItemFiltersEndpointEnding);
        RequestableFiltersResponse response;

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<RequestableFiltersResponse>(){}.getType();
            response = jsonHttpClient.Get(endpoint, null, returnType, StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            RequestableFiltersResponse errResult = new RequestableFiltersResponse ();
            errResult.ErrorMessage = e.getMessage();
            return errResult ;
        }
        return response;
    }
}
