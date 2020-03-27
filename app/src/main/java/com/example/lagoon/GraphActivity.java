package com.example.lagoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // Connects specifically to the Temperature and Date&Time tables in the database
    DatabaseReference time_Ref = database.getReference("Date&Time");
    static final List<String> time_list = new ArrayList<String>();
    static final List<String> counter_list = new ArrayList<String>();
    static int count_all;
    static int count_dis;
    static int month;
    static int day;
    static int year;
    static int setT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        final Spinner date_spin = (Spinner) findViewById(R.id.date_spinner);

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(GraphActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Date_Set));

        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date_spin.setAdapter(dateAdapter);

        final Query lastQ = time_Ref.orderByKey().limitToLast(1);

        date_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos,
                                       long id){
                lastQ.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dateSnap : dataSnapshot.getChildren()) {
                            String tempdate = dateSnap.child("Date").getValue().toString();
                            month = Integer.parseInt(tempdate.split("/")[0]);
                            day = Integer.parseInt(tempdate.split("/")[1]);
                            year = Integer.parseInt(tempdate.split("/")[2]);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                if(pos == 0){
                    count_all = 0;
                    count_dis = 0;
                    setT = 0;
                    time_list.clear();
                    counter_list.clear();

                    time_Ref.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot){

                            // Gets the Time values from Date&Time table then with the temperature values
                            // it is used to create a graph
                            for(DataSnapshot timeSnap : dataSnapshot.getChildren()) {

                                String dateV = timeSnap.child("Date").getValue().toString();
                                int month_t = Integer.parseInt(dateV.split("/")[0]);
                                int day_t = Integer.parseInt(dateV.split("/")[1]);
                                int year_t = Integer.parseInt(dateV.split("/")[2]);

                                if(month == month_t && day == day_t && year == year_t) {
                                    String timeV = timeSnap.child("Time").getValue().toString();
                                    time_list.add(timeV);
                                    counter_list.add(Integer.toString(count_all));
                                    count_dis ++;
                                }
                                count_all++;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError){
                        }
                    });
                }
                else if(pos == 1){
                    count_all = 0;
                    count_dis = 0;
                    setT = 0;
                    time_list.clear();
                    counter_list.clear();

                    time_Ref.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot){

                            // Gets the Time values from Date&Time table then with the temperature values
                            // it is used to create a graph
                            for(DataSnapshot timeSnap : dataSnapshot.getChildren()) {

                                String dateV = timeSnap.child("Date").getValue().toString();
                                int month_t = Integer.parseInt(dateV.split("/")[0]);
                                int day_t = Integer.parseInt(dateV.split("/")[1]);
                                int year_t = Integer.parseInt(dateV.split("/")[2]);

                                if(month == month_t && (day-1) == day_t && year == year_t) {
                                    String timeV = timeSnap.child("Time").getValue().toString();
                                    time_list.add(timeV);
                                    counter_list.add(Integer.toString(count_all));
                                    count_dis++;
                                }
                                count_all++;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError){
                        }
                    });
                }
                else if(pos == 2){
                    setT = 1;
                    int count_days = 30;
                    count_all = 0;
                    count_dis = 0;
                    time_list.clear();
                    counter_list.clear();

                    time_Ref.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot){

                            // Gets the Time values from Date&Time table then with the temperature values
                            // it is used to create a graph
                            for(DataSnapshot timeSnap : dataSnapshot.getChildren()) {

                                String dateV = timeSnap.child("Date").getValue().toString();
                                int month_t = Integer.parseInt(dateV.split("/")[0]);
                                int day_t = Integer.parseInt(dateV.split("/")[1]);
                                int year_t = Integer.parseInt(dateV.split("/")[2]);

                                if((month == month_t && (day-1) == day_t && year == year_t)||
                                        (day == 1 && month-1 == month_t && year == year_t && day_t == 31)||
                                        (day == 1 && month-1 == month_t && year == year_t &&
                                                (month_t == 4 || month_t == 6 || month_t == 9 || month_t == 11) && day_t == 30)||
                                        (day == 1 && month-1 == month_t && year == year_t &&
                                                month_t == 2  && (day_t == 28 || day_t == 29))||
                                        (day == 1 && day_t == 31 && month == 1 && month_t == 12 && year-1 == year_t)) {
                                    String timeV = timeSnap.child("Time").getValue().toString();
                                    time_list.add(timeV);
                                    counter_list.add(Integer.toString(count_all));
                                    count_dis++;
                                }
                                count_all++;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError){
                        }
                    });
                }
                else if(pos ==3){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView){
            }
        });
    }

    // Function that when called will take the user to another page
    public void goto_temp(View view){
        Intent intent = new Intent(this, TempGraphActivity.class);
        startActivity(intent);
    }

    public void goto_ph(View view){
        Intent intent = new Intent(this, PhGraphActivity.class);
        startActivity(intent);
    }

    public void goto_humidity(View view){
        Intent intent = new Intent(this, HumidityGraphActivity.class);
        startActivity(intent);
    }
}