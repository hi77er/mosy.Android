package com.mosy.kalin.mosy;

import android.os.Bundle;
import android.widget.TextView;

import com.mosy.kalin.mosy.Helpers.StringHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_app_info)
public class ApplicationInfoActivity extends BaseActivity {

    @ViewById(R.id.appinfo_appVersion)
    TextView AppInfo;

    int versionCode = 0;
    String versionName = StringHelper.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;
    }

    @AfterViews
    void afterViews() {
        this.AppInfo.setText(this.versionName);
    }




}

