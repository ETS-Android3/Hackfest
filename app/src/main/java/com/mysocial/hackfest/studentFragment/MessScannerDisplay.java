package com.mysocial.hackfest.studentFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appitup.MessManagerRecyclerAdapter;
import com.example.appitup.R;
import com.example.appitup.classes.MessScanCount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessScannerDisplay extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private List<MessScanCount> messScanCounts = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessManagerRecyclerAdapter messManagerRecyclerAdapter;
    private String mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_scanner_display);

        mess = getIntent().getStringExtra("Mess");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HOSTELS").child(mess).child("Mess Records");


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessScanCount messScanCount = snapshot.getValue(MessScanCount.class);
                messScanCounts.add(messScanCount);
                update();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);

        recyclerView = findViewById(R.id.recycler_view);
        messManagerRecyclerAdapter = new MessManagerRecyclerAdapter(this,messScanCounts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messManagerRecyclerAdapter);
    }

    public void update()
    {
        messManagerRecyclerAdapter = new MessManagerRecyclerAdapter(this,messScanCounts);
        recyclerView.setAdapter(messManagerRecyclerAdapter);
    }
}