package com.example.lagoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DataActivity extends AppCompatActivity {
    // Creates an instance of the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // Connects to the Update table inside the database
    DatabaseReference databaseRef = database.getReference("Update");

    TextView temp, hum, ph, wl;
    String temperature, humidity, p_h, water_lvl, wl_set;

    public static final String TAG = "D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        // Set variables to the actual displays by using their IDs
        temp = (TextView)findViewById(R.id.temp_display);
        hum = (TextView)findViewById(R.id.humidity_display);
        ph = (TextView)findViewById(R.id.ph_display);
        wl = (TextView)findViewById(R.id.water_lvl_display);

        // Has the database table Update check for when data is changed and take action when
        // values are changed
        databaseRef.addValueEventListener(new ValueEventListener()
        {
            // When data is changed then the values from the database are taken and stored
            // in the TextView so that the user can see then
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                temperature = dataSnapshot.child("Temp").getValue().toString();
                humidity = dataSnapshot.child("Hum").getValue().toString();
                p_h = dataSnapshot.child("PH").getValue().toString();
                water_lvl = dataSnapshot.child("WL").getValue().toString();

                Log.d("WATERLVL", water_lvl);
                // Checks if value is 0 and if it is display text and color green on app screen
                boolean water_check = water_lvl.equals("0");
                setwl(water_check);
                Log.d("WATERLVL", "check");
                // Sets the text for app screen
                temp.setText(temperature + "°F");
                hum.setText(humidity + " RH");
                ph.setText(p_h);
            }

            // If the action is cancelled then it is logged
            @Override
            public void onCancelled(DatabaseError dError){
                Log.w(TAG, "loadPost:OnCancelled", dError.toException());
            }
        });
    }

    // Check if water level is 0 or 1 and prints green or red depending on which
    void setwl(boolean check){
        if(check == true){
            Log.d("WATERLVL", "green");
            wl.setTextColor(Color.GREEN);
            wl.setText("Green");

        }
        else{
            wl.setTextColor(Color.RED);
            wl.setText("Red");
        }
    }
}


