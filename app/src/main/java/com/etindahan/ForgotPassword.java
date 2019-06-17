package com.etindahan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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


public class ForgotPassword extends AppCompatActivity {


    private Button PasswordResetButton;
    private EditText emailEditText;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityf_forgotpassword);

        mAuth = FirebaseAuth.getInstance();

        PasswordResetButton = (Button) findViewById(R.id.submitButton);
        emailEditText = (EditText) findViewById(R.id.emailText);

        PasswordResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailEditText.getText().toString();

                if (TextUtils.isEmpty(userEmail))
                {
                    Toast.makeText( ForgotPassword.this, "Please write your valid email address.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText( ForgotPassword.this, "Please check your Email Account...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                            }
                            else
                            {
                                String message =  task.getException().getMessage();
                                Toast.makeText(ForgotPassword.this, "Error occured.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
