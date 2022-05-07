package com.mysocial.hackfest.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysocial.hackfest.R;
import com.mysocial.hackfest.classes.Updates;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerAdapterNotice extends RecyclerView.Adapter<RecyclerAdapterNotice.RecyclerViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Updates> updatesList;
    private ComplaintSelected complaintSelected;
    private String club_notice;

    public RecyclerAdapterNotice (Context context , List<Updates> updatesList , ComplaintSelected complaintSelected , String club_notice)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.updatesList = updatesList;
        this.complaintSelected = complaintSelected;
        this.club_notice = club_notice;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recycler_notice, parent, false);
        return new RecyclerViewHolder(view , complaintSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerAdapterNotice.RecyclerViewHolder holder, int position) {

        holder.notice.setText(updatesList.get(position).getTitle());
        String date = updatesList.get(position).getDate();
        String str[] = date.split(" ",2);
        holder.notice_date.setText(str[0]);
        holder.notice_time.setText(str[1]);
    }

    @Override
    public int getItemCount() {
        if (updatesList ==  null) {
            return 3;
        }
        return updatesList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView notice , notice_date , notice_time;

        public RecyclerViewHolder(@NonNull @NotNull View itemView , ComplaintSelected complaintSelected ) {
            super(itemView);
            notice = itemView.findViewById(R.id.notice_heading);
            notice_date = itemView.findViewById(R.id.notice_date);
            notice_time = itemView.findViewById(R.id.notice_time);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            complaintSelected.FullComplaintView(getAdapterPosition(),club_notice);
        }
    }
    public interface ComplaintSelected
    {
        void FullComplaintView( int position , String club);
    }
}
