package com.example.aakash.hoptraffic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    Button user;
    Button trafficDep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //Getting UI elements
        user = findViewById(R.id.publicUser);
        trafficDep = findViewById(R.id.trafficDep);

        //Setting onClick listener on buttons
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, ComplaintActivity.class);
                startActivity(intent);
            }
        });

        trafficDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

    }
}
