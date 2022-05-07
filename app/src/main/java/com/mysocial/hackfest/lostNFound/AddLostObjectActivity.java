package com.mysocial.hackfest.lostNFound;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mysocial.hackfest.R;
import com.mysocial.hackfest.classes.LostNFound;
import com.mysocial.hackfest.classes.StudentsInfo;
import com.mysocial.hackfest.databinding.ActivityAddLostObjectBinding;

import org.jetbrains.annotations.NotNull;

public class AddLostObjectActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddLostObjectBinding binding;
    private FirebaseAuth auth;
    private StorageReference reference, photoRef;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private Uri photoUri = null;
    private String imageUrl = " " ;
    private DatabaseReference studentDatabase;
    private ChildEventListener childEventListener;
    private LostNFound lostNFound = new LostNFound();
    private Boolean isDone = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddLostObjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        reference = FirebaseStorage.getInstance().getReference("LostNFound Photos");
        studentDatabase = FirebaseDatabase.getInstance().getReference().child("Student Info");

        binding.back.setOnClickListener(this);
        binding.imageView.setOnClickListener(this);
        binding.post.setOnClickListener(this);
        binding.buttonDiscard.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                StudentsInfo studentsInfo = snapshot.getValue(StudentsInfo.class);
                if (studentsInfo.getEmail().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString())) {

                    lostNFound.setName(studentsInfo.getName());
                    lostNFound.setEmail(studentsInfo.getEmail());
                    lostNFound.setAdmissionNo(studentsInfo.getAdmNo());
                    isDone = true;
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

        studentDatabase.addChildEventListener(childEventListener);
    }

    public Boolean check_empty() {

        if (binding.title.getText().toString().isEmpty()) {
            binding.title.setError("Enter the Title");
            return false;
        } else if (binding.message.getText().toString().isEmpty()) {
            binding.message.setError("Enter the Message");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.image_view:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;

            case R.id.post:

                progressDialog.show();
                if (check_empty()) {
                    if (photoUri != null) {
                        upload_image();
                    }else
                    {
                        make_object();
                    }
                }

                break;

            case R.id.buttonDiscard:

                binding.title.setText("");
                binding.message.setText("");
                binding.imageView.setImageResource(R.drawable.sample_pic);

                break;
        }
    }

    private void upload_image() {

        photoRef.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl=uri.toString();
                        make_object();
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), (CharSequence) e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String generate_item_id() {
        long time = System.currentTimeMillis();
        return String.valueOf(time);
    }

    private void make_object ()
    {
        lostNFound.setId(generate_item_id());
        lostNFound.setTitle(binding.title.getText().toString());
        lostNFound.setMessage(binding.message.getText().toString());
        lostNFound.setImgUrl(imageUrl);
        lostNFound.setStatus("Stated");
        upload_to_firebase();
    }

    private void upload_to_firebase ()
    {
        if(isDone)
        {
            db.collection("LostNFound").document(lostNFound.getId()).set(lostNFound);
            Toast.makeText(this,"Lost item Uploded Successfully" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Check Internet Connection" , Toast.LENGTH_SHORT).show();
        }

        progressDialog.dismiss();

        binding.title.setText("");
        binding.message.setText("");
        binding.imageView.setImageResource(R.drawable.sample_pic);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            photoUri = data.getData();
            binding.imageView.setImageURI(photoUri);
            photoRef = reference.child(photoUri.getLastPathSegment());
        }
    }
}

