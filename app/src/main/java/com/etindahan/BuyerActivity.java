package com.etindahan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class BuyerActivity extends AppCompatActivity {

    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        test = findViewById(R.id.testtextview);

        if(getIntent().hasExtra("userGetterSetter")) {
            String data = getIntent().getStringExtra("userGetterSetter");
            UserGetterSetter userGetterSetter = new Gson().fromJson(data, UserGetterSetter.class);

            Toast.makeText(BuyerActivity.this, userGetterSetter.getshop_name(), Toast.LENGTH_LONG).show();

            String testtextlol = userGetterSetter.getshop_name() + "\n" + userGetterSetter.getAddress();

            test.setText(testtextlol);

        }
    }
}
