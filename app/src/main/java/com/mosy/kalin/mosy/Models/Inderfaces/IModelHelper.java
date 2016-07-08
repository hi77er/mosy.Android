package com.mosy.kalin.mosy.Models.Inderfaces;

import java.util.Map;

/**
 * Created by User on 7/7/2016.
 */
public interface IModelHelper {

    String Get(String rootUrl, String action, Map<String, String> params);
    String Post(String rootUrl, String action, Object Content);
    <T> void Deserialize(String jsonString);

}
