package com.mosy.kalin.mosy.Models.BindingModels;

public class RegisterBindingModel {

    public String Email;
    public String Password;
    public String ConfirmPassword;

    public RegisterBindingModel() { }
    public RegisterBindingModel(String email, String password, String confirmPassword) {
        this.Email = email;
        this.Password = password;
        this.ConfirmPassword = confirmPassword;
    }

}
