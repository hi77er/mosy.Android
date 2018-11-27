package com.mosy.kalin.mosy;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_app_info)
public class ApplicationInfoActivity extends BaseActivity {

    @ViewById(R.id.appinfo_appversion)
    TextView AppInfo;

    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    @AfterViews
    void afterViews() {





        this.AppInfo.setText(this.versionName);
    }




}

