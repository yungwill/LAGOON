package com.example.lagoon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import static com.example.lagoon.GraphActivity.count_all;
import static com.example.lagoon.GraphActivity.count_dis;
import static com.example.lagoon.GraphActivity.counter_list;
import static com.example.lagoon.GraphActivity.setT;
import static com.example.lagoon.GraphActivity.time_list;

public class HumidityGraphActivity extends AppCompatActivity {
    // Creates an instance of the database and the storage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // Connects specifically to the Humidity table in the database
    DatabaseReference humid_Ref = database.getReference("Humidity");

    // Variables for the display
    GraphView graphView;
    LineGraphSeries humid_series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_graph);

        // Set variables to the actual displays by using their IDs
        graphView = (GraphView) findViewById(R.id.humidity_graph);

        graphView.removeAllSeries();

        // Adds a new graph to the display
        humid_series = new LineGraphSeries();
        graphView.addSeries(humid_series);

        // Formats how the graph will look
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    double time_div = value / 60;
                    String doubleS = String.valueOf(time_div);

                    int idx_d = doubleS.indexOf(".");
                    int hr = Integer.parseInt(doubleS.substring(0, idx_d));
                    int min = (int) (60 * (Double.parseDouble(doubleS.substring(idx_d))));

                    String new_tv;
                    String humid_hr = Integer.toString(hr);
                    String humid_min = Integer.toString(min);

                    if (humid_hr.length() == 1) {
                        humid_hr = "0" + humid_hr;
                    }
                    if (humid_min.length() == 1) {
                        humid_min = humid_min + "0";
                    }
                    new_tv = humid_hr + ":" + humid_min;
                    return new_tv;
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        graphView.getGridLabelRenderer().setNumHorizontalLabels(6);
        graphView.getGridLabelRenderer().setHumanRounding(false);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(1440);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(100);

        humid_Ref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                int count = 0;
                int counter = 0;
                // Gets all the values in the Temperature table and stores them in an arraylist
                DataPoint[] dp = new DataPoint[count_dis];

                // Gets all the values in the Temperature table and stores them in an arraylist
                for(DataSnapshot humidSnap : dataSnapshot.getChildren()){
                    String humidV = humidSnap.getValue().toString();
                    if (count < count_all && count == Integer.parseInt(counter_list.get(counter))) {

                        if(setT == 0) {
                            //Date tempT = timeF.parse(time_list.get(counter));
                            String hour_t = (time_list.get(counter).split(":")[0]);
                            String min_t = (time_list.get(counter).split(":")[1]);
                            int time_t = (60*Integer.parseInt(hour_t)) + (Integer.parseInt(min_t));
                            dp[counter] = new DataPoint(time_t, Integer.parseInt(humidV));
                        }

                        else if(setT == 1) {
                            //Date tempT = dateF.parse(time_list.get(counter));
                            String mon_t = (time_list.get(counter).split("/")[0]);
                            String day_t = (time_list.get(counter).split("/")[1]);
                            String yr_t = (time_list.get(counter).split("/")[2]);
                            int date_t = Integer.parseInt(mon_t + day_t + yr_t);
                            dp[counter] = new DataPoint(date_t, Integer.parseInt(humidV));
                        }
                        if(counter < count_dis-1) counter++;
                    }
                    count++;
                }
                humid_series.resetData(dp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }
}
