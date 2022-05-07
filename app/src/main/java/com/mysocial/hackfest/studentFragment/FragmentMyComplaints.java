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
import com.google.firebase.auth.FirebaseAuth;
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

public class FragmentMyComplaints extends Fragment {


    private FragmentManager fragmentManager;
    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private String Email;
    private ArrayList<Data> complaintList = new ArrayList<>();


    public FragmentMyComplaints() {
        // Required empty public constructor
    }

    public FragmentMyComplaints (FragmentManager fragmentManager , Context context)
    {
        this.fragmentManager = fragmentManager;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Complaints");
        Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String str[] = Email.split("@",2);
        Email = str[0];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                Data data = snapshot.getValue(Data.class);
                if (data.getRollNo().equalsIgnoreCase(Email)) {
                    complaintList.add(data);
                }
                display();
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