package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

/**
 * Created by kkras on 8/18/2017.
 */

public class VenueBadgeEndorsement
        extends ResultBase {

    @SerializedName("BadgeId")
    public String BadgeId;

    @SerializedName("Username")
    public String Username;

    @SerializedName("FBOId")
    public String VenueId;

}
