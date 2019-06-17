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
    private ListView Shoplist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View  mainview = inflater.inflate(R.layout.fragment_NearMe, container, false);

        Shoplist = mainview.findViewById(R.id.ShopListView);
        ShopRef = FirebaseDatabase.getInstance().getReference("shops");

        final List<UserGetterSetter> list_of_shops = new LinkedList<>();
        final ArrayAdapter<UserGetterSetter> adapter = new ArrayAdapter<UserGetterSetter>(
                getActivity(), android.R.layout.two_line_list_item, list_of_shops)
        {
            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                    view = getLayoutInflater().inflate(android.R.layout.two_line_list_item, parent, false);
                }
                UserGetterSetter func = list_of_shops.get(position);

                ((TextView) view.findViewById(android.R.id.text1)).setTextSize(25);
                ((TextView) view.findViewById(android.R.id.text1)).setTypeface(Typeface.DEFAULT);

                ((TextView) view.findViewById(android.R.id.text1)).setText(func.getshop_name());
                ((TextView) view.findViewById(android.R.id.text2)).setText(func.getAddress());

                return view;
            }
        };

        Shoplist.setAdapter(adapter);

        ShopRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserGetterSetter userGetterSetter = dataSnapshot.getValue(UserGetterSetter.class);
                list_of_shops.add(userGetterSetter);
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

        Shoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), BuyerActivity.class);

                UserGetterSetter userGetterSetter = (UserGetterSetter) parent.getAdapter().getItem(position);

                intent.putExtra("userGetterSetter", new Gson().toJson(userGetterSetter));
                startActivity(intent);
            }
        });


        return mainview;

    }






}
