package com.mysocial.hackfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ViewClubInfoActivity extends AppCompatActivity {
    private String club,club_image_url,details,youtube,instagram,sacRoom,facebook,establishment;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ValueEventListener vel;
    private TextView tv_details,tv_youtube,tv_instagram,tv_sacRoom,tv_facebook,tv_clubName,tv_establishment;
    private ImageView iv_club_image;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_club_info);
        Intent intent = getIntent();
        club = intent.getStringExtra("Club");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        tv_details=findViewById(R.id.club_details);
        tv_clubName=findViewById(R.id.club_name);
        tv_facebook=findViewById(R.id.tv_facebook);
        tv_instagram=findViewById(R.id.tv_instagram);
        tv_youtube=findViewById(R.id.tv_youtube);
        tv_sacRoom=findViewById(R.id.tv_sac_room);
        tv_establishment=findViewById(R.id.tv_year_of_establishment);
        iv_club_image=findViewById(R.id.iv_club_image);
        findTheComplainChild();
    }
    private void findTheComplainChild(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ClubInfo");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectDailyMapData((Map<String,Object>) dataSnapshot.getValue());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
    private void collectDailyMapData(Map<String, Object> value) {
        String club_name;String finalkey="";
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String ky = entry.getKey().toString();
            Map singleUser = (Map) entry.getValue();
            club_name = (String) singleUser.get("club_name");
            if(club_name.equalsIgnoreCase(club)){
                club_image_url=(String)singleUser.get("club_image_url");
                details=(String)singleUser.get("club_details1");
                youtube=(String)singleUser.get("youtube");
                facebook=(String)singleUser.get("facebook");
                instagram=(String)singleUser.get("instagram");
                sacRoom=(String)singleUser.get("sac_room");
                establishment=(String)singleUser.get("year_of_foundation");
                setDetails();
                finalkey=ky;
                break;
            }
        }
    }
    void setDetails(){
        tv_establishment.setText(establishment);
        tv_sacRoom.setText(sacRoom);
        tv_youtube.setText(youtube);
        tv_instagram.setText(instagram);
        tv_facebook.setText(facebook);
        tv_clubName.setText(club);
        tv_details.setText(details);
        switch (club) {
            case "LCI":
                iv_club_image.setImageResource(R.drawable.lci);
                break;
            case "Book Club":
                iv_club_image.setImageResource(R.drawable.bookclub);
                break;
            case "Chayanika Sangh":
                iv_club_image.setImageResource(R.drawable.chayanikasangh);
                break;
            case "LITC":
                iv_club_image.setImageResource(R.drawable.litc);
                break;
            case "Comedy Club":
                iv_club_image.setImageResource(R.drawable.comedyclub);
                break;
            case "Mic Drop":
                iv_club_image.setImageResource(R.drawable.micdrop);
                break;
            case "Cyber Labs":
                iv_club_image.setImageResource(R.drawable.cyberlabs);
                break;
            case "Quiz Club":
                iv_club_image.setImageResource(R.drawable.quizclub);
                break;
            case "Mailer Daemon":
                iv_club_image.setImageResource(R.drawable.mailerdaemon);
                break;
            case "LITM":
                iv_club_image.setImageResource(R.drawable.litm);
                break;
            case "RoboIsm":
                iv_club_image.setImageResource(R.drawable.roboism);
                break;
            case "WTC":
                iv_club_image.setImageResource(R.drawable.wtc);
                break;
            case "Table Tennis Club":
                iv_club_image.setImageResource(R.drawable.tt);
                break;
            case "Badminton Club":
                iv_club_image.setImageResource(R.drawable.badminton);
                break;
            case "Boxing Club":
                iv_club_image.setImageResource(R.drawable.boxing);
                break;
            case "Hockey Club":
                iv_club_image.setImageResource(R.drawable.hockey);
                break;
            case "Cricket Club":
                iv_club_image.setImageResource(R.drawable.cricket);
                break;
            default:
                break;
        }
        progressDialog.cancel();
    }

    public void Back(View view) {
        finish();
    }
}