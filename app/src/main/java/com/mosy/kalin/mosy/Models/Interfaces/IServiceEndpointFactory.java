package com.mosy.kalin.mosy.Models.Interfaces;

public interface IServiceEndpointFactory {

    String getMosyWebAPIDevTokenEndpoint();
    String getMosyWebAPIDevEndpoint(String action);
}
