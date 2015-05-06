package com.xj.ij.projectxfinal;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xj.ij.projectxfinal.lib3rd.GoogleDirection;

import org.w3c.dom.Document;

import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    private LatLng startLocation  ;
    private LatLng targetLocation ;
    private LatLng currentLocation ;
    private GoogleDirection gd;
    private GoogleDirection gdNav ;
    private Document mDoc;
    private Document mDocNav;
    private Marker mCurr;

    private TextView resName , time, paese, lat ,lang , des ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        resName = (TextView) findViewById(R.id.resNameMap);
        time = (TextView) findViewById(R.id.resTimeMap);
        paese = (TextView) findViewById(R.id.resPhaseMap);
        lat = (TextView) findViewById(R.id.resLatMap);
        lang = (TextView) findViewById(R.id.resLangMap);
        des = (TextView) findViewById(R.id.resDescMap);


        targetLocation = new LatLng(
                Double.parseDouble(getIntent().getStringExtra("lat")),
                Double.parseDouble(getIntent().getStringExtra("lang")));

        setUpMapIfNeeded();


        gd = new GoogleDirection(this);
        gdNav = new GoogleDirection(this);

        gd.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
            @Override
            public void onResponse(String status, Document doc, GoogleDirection gd) {
                mDoc = doc;
                mMap.addPolyline(gd.getPolyline(mDoc,3, Color.RED));


            }
        });

        gdNav.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
            @Override
            public void onResponse(String status, Document doc, GoogleDirection gd) {
                mDocNav = doc;
                mMap.addPolyline(gd.getPolyline(mDocNav,3,Color.YELLOW));

                time.setText(gd.getTotalDurationText(mDocNav));
                paese.setText(gd.getTotalDistanceText(mDocNav));



            }
        });





        gd.request(startLocation,targetLocation,GoogleDirection.MODE_DRIVING);


    }

    public void updateMyLocatino(){

      gdNav.request(currentLocation,targetLocation,GoogleDirection.MODE_DRIVING);
      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
      Toast.makeText(this,"LocationChange"+currentLocation.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                GPSTracker gpsTracker = new GPSTracker(this);
                // check if GPS enabled
                if(gpsTracker.canGetLocation()){

                    startLocation = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
                    currentLocation = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());


                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();

                   // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    gpsTracker.showSettingsAlert();
                }






                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(startLocation).title("start"));
        mMap.addMarker(new MarkerOptions().position(targetLocation).title("target"));

        MarkerOptions curr = new MarkerOptions().position(startLocation).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        mCurr = mMap.addMarker(curr);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 10));




    }


    private class LocationEvent implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
            updateMyLocatino();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public class GPSTracker extends Service  {

        private  LocationEvent locationEvent = new LocationEvent();

        private final Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,locationEvent );
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, locationEvent);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app
         * */
        public void stopUsingGPS(){
            if(locationManager != null){
                locationManager.removeUpdates(locationEvent);
            }
        }

        /**
         * Function to get latitude
         * */
        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         * */
        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         * @return boolean
         * */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog
         * On pressing Settings button will lauch Settings Options
         * */
        public void showSettingsAlert(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }


        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

}
