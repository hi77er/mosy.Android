package com.mosy.kalin.mosy.Models;

import com.mosy.kalin.mosy.Models.Interfaces.IServiceEndpointFactory;

/**
 * Created by kkras on 10.7.2016 г..
 */
public class ServiceEndpointFactory implements IServiceEndpointFactory {

    @Override
    public String getMosyEndpoint(String action) {
        //debug
        String path = "http://10.0.2.2:8080/api/" + action;
        return path;
    }
}
