package com.etindahan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class BuyerActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {


    private GoogleMap mMap;
    View mapView;
    private float longitude = 0.0f, latitude = 0.0f;
    private FusedLocationProviderClient client;

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.design_default_color_primary_dark};


    LatLng shoploc;

    public String ShopName, shop_owner_uid;
    private ImageButton back_button;



    private TextView shop_name_field, owner_name_field, shop_name_infobar_field, phone_number_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        shop_name_field = findViewById(R.id.shopNameField);
        owner_name_field = findViewById(R.id.owner_name);
        shop_name_infobar_field = findViewById(R.id.shopnameinfobar);
        phone_number_field = findViewById(R.id.contact_numberView);

        back_button = findViewById(R.id.returnbutton);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        // Set Data
        if (getIntent().hasExtra("userGetterSetter")) {
            String data = getIntent().getStringExtra("userGetterSetter");
            UserGetterSetter userGetterSetter = new Gson().fromJson(data, UserGetterSetter.class);

            //Get shop info including location
            ShopName = userGetterSetter.getshop_name();
            latitude = userGetterSetter.getLatitude();
            longitude = userGetterSetter.getLongitude();
            shop_owner_uid = userGetterSetter.getOwner_id();

            Toast.makeText(BuyerActivity.this, userGetterSetter.getOwner_id(), Toast.LENGTH_LONG).show();
            shop_name_field.setText(userGetterSetter.getshop_name());

            shop_name_infobar_field.setText(userGetterSetter.getshop_name());

            if (shop_owner_uid != null) {
                // Read from the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users").child(shop_owner_uid);


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserGetterSetter getShopReferences = dataSnapshot.getValue(UserGetterSetter.class);
                        owner_name_field.setText("  " + getShopReferences.getLast_name() + ", " + getShopReferences.getFirst_name());
                        phone_number_field.setText("  +63" + getShopReferences.getContact_number());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Request GPS Permission
        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(BuyerActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
            if(location != null) {

                   // double userlat = location.getLatitude();
                   // double userlong = location.getLongitude();

                   // LatLng user_location = new LatLng(userlat,userlong);

                   // polylines = new ArrayList<>();
                   // getRouteToMarker(user_location);
                }
            }
        });


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

        // Add a marker in Sydney and move the camera
        shoploc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(shoploc).title(ShopName));

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //   BEING CALLED AT FIRST LAUNCH :D
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(shoploc));


        // GPS BUTTON LOCATION
            if (mapView != null &&
                    mapView.findViewById(Integer.parseInt("1")) != null) {
                // Get the button view
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                // and next place it, on bottom right (as Google Maps app)
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationButton.getLayoutParams();
                // position on right bottom
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 30, 30);
            }
        //





    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }

    //Routing/Directions :D



    // TODO: Decide if we should enable DIRECTIONS API (Expensive AF $5 /1k Users) :(
    // For now disabled muna tong API na to :(

    public void getRouteToMarker(LatLng user_location){
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.WALKING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(user_location,shoploc)
                .key("AIzaSyC8bNg7gXeWbuCcXdHhYNT38yB6UJ8E2Qk")
                .build();
        routing.execute();
    }



    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null){
            Toast.makeText(BuyerActivity.this, "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(BuyerActivity.this, "Something went wrong. Try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0){
            for(Polyline poly : polylines){
                poly.remove();
            }
        }


        polylines = new ArrayList<>();
        //add route(s) to the MAP
        for (int i = 0; i<route.size(); i++){

            //In the case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(getResources().getColor(colorIndex));
            polylineOptions.width((10 + i * 3));
            polylineOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polylineOptions);
            polylines.add(polyline);

            Toast.makeText(BuyerActivity.this,
                    "Route " + (i+1) +": distance - " + route.get(i).getDistanceValue()+": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_LONG).show();


        }


    }

    @Override
    public void onRoutingCancelled() {

    }
}
