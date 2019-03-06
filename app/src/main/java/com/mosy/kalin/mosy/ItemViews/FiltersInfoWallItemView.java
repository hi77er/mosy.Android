package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_filtersinfo)
public class FiltersInfoWallItemView
        extends WallItemViewBase {

    private Context baseContext;

    @ViewById(R.id.filtersInfoItem_tvMatchingFiltersInfo)
    TextView matchingFiltersInfo;
    @ViewById(R.id.filtersInfoItem_tvMismatchingFiltersInfo)
    TextView mismatchingFiltersInfo;
    @ViewById(R.id.filtersInfoItem_tvMismatchingFiltersInfoLabel)
    TextView mismatchingFiltersInfoLabel;

    public FiltersInfoWallItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(String matchingFiltersInfo, String mismatchingFiltersInfo) {
        this.clearState();

        if (StringHelper.isNotNullOrEmpty(matchingFiltersInfo)) {
            this.matchingFiltersInfo.setText(matchingFiltersInfo);
            this.matchingFiltersInfo.setVisibility(VISIBLE);
        }
        if (StringHelper.isNotNullOrEmpty(mismatchingFiltersInfo)) {
            this.mismatchingFiltersInfo.setText(mismatchingFiltersInfo);
            this.mismatchingFiltersInfo.setPaintFlags(this.mismatchingFiltersInfo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            this.mismatchingFiltersInfoLabel.setVisibility(VISIBLE);
            this.mismatchingFiltersInfo.setVisibility(VISIBLE);
        }
    }

    private void clearState() {
        this.matchingFiltersInfo.setText(StringHelper.empty());
        this.mismatchingFiltersInfo.setText(StringHelper.empty());
        this.matchingFiltersInfo.setVisibility(GONE);
        this.mismatchingFiltersInfoLabel.setVisibility(GONE);
        this.mismatchingFiltersInfo.setVisibility(GONE);
    }

//    @Click(resName = "Id of element to handled it´s click")
//    public void click()
//    {
//    }

}
