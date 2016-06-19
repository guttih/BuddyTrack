package com.guttih.buddytrack.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GuðjónHólm on 17.6.2016.
 *
 * Example data:
 * {    "bensin95":             204.4,
 *      "bensin95_discount":    201.2,
 *      "company":              "Atlantsolía",
 *      "diesel":               186.9,
 *      "diesel_discount":      183.7,
 *      "geo":                  {
 *                                  "lat":  65.69913,
 *                                  "lon":  -18.135231
 *                              },
 *      "key":                  "ao_000",
 *      "name":                 "Baldursnes Akureyri"}
 */
public class PetrolStation {
    /*geo":{"lat":65.69913,"lon":-18.135231}*/
    public String bensin95="", bensin95_discount="", company="", diesel="", diesel_discount="", key="",
            name="", lat="", lon="";

    public double getLatitude(){ return toDouble(this.lat);}
    public double getLongtitude(){ return toDouble(this.lon);}
    public double getBensin95Price(){return toDouble(this.bensin95);}
    public double getBensin95Discount(){return toDouble(this.bensin95_discount);}
    public double getDiselPrice(){return toDouble(this.diesel);}
    public double getDiselDiscount(){return toDouble(this.diesel_discount);}
    /**
     * If string was null or empty or matches the string "null"
     *   then the returnvalue is 0.
     * If there was an error converting the string to a number
     *   then the returnvalue is Double.POSITIVE_INFINITY.
    * */
    private double toDouble(String str){
        if(str == null || str.isEmpty() || str.toLowerCase().equals("null")) {
            return 0;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return Double.POSITIVE_INFINITY;
        }
    }

    public boolean isValid() {
        return isValid;
    }

    private boolean isValid;
    public PetrolStation(JSONObject object) {
        try {
            name              = object.getString("name");
            bensin95          = object.getString("bensin95");
            bensin95_discount = object.getString("bensin95_discount");
            company           = object.getString("company");
            diesel            = object.getString("diesel");
            diesel_discount   = object.getString("diesel_discount");
            key               = object.getString("key");
            JSONObject geo = new JSONObject(object.getString("geo"));
            lat = geo.getString("lat");
            lon = geo.getString("lon");
        /*lat="", lon="";*/
            isValid = true;
        }
        catch (JSONException e){
            isValid = false;
        }

    }
}
