package com.etindahan;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SMSRegister extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText firstname, lastname, EmailREG, Phone_Number, Password, ConfirmPass;
    private TextView Buyer, Seller;
    private Button RegisterREGA;
    private Boolean Status = false;
    private String Type = "";
    private CheckBox toRegister;

    UserGetterSetter userGetterSetter;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsregister);

        firstname = findViewById(R.id.FirstnameFieldREG);
        lastname = findViewById(R.id.LastnameFieldREG);
        Phone_Number = findViewById(R.id.phonenumFieldREG);
        Password = findViewById(R.id.passwordFieldREG);
        ConfirmPass = findViewById(R.id.confirmFieldREG);

        RegisterREGA = (Button) findViewById(R.id.RegisterButton);
        toRegister = (CheckBox) findViewById(R.id.toRegisterActivity);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        userGetterSetter = new UserGetterSetter();

        RegisterREGA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String FirstNameText = firstname.getText().toString();
                final String LastNameText = lastname.getText().toString();
                final String PhoneNUM = Phone_Number.getText().toString().trim();
                final String PasswordText = Password.getText().toString();
                final String ConfirmText = ConfirmPass.getText().toString();

                if (FirstNameText.isEmpty() || LastNameText.isEmpty() || PhoneNUM.isEmpty()  || PasswordText.isEmpty() || ConfirmText.isEmpty()) {
                    Toast.makeText(SMSRegister.this, "Please complete all the fields to continue!", Toast.LENGTH_SHORT).show();
                } else if (!PasswordText.equals(ConfirmText)) {
                    Toast.makeText(SMSRegister.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                } else if (PhoneNUM.length() <= 10) {
                    Toast.makeText(SMSRegister.this, "Phone number must contain 10 digits", Toast.LENGTH_SHORT).show();
                } else
                {
                    String phoneNumber = "+" + PhoneNUM;
                    Intent intent = new Intent(SMSRegister.this , PhoneVerification.class);
                    intent.putExtra("phonenumber", phoneNumber);
                    startActivity(intent);
                }









            }
        });
    }
}



