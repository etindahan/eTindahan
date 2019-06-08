package com.etindahan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private static final String SHARED_PREFS = "ProfilePrefs";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        final TextView Name = view.findViewById(R.id.NameTEXTVIEW);
        final TextView Email = view.findViewById(R.id.EmailTextView);
        final TextView Pnum = view.findViewById(R.id.PhoneTextView);

        Button Logout = view.findViewById(R.id.LogoutButton);

        // SharedPref To store user info locally and not query everytime the user clicks on the profile tab :D (Bandwidth issues :( )

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        String Data_Gather_Status = sharedPreferences.getString("Data_Gather_Status", "");

        if (Data_Gather_Status.equals("")){
            DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserGetterSetter userGetterSetter = dataSnapshot.getValue(UserGetterSetter.class);

                    String FullName = userGetterSetter.getLast_name() + ", " + userGetterSetter.getFirst_name();
                    String FullNumber = "+63" + userGetterSetter.getContact_number();

                    Name.setText(FullName);
                    Email.setText(userGetterSetter.getEmail());
                    Pnum.setText(FullNumber);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Name", FullName);
                    editor.putString("Email", userGetterSetter.getEmail());
                    editor.putString("Number", FullNumber);
                    editor.putString("Data_Gather_Status", "DONE");
                    editor.apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            String FullNamePref = sharedPreferences.getString("Name","");
            String EmailPref = sharedPreferences.getString("Email","");
            String NumberPref = sharedPreferences.getString("Number","");

            Name.setText(FullNamePref);
            Email.setText(EmailPref);
            Pnum.setText(NumberPref);
        }

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Name", "");
                editor.putString("Email", "");
                editor.putString("Number", "");
                editor.putString("Data_Gather_Status", "");
                editor.apply();

                firebaseAuth.signOut();
                Toast.makeText(getActivity(), "Successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }
}
