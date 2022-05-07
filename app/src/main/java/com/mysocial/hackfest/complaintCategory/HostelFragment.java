package com.mysocial.hackfest.complaintCategory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appitup.R;
import com.example.appitup.RecyclerAdapter;
import com.example.appitup.admin.AdminSingleComplaintActivity;
import com.example.appitup.classes.Data;
import com.example.appitup.studentFragment.SingleComplaintDisplay;

import java.util.List;

public class HostelFragment extends Fragment implements RecyclerAdapter.ComplaintSelected {

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<Data> complaintList;
    private String type;

    public HostelFragment() {
        // Required empty public constructor
    }

    public HostelFragment (Context context , List<Data> complaintList, String type )
    {
        this.context = context;
        this.complaintList = complaintList;
        this.type=type;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_academic, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerAdapter = new RecyclerAdapter(context , complaintList , this::FullComplaintView,type);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

    @Override
    public void FullComplaintView( int position ) {

        if(type.equalsIgnoreCase("admin")){
            Intent intent = new Intent(context, AdminSingleComplaintActivity.class);
            intent.putExtra("object", complaintList.get(position));
            startActivity(intent);
            getActivity().finish();
        }
        else {
            Intent intent = new Intent(context, SingleComplaintDisplay.class);
            intent.putExtra("object", complaintList.get(position));
            startActivity(intent);
        }

    }
}