package com.example.appitup.StudentSignIn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appitup.R;
import com.example.appitup.classes.StudentsInfo;
import com.example.appitup.studentFragment.StudentMainActvity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name,mailId,password,confirmPassword,hostel;
    private Button signupBtn, profileBtn, verifybtn;
    private ImageView profilePic, noAccount;
    private DatabaseReference studentInfoDatabase;
    private StorageReference studentProfileStore;
    private Uri profilePicUri,photoUri;
    private StorageReference profilePicRef;
    private String email,pass,sName,profilePicUriStr,hostel_name;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.editTxtName);
        mailId = (EditText) findViewById(R.id.editTxtMail);
        password = (EditText) findViewById(R.id.editTxtPassword);
        confirmPassword = (EditText) findViewById(R.id.editTxtConfirm);
        profileBtn = (Button) findViewById(R.id.btnprofile);
        noAccount = (ImageView) findViewById(R.id.goToLogin);
        verifybtn = (Button) findViewById(R.id.verify);
        hostel=(EditText)findViewById(R.id.ed_hostel);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        signupBtn = (Button) findViewById(R.id.btnSignUp);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        profileBtn.setOnClickListener(this);
        noAccount.setOnClickListener(this);
        verifybtn.setOnClickListener(this);

        studentInfoDatabase = FirebaseDatabase.getInstance().getReference().child("Student Info");
        studentProfileStore = FirebaseStorage.getInstance().getReference().child("Student profile pic");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseUser user = mAuth.getCurrentUser();
    }

    private void studentVerification(){
        email = mailId.getText().toString().trim().toLowerCase();
        pass = password.getText().toString().trim();
        hostel_name=hostel.getText().toString().toUpperCase();
        String confirmPass = confirmPassword.getText().toString().trim();
        sName = name.getText().toString().trim();

        if(email.isEmpty()){
            mailId.setError("Please enter your Email ID");
            mailId.requestFocus();
            return;
        }

        if(sName.isEmpty()){
            name.setError("Please enter your name");
            name.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mailId.setError("Please enter a valid Email ID");
            mailId.requestFocus();
            return;
        }

        if(!email.contains(".iitism.ac.in")){
            mailId.setError("Please enter the institute's Email ID");
            mailId.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            password.setError("Please set a password");
            password.requestFocus();
            return;
        }

        if(pass.length()<6){
            password.setError("Password should be of atleast 6 characters.");
            password.requestFocus();
            return;
        }

        if(!pass.equals(confirmPass)){
            confirmPassword.setError("Not matching with the password you set.");
            confirmPassword.requestFocus();
            return;
        }

       progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success
                            FirebaseUser user = mAuth.getCurrentUser();
                            noAccount.setOnClickListener(null);
                                user.sendEmailVerification().addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            signupBtn.setOnClickListener(SignUpActivity.this);
                                            signupBtn.setBackgroundResource(R.drawable.signup);
                                            progressDialog.cancel();

                                            Toast.makeText(SignUpActivity.this, "Verification mail sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().getCurrentUser().reload();
                                            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                        } else {
                                            progressDialog.cancel();
                                            Toast.makeText(SignUpActivity.this, "Failed to send Verification mail", Toast.LENGTH_SHORT).show();

                                            user.delete();
                                        }
                                    }
                                });
                        } else {
                            // If sign up fails, display a message to the user.
                            progressDialog.cancel();
                            Toast.makeText(SignUpActivity.this,"Error occurred, try Sign In.",Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    private void studentDatabase(String email, String studentName) {
        //Add student's required data to firebase database
        progressDialog.show();
        StudentsInfo studentData = new StudentsInfo();
        studentData.setEmail(email);
        studentData.setHostel(hostel_name);
        studentData.setName(studentName);
        int index = email.indexOf("@");
        String admNo = email.substring(0, index);
        studentData.setAdmNo(admNo);

        if(profilePicRef!=null) {
            profilePicRef.putFile(profilePicUri).addOnSuccessListener((Activity) SignUpActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profilePicUriStr = uri.toString();
                            studentData.setProfilePicUri(profilePicUriStr);
                            studentInfoDatabase.push().setValue(studentData);
                            Intent intent = new Intent(SignUpActivity.this, StudentMainActvity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener((Activity) SignUpActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(SignUpActivity.this, (CharSequence) e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
                    .addOnFailureListener((Activity) SignUpActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(SignUpActivity.this, (CharSequence) e, Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            studentData.setProfilePicUri("");
            studentInfoDatabase.push().setValue(studentData);
            Intent intent = new Intent(SignUpActivity.this, StudentMainActvity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == 1 )
        {
            profilePicUri = data.getData();
            profilePic.setImageURI(profilePicUri);
            profilePicRef = studentProfileStore.child(profilePicUri.getLastPathSegment());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnprofile:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
                break;

            case R.id.goToLogin:
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.delete();
                }
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
                break;

            case R.id.verify:
                studentVerification();
                break;

            case R.id.btnSignUp:
                mAuth.getCurrentUser().reload().addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()) {
                                verifybtn.setOnClickListener(null);
                                signupBtn.setOnClickListener(null);
                                studentDatabase(email, sName);
                            }else{
                                Toast.makeText(SignUpActivity.this,"Email not verified.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SignUpActivity.this,"Error occurred.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
        }
    }
}