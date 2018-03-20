package com.mosy.kalin.mosy.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.FilterListAdapter;
import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.Helpers.StringHelper;
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

    @ViewById(R.id.tv_FiltersNote)
    TextView FiltersNoteLabel;

    String Note;
    public void setNote(String noteLabel)
    {
        this.Note = noteLabel;
    }

    public FiltersPage() { }

    @AfterViews
    void init() {
        if (this.Filters != null) {
            filtersAdapter.setFilters(this.Filters);
            this.FiltersList.setAdapter(filtersAdapter);
        }
        if (StringHelper.isNotNullOrEmpty(this.Note))
        {
            this.FiltersNoteLabel.setText(this.Note);
            this.FiltersNoteLabel.setVisibility(View.VISIBLE);
        }
    }


}
