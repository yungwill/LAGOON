package com.example.lagoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    // Function that when called will take the user to another page
    public void goto_controls(View view){
        Intent intent = new Intent(this, ControlActivity.class);
        startActivity(intent);
    }

    public void goto_data(View view) {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }

    public void goto_graph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    public void goto_image(View view) {
        Intent intent = new Intent(this, ImageActivity.class);
        startActivity(intent);
    }
}
