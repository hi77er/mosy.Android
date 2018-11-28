package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;
import com.mosy.kalin.mosy.Helpers.MenuListItemHelper;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_menulistitem)
public class MenuListItemView
        extends RelativeLayout {

    @ViewById(R.id.menuListItem_llRow)
    LinearLayout rowLayout;
    @ViewById(R.id.menuListItem_tvName)
    TextView name;
    @ViewById(R.id.menuListItem_tvPrice)
    TextView price;
    @ViewById(R.id.menuListItem_ivDown)
    ImageView downImageView;

    public MenuListItemView(Context context) {
        super(context);
    }

    public void bind(MenuListItem menuListItem) {
        if (menuListItem != null)
        {
            MenuListItemCulture selectedCulture = MenuListItemHelper.getMenuListItemCulture(getContext(), menuListItem);

            this.name.setText(selectedCulture.MenuListItemName);
            this.price.setText(menuListItem.PriceDisplayText);
            // if (isSelected)
            // this.setBackgroundColor(Color.WHITE);
        }
    }
//
//    @Click(R.id.menuListItem_llRow)
//    public void itemClick() {
//        String asdas = "";
//    }

}