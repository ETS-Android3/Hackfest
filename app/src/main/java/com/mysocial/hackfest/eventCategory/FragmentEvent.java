package com.mysocial.hackfest.eventCategory;

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

import com.example.appitup.R;
import com.example.appitup.ViewPagerEvent;
import com.example.appitup.classes.Updates;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FragmentEvent extends Fragment {


    private FragmentManager fragmentManager;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Context context;
    private List<Updates> updatesListEvent = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    public FragmentEvent() {
        // Required empty public constructor
    }


    public FragmentEvent( FragmentManager fragmentManager , Context context)
    {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference().child("Updates");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                Updates updates = snapshot.getValue(Updates.class);
                if (!updates.getNotice()) {
                    if (updates.getClubName().equalsIgnoreCase("IIT")) {
                        updatesListEvent.add(updates);
                        update();
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

        View view = inflater.inflate(R.layout.fragment_event, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        viewPager = view.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerEvent viewPagerEvent = new ViewPagerEvent(fragmentManager,tabLayout.getTabCount(),context,updatesListEvent);
        viewPager.setAdapter(viewPagerEvent);
        return view;
    }

    public void update() {

        ViewPagerEvent viewPagerEvent = new ViewPagerEvent(fragmentManager,tabLayout.getTabCount(),context,updatesListEvent);
        viewPager.setAdapter(viewPagerEvent);
    }
}