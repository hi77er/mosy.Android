package com.mosy.kalin.mosy.DAL.Repositories;

import com.mosy.kalin.mosy.DTOs.Results.RegisterResult;
import com.mosy.kalin.mosy.DTOs.Results.TokenResult;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.DAL.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;

public class AccountRepository {


    public TokenResult tokenLogin(LoginBindingModel model) {
        String tokenEndpoint = ServiceEndpointFactory.apiTokenEndpoint;
        TokenResult tokenResult = null;
        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            tokenResult = jsonHttpClient.GetToken(tokenEndpoint, model.Email, model.Password);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return tokenResult;
    }

    }
