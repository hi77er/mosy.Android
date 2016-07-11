package com.mosy.kalin.mosy.Models;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.Interfaces.IModelHelper;
import com.mosy.kalin.mosy.Models.Interfaces.IServiceEndpointFactory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 7/7/2016.
 */
public class VenueModel {

    private IModelHelper ModelHelper = null;
    private IServiceEndpointFactory Factory = null;

    public VenueModel(IModelHelper modelHelper, IServiceEndpointFactory endpointFactory){
        if (modelHelper == null) throw new NullPointerException("modelHelper");
        if (endpointFactory == null) throw new NullPointerException("endpointFactory");

        this.ModelHelper = modelHelper;
        this.Factory = endpointFactory;
    }

    public Venue GetById(String id){
        Map<String, String> params = new HashMap<String, String>();
        params.put("Id", id);
        JSONObject response = this.ModelHelper.Get(Factory.getMosyEndpoint("fbo"), "Get", params);

        if (response == null) return null;
        Venue result = this.ModelHelper.Deserialize(response, Venue.class);

        return result;
    }

}
