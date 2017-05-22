package edu.unc.nirjon.classlocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;



public class GeocoderAsyncTask extends AsyncTask<String, Void, List<Address>> {
    private Context mContext;
    private Location mLocation;
    private TextView view;

    public GeocoderAsyncTask (Context context, TextView textView, Location location){
        mContext = context;
        mLocation  = location;
        view = textView;
    }

    @Override
    protected List<Address> doInBackground(String... strings) {
        Geocoder g = new Geocoder(mContext);

        List<Address> addresses = null;

        try {
            addresses = g.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        TextView text = (TextView) view.findViewById(R.id.currentLocation);
        boolean address = false;

        if (addresses!=null) {
            if (addresses.size() > 0) {
                text.setText(mContext.getResources().getString(R.string.location, addresses.get(0).getAddressLine(0), mLocation.getLatitude(), mLocation.getLongitude()));
                address = true;
            }
        }

        if (!address){
            text.setText(mContext.getResources().getString(R.string.location, "", mLocation.getLatitude(), mLocation.getLongitude()));
        }
    }
}
