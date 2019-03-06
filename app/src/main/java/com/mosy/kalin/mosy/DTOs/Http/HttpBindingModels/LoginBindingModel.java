package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

public class LoginBindingModel {

    public String Username;

    public String Password;

    public String RememberMe;

    public LoginBindingModel() { }
    public LoginBindingModel(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

}
