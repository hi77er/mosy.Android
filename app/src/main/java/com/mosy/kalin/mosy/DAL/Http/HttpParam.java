package com.mosy.kalin.mosy.DAL.Http;

import java.security.InvalidParameterException;

/**
 * Created by kkras on 12/19/2017.
 */

public class HttpParam {
    private String Name;
    public String getName(){ return this.Name; }

    private String Value;
    public String getValue(){ return this.Value; }

    public HttpParam(String name, String value){
        if (name == null) throw new InvalidParameterException("Name cannot be null");
        if (value == null) throw new InvalidParameterException("Value cannot be null");

        this.Name = name;
        this.Value = value;
    }
}
