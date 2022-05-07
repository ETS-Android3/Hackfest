package com.mysocial.hackfest.studentFragment;

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

public class FragmentPublicTimeLine extends Fragment {

    private FragmentManager fragmentManager;
    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ArrayList<Data> complaintList = new ArrayList<>();


    public FragmentPublicTimeLine() {
        // Required empty public constructor
    }

    public FragmentPublicTimeLine (FragmentManager fragmentManager , Context context )
    {
        this.fragmentManager = fragmentManager;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Complaints");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                Data data = snapshot.getValue(Data.class);

                if (data.getPublicPrivate()) {
                    complaintList.add(data);
                    display();
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
        View view = inflater.inflate(R.layout.fragment_public_time_line, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        viewPager = view.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager, tabLayout.getTabCount(), context , complaintList , "Student");
        viewPager.setAdapter(adapter);

        return view;
    }

    public void display()
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager, tabLayout.getTabCount(), context , complaintList , "Student");
        viewPager.setAdapter(adapter);
    }
}