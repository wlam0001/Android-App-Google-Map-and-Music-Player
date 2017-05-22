package edu.unc.nirjon.classlocation;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;
    private Marker mCurrLocation;
    private LocationRequest mLocationRequest;
    Intent intent;
    public static final String SONG = "com.example.myfirstapp.SONG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(35.9105, -79.052)), 16.9F));

        buildGoogleApiClient();

        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Unregister for location callbacks:
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        if (intent != null) {
            stopService(intent);
            intent = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.v("LOC", "" + mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude());

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(500);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(getApplicationContext(), "Location Changed!", Toast.LENGTH_LONG).show();

        double variation = 0.0005;
        boolean createMarker = false;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //use geocoder to get location
        TextView view = (TextView) findViewById(R.id.currentLocation);
        GeocoderAsyncTask task = new GeocoderAsyncTask(getApplicationContext(), view, location);
        task.execute();

        //at Sitterson
        double sittersonLat = 35.9099;
        double sittersonLong = -79.0531;
        if (location.getLatitude() <= sittersonLat + (variation * 3) &&
                location.getLatitude() >= sittersonLat - (variation * 3) &&
                location.getLongitude() <= sittersonLong + variation &&
                location.getLongitude() >= sittersonLong - variation) {
            createMarker = true;
            latLng = new LatLng(sittersonLat, sittersonLong);

            if (intent == null) {
                intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra(SONG, "one");
                startService(intent);
            }
        }

        //at Polk Place
        double polkLat = 35.9106;
        double polkLong = -79.0504;
        if (location.getLatitude() <= polkLat + variation &&
                location.getLatitude() >= polkLat - variation &&
                location.getLongitude() <= polkLong + (variation * 1.8) &&
                location.getLongitude() >= polkLong - (variation * 1.8)) {
            createMarker = true;
            latLng = new LatLng(polkLat, polkLong);

            if (intent == null) {
                intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra(SONG, "two");
                startService(intent);
            }
        }

        //at Old Well
        double wellLat = 35.9120;
        double wellLong = -79.0512;
        if (location.getLatitude() <= wellLat + variation &&
                location.getLatitude() >= wellLat - variation &&
                location.getLongitude() <= wellLong + variation &&
                location.getLongitude() >= wellLong - variation) {
            createMarker = true;
            latLng = new LatLng(wellLat, wellLong);

            if (intent == null) {
                intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra(SONG, "three");
                startService(intent);
            }
        }

        //remove previous current location
        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }
        if (createMarker) {
            //add new marker at one of three position
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mCurrLocation = mGoogleMap.addMarker(markerOptions);
        } else {
            //stop intent if exist
            if (intent != null) {
                stopService(intent);
                intent = null;
            }
        }
    }

}