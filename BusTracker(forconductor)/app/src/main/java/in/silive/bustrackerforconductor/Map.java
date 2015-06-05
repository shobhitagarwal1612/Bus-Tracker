
package in.silive.bustrackerforconductor;

import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.ArrayList;

public class Map extends FragmentActivity {

    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    GoogleMap mMap;
    GoogleMapV2Direction gmDirection;
    LatLng loc1, loc2;
    private Document doc;
    @SuppressWarnings("unused")
    private static final float DEFAULTZOOM = 15;
    private static final String LOGTAG = "Maps";
    private static JSONArray path;
    private int counter = 0;

    public static void SetPath(JSONArray s) {
        path = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (path != null)
            Toast.makeText(this, path + "", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "path is null", Toast.LENGTH_SHORT).show();
        if (servicesOK()) {
            setContentView(R.layout.activity_map);
            if (initMap()) {
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            setContentView(R.layout.activity_map);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,
                    this, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to Google Play services",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mMap = mapFrag.getMap();
        }

        GPSTracker gps = new GPSTracker(this);

        try {
            JSONArray arr = path;
            Log.d("route", path.toString());
            Log.d("length", arr.length() + "");
            for (int i = 0; i < arr.length() - 1; i = i + 1) {
                JSONObject obj1 = arr.getJSONObject(i);
                JSONObject obj2 = arr.getJSONObject(i + 1);
                loc1 = new LatLng(Double.parseDouble(obj1.getString("lat")), Double.parseDouble(obj1.getString("longi")));
                loc2 = new LatLng(Double.parseDouble(obj2.getString("lat")), Double.parseDouble(obj2.getString("longi")));
                mMap.addMarker(new MarkerOptions().title(obj1.getString("stop_id")).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp)).position(loc1));
                mMap.addMarker(new MarkerOptions().title(obj2.getString("stop_id")).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp)).position(loc2));
                new DrawPath().execute(loc1, loc2);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc1));
        LatLng ll = new LatLng(gps.getLatitude(), gps.getLongitude());
        mMap.addMarker(new MarkerOptions().title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker)).position(ll));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        return (mMap != null);
    }

    private class DrawPath extends AsyncTask<LatLng, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(LatLng... params) {
            gmDirection = new GoogleMapV2Direction();
            doc = gmDirection.getDocument(params[0], params[1], GoogleMapV2Direction.MODE_DRIVING);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayList<LatLng> points = gmDirection.getDirection(doc);
            PolylineOptions pLine = new PolylineOptions().width(10).color(Color.RED);
            for (int i = 0; i < points.size(); i++) {
                pLine.add(points.get(i));
            }
            mMap.addPolyline(pLine);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void gotoLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mMap.moveCamera(update);
    }


    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    public void onConnected(Bundle arg0) {
        Toast.makeText(this, "Connected to location service",
                Toast.LENGTH_SHORT).show();
        requestLocationUpdates();
    }

    public void onDisconnected() {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    private void requestLocationUpdates() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(2000);
        request.setFastestInterval(1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
