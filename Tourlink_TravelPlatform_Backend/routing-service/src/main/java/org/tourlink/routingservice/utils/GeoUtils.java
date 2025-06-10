package org.tourlink.routingservice.utils;
public class GeoUtils {
    public static double estimatePrice(double distanceKm, String type) {
        if ("FLIGHT".equals(type)) {
            return distanceKm * 0.6 + 150;
        } else {
            return distanceKm * 0.45 + 50;
        }
    }

}
