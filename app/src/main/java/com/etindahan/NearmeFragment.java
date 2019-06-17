package com.etindahan;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

public class NearmeFragment extends Fragment {

    private DatabaseReference ShopRef;
    private ListView NearShopsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View  nearmeview = inflater.inflate(R.layout.fragment_nearme, container, false);

        NearShopsList = nearmeview.findViewById(R.id.NearmeList);
        ShopRef = FirebaseDatabase.getInstance().getReference("shops");

        final List<UserGetterSetter> shops_near_me = new LinkedList<>();
        final ArrayAdapter<UserGetterSetter> adapter = new ArrayAdapter<UserGetterSetter>(
                getActivity(), android.R.layout.two_line_list_item, shops_near_me)
        {
            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                    view = getLayoutInflater().inflate(android.R.layout.two_line_list_item, parent, false);
                }

                UserGetterSetter func = shops_near_me.get(position);

                ((TextView) view.findViewById(android.R.id.text1)).setTextSize(25);
                ((TextView) view.findViewById(android.R.id.text1)).setTypeface(Typeface.DEFAULT);

                    ((TextView) view.findViewById(android.R.id.text1)).setText(func.getshop_name());
                    ((TextView) view.findViewById(android.R.id.text2)).setText(func.getAddress());

                return view;
            }
        };

        NearShopsList.setAdapter(adapter);

        ShopRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserGetterSetter userGetterSetter = dataSnapshot.getValue(UserGetterSetter.class);
                shops_near_me.add(userGetterSetter);

                //GET SHOPS NEAR THE USER
                for (int i = 0; i < shops_near_me.size(); i++){
                    UserGetterSetter func = shops_near_me.get(i);

                    // Get radius
                    double lat1, lat2, lon1, lon2;

                    lat1 = func.getLatitude();
                    lat2 = 14.3904266;              //TODO: CREATE AN ACTIVITY WITH USER'S LOCATION (CURRENTLY USES A PRE-SET LOCATION)

                    lon1 = func.getLongitude();
                    lon2 = 120.9717952;

                    int R = 6371; // Radius daw ni earth
                    double latDistance = Math.toRadians(lat2 - lat1);
                    double lonDistance = Math.toRadians(lon2 - lon1);

                    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    double distance = R * c * 1000; // Convert to meters
                    //

                    String tae = Double.toString(distance);

                    if (distance > 400) {
                        shops_near_me.remove(i);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        NearShopsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), BuyerActivity.class);

                UserGetterSetter userGetterSetter = (UserGetterSetter) parent.getAdapter().getItem(position);

                intent.putExtra("userGetterSetter", new Gson().toJson(userGetterSetter));
                startActivity(intent);
            }
        });

        Toast.makeText(getActivity(), "Currently uses a preset gps location!", Toast.LENGTH_LONG).show();

        return nearmeview;

    }






}
