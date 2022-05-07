package com.mysocial.hackfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appitup.classes.Doctor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class HealthUpdaterActivity extends AppCompatActivity {
    private Uri photoUri;
    private String name,speciality,vising_time,current_status,imageUrl;
    private EditText ed_name,ed_speciality,ed_vising_time,ed_status;
    private TextView tv_date,tv_day;
    private ImageView iv_image;
    private Calendar calendar;
    private SimpleDateFormat simpledateformat;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog,progressDialog2;
    private String key="null";
    private String currentDay;
    private int dayOfWeek;
    private StorageReference photoRef;
    private int isUpdated=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_updater);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Health").child("CurrentSchedule");
        storageReference = FirebaseStorage.getInstance().getReference().child("Update_Photos");
        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("isLogged",7);
        myEdit.commit();
        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Please Wait ...");
        progressDialog2.show();
        ed_name=findViewById(R.id.breakfast_menu);
        tv_day=findViewById(R.id.tv_days);
        tv_date=findViewById(R.id.tv_dates);
        ed_speciality=findViewById(R.id.lunch_menu);
        ed_vising_time=findViewById(R.id.snacks_menu);
        ed_status=findViewById(R.id.dinner_menu);
        iv_image=findViewById(R.id.iv_doc);
        findTheComplainChild();
        imageUrl="";
        dayOfWeek=getDayNumberOld();
        currentDay=getDayOfWeak(dayOfWeek);
        name="";
        speciality="";
        vising_time="";
        current_status="Not Available";
        imageUrl="";
    }

    public void Back(View view) {
        finish();
    }

    public void iv_doc_clicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    public void updateStatus(View view) {
        if(key.equalsIgnoreCase("null")) Toast.makeText(this, "Please update the details First", Toast.LENGTH_SHORT).show();
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Health").child("CurrentSchedule");
            databaseReference.child(key).child("currentStatus").setValue(ed_status.getText().toString());
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }

    }
    void UploadImage(){
        photoRef.putFile(photoUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl=uri.toString();
                        progressDialog.cancel();
                    }
                });
            }
        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getApplicationContext(), (CharSequence) e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading Image...");
            progressDialog.show();
            photoUri = data.getData();
            iv_image.setImageURI(photoUri);
            photoRef = storageReference.child(photoUri.getLastPathSegment());
            UploadImage();
        }
    }
    public void update_Schedule(View view) {
        name=ed_name.getText().toString();
        speciality=ed_speciality.getText().toString();
        current_status=ed_status.getText().toString();
        vising_time=ed_vising_time.getText().toString();
        Doctor doc=new Doctor(name,speciality,vising_time,current_status,imageUrl,getDate().substring(0,8).toString());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Health").child("CurrentSchedule");
        key=databaseReference.push().getKey();
        databaseReference.child(key).setValue(doc);
        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
    }
    String getDate(){
        String Date="";
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calendar.getTime());
        return Date;
    }
    private void findTheComplainChild(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Health").child("CurrentSchedule");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        key=collectDailyMapData((Map<String,Object>) dataSnapshot.getValue());
                        ref.removeEventListener(this);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        ref.removeEventListener(this);
                    }
                });
    }
    private String collectDailyMapData(Map<String, Object> value) {
        String date;String finalkey="null";
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String ky = entry.getKey().toString();
            Map singleUser = (Map) entry.getValue();
            date = (String) singleUser.get("date");
            if(date.equalsIgnoreCase(getDate().substring(0,8))){
                name=(String)singleUser.get("name");
                speciality=(String)singleUser.get("speciality");
                vising_time=(String)singleUser.get("visitingTime");
                current_status=(String)singleUser.get("currentStatus");
                imageUrl=(String)singleUser.get("imageUrl");
                finalkey=ky;
                isUpdated=1;
                Toast.makeText(this, "fond key as "+ky, Toast.LENGTH_SHORT).show();
                updateDetails();
                return finalkey;
            }
        }
        if(isUpdated==0)updateDetails();
        return finalkey;

    }
    void updateDetails(){
        tv_date.setText("Date :"+getDate().substring(0,8));
        ed_name.setText(name);
        ed_speciality.setText(speciality);
        ed_status.setText(current_status);
        ed_vising_time.setText(vising_time);
        tv_day.setText("Day :"+currentDay);
        if(!imageUrl.equalsIgnoreCase("")){
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(imageUrl)
                    .circleCrop()
                    .into(iv_image);

        }
        else iv_image.setImageResource(R.drawable.johnnydoctor);
        progressDialog2.cancel();
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
}