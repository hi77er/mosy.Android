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

//    @FragmentArg
//    ArrayList<Requestable> Requestables;
//
    @FragmentArg("BrochureName")
    String Name;

    @ViewById
    TextView brochure_tvName;

    @ViewById
    ListView brochure_lvRequestables;

    @Bean
    RequestablesAdapter requestablesAdapter;

    @AfterViews
    void init() {
        brochure_tvName.setText(Name);

        ArrayList<Requestable> requestables = new ArrayList<Requestable>();
        Requestable req1 = new Requestable();
        req1.Name = "Test Req 1";
        requestables.add(req1);

        Requestable req2 = new Requestable();
        req2.Name = "Test Req 2";
        requestables.add(req2);

        Requestable req3 = new Requestable();
        req3.Name = "Test Req 2";
        requestables.add(req3);

        Requestable req4 = new Requestable();
        req4.Name = "Test Req 2";
        requestables.add(req4);

        Requestable req5 = new Requestable();
        req5.Name = "Test Req 2";
        requestables.add(req5);

        Requestable req6 = new Requestable();
        req6.Name = "Test Req 2";
        requestables.add(req5);

        Requestable req7 = new Requestable();
        req7.Name = "Test Req 2";
        requestables.add(req7);

        Requestable req8 = new Requestable();
        req8.Name = "Test Req 2";
        requestables.add(req8);

        Requestable req9 = new Requestable();
        req9.Name = "Test Req 2";
        requestables.add(req9);

        Requestable req10 = new Requestable();
        req10.Name = "Test Req 2";
        requestables.add(req10);

        Requestable req11 = new Requestable();
        req11.Name = "Test Req 2";
        requestables.add(req11);

        Requestable req12 = new Requestable();
        req12.Name = "Test Req 2";
        requestables.add(req12);

        requestablesAdapter.setRequestables(requestables);
        brochure_lvRequestables.setAdapter(requestablesAdapter);

    }


    public BrochureFragment() { }

}
