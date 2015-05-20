package com.csform.android.uiapptemplate.view;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.csform.android.uiapptemplate.FavorFormActivity;
import com.csform.android.uiapptemplate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, OnMapClickListener {

    public double lati;
    public double loni;
    Location lastLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng YOUR_LOCATION = new LatLng(lati, loni);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(YOUR_LOCATION, 13));

        map.addMarker(new MarkerOptions()
                .title("Your Location")
                .snippet("You Are Here")
                .position(YOUR_LOCATION));
    }
    @Override
    protected void onPause() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
        super.onPause();
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            displayLocation(location);
            lastLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}

     };

    @Override
    public void onMapClick(LatLng point) {
        Bundle args = new Bundle();
        args.putParcelable("user_position", point);
        Intent intent = new Intent(MapActivity.this, FavorFormActivity.class);
        intent.putExtra("bundle", args);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void displayLocation(Location location){
       // TextView latView = (TextView)findViewById(R.id.latView);
        //TextView lonView = (TextView)findViewById(R.id.lonView);
        if(location == null){
           // latView.setText("Cant get location");
           // lonView.setText("Cant get location");
        }
        else {
            lati = location.getLatitude();
            loni = location.getLongitude();
            //latView.setText(Double.toString(lati));
            //lonView.setText(Double.toString(loni));
        }
    }
}
