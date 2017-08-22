package com.mosy.kalin.mosy.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Requestable;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by kkras on 8/3/2017.
 */

@EViewGroup(R.layout.activity_requestable_item)
public class RequestableItemView extends RelativeLayout {

    @ViewById(resName = "requestableItem_tvName")
    TextView Name;

    public RequestableItemView(Context context) {
        super(context);
    }

    public void bind(Requestable requestable) {
        this.Name.setText(requestable.Name);
    }
}