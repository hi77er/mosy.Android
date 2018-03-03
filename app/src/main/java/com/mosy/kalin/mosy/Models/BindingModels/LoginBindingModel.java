package com.mosy.kalin.mosy.Models.BindingModels;

public class LoginBindingModel {

    public String Email;

    public String Password;

    public LoginBindingModel() { }
    public LoginBindingModel(String email, String password) {
        this.Email = email;
        this.Password = password;
    }

}
