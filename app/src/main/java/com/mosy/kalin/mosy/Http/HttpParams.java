package com.mosy.kalin.mosy.Http;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class HttpParams {

    private List<HttpParam> params;
    public List<HttpParam> get(){ return this.params; }

    public HttpParams()
    {
        this.params = new ArrayList<>();
    }

    public void put(String name, String value)
    {
        if (name == null) throw new InvalidParameterException("Name cannot be null");
        if (value == null) throw new InvalidParameterException("Value cannot be null");

        this.params.add(new HttpParam(name, value));
    }


}
