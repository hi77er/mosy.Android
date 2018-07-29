package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_promotions)
public class PromotionsActivity extends BaseActivity {

    @ViewById(R.id.promotions_textView)
    TextView msg;

    @AfterViews
    public void afterViews(){
        this.msg.setText("test");
        this.msg.setEnabled(true);
        this.msg.setVisibility(TextView.VISIBLE);

    }

}
