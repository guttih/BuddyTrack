package com.guttih.buddytrack.Entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GuðjónHólm on 17.6.2016.
 *
 * This is a responce object for the API "http://apis.is/car?number="
 *
 * Example: "http://apis.is/car?number=ly624"
 *
 *
 */
public class Car {
    public String type="", subType="", color="", registryNumber="", number="", factoryNumber="",
            registeredAt="", pollution="", weight="", status="", nextCheck="";

    public boolean isValid() {
        return isValid;
    }

    private boolean isValid;
    public Car(String JSONResult) {
        try {
            JSONObject topObject = new JSONObject(JSONResult);
            JSONArray arr = (JSONArray)topObject.get("results");
            JSONObject object = (JSONObject) arr.get(0);
            type            = object.getString("type");
            subType         = object.getString("subType");
            color           = object.getString("color");
            registryNumber  = object.getString("registryNumber");
            number          = object.getString("number");
            factoryNumber   = object.getString("factoryNumber");
            registeredAt    = object.getString("registeredAt");
            pollution       = object.getString("pollution");
            weight          = object.getString("weight");
            status          = object.getString("status");
            nextCheck       = object.getString("nextCheck");
            isValid = true;
        }
        catch (JSONException e){
            isValid = false;
        }

    }
}
