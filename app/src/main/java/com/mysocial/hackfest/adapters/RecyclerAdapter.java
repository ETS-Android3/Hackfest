package com.mysocial.hackfest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mysocial.hackfest.R;
import com.mysocial.hackfest.classes.Data;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Data> complaintList;
    private ComplaintSelected complaintSelected;
    private String type;

    public RecyclerAdapter(Context context , List<Data> complaintList , ComplaintSelected complaintSelected , String type)
    {
        layoutInflater = LayoutInflater.from(context);
        this.complaintList = complaintList;
        this.complaintSelected = complaintSelected;
        this.type=type;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_view_layout,parent,false);
        return new RecyclerViewHolder(view , complaintSelected);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewHolder holder, int position) {
        if(type.equalsIgnoreCase("Admin")){
            if(isPrivate(complaintList.get(position))){
                holder.textPrivacy.setImageResource(R.drawable.private_button01);//private
                holder.complaint_upvote.setText(String.valueOf(complaintList.get(position).getUpVotes()));
                holder.complaint_title.setText(complaintList.get(position).getComplaintTitle());
                holder.student_name.setText(complaintList.get(position).getStudentName());
//                holder.complaint_status.setText(complaintList.get(position).getComplaintStatus());
                holder.sView.setImageResource(setStatusImage(complaintList.get(position).getComplaintStatus()));
                holder.upvote_image.setVisibility(View.GONE);
                if ( !complaintList.get(position).getStudentProfilePicUrl().equalsIgnoreCase("")) {
                    Glide.with(holder.student_image)
                            .asBitmap()
                            .load(complaintList.get(position).getStudentProfilePicUrl())
                            .circleCrop()
                            .into(holder.student_image);
                }
                else{
                    String ur="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/student_Photos%2Fprimary%3ADownload%2Fprofiledp.png?alt=media&token=1b26bd64-7816-41b0-8205-26f8b6b37654";
//                    Glide.with(holder.student_image)
//                            .asBitmap()
//                            .load(ur)
//                            .into(holder.student_image);
                }
            }
            else{
                holder.textPrivacy.setImageResource(R.drawable.public_button01);//public
                holder.complaint_upvote.setText(String.valueOf(complaintList.get(position).getUpVotes()));
                holder.complaint_title.setText(complaintList.get(position).getComplaintTitle());
                holder.student_name.setText(complaintList.get(position).getStudentName());
//                holder.complaint_status.setText(complaintList.get(position).getComplaintStatus());
                holder.sView.setImageResource(setStatusImage(complaintList.get(position).getComplaintStatus()));
                holder.upvote_image.setVisibility(View.GONE);
                if ( !complaintList.get(position).getStudentProfilePicUrl().equalsIgnoreCase("")) {

                    Glide.with(holder.student_image)
                            .asBitmap()
                            .load(complaintList.get(position).getStudentProfilePicUrl())
                            .circleCrop()
                            .into(holder.student_image);
                }

            }

        }
        else{
            if(isPrivate(complaintList.get(position))){
                holder.textPrivacy.setImageResource(R.drawable.private_button01);
                holder.complaint_upvote.setText(String.valueOf(complaintList.get(position).getUpVotes()));
                holder.complaint_title.setText(complaintList.get(position).getComplaintTitle());
                holder.student_name.setText(complaintList.get(position).getStudentName());
//                holder.complaint_status.setText(complaintList.get(position).getComplaintStatus());
                holder.sView.setImageResource(setStatusImage(complaintList.get(position).getComplaintStatus()));
                holder.upvote_image.setVisibility(View.GONE);

                if ( !complaintList.get(position).getStudentProfilePicUrl().equalsIgnoreCase("")) {

                    Glide.with(holder.student_image)
                            .asBitmap()
                            .load(complaintList.get(position).getStudentProfilePicUrl())
                            .circleCrop()
                            .into(holder.student_image);
                }
            }
            else{
                holder.textPrivacy.setImageResource(R.drawable.public_button01);
                holder.complaint_upvote.setText(String.valueOf(complaintList.get(position).getUpVotes()));
                holder.complaint_title.setText(complaintList.get(position).getComplaintTitle());
                holder.student_name.setText(complaintList.get(position).getStudentName());
//                holder.complaint_status.setText(complaintList.get(position).getComplaintStatus());
                holder.sView.setImageResource(setStatusImage(complaintList.get(position).getComplaintStatus()));
                holder.upvote_image.setVisibility(View.VISIBLE);

                if ( !complaintList.get(position).getStudentProfilePicUrl().equalsIgnoreCase("")) {

                    Glide.with(holder.student_image)
                            .asBitmap()
                            .load(complaintList.get(position).getStudentProfilePicUrl())
                            .circleCrop()
                            .into(holder.student_image);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (complaintList == null) {
            return 0;
        }
        return complaintList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private ImageView student_image , upvote_image,textPrivacy,sView;
        private ComplaintSelected complaintSelected;
        private TextView student_name , complaint_title  , complaint_upvote;

        public RecyclerViewHolder(@NonNull @NotNull View itemView , ComplaintSelected complaintSelected) {
            super(itemView);
            this.complaintSelected = complaintSelected;
            itemView.setOnClickListener(this);
            student_image = itemView.findViewById(R.id.student_image);
            upvote_image = itemView.findViewById(R.id.upvote_image);
            student_name = itemView.findViewById(R.id.student_name);
            complaint_title = itemView.findViewById(R.id.complaint_title);
            complaint_upvote = itemView.findViewById(R.id.complaint_upvotes);
            textPrivacy=itemView.findViewById(R.id.tv_privacy);
            sView=itemView.findViewById(R.id.complaint_status);

        }
        @Override
        public void onClick(View v) {
            complaintSelected.FullComplaintView(getAdapterPosition());
        }
    }

    public interface ComplaintSelected
    {
        void FullComplaintView( int position);
    }

    boolean isPrivate(Data dta){
        if(dta.getPublicPrivate())
            return false;
        else return true;
    }
    int setStatusImage(String status){
        if(status.equalsIgnoreCase("Unseen"))return (R.drawable.unseen_);
        if(status.equalsIgnoreCase("Processing"))return (R.drawable.processing);
        if(status.equalsIgnoreCase("Processed"))return (R.drawable.resolved_);
        if(status.equalsIgnoreCase("Turned Down"))return (R.drawable.turneddown);
        else return 0;
    }

}