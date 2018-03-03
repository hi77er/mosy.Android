package com.mosy.kalin.mosy.Services;

import org.androidannotations.annotations.EBean;

@EBean
public class VenueService {

    public VenueService() { }

    //public void sortVenuesByDistanceToDevice(ArrayList<Venue> venues) {
    //    for (Venue venue: venues)
    //        if (venue.DistanceToCurrentDeviceLocation == 0) venue.DistanceToCurrentDeviceLocation = 999999999;
    //
    //    Collections.sort(venues, new Comparator<Venue>() {
    //        @Override
    //        public int compare(Venue v1, Venue v2) {
    //            return Double.compare(v1.DistanceToCurrentDeviceLocation, v2.DistanceToCurrentDeviceLocation);
    //        }
    //    });
    //}
}