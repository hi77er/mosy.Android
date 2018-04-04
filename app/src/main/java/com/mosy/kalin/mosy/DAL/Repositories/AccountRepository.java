package com.mosy.kalin.mosy.DAL.Repositories;

import com.mosy.kalin.mosy.DTOs.Results.RegisterResult;
import com.mosy.kalin.mosy.DTOs.Results.TokenResult;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.DAL.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;

public class AccountRepository {

    private static final String registerEndpointEnding = "account/register";


    public TokenResult tokenLogin(LoginBindingModel model) {
        String tokenEndpoint = new ServiceEndpointFactory().getMosyWebAPIDevTokenEndpoint();
        TokenResult tokenResult = null;
        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            tokenResult = jsonHttpClient.GetToken(tokenEndpoint, model.Email, model.Password);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return tokenResult;
    }

    public RegisterResult register(RegisterBindingModel model) {
        String registerEndpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(registerEndpointEnding);
        JSONHttpClient jsonHttpClient = new JSONHttpClient();

        try {
            String result = jsonHttpClient.PostObject(registerEndpoint, model, String.class, StringHelper.empty(), StringHelper.empty());
            return new RegisterResult(true, result);
        }
        catch(Exception e)
        {
            return new RegisterResult(false, e.getMessage());
        }
    }

}
