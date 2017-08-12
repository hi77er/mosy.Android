package com.mosy.kalin.mosy.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.widget.ListView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.RequestablesAdapter;
import com.mosy.kalin.mosy.DTOs.Requestable;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Views.RequestableItemView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_brochure)
public class BrochureFragment extends Fragment {

    @FragmentArg("BrochureName")
    String BrochureName;

    @FragmentArg("BrochureRequestables")
    ArrayList<Requestable> Requestables;

    @ViewById
    TextView brochure_tvName;

    @ViewById
    ListView brochure_lvRequestables;

    @Bean
    RequestablesAdapter requestablesAdapter;

    @AfterViews
    void init() {
        brochure_tvName.setText(this.BrochureName);

        if (this.Requestables != null) {
            requestablesAdapter.setRequestables(this.Requestables);
            brochure_lvRequestables.setAdapter(requestablesAdapter);
        }
    }

    public BrochureFragment() { }

}
