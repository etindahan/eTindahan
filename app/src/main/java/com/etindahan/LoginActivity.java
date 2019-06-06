package com.etindahan;

import android.content.Intent;
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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button Login;
    private EditText Email, Password;
    private TextView Register,TOMAIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login = findViewById(R.id.SigninButton);
        Email = findViewById(R.id.EmailField);
        Password = findViewById(R.id.PasswordField);
        Register = findViewById(R.id.RegisterText);
        TOMAIN = findViewById(R.id.ToMainButton);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Email.getText().toString();
                String password = Password.getText().toString();

                if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please complete the field!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if(mAuth.getCurrentUser().isEmailVerified()){
                                            startActivity(new Intent(LoginActivity.this  , MainActivity.class));
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    // ...
                                }
                            });
                    }


            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this  , RegisterActivity.class));
            }
        });

        TOMAIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this  , MainActivity.class));
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null && user.isEmailVerified()){
            startActivity(new Intent(LoginActivity.this  , MainActivity.class));
            finish();
        }


    }
}
