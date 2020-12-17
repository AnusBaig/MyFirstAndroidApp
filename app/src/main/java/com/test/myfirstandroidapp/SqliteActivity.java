package com.test.myfirstandroidapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class SqliteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tempText= (TextView)findViewById(R.id.txt_sql_temp_value);
        TextView locText= (TextView)findViewById(R.id.txt_sql_location_value);
        String temp = First2Fragment.temprature;
        String loc = First2Fragment.location;

        if(temp.equals("") || temp.equals(null))
            tempText.setText("----");
        else
            tempText.setText(temp);

        if(loc.equals("") || loc.equals(null))
            locText.setText("  ----");
        else
            locText.setText(loc);

        findViewById(R.id.img_sql_cloud_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),WeatherActivity.class);
                startActivity(intent);
            }
        });
    }
}