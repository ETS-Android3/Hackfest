package com.mysocial.hackfest.studentFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appitup.R;
import com.example.appitup.RecyclerAdapterClubUpdates;
import com.example.appitup.RecyclerAdapterNotice;
import com.example.appitup.SingleClubUpdateDisplay;
import com.example.appitup.classes.Updates;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FragmentUpdates extends Fragment implements RecyclerAdapterClubUpdates.ComplaintSelected, RecyclerAdapterNotice.ComplaintSelected {

    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private List<Updates> updatesListClub = new ArrayList<>();
    private List<Updates> updatesListNotice = new ArrayList<>();
    private String notice_club;
    private RecyclerView recyclerView;
    private RecyclerAdapterClubUpdates recyclerAdapterClubUpdates;
    private RecyclerAdapterNotice recyclerAdapterNotice;

    public FragmentUpdates() {
        // Required empty public constructor
    }

    public FragmentUpdates(Context context, String notice_club) {
        this.context = context;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference().child("Updates");
        this.notice_club = notice_club;
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
                    } else {
                        updatesListClub.add(updates);
                        update();
                    }
                } else {
                    updatesListNotice.add(updates);
                    update();
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
        //don't know why there is null but commenting for now
        databaseReference.addChildEventListener(childEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_club_updates, container, false);

        if (notice_club.equalsIgnoreCase("club")) {
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerAdapterClubUpdates = new RecyclerAdapterClubUpdates(context, updatesListClub, this::FullComplaintView, "club");
            recyclerView.setAdapter(recyclerAdapterClubUpdates);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else if (notice_club.equalsIgnoreCase("notice")) {
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerAdapterNotice = new RecyclerAdapterNotice(context, updatesListNotice, this::FullComplaintView, "notice");
            recyclerView.setAdapter(recyclerAdapterNotice);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        return view;
    }

    @Override
    public void FullComplaintView(int position, String notice_club) {

        if (notice_club.equalsIgnoreCase("event")) {
        } else {
            Intent intent = new Intent(getContext(), SingleClubUpdateDisplay.class);
            if (notice_club.equalsIgnoreCase("club")) {
                intent.putExtra("object", updatesListClub.get(position));
            } else if (notice_club.equalsIgnoreCase("notice")) {
                intent.putExtra("object",updatesListNotice.get(position));
            }
            intent.putExtra("club_notice", notice_club);
            startActivity(intent);
        }
    }

    public void update() {
        if (notice_club.equalsIgnoreCase("club")) {
            recyclerAdapterClubUpdates = new RecyclerAdapterClubUpdates(context, updatesListClub, this::FullComplaintView, "club");
            recyclerView.setAdapter(recyclerAdapterClubUpdates);
        } else if (notice_club.equalsIgnoreCase("notice")) {
            recyclerAdapterClubUpdates = new RecyclerAdapterClubUpdates(context, updatesListNotice, this::FullComplaintView, "notice");
            recyclerView.setAdapter(recyclerAdapterNotice);
        }
    }
}