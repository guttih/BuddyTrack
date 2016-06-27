package com.guttih.buddytrack;

import android.app.Application;

import com.guttih.buddytrack.Entity.Petrol;
import com.guttih.buddytrack.Entity.PetrolStation;

import gicalls.ApiCall;
import gicalls.CallBackListener;

/**
 * Created by GuðjónHólm on 19.6.2016.
 */
public class AppBuddyTrack extends Application{
    private String data;
    public Petrol mPetrol;
    public String getData() {return this.data;}
    public void setData(String str){
        this.data = str;}

    public void onCreate(){
        super.onCreate();

        setData("starting");

    }
}
