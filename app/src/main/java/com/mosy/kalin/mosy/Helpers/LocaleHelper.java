package com.mosy.kalin.mosy.Helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import com.mosy.kalin.mosy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class is used to change your application locale and persist this change for the next time
 * that your app is going to be used.
 * <p/>
 * You can also change the locale of your application on the fly by using the setLocale method.
 * <p/>
 * Created by gunhansancar on 07/10/15.
 */
public class LocaleHelper {

    public static final Map<String, Integer> SUPPORTED_LOCALES; // locale Id - resource Id
    static
    {
        SUPPORTED_LOCALES = new HashMap<>();
        SUPPORTED_LOCALES.put("en", R.string.activity_landing_languageEnUSSpinner);
        SUPPORTED_LOCALES.put("bg", R.string.activity_landing_languageBgSpinner);
        SUPPORTED_LOCALES.put("de", R.string.activity_landing_languageDeSpinner);
        SUPPORTED_LOCALES.put("el", R.string.activity_landing_languageElSpinner);
        SUPPORTED_LOCALES.put("es", R.string.activity_landing_languageEsSpinner);
        SUPPORTED_LOCALES.put("ru", R.string.activity_landing_languageRuSpinner);
    }

    private static final String SELECTED_LANGUAGE = "SpinnerLocale.Helper.Selected.Language";

    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }

    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    public static Context setLocale(Context context, String language) {
        persist(context, language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }

        return updateResourcesLegacy(context, language);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public static ArrayList<String> getPreferredLanguages(Context applicationContext) {
        ArrayList<String> preferredLanguages = new ArrayList<>();
        preferredLanguages.add(LocaleHelper.getLanguage(applicationContext));
        preferredLanguages.add(getSystemLanguage());

        return preferredLanguages;
    }

    public static String getSystemLanguage() {
        return Resources.getSystem().getConfiguration().locale.getCountry();
    }
}