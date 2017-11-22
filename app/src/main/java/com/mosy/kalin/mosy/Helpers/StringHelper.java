package com.mosy.kalin.mosy.Helpers;

import java.util.List;
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
}