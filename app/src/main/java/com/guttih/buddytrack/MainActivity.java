package com.guttih.buddytrack;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.guttih.buddytrack.Entity.Petrol;
import com.guttih.buddytrack.Entity.PetrolStation;
import com.guttih.buddytrack.Entity.StationViewItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import gicalls.ApiCall;
import gicalls.CallBackListener;
import gicalls.GeoCalcs;

public class MainActivity extends AppCompatActivity
        implements CallBackListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_FOR_ACCESS_FINE_AND_COARSE_LOCATION = 20000;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mGoogleApiClient;
    private List<StationViewItem> mStationList;
    private LocationRequest mLocationRequest;
    private GeoCalcs mGeo;
    ArrayAdapter<StationViewItem> mAdapter;

    LatLng mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/

                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","gudjonholm@gmail.com", null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding the BuddyTrack app");
                    intent.putExtra(Intent.EXTRA_TEXT, "Thank you for your feed back :)\n");
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));





                }
            });
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        AppBuddyTrack buddy = ((AppBuddyTrack) getApplicationContext());
        buddy.setData("MainActivity setti þetta");
        mStationList = new ArrayList<>();
        mGeo = new GeoCalcs();
        getFuelPrices();
        // Create an instance of GoogleAPIClient.

    }

    //fetch fuelprices from the internet.
    private void getFuelPrices() {
        ApiCall task = new ApiCall(this, "http://apis.is/petrol");
        task.execute();
    }


    private void sortStationList(LatLng location, boolean notifyDataSetChanged){

        if (location == null)
            return;

        for(StationViewItem item: mStationList){
           item.setDistance(location);
        }
        Collections.sort(mStationList);
        /*ListView list = (ListView) findViewById(R.id.listViewMain);
        ArrayAdapter<StationViewItem> adapter = (ArrayAdapter<StationViewItem>) list.getAdapter();
        adapter.notifyDataSetChanged();*/
        if (notifyDataSetChanged) {
            mAdapter.notifyDataSetChanged();
        }
    }
    private void populateStationList() {

        if (mLocation == null)
            return;
        sortStationList(mLocation, false);
        // create a list of items
       mAdapter = new StationListAdapter(this, R.layout.listitem, mStationList);
        ListView list = (ListView) findViewById(R.id.listViewMain);
        list.setAdapter(mAdapter);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (handleLocationPermission(this)) {
            //Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            //mLocation = new LatLng(location.getLatitude(), location.getLongitude());

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location == null) return;
        double distance;
        boolean update = true;
        LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (mLocation != null){
            distance = mGeo.distance(newLocation, mLocation, 'M');
            update = (distance > 100); //only update if the new location is more than 100 meters away.
        }

        if (update == true)
        {
            mLocation = newLocation;

            if (mAdapter == null)
            {
                populateStationList();
            }
            else{

                sortStationList(mLocation, true);
            }
        }

    }

    @Override
    public void onStart() {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }



    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();

    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        super.onPause();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void ApiCallback(String response)
    {

        AppBuddyTrack buddy = ((AppBuddyTrack) getApplicationContext());

        buddy.mPetrol = new Petrol(response);


        ArrayList <PetrolStation> list = buddy.mPetrol.list;
        //formatNumber
        for(PetrolStation item: list){
            mStationList.add(new StationViewItem(
                    item.company,
                    item.name,
                    GeoCalcs.formatKronur(item.getCheapestBensin()),
                    GeoCalcs.formatKronur(item.getCheapestDisel()),
                    item.getIconID(),
                    item.getLatitude(), item.getLongtitude(),
                    mLocation));
        }

        populateStationList();



    }

    private class StationListAdapter extends ArrayAdapter<StationViewItem>{

        Context mContext;
        public StationListAdapter(Context context, int resource, List<StationViewItem> stationList) {

            super(context, resource, stationList);
            //ListView list = (ListView) findViewById(R.id.listViewMain);
            mContext = context;
            Activity activity = (Activity)mContext;

            ListView listView = (ListView) activity.findViewById(R.id.listViewMain);
            //todo: how to find listView without having to provide the id?
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3)
                {
                    StationViewItem item;
                    item = (StationViewItem)adapter.getItemAtPosition(position);
/*

                    String address = "", city = "", state = "",
                           country = "", postalCode ="", knownName="", strUri = "";
                    List<android.location.Address> addresses;
                    Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(item.getLatitude(), item.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                        country = addresses.get(0).getCountryName();
                        postalCode = addresses.get(0).getPostalCode();
                        knownName = addresses.get(0).getFeatureName();
                        strUri = "google.navigation:q="

                                + address + "+"
                                + city
                                ;

                    } catch (IOException e) {
                        //todo: only show the marker so user can click it to get directions
                        e.printStackTrace();
                    }
*/
                    // info here : https://developers.google.com/maps/documentation/android-api/intents#launch_turn-by-turn_navigation
                    Uri uri;
                    String strUri = "geo:0,0?q="
                            + item.getLatitude()
                            + ","
                            + item.getLongitude()
                            + " ("
                            + item.getCompany()
                            + " - "
                            + item.getName()
                            + ")";
                    uri = Uri.parse(strUri);



                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        uri);
                    try
                    {
                        startActivity(intent);
                    }
                    catch(ActivityNotFoundException ex)
                    {
                        //todo: what to do
                        String str = "";
                        str = "todo: what to do I wonder";
                    }














                }
            });

        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null){
              itemView =  getLayoutInflater().inflate(R.layout.listitem, parent, false);
            }

            GeoCalcs geo = new GeoCalcs();
            //find the car by position

            //StationViewItem station = mStationList.get(position);
            StationViewItem station = getItem(position);

            TextView text1 = (TextView) itemView.findViewById(R.id.tvTextItem1);
            TextView text2 = (TextView) itemView.findViewById(R.id.tvTextItem2);
            TextView text3 = (TextView) itemView.findViewById(R.id.tvTextItem3);
            TextView textDisel = (TextView) itemView.findViewById(R.id.tvDisel);
            ImageView image = (ImageView) itemView.findViewById(R.id.imageViewCompany) ;
            text1.setText(station.getName());
            text2.setText(station.getBensin95());
            textDisel.setText(station.getDisel());
            text3.setText(station.formatDistance());
            image.setImageResource(station.getIconID());

            return itemView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        int xxid =R .id.action_settings;
        xxid = R.id.action_open_map;
        String str = "";
        switch (id) {
            case R .id.action_settings:
                str = "settings";
                break;

            case R.id.action_open_map:
                str = "open map";
                Intent intent = new Intent(this, MapsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.action_download_data:
                test();
                break;
            case R.id.action_exit:
                this.finish();
                System.exit(0);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    private void test() {
    }


    private boolean handleLocationPermission(Activity activity){
        if (    ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{   android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSIONS_REQUEST_FOR_ACCESS_FINE_AND_COARSE_LOCATION);
            return false;  //the calling method should not use methods that need this permission yet;
        }
        return true; // the calling method can use methods that need this permission
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_FOR_ACCESS_FINE_AND_COARSE_LOCATION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    // todo: dostuff
                } else {
                    // Permission denied
                    this.finish(); //let's close the dialog
                    System.exit(0);

                }
                return;
            }

        }
    }

}
