package com.mosy.kalin.mosy.Helpers;

import com.mosy.kalin.mosy.Models.Interfaces.IServiceEndpointFactory;

/**
 * Created by kkras on 10.7.2016 г..
 */
public class ServiceEndpointFactory implements IServiceEndpointFactory {

    // any additional binding will cause 10.0.2.2 not to be resolved
    // the server should be configured t oserve content on exactly 127.0.0.1
    // the server should be configured t oserve content on exactly 127.0.0.1
    private static final String publicEndpoint = "http://mosyws/api";
    private static final String publicTokenEndpoint = "http://mosyws/token";

    private static final String devEndpoint = "http://10.0.2.2:8080/api/"; // Use when debugging on Emulator
    private static final String devTokenEndpoint = "http://10.0.2.2:8080/token"; // Use when debugging on Emulator

    //private static final String devEndpoint = "http://172.16.1.100:8080/api"; // When debugging on actual Device
    //private static final String devTokenEndpoint = "http://172.16.1.100:8080/token"; // When debugging on actual Device

    @Override
    public String getMosyWebAPIPublicEndpoint(String action) { return publicEndpoint + action; }

    @Override
    public String getMosyWebAPIDevEndpoint(String action) {
        return devEndpoint + action;
    }
    @Override
    public String getMosyWebAPIDevTokenEndpoint() { return devTokenEndpoint; }



}
