package org.tourlink.routingservice.utils;
public class GeoUtils {
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c;
    }
    public static double estimatePrice(double distanceKm, String type) {
        if ("FLIGHT".equals(type)) {
            return distanceKm * 0.6 + 150;
        } else {
            return distanceKm * 0.45 + 50;
        }
    }

}
