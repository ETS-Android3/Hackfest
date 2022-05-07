package com.mysocial.hackfest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ClubsList extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs_list);
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
        Intent intent = new Intent(ClubsList.this, CCLogin.class);
        String club="";
        switch (v.getId()){
            case R.id.lci:
                club = "lci";
                break;
            case R.id.bookclub:
                club = "bookclub";
                break;
            case R.id.chayanikasangh:
                club = "chayanikasangh";
                break;
            case R.id.litc:
                club = "litc";
                break;
            case R.id.comedyclub:
                club = "comedyclub";
                break;
            case R.id.micdrop:
                club = "micdrop";
                break;
            case R.id.cyberlabs:
                club = "cyberlabs";
                break;
            case R.id.quizclub:
                club = "quizclub";
                break;
            case R.id.mailerdaemon:
                club = "mailerdaemon";
                break;
            case R.id.litm:
                club = "litm";
                break;
            case R.id.roboism:
                club = "roboism";
                break;
            case R.id.wtc:
                club = "wtc";
                break;
            case R.id.tt:
                club = "tt";
                break;
            case R.id.badminton:
                club = "badminton";
                break;
            case R.id.boxing:
                club = "boxing";
                break;
            case R.id.hockey:
                club = "hockey";
                break;
            case R.id.cricket:
                club = "cricket";
                break;
        }
        intent.putExtra("Club",club);
        startActivity(intent);
    }
}