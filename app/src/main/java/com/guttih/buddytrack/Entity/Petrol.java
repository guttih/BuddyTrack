package com.guttih.buddytrack.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by GuðjónHólm on 17.6.2016.
 */
public class Petrol {

    public ArrayList <PetrolStation> list;
    public String timestampApis="", timestampPriceChanges="", timestampPriceCheck="";
    public boolean isValid() {
        return isValid;
    }

    private boolean isValid;
    public Petrol(String JSONResult) {
        list = new ArrayList<>();

        try {
            JSONObject topObject = new JSONObject(JSONResult);
            JSONArray arr = (JSONArray)topObject.get("results");
            timestampApis = topObject.getString("timestampApis");
            timestampPriceChanges = topObject.getString("timestampPriceChanges");
            timestampPriceCheck = topObject.getString("timestampPriceCheck");

            for(int i = 0; i < arr.length(); i++)
            {
                JSONObject object = (JSONObject) arr.get(i);
                PetrolStation station = new PetrolStation(object);
                list.add(station);
            }
        }
        catch (JSONException e){
            isValid = false;
        }
    }
}
