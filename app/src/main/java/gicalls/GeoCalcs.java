package gicalls;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

/**
 * Created by GuðjónHólm on 14.6.2016.
 */

public class GeoCalcs {

    public static double bearing(LatLng from, LatLng to){

        return bearingInitial(from.latitude, from.longitude, to.latitude, to.longitude);
    }

    static public double bearingInitial (double lat1, double long1, double lat2, double long2)
    {
        return (_bearing(lat1, long1, lat2, long2) + 360.0) % 360;
    }

    public static double bearingFinal(double lat1, double long1, double lat2, double long2)
    {
        return (_bearing(lat2, long2, lat1, long1) + 180.0) % 360;
    }

    public static double _bearing(double lat1, double long1, double lat2, double long2)
    {
        double degToRad = Math.PI / 180.0;
        double phi1 = lat1 * degToRad;
        double phi2 = lat2 * degToRad;
        double lam1 = long1 * degToRad;
        double lam2 = long2 * degToRad;

        return Math.atan2(Math.sin(lam2-lam1)* Math.cos(phi2),
                Math.cos(phi1)* Math.sin(phi2) - Math.sin(phi1)* Math.cos(phi2)* Math.cos(lam2-lam1)
        ) * 180/ Math.PI;
    }

    public static double distance(LatLng from, LatLng to){

        return distance(from.latitude, from.longitude, to.latitude, to.longitude, 'K');
    }
    public static double distance(LatLng from, LatLng to, char unit){

        return distance(from.latitude, from.longitude, to.latitude, to.longitude, unit);
    }
    /*
 * Calculate distance between two points in latitude and longitude taking
 * into account height difference. If you are not interested in height
 * difference pass 0.0. Uses Haversine method as its base.
 *
 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
 * el2 End altitude in meters
 * @returns Distance in Meters
 */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    //does not take elevation into account.
    public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515; //Miles
        if (unit == 'K') { // Kilometers
            dist = dist * 1.609344;
        } else if (unit == 'M') { //Nautical Miles
            dist = dist * 1609.344;
        } else if (unit == 'N') { //Nautical Miles
            dist = dist * 0.8684;
        }
        return dist;
    }

    // formats the distance, if distance is less than 1 km the function will return distance in meters.
    // otherwise the returned string will be in kilomenters.
    public static String formatDistance(double distanceInMeters){
        //DecimalFormat df = new DecimalFormat("#.00");
        String strNum;

        if (distanceInMeters >= 1000) {
            strNum = String.format("%.1f km.", distanceInMeters / 1000);
        }
        else
            strNum = String.format("%d m.", Math.round(distanceInMeters));

        return strNum;
    }

    public static String formatKronur(double amount){
        String str = String.format("%.1f kr.", amount);
        return str;
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  convert decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  Convert radians to decimal degrees        :*/
    /*::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    // System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'M') + " Miles\n");
    // System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'K') + " Kilometers\n");
    // System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, 'N') + " Nautical Miles\n");

}
