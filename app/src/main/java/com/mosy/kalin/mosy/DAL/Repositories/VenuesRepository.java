package com.mosy.kalin.mosy.DAL.Repositories;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.DAL.Http.HttpParams;
import com.mosy.kalin.mosy.DAL.Http.JSONHttpClient;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBadgeEndorsementsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueByIdBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueContactsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageMetaBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueLocationBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public class VenuesRepository {

    private static final String getVenueBusinessHoursEndpointEnding = "fbo/businesshours";
    private static final String searchVenuesEndpointEnding = "fbo/closest";
    private static final String getVenueContactsEndpointEnding = "fbo/contacts";
    private static final String getVenueBadgeEndorsementsEndpointEnding = "fbo/endorsements";
    private static final String getVenueByIdEndpointEnding = "fbo/id";
    private static final String getVenueIndoorImageMetaEndpointEnding = "fbo/images/metadataindoor";
    private static final String getVenueLocationEndpointEnding = "fbo/location";
    private static final String getVenueMenuEndpointEnding = "fbo/publicmenu";


    public VenueContacts getContacts(GetVenueContactsBindingModel model){
        String endpoint = new ServiceEndpointFactory().constructMosyWebAPIDevEndpoint(getVenueContactsEndpointEnding);
        VenueContacts result = null;

        try {
            HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            result = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueContacts>(){}.getType(), "HH:mm:ss", model.AuthTokenHeader);
        } catch(Exception e) {
            e.printStackTrace();
            VenueContacts venueContacts = new VenueContacts();
            venueContacts.ErrorMessage = e.getMessage();
            return venueContacts;
        }
        return result;
    }

    public ArrayList<VenueBadgeEndorsement> getBadgeEndorsements(GetVenueBadgeEndorsementsBindingModel model){
        String endpoint = new ServiceEndpointFactory().constructMosyWebAPIDevEndpoint(getVenueBadgeEndorsementsEndpointEnding);
        ArrayList<VenueBadgeEndorsement> badgeEndorsements = new ArrayList<VenueBadgeEndorsement>();

        try {HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            badgeEndorsements = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<VenueBadgeEndorsement>>(){}.getType(), StringHelper.empty(), model.AuthTokenHeader);
        } catch(Exception e) {
            e.printStackTrace();
            VenueBadgeEndorsement errResult = new VenueBadgeEndorsement();
            errResult.ErrorMessage = e.getMessage();
            badgeEndorsements.add(errResult);
            return badgeEndorsements;
        }
        return badgeEndorsements;
    }

    public VenueLocation getLocation(GetVenueLocationBindingModel model){
        String endpoint = new ServiceEndpointFactory().constructMosyWebAPIDevEndpoint(getVenueLocationEndpointEnding);
        VenueLocation location;

        try {
            HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            location = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueLocation>(){}.getType(), StringHelper.empty(), model.AuthTokenHeader);
        } catch(Exception e) {
            e.printStackTrace();
            VenueLocation errResult = new VenueLocation();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return location;
    }

    public VenueImage getImageMetaIndoor(GetVenueIndoorImageMetaBindingModel model){
        String endpoint = new ServiceEndpointFactory().constructMosyWebAPIDevEndpoint(getVenueIndoorImageMetaEndpointEnding);
        VenueImage imageResult;

        try {
            HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            imageResult = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueImage>(){}.getType(), StringHelper.empty(), model.AuthTokenHeader);
        } catch(Exception e) {
            e.printStackTrace();
            VenueImage errResult = new VenueImage();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return imageResult;
    }

    public ArrayList<MenuList> getMenu(GetVenueMenuBindingModel model){
        String endpoint = new ServiceEndpointFactory().constructMosyWebAPIDevEndpoint(getVenueMenuEndpointEnding);
        ArrayList<MenuList> brochuresResult = new ArrayList<>();

        try {
            HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            brochuresResult = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<MenuList>>(){}.getType(), StringHelper.empty(), model.AuthTokenHeader);
        } catch(Exception e) {
            e.printStackTrace();
            MenuList errResult = new MenuList();
            errResult.ErrorMessage = e.getMessage();
            brochuresResult.add(errResult);
            errResult.ErrorMessage = e.getMessage();
            return brochuresResult;
        }
        return brochuresResult;
    }


//    public ArrayList<Venue> loadVenues(SearchVenuesBindingModel model){
//        String endpoint = new ServiceEndpointFactory().constructMosyWebAPIDevEndpoint(searchVenuesEndpointEnding);
//        ArrayList<Venue> venuesResult = new ArrayList<>();
//
//        try {
//            JSONHttpClient jsonHttpClient = new JSONHttpClient();
//            Type returnType = new TypeToken<ArrayList<Venue>>(){}.getType();
//            venuesResult = jsonHttpClient.PostObject(endpoint, model, returnType, "HH:mm:ss", model.AuthTokenHeader);
//            return venuesResult;
//        } catch(Exception e) {
//            e.printStackTrace();
//            Venue errResult = new Venue();
//            errResult.ErrorMessage = e.getMessage();
//            venuesResult.add(errResult);
//            return venuesResult;
//        }
//    }



//    public Venue getById(GetVenueByIdBindingModel model){
//        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getVenueByIdEndpointEnding);
//        Venue venue;
//
//        try {
//            HttpParams params = new HttpParams();
//            params.put("id", model.VenueId);
//
//            JSONHttpClient jsonHttpClient = new JSONHttpClient();
//            Type returnType = new TypeToken<Venue>(){}.getType();
//            venue = jsonHttpClient.Get(endpoint, params, returnType, "yyyy-MM-dd'T'HH:mm:ss.", model.AuthTokenHeader);
//        } catch(Exception e) {
//            e.printStackTrace();
//            Venue errResult = new Venue();
//            errResult.ErrorMessage = e.getMessage();
//            return errResult;
//        }
//        return venue;
//    }

//    public VenueBusinessHours getBusinessHours(GetVenueBusinessHoursBindingModel model){
//        String endpoint = new ServiceEndpointFactory().constructMosyWebAPIDevEndpoint(getVenueBusinessHoursEndpointEnding);
//        VenueBusinessHours businessHours = null;
//
//        try {
//            HttpParams params = new HttpParams();
//            params.put("fboId", model.VenueId);
//
//            JSONHttpClient jsonHttpClient = new JSONHttpClient();
//            businessHours = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueBusinessHours>(){}.getType(), "HH:mm:ss", model.AuthTokenHeader);
//        } catch(Exception e) {
//            e.printStackTrace();
//            VenueBusinessHours errResult = new VenueBusinessHours();
//            errResult.ErrorMessage = e.getMessage();
//            return errResult;
//        }
//        return businessHours;
//    }

}
