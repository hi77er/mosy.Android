package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.mosy.kalin.mosy.Async.Tasks.GetVenueMenuAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Brochure;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Fragments.BrochureFragment;
import com.mosy.kalin.mosy.Fragments.BrochureFragment_;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;
import com.mosy.kalin.mosy.VenueActivity;
import com.mosy.kalin.mosy.VenueActivity_;
import com.mosy.kalin.mosy.Views.VenueItemView;
//import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by kkras on 8/10/2017.
 */

public class MenuAdapter extends FragmentStatePagerAdapter {

    ArrayList<Brochure> Brochures;

    String VenueId;
    public void setVenueId(String venueId){ this.VenueId = venueId; }

//    @AfterInject
//    void initAdapter() {
//        try {
//            GetVenueMenuBindingModel model = new GetVenueMenuBindingModel(this.VenueId);
//            this.Brochures = new GetVenueMenuAsyncTask(context).execute(model).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public MenuAdapter(FragmentManager fm, ArrayList<Brochure> brochures) {
        super(fm);
        this.Brochures = brochures;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new BrochureFragment_();
        Bundle bundle = new Bundle();
        Brochure brochure = this.Brochures.get(position);
        bundle.putString("BrochureName", brochure.Name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        if (this.Brochures != null)
            return this.Brochures.size();
        return 0;
    }
}
