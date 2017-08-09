package com.mosy.kalin.mosy.DTOs.Results;

/**
 * Created by kkras on 7/29/2017.
 */

public class RegisterResult extends ResultBase {
    private boolean isSuccessful;
    public boolean isSuccessful() {
        return isSuccessful;
    }

    private String errorMessage;
    public String getErrorMessage() {
        return errorMessage;
    }

    public RegisterResult(boolean isSuccessful, String errorMessage) {
        this.isSuccessful = isSuccessful;
        this.errorMessage = errorMessage;
    }
}
