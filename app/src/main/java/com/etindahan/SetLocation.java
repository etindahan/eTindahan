package com.etindahan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class SetLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button setLocationButton;
    String Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        setLocationButton = findViewById(R.id.set_locationButton);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(SetLocation.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {

                    double userlat  =  location.getLatitude();
                    double userlong =  location.getLongitude();

                    LatLng userloc = new LatLng(userlat,userlong);

                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userloc));
                }
            }
        });

    //    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
    //        @Override
    //        public void onMapClick(LatLng latLng) {
    //          // Creating a marker
    //          MarkerOptions markerOptions = new MarkerOptions();
    //
    //          // Setting the position for the marker
    //          markerOptions.position(latLng);
    //
    //          // Setting the title for the marker.
    //          // This will be displayed on taping the marker
    //          markerOptions.title(latLng.latitude + " : " + latLng.longitude);
    //
    //          // Clears the previously touched position
    //          mMap.clear();
    //
    //          // Animating to the touched position
    //          mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    //
    //            // Placing a marker on the touched position
    //            mMap.addMarker(markerOptions);
    //        }
    //    });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                mMap.clear();

                Log.i("CenterLat", String.valueOf(mMap.getCameraPosition().target.latitude));
                Log.i("CenterLong",String.valueOf(mMap.getCameraPosition().target.longitude));

                Location = mMap.getCameraPosition().target.latitude + "" + mMap.getCameraPosition().target.longitude;

                setLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SetLocation.this, Location, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
