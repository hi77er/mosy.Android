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

    public BrochuresAdapter(FragmentManager fm, ArrayList<Brochure> brochures) {
        super(fm);
        this.Brochures = brochures;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new BrochureFragment_();
        Brochure brochure = this.Brochures.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("BrochureName", brochure.Name);
        bundle.putParcelableArrayList("BrochureRequestables",brochure.Requestables);

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
