package com.mysocial.hackfest.studentFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class FragmentMess extends Fragment {

    private Context context;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private List<MessScanCount> messScanCounts = new ArrayList<>();
    private  RecyclerView recyclerView;
    private MessManagerRecyclerAdapter messManagerRecyclerAdapter;

    public FragmentMess() {
        // Required empty public constructor
    }

    public  FragmentMess ( Context context )
    {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Mess Records");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mess, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        messManagerRecyclerAdapter = new MessManagerRecyclerAdapter(context,messScanCounts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(messManagerRecyclerAdapter);
        return view;
    }

    public void update()
    {
        messManagerRecyclerAdapter = new MessManagerRecyclerAdapter(context,messScanCounts);
        recyclerView.setAdapter(messManagerRecyclerAdapter);
    }
}