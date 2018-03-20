package com.mosy.kalin.mosy.Helpers;

import android.content.Context;

public class DimensionsHelper {

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

}
