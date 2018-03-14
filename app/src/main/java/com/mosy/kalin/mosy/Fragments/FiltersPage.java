package com.mosy.kalin.mosy.Fragments;

import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.mosy.kalin.mosy.Adapters.FilterListAdapter;
import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;


@EFragment(R.layout.fragment_filters)
public class FiltersPage
        extends Fragment {

    @Bean
    FilterListAdapter filtersAdapter;

    @FragmentArg("Filters")
    ArrayList<DishFilter> Filters;

    @ViewById(R.id.lv_Filters)
    ListView FiltersList;


    @AfterViews
    void init() {
        if (this.Filters != null) {
            filtersAdapter.setFilters(this.Filters);
            this.FiltersList.setAdapter(filtersAdapter);
        }
    }

    public FiltersPage() { }

}
