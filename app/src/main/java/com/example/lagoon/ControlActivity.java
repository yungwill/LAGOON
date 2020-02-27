package com.example.lagoon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ControlActivity extends AppCompatActivity {
    // Creates an instance of the database and the storage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // Connects specifically to the Temperature table in the database
    DatabaseReference temp_Ref = database.getReference("Controls");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Switch pump_sw = (Switch) findViewById(R.id.pump_switch);
        Switch cam_sw = (Switch) findViewById(R.id.cam_switch);

        pump_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               ;
                if(isChecked){
                    temp_Ref.child("Pump").setValue(1);
                }
                else{
                    temp_Ref.child("Pump").setValue(0);
                }
            }
        });

        cam_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ;
                if(isChecked){
                    temp_Ref.child("Camera").setValue(1);
                }
                else{
                    temp_Ref.child("Camera").setValue(0);
                }
            }
        });
    }
}
