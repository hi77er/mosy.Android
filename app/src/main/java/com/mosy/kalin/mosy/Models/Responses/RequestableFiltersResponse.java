package com.mosy.kalin.mosy.Models.Responses;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.CuisinePhase;
import com.mosy.kalin.mosy.DTOs.CuisineRegion;
import com.mosy.kalin.mosy.DTOs.CuisineSpectrum;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

import java.io.Serializable;
import java.util.ArrayList;

public class RequestableFiltersResponse
        extends ResultBase
        implements Serializable {

    @SerializedName("CuisinePhaseFilters")
    public ArrayList<CuisinePhase> CuisinePhaseFilters;

    @SerializedName("CuisineRegionFilters")
    public ArrayList<CuisineRegion> CuisineRegionFilters;

    @SerializedName("CuisineSpectrumFilters")
    public ArrayList<CuisineSpectrum> CuisineSpectrumFilters;

}
