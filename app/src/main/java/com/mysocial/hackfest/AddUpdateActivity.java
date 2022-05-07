package com.mysocial.hackfest;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appitup.classes.Updates;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddUpdateActivity extends AppCompatActivity {
    private Calendar calendar;
    private SimpleDateFormat simpledateformat;
    private String Date;
    private ImageView iv_image,iv_pdf;
    private Uri photoUri;
    private String imageUrl,pdfUrl,club;
    private EditText ed_fbLink,ed_instaLInk,ed_otherLink,ed_webLink,ed_title,ed_content,ed_exp_date;
    private DatabaseReference databaseReference;
    ImageView upload;
    Uri imageuri = null;
    private StorageReference storageReference;
    private String topic="";
    ProgressDialog dialog;
    private StorageReference photoRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);
        iv_image=findViewById(R.id.image_view);
        iv_pdf=findViewById(R.id.pdf_view);
        ed_content=findViewById(R.id.message);
        ed_title=findViewById(R.id.title);
        upload=findViewById(R.id.pdf_view);
        ed_fbLink=findViewById(R.id.fb_link);
        ed_instaLInk=findViewById(R.id.insta_link);
        ed_webLink=findViewById(R.id.web_link);
        ed_otherLink=findViewById(R.id.other_link);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Updates");
        storageReference = FirebaseStorage.getInstance().getReference().child("Update_Photos");
        pdfUrl="";
        imageUrl="Noting here";
       Intent intent = getIntent();
        club = intent.getStringExtra("ClubName");
    }

    public void pdf_clicked(View view) {
        Toast.makeText(AddUpdateActivity.this, "pdf clicked", Toast.LENGTH_SHORT).show();
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("application/pdf");
        startActivityForResult(galleryIntent, 2);
    }

    public void iv_clicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
    void UploadImage(){
        photoRef.putFile(photoUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl=uri.toString();
                        dialog.cancel();
                    }
                });

            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
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
            dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading Image");
            dialog.show();
            photoUri = data.getData();
            iv_image.setImageURI(photoUri);
            photoRef = storageReference.child(photoUri.getLastPathSegment());
            UploadImage();
        }
        if(requestCode==2){

            if (resultCode == RESULT_OK) {
                // Here we are initialising the progress dialog box
                dialog = new ProgressDialog(this);
                dialog.setMessage("Uploading Pdf");

                // this will show message uploading
                // while pdf is uploading
                dialog.show();
                imageuri = data.getData();
                final String timestamp = "" + System.currentTimeMillis();
                StorageReference sReference = FirebaseStorage.getInstance().getReference();
                final String messagePushID = timestamp;
                // Here we are uploading the pdf in firebase storage with the name of current time
                final StorageReference filepath = sReference.child("PDF").child(messagePushID + "." + "pdf");
                filepath.putFile(imageuri).continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            // After uploading is done it progress
                            // dialog box will be dismissed
                            dialog.dismiss();
                            iv_pdf.setImageResource(R.drawable.pdf_ticked);
                            Uri uri = task.getResult();
                            String myurl;
                            myurl = uri.toString();
                            pdfUrl=myurl;
                            Toast.makeText(AddUpdateActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(AddUpdateActivity.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    void PushAllData(){
        Updates updt=new Updates(Date,ed_content.getText().toString(),ed_title.getText().toString(),false,club,imageUrl
                ,pdfUrl,ed_fbLink.getText().toString(),ed_instaLInk.getText().toString(),ed_webLink.getText().toString(),ed_otherLink.getText().toString());
        String key=databaseReference.push().getKey();
        databaseReference.child(key).setValue(updt);
        // handel the expiry date here
    }

    public void btn_post(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calendar.getTime());
        PushAllData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
                finish();
            }
        }, 500 );

    }
    public void btn_discard(View view) {
        ed_instaLInk.setText("");
        ed_fbLink.setText("");
        ed_webLink.setText("");
        ed_otherLink.setText("");
        ed_title.setText("");
        ed_content.setText("");
        iv_image.setImageResource(R.drawable.sample_pic);
        iv_pdf.setImageResource(R.drawable.pdfwithoutlogonew);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void Back(View view) {
        finish();
    }

    public void Button_Click(View view) {
    }
}