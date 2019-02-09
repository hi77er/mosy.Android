package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import java.io.Serializable;
import java.util.ArrayList;

public class ExternalLoginData
        implements Serializable {

    public String Provider;
    public String ResponseType;
    public String ClientId;
    public String RedirectUri;
    public String State;

}
