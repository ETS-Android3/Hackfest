package com.mysocial.hackfest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.mysocial.hackfest.StudentSignIn.SignInActivity;
import com.mysocial.hackfest.admin.AdminMainActivity;
import com.mysocial.hackfest.studentFragment.StudentMainActvity;

public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageButton btn_admin = (ImageButton) findViewById(R.id.administrator_btn);
        ImageButton btn_student = (ImageButton) findViewById(R.id.student_btn);
        ImageButton btn_cc = (ImageButton) findViewById(R.id.cc_btn);
        ImageButton btn_mess = (ImageButton) findViewById(R.id.mess_btn);
        ImageButton btn_events = (ImageButton) findViewById(R.id.events_btn);
        btn_admin.setOnClickListener(this);
        btn_student.setOnClickListener(this);
        btn_mess.setOnClickListener(this);
        btn_cc.setOnClickListener(this);
        btn_events.setOnClickListener(this);
        SharedPreferences sh = getSharedPreferences("Login", MODE_PRIVATE);
        int number = sh.getInt("isLogged", 0);
        if(number == 0) {
            //Update accordingly to which button is pressed
        }
        else if(number==1) {
            Intent i=new Intent(this, AdminMainActivity.class);
            startActivity(i);
            finish();
        }
        else if(number==2){
            Intent i=new Intent(this, StudentMainActvity.class);
            startActivity(i);
            finish();
        }
        else if(number==3){
            Intent i=new Intent(this, MessManagerActivity.class);
            i.putExtra("Mess","null");
            startActivity(i);
            finish();
        }
        else if(number==4){
            Intent i=new Intent(this, OuterEventUpdatesActivity.class);
            startActivity(i);
            finish();
        }
        else if(number==5){
            Intent i=new Intent(this, NoticeActivity.class);
            startActivity(i);
            finish();
        }
        else if(number==6){
            Intent i=new Intent(this, UpdateAdministratorActivity.class);
            i.putExtra("Club","null");
            startActivity(i);
            finish();
        }
        else if(number==7){
            Intent i=new Intent(this, HealthManagerActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.administrator_btn:
                startActivity(new Intent(SplashScreenActivity.this, AdminSecondPage.class));
                finish();
                break;

            case R.id.student_btn:
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                finish();
                break;
            case R.id.cc_btn:
                startActivity(new Intent(SplashScreenActivity.this,ClubsList.class));
                finish();
                break;
            case R.id.mess_btn:
                Intent intent = new Intent(SplashScreenActivity.this,MessLoginSelecterActivity.class);
                intent.putExtra("isLogin",0);
                intent.putExtra("Accesser Name","mess");
                startActivity(intent);
                break;
            case R.id.events_btn:
                Intent intent1 = new Intent(SplashScreenActivity.this,LoginActivity.class);
                intent1.putExtra("Accesser Name","event");
                startActivity(intent1);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        SplashScreenActivity.this.finish();
        System.exit(0);
    }
}