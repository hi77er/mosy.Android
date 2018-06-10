package com.mosy.kalin.mosy.Views;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_menulistitem)
public class MenuListItemView
        extends RelativeLayout {

    @ViewById(resName = "menuListItem_tvName")
    TextView Name;
    @ViewById(resName = "menuListItem_tvPrice")
    TextView Price;

    public MenuListItemView(Context context) {
        super(context);
    }

    public void bind(MenuListItem menuListItem) {
        if (menuListItem != null)
        {
            this.Name.setText(menuListItem.Name);
            this.Price.setText(menuListItem.PriceDisplayText);
            // if (isSelected)
            // this.setBackgroundColor(Color.WHITE);
        }
    }
}