package com.mosy.kalin.mosy.Models.Interfaces;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by User on 7/7/2016.
 */
public interface IModelHelper {

    JSONObject Get(String rootUrl, String action, Map<String, String> params);
    JSONObject Post(String rootUrl, String action,  Map<String, String> params);
    <T> T Deserialize(JSONObject json, Class<T> clazz);

}
