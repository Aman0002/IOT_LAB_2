package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_NAME= "com.example.myapplication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton fan = (ImageButton) findViewById(R.id.img_fan);
        ImageButton bulb = (ImageButton) findViewById(R.id.img_light);

        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_Info("Fan!");
            }
        });
        bulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_Info("Bulb!");

            }
        });
    }
    public void open_Info(String device)
    {
        Intent intent = new Intent(this, Info.class);
        intent.putExtra(EXTRA_NAME,device);
        startActivity(intent);
    }
}