package com.mysocial.hackfest.RecyclerAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mysocial.hackfest.R;
import com.mysocial.hackfest.classes.LostNFound;

import java.util.ArrayList;
import java.util.List;

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.LostViewHolder> {

    private List<LostNFound> list = new ArrayList<>() ;
    private On_Click onClick ;

    public LostAdapter ( List<LostNFound> list , On_Click onClick )
    {
        this.list = list ;
        this.onClick = onClick ;
    }

    @NonNull
    @Override
    public LostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lost_found,parent,false);
        return new LostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LostViewHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.message.setText(list.get(position).getMessage());

        if ( !list.get(position).getImgUrl().equalsIgnoreCase("") )
        {
            Glide.with(holder.imageView)
                    .asBitmap()
                    .load(list.get(position).getImgUrl())
                    .into(holder.imageView);
        }

        holder.claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.claim(holder.getAdapterPosition());
            }
        });

        if( list.get(position).getStatus().equalsIgnoreCase("Claimed") )
        {
            holder.claim.setVisibility(View.GONE);
            holder.claimedAdmNo.setText(list.get(position).getClaimedAdmiNo());
            holder.claimedName.setText(list.get(position).getClaimedName());
            holder.claimedEmail.setText(list.get(position).getClaimedEmail());
        }
        else
        {
            holder.linearLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LostViewHolder extends RecyclerView.ViewHolder {

        private TextView title , message , claimedName , claimedAdmNo , claimedEmail;
        private ImageView imageView ;
        private CardView claim ;
        private LinearLayout linearLayout ;

        public LostViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.claimed_details);
            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            claimedName = itemView.findViewById(R.id.claimed_name);
            claimedAdmNo = itemView.findViewById(R.id.claimed_adm_no);
            claimedEmail = itemView.findViewById(R.id.claimed_email);
            imageView = itemView.findViewById(R.id.image_view);
            claim = itemView.findViewById(R.id.claim);
        }
    }

    public interface On_Click{
        void claim( int a ) ;
    }

}
