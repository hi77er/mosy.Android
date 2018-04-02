package com.mosy.kalin.mosy.Services;

import android.content.Context;

import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBadgeEndorsementsBindingModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueEndorsementsAsyncTask;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

@EBean
public class EndorsementsService {

    private AccountService accountService;

    public void loadVenueEndorsements(Context applicationContext,
                                      AsyncTaskListener<ArrayList<VenueBadgeEndorsement>> listener,
                                      String venueId)
    {
        String authToken = new AccountService().getAuthTokenHeader(applicationContext);

        GetVenueBadgeEndorsementsBindingModel model = new GetVenueBadgeEndorsementsBindingModel(authToken, venueId);
        new LoadVenueEndorsementsAsyncTask(listener).execute(model);
    }

}