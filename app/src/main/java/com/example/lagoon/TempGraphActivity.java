package com.example.lagoon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.lagoon.GraphActivity.count_all;
import static com.example.lagoon.GraphActivity.date_list;
import static com.example.lagoon.GraphActivity.setT;
import static com.example.lagoon.GraphActivity.count_dis;
import static com.example.lagoon.GraphActivity.counter_list;
import static com.example.lagoon.GraphActivity.time_list;
import static java.sql.DriverManager.println;

public class TempGraphActivity extends AppCompatActivity {
    // Creates an instance of the database and the storage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // Connects specifically to the Temperature table in the database
    DatabaseReference temp_Ref = database.getReference("Temperature");

    // Variables for the display
    GraphView graphView;
    LineGraphSeries temp_series;

    int min_x = 0;
    int max_x = 1440;

    //final DateFormat timeF = new SimpleDateFormat("mm:ss");
    //final DateFormat dateF = new SimpleDateFormat("MMM:dd:yy");
    //int count_t = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_graph);

        // Set variables to the actual displays by using their IDs
        graphView = (GraphView) findViewById(R.id.temp_graph);

        graphView.removeAllSeries();
        // Adds a new graph to the display
        temp_series = new LineGraphSeries();
        graphView.addSeries(temp_series);

        // Formats how the graph will look
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if(isValueX){
                    //if(setT == 0 || setT == 1) {
                        double time_div = value / 60;
                        String doubleS = String.valueOf(time_div);

                        int idx_d = doubleS.indexOf(".");
                        int hr = Integer.parseInt(doubleS.substring(0, idx_d));
                        int min = (int) (60 * (Double.parseDouble(doubleS.substring(idx_d))));

                        String new_tv;
                        String temp_hr = Integer.toString(hr);
                        String temp_min = Integer.toString(min);

                        if (temp_hr.length() == 1) {
                            temp_hr = "0" + temp_hr;
                        }
                        if (temp_min.length() == 1) {
                            temp_min = temp_min + "0";
                        }
                        new_tv = temp_hr + ":" + temp_min;
                        return new_tv;
                    }
                    /*else if (setT == 2){

                    }*/
                //}
                else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        graphView.getGridLabelRenderer().setNumHorizontalLabels(6);

        graphView.getGridLabelRenderer().setHumanRounding(false);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(20200101);
        graphView.getViewport().setMaxX(20200201);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMaxY(100);

        temp_Ref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                int count = 0;
                int counter = 0;
                int sum_of_day = 0;
                int avg_temp = 0;
                // Gets all the values in the Temperature table and stores them in an arraylist
                DataPoint[] dp = new DataPoint[count_dis];

                for(DataSnapshot tempSnap : dataSnapshot.getChildren()){
                    String tempV = tempSnap.getValue().toString();
                    //System.out.println("c: " + count_all);
                    if (count < count_all && count == Integer.parseInt(counter_list.get(counter))) {

                        if (setT == 0) {
                            //Date tempT = timeF.parse(time_list.get(counter));
                            String hour_t = (time_list.get(counter).split(":")[0]);
                            String min_t = (time_list.get(counter).split(":")[1]);
                            int time_t = (60 * Integer.parseInt(hour_t)) + (Integer.parseInt(min_t));
                            dp[counter] = new DataPoint(time_t, (Integer.parseInt(tempV) * 1.8 + 32));
                        }

                        else if (setT == 1) {
                            String hour_t = (time_list.get(counter).split(":")[0]);
                            String min_t = (time_list.get(counter).split(":")[1]);
                            int time_t = (60 * Integer.parseInt(hour_t)) + (Integer.parseInt(min_t));
                            dp[counter] = new DataPoint(time_t, (Integer.parseInt(tempV) * 1.8 + 32));
                        }
                        if (counter < count_dis - 1 && (setT == 0 || setT == 1)) counter++;
                    }


                    if(setT == 2 && count < count_all){
                        // Sets the x axis to date
                        int length = time_list.size()-1;
                        /*max_x = Integer.parseInt(time_list.get(0).split("-")[1]
                                + time_list.get(0).split("-")[2] + time_list.get(0).split("-")[0]);
                        min_x = Integer.parseInt(time_list.get(length).split("-")[1]
                                + time_list.get(length).split("-")[2] + time_list.get(length).split("-")[0]);*/

                        Log.d("Date",date_list.get(counter));

                        String yr_t = (date_list.get(counter).split("-")[0]);
                        String mon_t = (date_list.get(counter).split("-")[1]);
                        String day_t = (date_list.get(counter).split("-")[2]);
                        int date_t = Integer.parseInt(yr_t + mon_t + day_t);

                        /*if(count == 0){
                            count = Integer.parseInt(counter_list.get(counter));
                            //if(counter < count_dis-1) counter++;
                        }*/

                        if (Integer.parseInt(counter_list.get(counter)) == count) {
                            //Log.d("Prev", counter_list.get(counter - 1));
                            //Log.d("Next", counter_list.get(counter));
                            //Log.d("Temp", tempV);
                            sum_of_day += Integer.parseInt(tempV);
                            //Log.d("Sum", String.valueOf(sum_of_day));
                        }

                        if(Integer.parseInt(counter_list.get(counter)) == count) {
                            if(counter==0) {
                                avg_temp = sum_of_day / (count+1);
                                Log.d("AvgTemp", String.valueOf(avg_temp));
                            }
                            else {
                                avg_temp = sum_of_day / (Integer.parseInt(counter_list.get(counter)) - Integer.parseInt(counter_list.get(counter - 1)));
                                Log.d("AvgTemp", String.valueOf(avg_temp));
                            }
                            dp[counter] = new DataPoint(date_t, (avg_temp * 1.8 + 32));
                            if(counter < count_dis-1) counter++;
                            sum_of_day = 0;
                        }
                    }
                    count++;
                }
                temp_series.resetData(dp);
            }


            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }
}