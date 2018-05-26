package com.mosy.kalin.mosy.Helpers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.mosy.kalin.mosy.App;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by kkras on 7/24/2017.
 */

public class StringHelper {

    public static boolean isMatch(String regex,String text)
    {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotNullOrEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static boolean isNullOrWhitespace(String s) {
        return s == null || isWhitespace(s);
    }

    private static boolean isWhitespace(String s) {
        int length = s.length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isEmailAddress(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String join(String separator, List<String> mList) {
        if (mList.size() <= 0) return StringHelper.empty();
        StringBuilder builder = new StringBuilder();
        int count = 0;
        int elementsCount = mList.size();

        for (String item: mList) {
            builder.append(item);
            count++;
            if (count < elementsCount) builder.append(separator);
        }
        return builder.toString();
    }

    public static String empty() {
        return "";
    }

    @NonNull
    public static String getStringAppDefaultLocale(Context context, int resId) {
        return getStringForLocale(context, resId, Locale.getDefault());
    }

    @NonNull
    public static String getStringDeviceDefaultLocale(Context context, int resId) {
        return getStringForLocale(context, resId, App.sDefSystemLocale);
    }

    @NonNull
    public static String getStringForLanguage(Context context, int resId, String desiredLanguage) {
        Locale desiredLocale = new Locale(desiredLanguage);
        return getStringForLocale(context, resId, desiredLocale);
    }

    @NonNull
    public static String getStringForLocale(Context context, int resourceId, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return getStringByLocalPlus17(context, resourceId, locale);
        else
            return getStringByLocalBefore17(context, resourceId, locale);
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static String getStringByLocalPlus17(Context context, int resourceId, Locale locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration).getResources().getString(resourceId);
    }

    private static String getStringByLocalBefore17(Context context, int resId, Locale locale) {
        Resources currentResources = context.getResources();
        AssetManager assets = currentResources.getAssets();
        DisplayMetrics metrics = currentResources.getDisplayMetrics();
        Configuration config = new Configuration(currentResources.getConfiguration());
        Locale.setDefault(locale);
        config.locale = locale;
        /*
         * Note: This (temporarily) changes the devices locale! TODO find a
         * better way to get the string in the specific locale
         */
        Resources defaultLocaleResources = new Resources(assets, metrics, config);
        String string = defaultLocaleResources.getString(resId);
        // Restore device-specific locale
        new Resources(assets, metrics, currentResources.getConfiguration());
        return string;
    }

}