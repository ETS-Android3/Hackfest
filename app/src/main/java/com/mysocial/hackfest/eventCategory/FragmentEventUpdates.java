package com.mysocial.hackfest.eventCategory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mysocial.hackfest.R;
import com.mysocial.hackfest.RecyclerAdapterClubUpdates;
import com.mysocial.hackfest.RecyclerAdapters.RecyclerAdapterNotice;
import com.mysocial.hackfest.SingleEventUpdateDisplay;
import com.mysocial.hackfest.classes.Updates;

import java.util.List;


public class FragmentEventUpdates extends Fragment implements RecyclerAdapterClubUpdates.ComplaintSelected, RecyclerAdapterNotice.ComplaintSelected{

    private List<Updates> updates;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerAdapterNotice recyclerAdapterNotice;

    public FragmentEventUpdates() {
        // Required empty public constructor
    }

    public FragmentEventUpdates( Context context , List<Updates> updates )
    {
        this.context = context;
        this.updates = updates;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_updates, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerAdapterNotice = new RecyclerAdapterNotice(context, updates, this::FullComplaintView, "event");
        recyclerView.setAdapter(recyclerAdapterNotice);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    @Override
    public void FullComplaintView(int position, String notice_club) {

        if (notice_club.equalsIgnoreCase("event")) {
            Intent intent = new Intent(getContext(), SingleEventUpdateDisplay.class);
            intent.putExtra("object", updates.get(position));
            startActivity(intent);
        }
    }
}