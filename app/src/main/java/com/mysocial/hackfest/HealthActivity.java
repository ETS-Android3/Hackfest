package com.mysocial.hackfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class HealthActivity extends AppCompatActivity {
    private TextView tv_name,tv_speciality,tv_vising_time,tv_current_stat,tv_date,tv_day;
    private ImageView iv_doc_pic;
    private Calendar calendar;
    private SimpleDateFormat simpledateformat;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String currentDay;
    private int dayOfWeek;
    private  int isUpdated=0;
    private ProgressDialog progressDialog;
    private String name,speciality,visiting_Time,status,imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        tv_name=findViewById(R.id.breakfast_menu);
        tv_date=findViewById(R.id.tv_date);
        tv_day=findViewById(R.id.tv_day);
        tv_speciality=findViewById(R.id.lunch_menu);
        tv_vising_time=findViewById(R.id.snacks_menu);
        tv_current_stat=findViewById(R.id.dinner_menu);
        iv_doc_pic=findViewById(R.id.iv_doc);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        dayOfWeek=getDayNumberOld();
        currentDay=getDayOfWeak(dayOfWeek);
        name="Not Yet Updated";
        speciality="Not Yet Updated";
        visiting_Time="Not Yet Updated";
        status="Not Yet Updated";
        imageUrl="";
        findTheComplainChild();
    }

    public void Back(View view) {
        finish();
    }

    public void iv_doc_clicked(View view) {
        Intent i=new Intent(HealthActivity.this,ImageViewActivity.class);
        i.putExtra("Url",imageUrl);
        startActivity(i);
    }
    private void findTheComplainChild(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Health").child("CurrentSchedule");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectDailyMapData((Map<String,Object>) dataSnapshot.getValue());
                        ref.removeEventListener(this);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        ref.removeEventListener(this);
                    }
                });

    }
    private void collectDailyMapData(Map<String, Object> value) {
        String date;String finalkey="";
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String ky = entry.getKey().toString();
            Map singleUser = (Map) entry.getValue();
            date = (String) singleUser.get("date");
            if(date.equalsIgnoreCase(getDate().substring(0,8))){
                name=(String)singleUser.get("name");
                speciality=(String)singleUser.get("speciality");
                visiting_Time=(String)singleUser.get("visitingTime");
                status=(String)singleUser.get("currentStatus");
                imageUrl=(String)singleUser.get("imageUrl");
                finalkey=ky;
                updateDetails();
                isUpdated=1;
                break;
            }
        }
        if(isUpdated==0)updateDetails();
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
    void updateDetails(){
        tv_date.setText("Date :"+getDate().substring(0,8));
        tv_name.setText(name);
        tv_speciality.setText(speciality);
        tv_current_stat.setText(status);
        tv_vising_time.setText(visiting_Time);
        tv_day.setText("Day :"+currentDay);
        if(!imageUrl.equalsIgnoreCase("")){
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(imageUrl)
                    .circleCrop()
                    .into(iv_doc_pic);
        }
        else iv_doc_pic.setImageResource(R.drawable.johnnydoctor);
        progressDialog.cancel();
    }
    String getDate(){
        String Date="";
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calendar.getTime());
        return Date;
    }
}