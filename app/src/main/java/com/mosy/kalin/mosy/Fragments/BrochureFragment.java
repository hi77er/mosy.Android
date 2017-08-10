package com.mosy.kalin.mosy.Fragments;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_brochure)
public class BrochureFragment extends Fragment {

    @FragmentArg("BrochureName")
    String Name;

    @ViewById
    TextView brochure_tvName;

    @AfterViews
    void init() {
        brochure_tvName.setText(Name);
    }

    public BrochureFragment() { }

}
