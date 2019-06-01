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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText firstname,lastname,EmailREG,Phone_Number,Password,ConfirmPass;
    private TextView Buyer,Seller;
    private Button RegisterREGA;
    private Boolean Status=false;
    private String Type="";

    UserGetterSetter userGetterSetter;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname = findViewById(R.id.FirstnameFieldREG);
        lastname = findViewById(R.id.LastnameFieldREG);
        EmailREG = findViewById(R.id.emailFieldREG);
        Phone_Number = findViewById(R.id.phonenumFieldREG);
        Password = findViewById(R.id.passwordFieldREG);
        ConfirmPass = findViewById(R.id.confirmFieldREG);

        Buyer = (TextView) findViewById(R.id.BuyerSelection);
        Seller = (TextView) findViewById(R.id.SellerSelection);

        RegisterREGA = (Button) findViewById(R.id.RegisterButton);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        userGetterSetter = new UserGetterSetter();

        final ColorStateList oldcolorz = Buyer.getTextColors();

        Buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buyer.setTextColor(Color.RED);
                Seller.setTextColor(oldcolorz);
                Type = "Buyer";
                Status = true;
            }
        });

        Seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Seller.setTextColor(Color.RED);
                Buyer.setTextColor(oldcolorz);
                Type = "Seller";
                Status = true;
            }
        });

        RegisterREGA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String FirstNameText = firstname.getText().toString();
                final String LastNameText = lastname.getText().toString();
                final String EmailText = EmailREG.getText().toString();
                final String PhoneNUM = Phone_Number.getText().toString();
                final String PasswordText = Password.getText().toString();
                final String ConfirmText = ConfirmPass.getText().toString();


                if (FirstNameText.isEmpty() || LastNameText.isEmpty() || EmailText.isEmpty() || PhoneNUM.isEmpty() || PasswordText.isEmpty() || ConfirmText.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please complete all the fields to continue!", Toast.LENGTH_SHORT).show();
                } else if (!PasswordText.equals(ConfirmText)) {
                    Toast.makeText(RegisterActivity.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                } else if (!EmailText.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
                } else if(Type.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please select user type!", Toast.LENGTH_SHORT).show();
                } else if(PhoneNUM.length() <= 9){
                    Toast.makeText(RegisterActivity.this, "Phone number must contain 10 digits", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(EmailText, PasswordText)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    userGetterSetter.setAddress("");
                                                    userGetterSetter.setEmail(EmailText);
                                                    userGetterSetter.setFirst_name(FirstNameText);
                                                    userGetterSetter.setLast_name(LastNameText);
                                                    userGetterSetter.setContact_number(PhoneNUM);
                                                    userGetterSetter.setUser_type(Type);
                                                    reference.child(mAuth.getCurrentUser().getUid()).setValue(userGetterSetter);
                                                    Toast.makeText(RegisterActivity.this, "Registered successfully! Please check your email for verification", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    finish();

                                                } else {
                                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                    }


            }
        });


    }

}
