package com.mosy.kalin.mosy.Helpers;

import android.location.Location;

import java.text.DecimalFormat;

public class LocationHelper {

    private static final int APPROXIMATE_WALKING_SPEED = 5; // km per hour

    public static String buildDistanceText(double distanceMeters){
        distanceMeters = DoubleHelper.round(distanceMeters, 2);
        String text = StringHelper.empty();

        if (distanceMeters == 0)
            text = StringHelper.empty();
        else if (distanceMeters < 10)
            text = "inside";
//        else if (distanceMeters < 50)
//            text = "less than 50m";
//        else if (distanceMeters < 100)
//            text = "less than 100m";
        else if (distanceMeters < 1000) {
            long longValue =  (long) distanceMeters;
            text = String.valueOf(longValue) + "m";
        } else if (distanceMeters >= 1000  && distanceMeters < 10000 ) {       // 1km > d < 999km
            distanceMeters =  distanceMeters / 1000;
            text = new DecimalFormat("##.#").format(distanceMeters) + "km";
        } else if (distanceMeters >= 10000  && distanceMeters < 999999 ) {       // 1km > d < 999km
            distanceMeters =  distanceMeters / 1000;
            long wholePart = (long) distanceMeters;
            text = String.valueOf(wholePart) + "km";
        } else if (distanceMeters >= 999999  && distanceMeters < 999999999 )    // d > 999km
            text = StringHelper.empty();

        return text;
    }

    public static String buildMinutesWalkingText(double distanceMeters) {
        String text = StringHelper.empty();
        distanceMeters = DoubleHelper.round(distanceMeters, 2);
        double time = calcMinutesWalkingToLocation(distanceMeters);

        if (time > 0 && time < 1)
            text = StringHelper.empty();
        if (time > 0 && time < 1)
            text = "less than 1min";
        if (time > 1 && time < 59)
            text = (int)time + "min";
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
