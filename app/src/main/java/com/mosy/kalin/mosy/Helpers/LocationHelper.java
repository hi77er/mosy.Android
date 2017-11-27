package com.mosy.kalin.mosy.Helpers;

import android.location.Location;

import java.text.DecimalFormat;

/**
 * Created by kkras on 8/23/2017.
 */

public class LocationHelper {

    private static final int APPROXIMATE_WALKING_SPEED = 5; // km per hour

    public static String buildDistanceText(double distanceMeters){
        String text = StringHelper.empty();

        if (distanceMeters == 0)
            text = StringHelper.empty();
        else if (distanceMeters < 5)
            text = "Standing in front ou it";
        else if (distanceMeters < 50)
            text = "Less than 50 meters";
        else if (distanceMeters < 100)
            text = "Less than 100 meters";
        else if (distanceMeters < 1000) {
            long longValue =  (long) distanceMeters;
            text = String.valueOf(longValue) + "m";
        } else if (distanceMeters >= 1000  && distanceMeters < 999999999 ) {
            distanceMeters =  distanceMeters / 1000;
            text = new DecimalFormat("##.#").format(distanceMeters) + "km";
        } else if (distanceMeters == 999999999)
            text = StringHelper.empty();

        return text;
    }

    public static String buildMinutesWalkingText(double distanceMeters) {
        String text = StringHelper.empty();
        double time = calcMinutesWalkingToLocation(distanceMeters);

        if (time > 0 && time < 1)
            text = StringHelper.empty();
        if (time > 0 && time < 1)
            text = " | Less than 1 minute walking";
        if (time > 1 && time < 59)
            text = " | " + new DecimalFormat("##.#").format(time) + " minutes walking";
        if (time > 59 && time < 999999999)
            text = StringHelper.empty(); // too far, unnecessary to display time walking
        else if (time == 999999999)
            text = StringHelper.empty();
        return text;
    }

    public static double calcMinutesWalkingToLocation(double distanceMeters) {
        double time = ((distanceMeters / 1000) / APPROXIMATE_WALKING_SPEED) * 60;
        return time;
    }

    public static double calculateDistanceToLocation(double fromLongitude, double fromLatitude, double toLongitude, double toLatitude) {
        float[] distance = new float[1];
        Location.distanceBetween(fromLatitude, fromLongitude, toLatitude, toLongitude, distance); // in Meters
        return distance[0];
    }
}
