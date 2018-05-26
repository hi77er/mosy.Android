package com.mosy.kalin.mosy.Views;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.mosy.kalin.mosy.BuildConfig;
import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(R.layout.activity_item_filter)
public class FilterItemView
        extends LinearLayout {

    @ViewById(resName = "filterItem_cbValue")
    CheckBox checkBox;
    private boolean selected = false;
    public boolean isSelected(){
        return selected;
    }

    public FilterItemView(Context context) {
        super(context);
    }

    public void bind(DishFilter filter) {
        //INFO: It's obligatory the listener to be assigned before the setChecked,
        // otherwise another checkbox listener is being hit immediately after setChecked
        this.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selected = isChecked;
            filter.IsChecked = isChecked;
        });

        this.checkBox.setChecked(filter.IsChecked);

        String name = StringHelper.empty();
        int identifier = getResources().getIdentifier(filter.I18nResourceName, "string", BuildConfig.APPLICATION_ID);

        try {
            name = StringHelper.getStringAppDefaultLocale(getContext(), identifier);
        }
        catch (Exception ex) {
            name = filter.Name;

            ex.printStackTrace();
        }

        this.checkBox.setText(name);
    }


}
