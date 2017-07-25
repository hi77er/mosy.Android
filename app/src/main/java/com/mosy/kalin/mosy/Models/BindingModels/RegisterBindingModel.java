package com.mosy.kalin.mosy.Models.BindingModels;

/**
 * Created by kkras on 7/24/2017.
 */

public class RegisterBindingModel {
    private String email;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    private String password;
    public String getPassword() { return password; }

    private String confirmPassword;
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public RegisterBindingModel() { }
    public RegisterBindingModel(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }


    
}
