package in.silive.bustracker.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by Shobhit Agarwal on 31-03-2015.
 */
public class CheckNetwork implements LocationListener {

    Context context;
    Double latitude, longitude;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false, canGetLocation = false;
    protected LocationManager locationManager;
    Location location;
    Geocoder geocoder;
    Address address;
    long minTime = 5 * 1000; // Minimum time interval for update in seconds, i.e. 5 seconds.
    long minDistance = 10; // Minimum distance change for update in meters, i.e. 10 meters.

    public CheckNetwork(Context mContext) {
        context = mContext;
    }

    public boolean checkConnectivity() {
        try {
            locationManager = (LocationManager) context
                    .getSystemService(context.LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                Toast.makeText(context, "Could not connect to GPS service", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!isNetworkEnabled) {
                Toast.makeText(context, "Could not connect to network provider", Toast.LENGTH_SHORT).show();
                return false;
            } else
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = (NetworkInfo) cm.getActiveNetworkInfo();
        boolean status = false;
        if (netInfo != null && netInfo.isConnected())
            status = true;
        if (status == true)
            Toast.makeText(context, "Connected to internet", Toast.LENGTH_SHORT)
                    .show();
        else
            Toast.makeText(context, "Couldn't connect to internet",
                    Toast.LENGTH_SHORT).show();
        return status;
    }

    public boolean isGPSEnabled() {
        LocationManager loc = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!loc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayAlertBox();
        } else
            return true;
        return false;
    }

    public void displayAlertBox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(
                "Your GPS seems to be disabled \nDo you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                context.startActivity(new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setTitle("Error");
        AlertDialog alert = builder.create();
        alert.show();
    }


    public String currentLocation() {

        try {
            locationManager = (LocationManager) context
                    .getSystemService(context.LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            geocoder = new Geocoder(context, Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Location loc = null;
        String currentLoc = "";
        if (isNetworkEnabled && isGPSEnabled) {
            loc = getLocation();
            if (loc != null) {
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocation(loc.getLatitude(),
                            loc.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (list != null) {
                    address = list.get(0);
                    currentLoc = address.getAddressLine(0) + "\n" + address.getAdminArea() + "\n" + address.getCountryCode() + "\n" + address.getCountryName() + "\n" + address.getFeatureName() + "\n" + address.getLocality() + "\n" + address.getSubAdminArea() + "\n" + address.getSubLocality();
                    Log.d("error", "loc " + currentLoc);
                    return address.getAddressLine(0).toString();
                }
            } else {
                Log.v("Location", "loc is nul");
                Toast.makeText(context, "location is null", Toast.LENGTH_SHORT).show();
            }
        }
        return " ";
    }


    public Location getLocation() {
        try {
            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("Service status", "Not connected");
                Toast.makeText(context, "Not connected to network", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, minTime,
                        minDistance, this);
                Log.d("Network", "network");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    Toast.makeText(context, "location manager == null", Toast.LENGTH_SHORT).show();
                    Log.d("location", "null");
                }

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            minTime,
                            minDistance, this);
                    if (locationManager != null) {
                        Location loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc == null) {

                        } else
                            location = loc;
                    } else {
                        Log.v("location manager", "gps location manager is null");
                    }
                }
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    return location;
                } else
                    Toast.makeText(context, "Location is null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("Error", "Exception getLocation" + e);
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

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
