package com.guttih.buddytrack.Entity;

import com.google.android.gms.maps.model.LatLng;

import gicalls.GeoCalcs;

/**
 * Created by GuðjónHólm on 25.6.2016.
 */
public class StationViewItem implements Comparable<StationViewItem>{

    private String company;
    private String name;
    private String bensin95;
    private String disel;
    private double latitude;
    private double longitude;
    private double distance;
    private int iconID;

    private void setValues(  String company, String name, String bensin95, String disel,
                             int iconID, double latitude, double longditude, double distance) {
        this.company = company;
        this.name = name;
        this.bensin95 = bensin95;
        this.disel = disel;
        this.iconID = iconID;
        this.latitude = latitude;
        this.longitude = longditude;
        this.distance = distance;
    }
    public StationViewItem( String company, String name, String bensin95, String diesel,
                            int iconID,  double latitude, double longitude,
                            LatLng calcDistanceToThisLocation) {


            distance = calcDistance(calcDistanceToThisLocation);

       setValues(company, name, bensin95, diesel, iconID, latitude, longitude, distance);
    }
    public double calcDistance(LatLng to){
        if (to == null ) return -1;
        long distance = 0;

        GeoCalcs geo = new GeoCalcs();
        double dist = geo.distance(latitude,   longitude,
                                  to.latitude, to.longitude,
                                  'M');
        return dist;
    }
    // set the distance to the specified location
    // returns the new distance;

    public double setDistance(LatLng to){
        return this.distance = calcDistance(to);
    }
    public StationViewItem( String company, String name, String bensin95, String disel,
                            int iconID,  double latitude, double longditude) {

        setValues(company, name, bensin95, disel, iconID, latitude, longditude, 0);

    }
    public String getCompany()  { return company;  }
    public String getName()     { return name;     }
    public String getBensin95() { return bensin95; }
    public String getDisel()    { return disel;    }
    public int getIconID()      { return iconID;   }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude(){ return longitude;}


    @Override
    public int compareTo(StationViewItem another) {
        return new Double(distance).compareTo( another.distance);
    }

    public String formatDistance(){

        return GeoCalcs.formatDistance(this.distance);
    }


}
