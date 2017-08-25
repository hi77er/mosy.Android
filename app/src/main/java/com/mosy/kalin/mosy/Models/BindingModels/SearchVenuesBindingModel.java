package com.mosy.kalin.mosy.Models.BindingModels;

/**
 * Created by kkras on 8/7/2017.
 */

public class SearchVenuesBindingModel {

    public String Query;
    public String getQuery() { return Query; }
    public void setQuery(String query) { this.Query = query; }

    public SearchVenuesBindingModel() { }
    public SearchVenuesBindingModel(String query) { this.Query = query; }

}
