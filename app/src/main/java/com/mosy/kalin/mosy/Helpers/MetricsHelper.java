package com.mosy.kalin.mosy.Helpers;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class MetricsHelper {

    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp){
        float density = Resources.getSystem()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

}