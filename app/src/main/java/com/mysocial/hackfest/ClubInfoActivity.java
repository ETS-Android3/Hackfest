package com.mysocial.hackfest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ClubInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_info);
        findViewById(R.id.lci).setOnClickListener(this);
        findViewById(R.id.bookclub).setOnClickListener(this);
        findViewById(R.id.chayanikasangh).setOnClickListener(this);
        findViewById(R.id.litc).setOnClickListener(this);
        findViewById(R.id.comedyclub).setOnClickListener(this);
        findViewById(R.id.micdrop).setOnClickListener(this);
        findViewById(R.id.cyberlabs).setOnClickListener(this);
        findViewById(R.id.quizclub).setOnClickListener(this);
        findViewById(R.id.mailerdaemon).setOnClickListener(this);
        findViewById(R.id.litm).setOnClickListener(this);
        findViewById(R.id.roboism).setOnClickListener(this);
        findViewById(R.id.wtc).setOnClickListener(this);
        findViewById(R.id.tt).setOnClickListener(this);
        findViewById(R.id.badminton).setOnClickListener(this);
        findViewById(R.id.boxing).setOnClickListener(this);
        findViewById(R.id.hockey).setOnClickListener(this);
        findViewById(R.id.cricket).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        String club="";
        switch (v.getId()){
            case R.id.lci:
                club = "LCI";
                break;
            case R.id.bookclub:
                club = "Book Club";
                break;
            case R.id.chayanikasangh:
                club = "Chayanika Sangh";
                break;
            case R.id.litc:
                club = "LITC";
                break;
            case R.id.comedyclub:
                club = "Comedy Club";
                break;
            case R.id.micdrop:
                club = "Mic Drop";
                break;
            case R.id.cyberlabs:
                club = "CyberLabs";
                break;
            case R.id.quizclub:
                club = "Quiz Club";
                break;
            case R.id.mailerdaemon:
                club = "Mailer Daemon";
                break;
            case R.id.litm:
                club = "LITM";
                break;
            case R.id.roboism:
                club = "RoboIsm";
                break;
            case R.id.wtc:
                club = "WTC";
                break;
            case R.id.tt:
                club = "Table Tennis Club";
                break;
            case R.id.badminton:
                club = "Badminton Club";
                break;
            case R.id.boxing:
                club = "Boxing Club";
                break;
            case R.id.hockey:
                club = "Hockey Club";
                break;
            case R.id.cricket:
                club = "Cricket Club";
                break;
        }
        Intent intent = new Intent(ClubInfoActivity.this, com.example.appitup.ViewClubInfoActivity.class);
        intent.putExtra("Club",club);
        startActivity(intent);
    }


}