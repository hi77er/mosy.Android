package com.mosy.kalin.mosy.Models.Interfaces;

/**
 * Created by kkras on 10.7.2016 г..
 */
public interface IServiceEndpointFactory {

    String getMosyWebAPIPublicEndpoint(String action);
    String getMosyWebAPIDevTokenEndpoint();
    String getMosyWebAPIDevEndpoint(String action);
}
