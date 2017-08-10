package com.mosy.kalin.mosy.Models.BindingModels;

/**
 * Created by kkras on 7/24/2017.
 */

public class RegisterBindingModel {

    public String Email;
    public String getEmail() { return Email; }
    public void setEmail(String email) { this.Email = email; }

    public String Password;
    public String getPassword() { return Password; }

    public String ConfirmPassword;
    public String getConfirmPassword() { return ConfirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.ConfirmPassword = confirmPassword; }

    public RegisterBindingModel() { }
    public RegisterBindingModel(String email, String password, String confirmPassword) {
        this.Email = email;
        this.Password = password;
        this.ConfirmPassword = confirmPassword;
    }

}
