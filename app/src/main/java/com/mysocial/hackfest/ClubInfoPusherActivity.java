package com.mysocial.hackfest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appitup.classes.Club;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClubInfoPusherActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private EditText tv_details,tv_youtube,tv_instagram,tv_sacRoom,tv_facebook,tv_clubName,tv_establishment;
    private ImageView iv_club_image;
    private String club,club_image_url,details,youtube,instagram,sacRoom,facebook,establishment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_info_pusher);
        Intent intent = getIntent();
        club = intent.getStringExtra("Club");
        tv_details=findViewById(R.id.club_details);
        tv_clubName=findViewById(R.id.club_name);
        tv_facebook=findViewById(R.id.tv_facebook);
        tv_instagram=findViewById(R.id.tv_instagram);
        tv_youtube=findViewById(R.id.tv_youtube);
        tv_sacRoom=findViewById(R.id.tv_sac_room);
        tv_establishment=findViewById(R.id.tv_year_of_establishment);
        iv_club_image=findViewById(R.id.iv_club_image);
    }
    public void Button_Click(View view) {
        findImageUrl();
        updateParameteres();
        Club clb=new Club(club_image_url,club,details,establishment,sacRoom,youtube,facebook,instagram);
        databaseReference = firebaseDatabase.getReference();
        String ky=databaseReference.child("ClubInfo").push().getKey();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("ClubInfo").child(ky).setValue(clb);
        tv_clubName.setText("");
        tv_youtube.setText("");
        tv_instagram.setText("");
        tv_facebook.setText("");
        tv_sacRoom.setText("");
        tv_details.setText("");
        tv_establishment.setText("");
        iv_club_image.setImageResource(R.drawable.cyberlabs);
    }
    void updateParameteres(){
        club=tv_clubName.getText().toString();
        youtube=tv_youtube.getText().toString();
        instagram=tv_instagram.getText().toString();
        facebook=tv_facebook.getText().toString();
        details=tv_details.getText().toString();
        sacRoom=tv_sacRoom.getText().toString();
        establishment=tv_establishment.getText().toString();
    }
    void  findImageUrl(){
        switch (tv_clubName.getText().toString()){
            case "CyberLabs":
                club_image_url="";
                break;
            case "LCI":
                club_image_url="";
                break;
            case "Mailer Deamon":
                club_image_url="";
                break;
            case "Book Club":
                club_image_url="";
                break;
            case "Chayanika Sangh":
                club_image_url="";
                break;
            case "Comedy Club":
                club_image_url="";
                break;
            case "Mic Drop":
                club_image_url="";
                break;
            case "Quiz Club":
                club_image_url="";
                break;
            case "LITM":
                club_image_url="";
                break;
            case "RoboIsm":
                club_image_url="";
                break;
            case "WTC":
                club_image_url="";
                break;
            case "Table Tennis Club":
                club_image_url="";
                break;
            case "Badminton Club":
                club_image_url="";
                break;
            case "Boxing Club":
                club_image_url="";
                break;
            case "Hockey Club":
                club_image_url="";
                break;
            case "Cricket Club":
                club_image_url="";
                break;
        }
    }
    public void Back(View view) {
        finish();
    }
}