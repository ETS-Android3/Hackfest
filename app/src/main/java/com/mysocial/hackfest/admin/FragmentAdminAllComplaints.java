package com.mysocial.hackfest.admin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mysocial.hackfest.R;
import com.mysocial.hackfest.ViewPagerAdapters.ViewPagerAdapter;
import com.mysocial.hackfest.classes.Data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FragmentAdminAllComplaints extends Fragment {

    private FragmentManager fragmentManager;
    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Data> complaintList = new ArrayList<>();
    private ChildEventListener childEventListener;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child("Complaints");
    private String s;

    public FragmentAdminAllComplaints() {
        // Required empty public constructor
    }

    public FragmentAdminAllComplaints(FragmentManager fragmentManager , Context context , String s)
    {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.s = s;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Data data = snapshot.getValue(Data.class);

                if (s.equalsIgnoreCase("All")) {
                    complaintList.add(data);
                    display();
                }
                if (s.equalsIgnoreCase("Unseen"))
                {
                    if ((data.getComplaintStatus().equalsIgnoreCase("Unseen")) || (data.getSeen() == false)) {
                        complaintList.add(data);
                        display();
                    }
                }
                if (s.equalsIgnoreCase("Resolved"))
                {
                    if ((data.getComplaintStatus().equalsIgnoreCase("Turned Down")) || ((data.getComplaintStatus().equalsIgnoreCase("processed")))) {
                        complaintList.add(data);
                        display();
                    }
                }
                if (s.equalsIgnoreCase("Processing"))
                {
                    if ((data.getComplaintStatus().equalsIgnoreCase("processing"))) {
                        complaintList.add(data);
                        display();
                    }

                }
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
        View view = inflater.inflate(R.layout.fragment_my_complaints, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        viewPager = view.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager, tabLayout.getTabCount(), context , complaintList , "admin");
        viewPager.setAdapter(adapter);
        return view;
    }
    public void display()
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager, tabLayout.getTabCount(), context , complaintList , "admin");
        viewPager.setAdapter(adapter);
    }
}