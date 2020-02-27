package com.example.lagoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewImgActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private DatabaseReference databaseRef;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_img);
        // Set variables to the actual displays by using their IDs
        mRecyclerView = findViewById(R.id.recycler_view);
        // Formats the layout
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();
        // Connects to Images table in the database
        databaseRef = FirebaseDatabase.getInstance().getReference("Images");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            // Stores all image urls in an arraylist then displays them by using the ImageAdapter
            // class and the urls in the arraylist then it is displayed in on the actual page
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload up = postSnapshot.getValue(Upload.class);
                    mUploads.add(up);
                }
                mAdapter = new ImageAdapter(ViewImgActivity.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewImgActivity.this,databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
