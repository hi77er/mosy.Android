package com.mosy.kalin.mosy.DTOs;

/**
 * Created by kkras on 7/25/2017.
 */

public class AuthenticationResult {
    private boolean isSuccessful;
    public boolean isSuccessful() {
        return isSuccessful;
    }

    private String accessToken;
    public String getAccessToken() {
        return accessToken;
    }

    private String error;
    public String getError() {
        return error;
    }

    public AuthenticationResult(boolean isSuccessful, String accessToken, String error) {
        this.isSuccessful = isSuccessful;
        this.accessToken = accessToken;
        this.error = error;
    }
}
