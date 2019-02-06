package com.mosy.kalin.mosy.DTOs.SignalR.SignalRBindingModels;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;

import java.util.ArrayList;

public class CreateTableAccountBindingModel {

    @SerializedName("OpenerUsername")
    public String OpenerUsername;

    @SerializedName("AssignedOperatorUsername")
    public String AssignedOperatorUsername;

    @SerializedName("FBOTableId")
    public String FBOTableId;

    @SerializedName("RequestableIds")
    public ArrayList<String> RequestableIds;

}
