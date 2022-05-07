package com.mysocial.hackfest;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appitup.classes.MessMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MasterMenuUpdaterActivity extends AppCompatActivity {
    private Calendar calendar;
    private String messs;
    private SimpleDateFormat simpledateformat;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private EditText ed_break,ed_lunch,ed_dinner,ed_snacks;
    private TextView tv_day,tv_del;
    int allowChange=0;
    int dy=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_menu_updater);
        ed_break=findViewById(R.id.breakfast_menu);
        tv_del=findViewById(R.id.tv_delete);
        ed_snacks=findViewById(R.id.snacks_menu);
        ed_lunch=findViewById(R.id.lunch_menu);
        ed_dinner=findViewById(R.id.dinner_menu);
        tv_day=findViewById(R.id.tv_day);
        tv_day.setText("Sunday");
        Intent intent =getIntent();
        messs=intent.getStringExtra("Mess");
    }
    public void SetMenu(View view) {
        if(allowChange==1){
            String ky="";
            String dt=getDate();
            dt=getDate().substring(0,8);
            MessMenu mess=new MessMenu(ed_break.getText().toString(),ed_lunch.getText().toString(),ed_dinner.getText().toString(),dt,ed_snacks.getText().toString(),tv_day.getText().toString());
            databaseReference = firebaseDatabase.getReference();
            ky=databaseReference.child("HOSTELS").child(messs).child("MessWeekMenu").push().getKey();
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("HOSTELS").child(messs).child("MessWeekMenu").child(ky).setValue(mess);
            ed_dinner.setText("");
            ed_lunch.setText("");
            ed_break.setText("");
            ed_snacks.setText("");
            Toast.makeText(this, "Changed", Toast.LENGTH_SHORT).show();
            dy++;
            if(dy==8){
                dy=1;
                allowChange=0;
                Toast.makeText(this, "You have changed full week menu ", Toast.LENGTH_SHORT).show();
                tv_del.setVisibility(View.VISIBLE);
            }
            tv_day.setText(getDayOfWeak(dy));
        }
        else{
            Toast.makeText(this, "First Delete The Full Menu", Toast.LENGTH_SHORT).show();
        }
    }
    String getDate(){
        String Date="";
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calendar.getTime());
        return Date;
    }
    public void onBackPressed() {
        finish();
    }
    String getDayOfWeak(int num){
        String day="";
        if(num==1)day="Sunday";
        else if(num==2)day="Monday";
        else if(num==3)day="Tuesday";
        else if(num==4)day="Wednesday";
        else if(num==5)day="Thursday";
        else if(num==6)day="Friday";
        else day="Saturday";
        return day;
    }

    public void delete_full_menu(View view) {
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("HOSTELS").child(messs).child("MessWeekMenu").removeValue();
        allowChange=1;
        tv_del.setVisibility(GONE);
        Toast.makeText(this, "Deleted full menu", Toast.LENGTH_SHORT).show();

    }

    public void Back(View view) {
        finish();
    }
}