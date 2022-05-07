package com.mysocial.hackfest.lostNFound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mysocial.hackfest.R;
import com.mysocial.hackfest.RecyclerAdapters.LostAdapter;
import com.mysocial.hackfest.classes.LostNFound;
import com.mysocial.hackfest.classes.StudentsInfo;
import com.mysocial.hackfest.databinding.ActivityLostNfoundBinding;

import java.util.ArrayList;
import java.util.List;

public class LostNFoundActivity extends AppCompatActivity implements View.OnClickListener, LostAdapter.On_Click {

    private ActivityLostNfoundBinding binding;
    private List<LostNFound> list = new ArrayList<>();
    private LostAdapter lostAdapter;
    private DatabaseReference studentDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLostNfoundBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(this::onClick);
        binding.addLostObj.setOnClickListener(this::onClick);

        lostAdapter = new LostAdapter(list, this::claim);
        binding.recyclerView.setAdapter(lostAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentDatabase = FirebaseDatabase.getInstance().getReference().child("Student Info");

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_data();
            }
        });

        get_data();
    }

    private void get_data() {
        list.clear();
        FirebaseFirestore.getInstance().collection("LostNFound").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                LostNFound lostNFound = snapshot.toObject(LostNFound.class);
                                list.add(lostNFound);
                                lostAdapter.notifyDataSetChanged();
                            }
                            binding.refreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.add_lost_obj:
                startActivity(new Intent(LostNFoundActivity.this, AddLostObjectActivity.class));
                break;
        }
    }

    private void get_user_data(LostNFound lostNFound) {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StudentsInfo studentsInfo = snapshot.getValue(StudentsInfo.class);
                if (studentsInfo.getEmail().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString())) {

                    lostNFound.setClaimedName(studentsInfo.getName());
                    lostNFound.setClaimedEmail(studentsInfo.getEmail());
                    lostNFound.setClaimedAdmiNo(studentsInfo.getAdmNo());
                    lostNFound.setStatus("Claimed");
                    upload_to_firebase(lostNFound);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
        studentDatabase.addChildEventListener(childEventListener);
    }

    private void upload_to_firebase(LostNFound lostNFound) {

        FirebaseFirestore.getInstance().collection("LostNFound").document(lostNFound.getId()).set(lostNFound);
        Toast.makeText(this, "Lost item Uploded Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void claim(int a) {
        get_user_data(list.get(a));
    }
}