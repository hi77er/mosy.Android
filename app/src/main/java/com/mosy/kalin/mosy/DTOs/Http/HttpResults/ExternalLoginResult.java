package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import java.io.Serializable;
import java.util.ArrayList;

public class ExternalLoginResult
        implements Serializable {


    public String Name;

    public String Url;

    public String State;

    public ArrayList<ExternalLoginData> ExternalLoginData;

}
