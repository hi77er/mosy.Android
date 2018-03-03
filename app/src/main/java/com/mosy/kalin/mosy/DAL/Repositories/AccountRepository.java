package com.mosy.kalin.mosy.DAL.Repositories;

import com.mosy.kalin.mosy.DTOs.Enums.AuthenticationResultStatus;
import com.mosy.kalin.mosy.DTOs.Results.RegisterResult;
import com.mosy.kalin.mosy.DTOs.Results.TokenResult;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.DAL.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;

public class AccountRepository {

    private static final String registerEndpointEnding = "Account/Register";


    public AuthenticationResultStatus tokenLogin(LoginBindingModel model) {
        String tokenEndpoint = new ServiceEndpointFactory().getMosyWebAPIDevTokenEndpoint();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            TokenResult tokenResult = jsonHttpClient.GetToken(tokenEndpoint, model.Email, model.Password);

            //TODO: Handle the case when no Internet connection
            //TODO: Handle the case when Server does not respond
            switch (tokenResult.Status){
                case Unknown:
                    return AuthenticationResultStatus.Unknown;
                case Unauthorized:
                    return AuthenticationResultStatus.Unauthorized;
                case Success:
                    return AuthenticationResultStatus.Authorized;
                case Fail:
                    return AuthenticationResultStatus.Failed;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return AuthenticationResultStatus.Failed;
        }
        return AuthenticationResultStatus.Failed;
    }

    public RegisterResult register(RegisterBindingModel model) {
        String registerEndpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(registerEndpointEnding);
        JSONHttpClient jsonHttpClient = new JSONHttpClient();

        try {
            String result = jsonHttpClient.PostObject(registerEndpoint, model, String.class, StringHelper.empty());
            return new RegisterResult(true, result);
        }
        catch(Exception e)
        {
            return new RegisterResult(false, e.getMessage());
        }
    }

}
