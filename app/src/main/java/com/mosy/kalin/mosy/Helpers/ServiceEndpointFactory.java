package com.mosy.kalin.mosy.Helpers;

import com.mosy.kalin.mosy.Models.Interfaces.IServiceEndpointFactory;

/**
 * Created by kkras on 10.7.2016 г..
 **=====================================
 *  Documentation for connecting to WebApi while debugging using actual device (not using emulator) *******
 * 0. Turn Off mobile data on the Phone .
 * 1. Unplug lan cable or turn off any other internet connection from your PC.
 * 2. Assure both PC and Phone are connected to the same Wi-Fi network.
 * 2. Connect your android mobile to your pc using usb.
 * 3. Turn On USB Tethering on the Phone
 * 4. Back to your PC. Check your IP. (example: 192.168.42.37)
 * 5. Check your WebApi address in your PC. (example: http://192.168.42.37/api
 * 6. Back to your Phone. Try this URL: http://192.168.42.37/api
 **======================================
 * While debugging on Android Emulator do the following:
 * The Web API server should be configured to serve content on exactly 127.0.0.1 (the port doesn't matter)
 * Any additional binding will cause 10.0.2.2 not to be resolved
 * The client Android App should be referencing 10.0.2.2 instead of 127.0.0.1 (th IP of the emulator)
 */
public class ServiceEndpointFactory implements IServiceEndpointFactory {

    private static final String devEndpoint = "http://10.0.2.2:8080/api/"; // Use when debugging on Emulator
    private static final String devTokenEndpoint = "http://10.0.2.2:8080/token"; // Use when debugging on Emulator

//    private static final String devEndpoint = "http://192.168.42.241:8080/api"; // When debugging on actual Device
//    private static final String devTokenEndpoint = "http://192.168.42.241:8080/token"; // When debugging on actual Device

//    private static final String publicEndpoint = "http://mosyws/api";
//    private static final String publicTokenEndpoint = "http://mosyws/token";

//    @Override
//    public String getMosyWebAPIPublicEndpoint(String action) { return publicEndpoint + action; }

//    @Override
//    public String getMosyWebAPIPublicTokenEndpoint(String action) { return publicTokenEndpoint + action; }

    @Override
    public String getMosyWebAPIDevEndpoint(String action) {
        return devEndpoint + action;
    }
    @Override
    public String getMosyWebAPIDevTokenEndpoint() { return devTokenEndpoint; }



}
