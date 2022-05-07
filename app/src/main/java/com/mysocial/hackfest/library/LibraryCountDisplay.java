package com.mysocial.hackfest.library;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mysocial.hackfest.databinding.ActivityLibraryCountDisplayBinding;

import org.eazegraph.lib.models.PieModel;

public class LibraryCountDisplay extends AppCompatActivity {

    private ActivityLibraryCountDisplayBinding binding;
    private String countVacant = " ", countOccupied = " ";
    private int countTotal = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLibraryCountDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        get_data();
    }

    private void get_data() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Library").child("Count");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int a = snapshot.getValue(Integer.class);
                countOccupied = String.valueOf(a);
                countVacant = String.valueOf(countTotal - a);
                setData();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int a = snapshot.getValue(Integer.class);
                countOccupied = String.valueOf(a);
                countVacant = String.valueOf(countTotal - a);
                setData();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.addChildEventListener(childEventListener);
    }

    public void back ( View view )
    {
        finish();
    }

    private void setData() {

        binding.occupiedCount.setText(countOccupied);
        binding.vacantCount.setText(countVacant);

        binding.piechart.clearChart();
        // Set the data and color to the pie chart
        binding.piechart.addPieSlice(
                new PieModel(
                        "Occupied",
                        Integer.parseInt(binding.occupiedCount.getText().toString()),
                        Color.parseColor("#FFEB3B")));
        binding.piechart.addPieSlice(
                new PieModel(
                        "Vacant",
                        Integer.parseInt(binding.vacantCount.getText().toString()),
                        Color.parseColor("#31CAFA")));
        // To animate the pie chart
        binding.piechart.startAnimation();
    }
}