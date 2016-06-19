package com.guttih.buddytrack;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guttih.buddytrack.Entity.Petrol;
import com.guttih.buddytrack.Entity.PetrolStation;

import gicalls.GeoCalcs;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private Petrol mPetrol;


    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    Marker mMarkerHere;
    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AppBuddyTrack buddy = ((AppBuddyTrack) getApplicationContext());
        buddy.setData("MapsActivity setti þetta");


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        AppBuddyTrack buddy = ((AppBuddyTrack) getApplicationContext());
        mPetrol = buddy.mPetrol;

        String str = mPetrol.list.get(2).name;
        double d;
        PetrolStation station = mPetrol.list.get(2);
        d = station.getDiselDiscount();

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap#public-method-summary
        // Map Objects https://developers.google.com/maps/documentation/android-api/map
        // https://developers.google.com/maps/documentation/android-api/start
        LatLng haseyla27 = new LatLng(63.974375, -22.519480);
        LatLng laseyla4 = new LatLng(63.975882, -22.518782);
        LatLng kot = new LatLng(65.829656, -21.796106);
        mMap.addMarker(new MarkerOptions().position(haseyla27).title("Heima"));
        mMap.addMarker(new MarkerOptions().position(laseyla4).title("Láseyla 4"));
        mMap.addMarker(new MarkerOptions().position(kot).title("Kot í selárdal"));

        float maxZoom = mMap.getMaxZoomLevel();
        str = String.format("Three numbers after decimal: %1$.3f", maxZoom);
        Log.d("ZOOM", str);
        GeoCalcs geo = new GeoCalcs();
        //63.983305
        double d1 = geo.distance(haseyla27.latitude, laseyla4.longitude, kot.latitude, kot.longitude, 0, 0);
        double d2 = geo.distance(haseyla27, kot);
        double bearing00 = geo.bearing(haseyla27, laseyla4);
        double bearing01 = geo.bearing(laseyla4, haseyla27);
        double bearing1 = geo.bearing(haseyla27, kot);
        double bearing2 = geo.bearing(kot, haseyla27);
        double bearing3 = geo.bearingFinal(haseyla27.latitude, haseyla27.longitude, kot.latitude, kot.longitude);
        double bearing4 = geo.bearingFinal(kot.latitude, kot.longitude, haseyla27.latitude, haseyla27.longitude);

        for (int i = 0; i < mPetrol.list.size(); i++) {
            String title;
            station = mPetrol.list.get(i);
            title = station.name + ".  *Bensín: ";
            if (station.getBensin95Discount() < station.getBensin95Price()) {
                title = title + " * " + station.bensin95_discount;
            } else {
                title = title + station.bensin95;
            }

            title = title + " Dísel: ";
            if (station.getBensin95Discount() < station.getDiselPrice()) {
                title = title + " * " + station.getDiselDiscount();
            } else {
                title = title + station.getDiselPrice();
            }

            int iIcon = 0;
            switch (station.company) {
                case "Atlantsolía":
                    iIcon = R.drawable.ic_atlantsolia24;
                    break;
                case "Dælan":
                    iIcon = R.drawable.ic_daelan24;
                    break;
                case "N1":
                    iIcon = R.drawable.ic_n124;
                    break;
                case "Olís":
                    iIcon = R.drawable.ic_olis_24;
                    break;
                case "Orkan":
                    iIcon = R.drawable.ic_orkan24;
                    break;
                case "Orkan X":
                    iIcon = R.drawable.ic_orkanx24;
                    break;
                case "ÓB":
                    iIcon = R.drawable.ic_odyrtbensin24;
                    break;
                case "Skeljungur":
                    iIcon = R.drawable.ic_skeljungur24;
                    break;

                default:
                    iIcon = 0;
            }

            if (iIcon != 0) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(station.getLatitude(), station.getLongtitude()))
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromResource((iIcon))));
            } else {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(station.getLatitude(), station.getLongtitude()))
                        .title(title)
                );

            }


        }
        // https://developers.google.com/maps/documentation/android-api/marker#make_a_marker_draggable

        mMap.addMarker(new MarkerOptions().position(kot).title("Kot í selárdal"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(haseyla27));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (loc != null)
        {
            handleNewLocation(loc);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    void handleNewLocation(Location location){
        //
        if (location != null) {
            if (mMarkerHere == null) {  //set camaera for the first time
                Log.d(TAG, location.toString());
                //https://developers.google.com/maps/documentation/android-api/marker#customize_a_marker
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                mMarkerHere = mMap.addMarker(new MarkerOptions()
                                                    .position(loc)
                                                    .title("Hér ertu!")
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_here16)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMarkerHere.getPosition().latitude, mMarkerHere.getPosition().longitude), 15));
            }
            else{
                mMarkerHere.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
    }


            @Override
            public void onConnectionSuspended(int i) {
                Log.i(TAG, "Location services suspended. Please reconnect.");
            }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 5000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


}
