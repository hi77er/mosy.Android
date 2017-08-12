package com.mosy.kalin.mosy.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mosy.kalin.mosy.DTOs.Brochure;
import com.mosy.kalin.mosy.Fragments.BrochureFragment_;

import java.util.ArrayList;

/**
 * Created by kkras on 8/10/2017.
 */

public class BrochuresAdapter extends FragmentStatePagerAdapter {

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

    public BrochuresAdapter(FragmentManager fm, ArrayList<Brochure> brochures) {
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
