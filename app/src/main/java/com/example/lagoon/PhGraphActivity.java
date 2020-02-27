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
import static com.example.lagoon.GraphActivity.time_list;

public class PhGraphActivity extends AppCompatActivity {
    // Creates an instance of the database and the storage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // Connects specifically to the PH tables in the database
    DatabaseReference ph_Ref = database.getReference("PH");

    // Variables for the display
    GraphView graphView;
    LineGraphSeries ph_series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ph_graph);
        // Set variables to the actual displays by using their IDs
        graphView = (GraphView) findViewById(R.id.ph_graph);

        // Adds a new graph to the display
        ph_series = new LineGraphSeries();
        graphView.addSeries(ph_series);

        // Formats how the graph will look
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter());
        graphView.getGridLabelRenderer().setNumHorizontalLabels(4);

        ph_Ref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                int count = 0;
                DataPoint[] dp = new DataPoint[count_all];
                // Gets all the values in the Temperature table and stores them in an arraylist
                for(DataSnapshot phSnap : dataSnapshot.getChildren()){
                    String phV = phSnap.getValue().toString();
                    if (count < count_all) {
                        dp[count] = new DataPoint(Integer.parseInt(time_list.get(count)),
                                Integer.parseInt(phV));
                        count++;
                    }
                }
                ph_series.resetData(dp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }
}