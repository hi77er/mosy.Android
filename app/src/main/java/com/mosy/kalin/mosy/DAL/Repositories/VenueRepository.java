package com.mosy.kalin.mosy.DAL.Repositories;

import com.google.gson.reflect.TypeToken;
import com.mosy.kalin.mosy.DTOs.Contact;
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

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class VenueRepository {

    private static final String searchVenuesEndpointEnding = "FBO/QueryClosestFBOs";
    private static final String getVenueByIdEndpointEnding = "FBO/ById";
    private static final String getVenueIndoorImageMetaEndpointEnding = "FBOFiles/IndoorMetaByFBOId";
    private static final String getVenueContactsEndpointEnding = "FBOContacts/ByFBOId";
    private static final String getVenueBusinessHoursEndpointEnding = "FBOBusinessHours/ByFBOId";
    private static final String getVenueBadgeEndorsementsEndpointEnding = "FBOEndorsements/ByFBOId";
    private static final String getVenueLocationEndpointEnding = "FBOLocation/ByFBOId";
    private static final String getVenueMenuEndpointEnding = "brochure/publicmenu";

    public ArrayList<Venue> searchVenues(SearchVenuesBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(searchVenuesEndpointEnding);
        ArrayList<Venue> venuesResult = new ArrayList<>();

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<ArrayList<Venue>>(){}.getType();
            venuesResult = jsonHttpClient.PostObject(endpoint, model, returnType, "HH:mm:ss");
            return venuesResult;
        } catch(Exception e) {
            e.printStackTrace();
            Venue errResult = new Venue();
            errResult.ErrorMessage = e.getMessage();
            venuesResult.add(errResult);
            return venuesResult;
        }
    }

    public Venue getVenueById(GetVenueByIdBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getVenueByIdEndpointEnding);
        Venue venue;

        try {
            HttpParams params = new HttpParams();
            params.put("id", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Type returnType = new TypeToken<Venue>(){}.getType();
            venue = jsonHttpClient.Get(endpoint, params, returnType, "yyyy-MM-dd'T'HH:mm:ss.");
        } catch(Exception e) {
            e.printStackTrace();
            Venue errResult = new Venue();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return venue;
    }

    public VenueImage getVenueIndoorImageMeta(GetVenueIndoorImageMetaBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getVenueIndoorImageMetaEndpointEnding);
        VenueImage imageResult;

        try {
            HttpParams params = new HttpParams();
            params.put("Id", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            imageResult = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueImage>(){}.getType(), StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            VenueImage errResult = new VenueImage();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return imageResult;
    }

    public VenueBusinessHours getVenueBusinessHours(GetVenueBusinessHoursBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getVenueBusinessHoursEndpointEnding);
        VenueBusinessHours businessHours = null;

        try {
            HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            businessHours = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueBusinessHours>(){}.getType(), "HH:mm:ss");
        } catch(Exception e) {
            e.printStackTrace();
            VenueBusinessHours errResult = new VenueBusinessHours();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return businessHours;
    }

    public ArrayList<Contact> getVenueContacts(GetVenueContactsBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getVenueContactsEndpointEnding);
        ArrayList<Contact> result = null;

        try {
            HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            result = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<Contact>>(){}.getType(), "HH:mm:ss");
        } catch(Exception e) {
            e.printStackTrace();
            ArrayList<Contact> errResult = new ArrayList<Contact>();
            Contact contact = new Contact();
            contact.ErrorMessage = e.getMessage();
            errResult.add(contact);
            return errResult;
        }
        return result;
    }

    public ArrayList<VenueBadgeEndorsement> getVenueBadgeEndorsements(GetVenueBadgeEndorsementsBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getVenueBadgeEndorsementsEndpointEnding);
        ArrayList<VenueBadgeEndorsement> badgeEndorsements = new ArrayList<VenueBadgeEndorsement>();

        try {HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            badgeEndorsements = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<VenueBadgeEndorsement>>(){}.getType(), StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            VenueBadgeEndorsement errResult = new VenueBadgeEndorsement();
            errResult.ErrorMessage = e.getMessage();
            badgeEndorsements.add(errResult);
            return badgeEndorsements;
        }
        return badgeEndorsements;
    }

    public VenueLocation getVenueLocation(GetVenueLocationBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getVenueLocationEndpointEnding);
        VenueLocation location;

        try {
            HttpParams params = new HttpParams();
            params.put("fboId", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            location = jsonHttpClient.Get(endpoint, params, new TypeToken<VenueLocation>(){}.getType(), StringHelper.empty());
        } catch(Exception e) {
            e.printStackTrace();
            VenueLocation errResult = new VenueLocation();
            errResult.ErrorMessage = e.getMessage();
            return errResult;
        }
        return location;
    }

    public ArrayList<MenuList> getVenueMenu(GetVenueMenuBindingModel model){
        String endpoint = new ServiceEndpointFactory().getMosyWebAPIDevEndpoint(getVenueMenuEndpointEnding);
        ArrayList<MenuList> brochuresResult = new ArrayList<>();

        try {
            HttpParams params = new HttpParams();
            params.put("Id", model.VenueId);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            brochuresResult = jsonHttpClient.Get(endpoint, params, new TypeToken<ArrayList<MenuList>>(){}.getType(), StringHelper.empty());
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
}
