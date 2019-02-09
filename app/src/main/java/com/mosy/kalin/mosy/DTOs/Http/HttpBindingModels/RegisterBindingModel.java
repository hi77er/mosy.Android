package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

public class RegisterBindingModel {

    public String Email;
    public String Password;
    public String ConfirmPassword;
    public boolean OriginMobile;

    public RegisterBindingModel() { }
    public RegisterBindingModel(String email, String password, String confirmPassword, boolean originMobile) {
        this.Email = email;
        this.Password = password;
        this.ConfirmPassword = confirmPassword;
        this.OriginMobile = originMobile;
    }

}
