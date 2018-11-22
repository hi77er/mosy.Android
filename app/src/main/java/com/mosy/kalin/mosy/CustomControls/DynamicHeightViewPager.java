package com.mosy.kalin.mosy.CustomControls;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class DynamicHeightViewPager extends ViewPager {

    private View mCurrentView;
    private int mPageItemsCount;
    private int mExcessHeight;

    public DynamicHeightViewPager(Context context) {
        super(context);
    }

    public DynamicHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int height = 0;
        mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h = mCurrentView.getMeasuredHeight();

        if (h > height) height = h;

        height = height * this.mPageItemsCount;

        int excess = this.mPageItemsCount * this.mExcessHeight;
        height = height - excess;

        int lost = this.mPageItemsCount * 6; // 3px are lost for each item. Unknown reason.
        height += lost;

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureCurrentView(View currentView, int pageItemsCount, int excessHeight) {
        this.mPageItemsCount = pageItemsCount;
        this.mExcessHeight = excessHeight;

        mCurrentView = currentView;
        requestLayout();
    }

    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }
}
