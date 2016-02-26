package edu.unc.nirjon.classlocation;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
//import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
implements  GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleApiClient c = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("TAG", "We are connected to Google Services");
        try {
            Location loc = LocationServices.FusedLocationApi.getLastLocation(c);
            Log.v("LOC", "" + loc.getLatitude() + ", " + loc.getLongitude());

            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(500);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(c, mLocationRequest, this);
        }
        catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        c.connect();
        super.onStart();

    }
    @Override
    protected void onStop() {
        c.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("CLASS GPS", location.getLatitude() + ", " + location.getLongitude());

        Geocoder g = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> la = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.v("Address", la.get(0).toString());

            //la = g.getFromLocationName("Sitterson Hall", 1);
            //Log.v("From name", la.get(0).toString());

        }catch (Exception ex) {

        }

    }




}
