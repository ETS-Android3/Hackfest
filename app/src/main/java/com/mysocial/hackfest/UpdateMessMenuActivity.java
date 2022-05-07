package com.mysocial.hackfest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appitup.classes.MessMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class UpdateMessMenuActivity extends AppCompatActivity {
    private Calendar calendar;
    private SimpleDateFormat simpledateformat;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String key;
    private EditText ed_break,ed_lunch,ed_dinner,ed_snacks;
    private TextView tv_day;
    private int allowChange=1;
    String currnetDay,messss;
    private int daynum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mess_menu);
        ed_break=findViewById(R.id.breakfast_menu);
        tv_day=findViewById(R.id.tv_day);
        ed_snacks=findViewById(R.id.snacks_menu);
        ed_lunch=findViewById(R.id.lunch_menu);
        ed_dinner=findViewById(R.id.dinner_menu);
        Intent intent = getIntent();
        messss = intent.getStringExtra("Hostel");
        daynum=getDayNumberOld();
        currnetDay=getDayOfWeak(daynum);
        tv_day.setText("Day :" + currnetDay);
        findTheComplainChild();
    }

    public void SetMenu(View view) {
        if(allowChange==1){
            String ky="";
            String dt=getDate();
            dt=getDate().substring(0,8);
            MessMenu mess=new MessMenu(ed_break.getText().toString(),ed_lunch.getText().toString(),ed_dinner.getText().toString(),dt,ed_snacks.getText().toString());
            databaseReference = firebaseDatabase.getReference();
            ky=databaseReference.child("HOSTELS").child(messss).child("MessDailyMenu").push().getKey();
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("HOSTELS").child(messss).child("MessDailyMenu").child(ky).setValue(mess);
            ed_dinner.setText("");
            ed_lunch.setText("");
            ed_break.setText("");
            ed_snacks.setText("");
            allowChange=0;
        }
        else{
            Toast.makeText(this, "You have already changed the menu", Toast.LENGTH_SHORT).show();
        }
    }
    String getDate(){
        String Date="";
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calendar.getTime());
        return Date;
    }
    private void findTheComplainChild(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("HOSTELS").child(messss).child("MessDailyMenu");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        key=collectMapData((Map<String,Object>) dataSnapshot.getValue());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }
    private String collectMapData(Map<String, Object> value) {
        String date;
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            if(entry!=null){
                String key = entry.getKey().toString();
                Map singleUser = (Map) entry.getValue();
                date = (String) singleUser.get("date");
                String dt = getDate().substring(0, 8);
                if(date.equalsIgnoreCase(dt)){
                    allowChange=0;
                }
                else{
//              databaseReference = firebaseDatabase.getReference().child(key).setValue(); delete the child
                    allowChange=1;
                }
            }
        }
        if(value.size()==0){
            allowChange=1;
        }
        return "dinNotFound";
    }
    public void onBackPressed() {
        finish();
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

    public void Back(View view) {
        finish();
    }
}