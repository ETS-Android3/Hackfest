package com.mysocial.hackfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class CurrentMessMenuActivity extends AppCompatActivity {
    private Calendar calendar;
    private SimpleDateFormat simpledateformat;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String key,breakfast,lunch,dinner,snacks;
    private TextView tv_break,tv_lunch,tv_dinner,tv_dates,tv_snacks,tv_day;
    private int dayOfWeek=1;
    private String currentDay="";
    private int changed=0;
    private String dty,mess;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        setContentView(R.layout.activity_current_mess_menu);
        tv_break=findViewById(R.id.breakfast_menu);
        tv_dates=findViewById(R.id.tv_date);
        tv_dinner=findViewById(R.id.dinner_menu);
        tv_snacks=findViewById(R.id.snacks_menu);
        tv_lunch=findViewById(R.id.lunch_menu);
        tv_day=findViewById(R.id.tv_day);

        Intent intent = getIntent();
        mess = intent.getStringExtra("Hostel");
        dayOfWeek=getDayNumberOld();
        currentDay=getDayOfWeak(dayOfWeek);
        dty=getDate();
        findTheComplainChild("MessDailyMenu");
    }
    String getDate(){
        String Date="";
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calendar.getTime());
        return Date;
    }
    public int getDayNumberOld() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
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
    void SetDetails(int dayOfWeek){
        if(changed==1){
            tv_break.setText(breakfast);
            tv_dinner.setText(dinner);
            tv_lunch.setText(lunch);
            tv_dates.setText("Date :"+getDate().substring(0,8));
            tv_snacks.setText(snacks);
            tv_day.setText("Day: "+currentDay);
            progressDialog.cancel();
        }
        else {
            tv_break.setText(breakfast);
            tv_dinner.setText(dinner);
            tv_lunch.setText(lunch);
            tv_dates.setText("Date :"+getDate().substring(0,8));
            tv_snacks.setText(snacks);
            tv_day.setText("Day: "+currentDay);
            progressDialog.cancel();
        }
    }
    private void findTheComplainChild(String dat){


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("HOSTELS").child(mess).child(dat);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dat.equalsIgnoreCase("MessDailyMenu"))
                           collectMapData((Map<String,Object>) dataSnapshot.getValue());
                        else collectDailyMapData((Map<String,Object>) dataSnapshot.getValue());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
    private void collectMapData(Map<String, Object> value) {
        String date;String finalkey="";
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            if(entry!=null){
                String ky = entry.getKey().toString();
                Map singleUser = (Map) entry.getValue();
                date = (String) singleUser.get("date");
                String dt = dty.substring(0, 8);
                if(date.equalsIgnoreCase(dt)){
                    changed=1;
                    breakfast=(String)singleUser.get("breakfast");
                    lunch=(String)singleUser.get("lunch");
                    dinner=(String)singleUser.get("dinner");
                    snacks=(String)singleUser.get("snacks");
                    finalkey=ky;
                    break;
                }
                else{
                    changed=0;
                }
            }
        }
        if(changed==1){
            SetDetails(dayOfWeek);
        }
        else if(value.size()==0){
            finalkey="";
            changed=0;
            findTheComplainChild("MessWeekMenu");
        }
        else{
            findTheComplainChild("MessWeekMenu");
        }
    }
    public void onBackPressed() {
        finish();
    }

    public void fullSchedule(View view) {
    }
    private void collectDailyMapData(Map<String, Object> value) {
        String date;String finalkey="";
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String ky = entry.getKey().toString();
            Map singleUser = (Map) entry.getValue();
            date = (String) singleUser.get("day");
            if(date.equalsIgnoreCase(currentDay)){
                breakfast=(String)singleUser.get("breakfast");
                lunch=(String)singleUser.get("lunch");
                dinner=(String)singleUser.get("dinner");
                snacks=(String)singleUser.get("snacks");
                finalkey=ky;
                break;
            }
        }
        SetDetails(dayOfWeek);
    }

    public void Back(View view) {
        finish();
    }
}