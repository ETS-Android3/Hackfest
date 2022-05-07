package com.mysocial.hackfest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appitup.classes.Updates;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerAdapterClubUpdates extends RecyclerView.Adapter<RecyclerAdapterClubUpdates.RecyclerViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Updates> updatesList;
    private ComplaintSelected complaintSelected;
    private String club_notice;

    public RecyclerAdapterClubUpdates(Context context , List<Updates> updatesList , ComplaintSelected complaintSelected , String club_notice )
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

            View view = layoutInflater.inflate(R.layout.recycler_club_updates, parent, false);
            return new RecyclerViewHolder(view , complaintSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewHolder holder, int position) {

        holder.notice.setText(updatesList.get(position).getTitle());
        String date = updatesList.get(position).getDate();
        String str[] = date.split(" ",2);
        holder.notice_date.setText(str[0]);
        holder.notice_time.setText(str[1]);
        holder.club_name.setText(updatesList.get(position).getClubName());

        switch (updatesList.get(position).getClubName())
        {
            case "LCI":
               holder.imageView.setBackgroundResource(R.drawable.lci);
                break;
            case "Book Club":
                holder.imageView.setBackgroundResource(R.drawable.bookclub);
                break;
            case "Chayanika Sangh":
                holder.imageView.setBackgroundResource(R.drawable.chayanikasangh);
                break;
            case "LITC":
                holder.imageView.setBackgroundResource(R.drawable.litc);
                break;
            case "Comedy Club":
                holder.imageView.setBackgroundResource(R.drawable.comedyclub);
                break;
            case "Mic Drop":
                holder.imageView.setBackgroundResource(R.drawable.micdrop);
                break;
            case "Cyber Labs":
                holder.imageView.setBackgroundResource(R.drawable.cyberlabs);
                break;
            case "Quiz Club":
                holder.imageView.setBackgroundResource(R.drawable.quizclub);
                break;
            case "Mailer Daemon":
                holder.imageView.setBackgroundResource(R.drawable.mailerdaemon);
                break;
            case "LITM":
                holder.imageView.setBackgroundResource(R.drawable.litm);
                break;
            case "RoboIsm":
                holder.imageView.setBackgroundResource(R.drawable.roboism);
                break;
            case "WTC":
                holder.imageView.setBackgroundResource(R.drawable.wtc);
                break;
            case "Table Tennis":
                holder.imageView.setBackgroundResource(R.drawable.tt);
                break;
            case "Badminton":
                holder.imageView.setBackgroundResource(R.drawable.badminton);
                break;
            case "Boxing":
                holder.imageView.setBackgroundResource(R.drawable.boxing);
                break;
            case "Hockey":
                holder.imageView.setBackgroundResource(R.drawable.hockey);
                break;
            case "Cricket":
                holder.imageView.setBackgroundResource(R.drawable.cricket);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {

        if (updatesList == null)
        {
            return 3;
        }
        return updatesList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private TextView club_name , notice , notice_date , notice_time;

        public RecyclerViewHolder(@NonNull @NotNull View itemView , ComplaintSelected complaintSelected) {
            super(itemView);
            imageView = itemView.findViewById(R.id.club_icon);
            club_name = itemView.findViewById(R.id.club_name);
            notice = itemView.findViewById(R.id.notice_heading);
            notice_date = itemView.findViewById(R.id.notice_date);
            notice_time = itemView.findViewById(R.id.notice_time);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            complaintSelected.FullComplaintView(getAdapterPosition(), club_notice);
        }
    }

    public interface ComplaintSelected
    {
        void FullComplaintView( int position , String s );
    }
}
