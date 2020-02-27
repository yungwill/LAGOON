package com.example.lagoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                String temperature = dataSnapshot.child("Temp").getValue().toString();
                String humidity = dataSnapshot.child("Hum").getValue().toString();
                String p_h = dataSnapshot.child("PH").getValue().toString();
                String water_lvl = dataSnapshot.child("WL").getValue().toString();

                temp.setText(temperature + "°F");
                hum.setText(humidity + " RH");
                ph.setText(p_h);
                wl.setText(water_lvl);
            }
            // If the action is cancelled then it is logged
            @Override
            public void onCancelled(DatabaseError dError){
                Log.w(TAG, "loadPost:OnCancelled", dError.toException());
            }
        });
    }
}
