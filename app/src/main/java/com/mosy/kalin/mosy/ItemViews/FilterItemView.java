package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FilterItem;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(R.layout.activity_item_filter)
public class FilterItemView
        extends LinearLayout {

    FilterItem filterItem;

    @ViewById(resName = "filterItem_cbValue")
    CheckBox selectedCheckBox;

    @ViewById(resName = "filterItem_ivIcon")
    ImageView iconImageView;


    private boolean selected = false;
    public boolean isSelected(){
        return selected;
    }

    public FilterItemView(Context context) {
        super(context);
    }

    public void bind(FilterItem filterItem) {
        this.filterItem = filterItem;

        //INFO: It's obligatory the listener to be assigned before the setChecked,
        // otherwise another checkbox listener is being hit immediately after setChecked
        this.selectedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selected = isChecked;
            filterItem.IsChecked = isChecked;
        });

        this.selectedCheckBox.setChecked(filterItem.IsChecked);

        String name = StringHelper.getStringAppDefaultLocale(getContext(), getResources(), filterItem.I18nResourceName, filterItem.Name);
        this.selectedCheckBox.setText(name);

        if (StringHelper.isNotNullOrEmpty(filterItem.Description))
            iconImageView.setVisibility(VISIBLE);
        else
            iconImageView.setVisibility(GONE);

//        if (ArrayHelper.hasValidBitmapContent(filterItem.Icon)) {
//            Bitmap bmp = BitmapFactory.decodeByteArray(filterItem.Icon, 0, filterItem.Icon.length);
//
//            try {
//                iconImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), false));
//            }
//            catch (Exception ex){
//                ex.printStackTrace();
//            }
//        }
    }
    @Click(resName = "filterItem_ivIcon")
    public void ImageClick()
    {
        if (this.filterItem != null && StringHelper.isNotNullOrEmpty(this.filterItem.Description))
            new AlertDialog.Builder(getContext())
                    .setTitle("Information")
                    .setMessage(filterItem.Description)
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->  dialog.cancel())
                    .show();
    }
}

