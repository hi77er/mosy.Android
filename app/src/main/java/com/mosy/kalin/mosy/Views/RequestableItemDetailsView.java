package com.mosy.kalin.mosy.Views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Ingredient;
import com.mosy.kalin.mosy.DTOs.Requestable;
import com.mosy.kalin.mosy.DTOs.RequestableIngredient;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by kkras on 8/21/2017.
 */


@EViewGroup(R.layout.activity_item_requestable_details)
public class RequestableItemDetailsView extends RelativeLayout {

    public RequestableItemDetailsView(Context context) {
        super(context);
    }

    @ViewById(resName = "requestableDetails_tvIngredients")
    TextView Ingredients;

    ///Add more controls here

    public void bind(Requestable requestable) {
        ArrayList<String> toJoin = new ArrayList<String>();
        String joined = StringHelper.empty();
        for (Ingredient ingredient: requestable.Ingredients)
            toJoin.add(ingredient.Name);

        joined = StringHelper.join(", ", toJoin);
        this.Ingredients.setText(joined);
    }



}
