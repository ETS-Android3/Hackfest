package com.mysocial.hackfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mysocial.hackfest.classes.StudentsInfo;
import com.mysocial.hackfest.studentFragment.StudentMainActvity;

import org.jetbrains.annotations.NotNull;

public class EditProfileActivity extends AppCompatActivity {

    private Uri photoUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private StorageReference photoRef;
    private ImageView imageView;
    private ChildEventListener childEventListener;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Student Info");
        storageReference = FirebaseStorage.getInstance().getReference().child("student_Photos");
        imageView = findViewById(R.id.image_view);
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      }

      public void select_image (View view)
      {
          Intent intent = new Intent();
          intent.setType("image/*");
          intent.setAction(Intent.ACTION_GET_CONTENT);
          startActivityForResult(Intent.createChooser(intent, "Select Picture"), 11);
      }

      public void save ( View view )
      {
          ProgressDialog progressDialog = new ProgressDialog(this);
          progressDialog.setMessage("Please Wait");
          progressDialog.show();


          childEventListener = new ChildEventListener() {
              @Override
              public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                  StudentsInfo studentsInfo = snapshot.getValue(StudentsInfo.class);

                  if (studentsInfo.getEmail().equalsIgnoreCase(email))
                  {
                      String key = snapshot.getKey();
                      photoRef.putFile(photoUri).addOnSuccessListener(EditProfileActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                  public void onSuccess(Uri uri) {
                                      databaseReference.child(key).child("profilePicUri").setValue(uri.toString());
                                      Toast.makeText(EditProfileActivity.this, "Image Changed successfully", Toast.LENGTH_SHORT).show();
                                      progressDialog.cancel();
                                  }
                              });

                          }
                      }).addOnFailureListener(EditProfileActivity.this, new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull @NotNull Exception e) {
                              Toast.makeText(EditProfileActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                          }
                      });
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , StudentMainActvity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            photoUri = data.getData();
            imageView.setImageURI(photoUri);
            photoRef = storageReference.child(photoUri.getLastPathSegment());
        }
    }
    public void back(View view) {
        Intent intent = new Intent(this , StudentMainActvity.class);
        startActivity(intent);
        finish();
    }
}