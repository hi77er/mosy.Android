package com.mosy.kalin.mosy.Models;

import com.mosy.kalin.mosy.Models.Interfaces.IServiceEndpointFactory;

/**
 * Created by kkras on 10.7.2016 г..
 */
public class ServiceEndpointFactory implements IServiceEndpointFactory {

    private static final String publicEndpoint = "http://10.0.2.2:8080/api/";
    private static final String devEndpoint = "http://10.0.2.2:8080/api/";


    @Override
    public String getMosyWebAPIDevEndpoint(String action) {
        return devEndpoint + action;
    }

    @Override
    public String getMosyWebAPIPublicEndpoint(String action) {
        return publicEndpoint + action;
    }
}
