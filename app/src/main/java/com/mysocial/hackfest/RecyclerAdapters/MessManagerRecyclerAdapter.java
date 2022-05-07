package com.mysocial.hackfest.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysocial.hackfest.R;
import com.mysocial.hackfest.classes.MessScanCount;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessManagerRecyclerAdapter extends RecyclerView.Adapter<MessManagerRecyclerAdapter.MessViewHolder> {

    private LayoutInflater layoutInflater;
    private List<MessScanCount> messScanCounts = new ArrayList<>();

    public MessManagerRecyclerAdapter(Context context, List<MessScanCount> messScanCounts) {
        this.layoutInflater = LayoutInflater.from(context);
        this.messScanCounts = messScanCounts;
    }

    @NonNull
    @NotNull
    @Override
    public MessViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_mess_manager, parent, false);
        return new MessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessManagerRecyclerAdapter.MessViewHolder holder, int position) {

        holder.date.setText(messScanCounts.get(position).getDate());
        holder.breakfast.setText(String.valueOf(messScanCounts.get(position).getBreakfast()));
        holder.lunch.setText(String.valueOf(messScanCounts.get(position).getLunch()));
        holder.snacks.setText(String.valueOf(messScanCounts.get(position).getSnacks()));
        holder.dinner.setText(String.valueOf(messScanCounts.get(position).getDinner()));
    }

    @Override
    public int getItemCount() {

        if (messScanCounts == null) {
            return 0;
        }
        return messScanCounts.size();
    }


    public class MessViewHolder extends RecyclerView.ViewHolder {

        private TextView date, breakfast, lunch, dinner, snacks;

        public MessViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            breakfast = itemView.findViewById(R.id.breakfast);
            lunch = itemView.findViewById(R.id.lunch);
            snacks = itemView.findViewById(R.id.snacks);
            dinner = itemView.findViewById(R.id.dinner);
        }
    }
}
