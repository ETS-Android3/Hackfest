package com.mysocial.hackfest.admin;

import static android.view.View.GONE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appitup.ImageViewActivity;
import com.example.appitup.R;
import com.example.appitup.classes.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class AdminSingleComplaintActivity extends AppCompatActivity {
    private TextView upvotes,type,student_name,message,title,roll_number,buttonProcessed,buttonTurnedDown,status;
    private ImageView iv_studentPhoto,iv_uploadedImage;
    private EditText edComplaint;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private String complaint_pic_url="";
    private String student_pic_url="";
    private String stat,compaintTitle;
    String cmnt;
    String upvt="";
    String key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_single_complaint);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        Data complaintData = (Data) getIntent().getSerializableExtra("object");
        upvotes=findViewById(R.id.tv_upvotes);
        buttonTurnedDown=findViewById(R.id.btn_turnedDown);
        buttonProcessed=findViewById(R.id.btn_processed);
        type=findViewById(R.id.pblprvt);
        edComplaint=findViewById(R.id.comment);
        student_name=findViewById(R.id.student_name);
        message=findViewById(R.id.complaint_message);
        roll_number=findViewById(R.id.student_admission_no);
        title=findViewById(R.id.complaint_title);
        iv_studentPhoto=findViewById(R.id.student_image);
        iv_uploadedImage=findViewById(R.id.complaint_image);
        status=findViewById(R.id.tv_status);
        // Getting Data
        if(!complaintData.getStudentProfilePicUrl().equalsIgnoreCase("")){
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(complaintData.getStudentProfilePicUrl())
                    .circleCrop()
                    .into(iv_studentPhoto);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    uploadData(complaintData);
                }
            }, 2000 );
        }
        else{
            uploadData(complaintData);
        }
        if(complaintData.getComplaintPicUrl().equalsIgnoreCase("")){
            iv_uploadedImage.setVisibility(GONE);
            progressDialog.cancel();
        }
        else{
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(complaintData.getComplaintPicUrl())
                    .into(iv_uploadedImage);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000 );
        }
    }

    public void sendEmailToReveal(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, "39gargpiyush@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Reveal the information of an anonymous student.");
        intent.putExtra(Intent.EXTRA_TEXT,"Reveal the information of candidate who sent a complaint of following heading\n"+compaintTitle);
        startActivity(intent);

    }

    public void turnDown(View view) {
        if(stat.equalsIgnoreCase("Turned Down")||stat.equalsIgnoreCase("Processed")){
            Toast.makeText(this, "You have already responded ", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.turndown_layout, null);
            b.setView(dialogView);

            b.setTitle("Choose the type of Turn Down");
            b.setMessage("Click yes if you want to send a mail to student \n Press no to just Turn Down the complaint");
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //manage the intent from here
                    sendEmailToReveal();
                    dialog.dismiss();
                }
            });
            b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            b.show();
            databaseReference = firebaseDatabase.getReference();
            status.setText("Turned Down");
            databaseReference.child("Complaints").child(key).child("complaintStatus").setValue("Turned Down");
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("Complaints").child(key).child("seen").setValue(true);
        }
    }

    public void btn_Processed(View view) {
        if(stat.equalsIgnoreCase("Turned Down")||stat.equalsIgnoreCase("Processed")){
            Toast.makeText(this, "You have already responded ", Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference = firebaseDatabase.getReference();
            status.setText("Processed");
            databaseReference.child("Complaints").child(key).child("complaintStatus").setValue("Processed");
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("Complaints").child(key).child("seen").setValue(true);
        }
    }
    private void uploadData(Data complaintData){
        student_name.setText(complaintData.getStudentName());
        if(complaintData.getRollNo().equalsIgnoreCase("")){
            roll_number.setVisibility(GONE);
        }
        else{
            roll_number.setText("Roll No : "+complaintData.getRollNo());
        }
        title.setText(complaintData.getComplaintTitle());
        message.setText(complaintData.getComplaintMessage());
        String s="Upvotes : "+String.valueOf(complaintData.getUpVotes());
        upvotes.setText(s);
        if (complaintData.getPublicPrivate()) {
            type.setText("Public");
        }
        else
        {
            type.setText("Private");
        }
        status.setText(complaintData.getComplaintStatus());
        stat=complaintData.getComplaintStatus().toString();
        if(complaintData.getComment().equalsIgnoreCase("Nothing Here")){
            edComplaint.setHint("Write a comment");
        }
        else{
            edComplaint.setText(complaintData.getComment());
        }
        student_pic_url=complaintData.getStudentProfilePicUrl();
        compaintTitle=complaintData.getComplaintTitle();
        complaint_pic_url=complaintData.getComplaintPicUrl();
        findTheComplainChild(complaintData);

    }


    private void findTheComplainChild(Data compaint){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Complaints");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        key=collectMapData((Map<String,Object>) dataSnapshot.getValue(),compaint);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    private String collectMapData(Map<String, Object> value,Data complaint) {
        String rollNumber,complaint_message;
        long upt;
        for (Map.Entry<String, Object> entry : value.entrySet()){
            String key=entry.getKey().toString();
            Map singleUser = (Map) entry.getValue();
            rollNumber= (String) singleUser.get("rollNo");
            complaint_message=(String) singleUser.get("complaintMessage");
            upt=(long) singleUser.get("upVotes");
            if((rollNumber.equalsIgnoreCase(complaint.getRollNo()))
                    &&(complaint_message.equalsIgnoreCase(complaint.getComplaintMessage()))&&(upt==complaint.getUpVotes())){
                return key;
            }
        }
        return "dinNotFound";
    }

    public void iv_complaint_pic_open(View view) {
        if(status.getText().toString().equalsIgnoreCase("Unseen")) {
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("Complaints").child(key).child("complaintStatus").setValue("Processing");
            status.setText("Processing");
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("Complaints").child(key).child("seen").setValue(true);
        }
        Intent i=new Intent(AdminSingleComplaintActivity.this, ImageViewActivity.class);
        i.putExtra("Url",complaint_pic_url);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        if(status.getText().toString().equalsIgnoreCase("Unseen")) {
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("Complaints").child(key).child("complaintStatus").setValue("Processing");
            status.setText("Processing");
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("Complaints").child(key).child("seen").setValue(true);
        }
        Intent i=new Intent(this, AdminMainActivity.class);
        startActivity(i);
        finish();
    }

    public void SetComment(View view) {
        cmnt=edComplaint.getText().toString();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Complaints").child(key).child("comment").setValue(cmnt);
        Toast.makeText(this, "Successfully commented.", Toast.LENGTH_SHORT).show();
    }

    public void studentButton(View view) {
        Intent i=new Intent(this,ImageViewActivity.class);
        i.putExtra("Url",student_pic_url);
        startActivity(i);
    }

}