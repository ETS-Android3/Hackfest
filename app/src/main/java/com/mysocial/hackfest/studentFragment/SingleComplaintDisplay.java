package com.mysocial.hackfest.studentFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mysocial.hackfest.ImageViewActivity;
import com.mysocial.hackfest.R;
import com.mysocial.hackfest.classes.Data;

import org.jetbrains.annotations.NotNull;

public class SingleComplaintDisplay extends AppCompatActivity {

    private TextView student_name , student_admission_no , complaint_title , complaint_message , complaint_upVotes , complaint_publicPrivate , complaint_status;
    private ImageView student_image , complaint_image , complaint_upvote_pic;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private String complaint_pic_url="";String profile_pic_url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_complaint_display);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Complaints");

        Data complaintData = (Data) getIntent().getSerializableExtra("object");

        student_name = findViewById(R.id.student_name);
        student_admission_no = findViewById(R.id.student_admission_no);
        student_image = findViewById(R.id.student_image);
        complaint_title = findViewById(R.id.complaint_title);
        complaint_message = findViewById(R.id.complaint_message);
        complaint_upVotes = findViewById(R.id.complaint_upvotes);
        complaint_publicPrivate = findViewById(R.id.complaint_public_private);
        complaint_status = findViewById(R.id.complaint_status);
        complaint_image = findViewById(R.id.complaint_image);
        complaint_upvote_pic = findViewById(R.id.complaint_upvote_pic);

        if ( !complaintData.getComplaintPicUrl().equalsIgnoreCase("")) {

            Glide.with(complaint_image)
                    .asBitmap()
                    .load(complaintData.getComplaintPicUrl())
                    .into(complaint_image);
        }

        if ( !complaintData.getStudentProfilePicUrl().equalsIgnoreCase("")) {
            complaint_pic_url=complaintData.getComplaintPicUrl().toString();
            Glide.with(student_image)
                    .asBitmap()
                    .load(complaintData.getStudentProfilePicUrl())
                    .circleCrop()
                    .into(student_image);
        }
        student_name.setText(complaintData.getStudentName());
        student_admission_no.setText(complaintData.getRollNo());
        profile_pic_url=complaintData.getStudentProfilePicUrl();
        complaint_title.setText(complaintData.getComplaintTitle());
        complaint_message.setText(complaintData.getComplaintMessage());
        complaint_upVotes.setText("Upvotes : " + String.valueOf(complaintData.getUpVotes()));
        if (complaintData.getPublicPrivate()) {
            complaint_publicPrivate.setText("public");
        }
        else
        {
            complaint_publicPrivate.setText("private");
        }
        complaint_status.setText("Status : " + complaintData.getComplaintStatus());

        progressDialog.cancel();


        complaint_upvote_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        Data data = snapshot.getValue(Data.class);
                        if (data.getRollNo().equalsIgnoreCase(complaintData.getRollNo()))
                        {
                            if (data.getComplaintTitle().equalsIgnoreCase(complaintData.getComplaintTitle()))
                            {
                                if (data.getComplaintMessage().equalsIgnoreCase(complaintData.getComplaintMessage()))
                                {
                                    String key = snapshot.getKey();
                                    databaseReference.child(key).child("upVotes").setValue(complaintData.getUpVotes()+1);
                                    complaint_upVotes.setText("Upvotes : " + String.valueOf(complaintData.getUpVotes()+1));
                                    Toast.makeText(SingleComplaintDisplay.this, "Complaint Upvote recorded", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                };
                databaseReference.addChildEventListener(childEventListener);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void Preview(View view) {
        Intent i=new Intent(this, ImageViewActivity.class);
        i.putExtra("Url",complaint_pic_url);
        startActivity(i);
    }
    public void Back(View view) {
        finish();
    }
    public void Preview2(View view) {
        Intent i=new Intent(this, ImageViewActivity.class);
        i.putExtra("Url",profile_pic_url);
        startActivity(i);
    }
}