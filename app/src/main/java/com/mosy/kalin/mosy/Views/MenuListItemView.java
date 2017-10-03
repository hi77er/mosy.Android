package com.mosy.kalin.mosy.Views;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by kkras on 8/3/2017.
 */

@EViewGroup(R.layout.activity_item_menulistitem)
public class MenuListItemView
        extends RelativeLayout {

    @ViewById(resName = "menuListItem_tvName")
    TextView Name;

    public MenuListItemView(Context context) {
        super(context);
    }

    public void bind(MenuListItem menuListItem) {
        this.Name.setText(menuListItem.Name);
    }
}