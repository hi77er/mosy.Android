package com.mosy.kalin.mosy.Models.BindingModels;

/**
 * Created by kkras on 7/29/2017.
 */

public class LoginBindingModel {
    public String Email;
    public String getEmail() { return Email; }
    public void setEmail(String email) { this.Email = email; }

    public String Password;
    public String getPassword() { return Password; }

    public LoginBindingModel() { }
    public LoginBindingModel(String email, String password) {
        this.Email = email;
        this.Password = password;
    }
}
