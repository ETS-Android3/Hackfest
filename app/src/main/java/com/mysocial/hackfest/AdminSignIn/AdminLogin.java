package com.mysocial.hackfest.AdminSignIn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mysocial.hackfest.R;

import org.jetbrains.annotations.NotNull;

public class AdminLogin extends AppCompatActivity implements View.OnClickListener{

    private EditText emailEdtTxt,passEdtTxt;
    private Button verifyBtn,enterBtn;
    private String email, password;
    private FirebaseAuth mAuth;
    private ValueEventListener vel,vel1,vel2;
    private Boolean isAdminDataAdded;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        emailEdtTxt = (EditText) findViewById(R.id.adminMailIdEdtTxt);
        passEdtTxt = (EditText) findViewById(R.id.adminPasswordEdtTxt);
        verifyBtn = (Button) findViewById(R.id.adminVerifyBtn);
        enterBtn = (Button) findViewById(R.id.adminEnterBtn);
        enterBtn.setOnClickListener(null);
        verifyBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

    }

    private void adminVerification(){
        email = emailEdtTxt.getText().toString().trim();
        password = passEdtTxt.getText().toString().trim();

        if(email.isEmpty()){
            emailEdtTxt.setError("Enter your email Id.");
            emailEdtTxt.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passEdtTxt.setError("Enter password.");
            passEdtTxt.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdtTxt.setError("Please enter a valid Email ID");
            emailEdtTxt.requestFocus();
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,"IITISM@admin239").addOnCompleteListener(AdminLogin.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(AdminLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user.sendEmailVerification().addOnCompleteListener(AdminLogin.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            enterBtn.setBackgroundResource(R.drawable.enterbutton);
                                            enterBtn.setOnClickListener(AdminLogin.this::onClick);
                                            progressDialog.cancel();
                                            Toast.makeText(AdminLogin.this,"Verification mail sent to "+user.getEmail(),Toast.LENGTH_SHORT).show();

                                        }else{

                                            Toast.makeText(AdminLogin.this,"Error occurred.",Toast.LENGTH_SHORT).show();
                                            user.delete();
                                            progressDialog.cancel();
                                        }
                                    }
                                });
                            }else{
                                progressDialog.cancel();
                                passEdtTxt.getText().clear();
                                passEdtTxt.setError("Enter correct password.");
                                passEdtTxt.requestFocus();
                                user.delete();
                            }
                        }
                    });
                }else{
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(AdminLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                isAdminDataAdded = false;
                                FirebaseUser user = mAuth.getCurrentUser();
                                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Admin");
                                vel1 = reff.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        for(DataSnapshot child : snapshot.getChildren()){
                                            String mail = (String) child.getValue();
                                            String crntMail = (String) user.getEmail();
                                            if(crntMail.equals(mail)){
                                                isAdminDataAdded = true;
                                            }
                                        }
                                        if(isAdminDataAdded){
                                            enterBtn.setBackgroundResource(R.drawable.enterbutton);
                                            enterBtn.setOnClickListener(AdminLogin.this::onClick);
                                            progressDialog.cancel();
                                            Toast.makeText(AdminLogin.this,"Press Enter.",Toast.LENGTH_SHORT).show();
                                        }else{
                                            progressDialog.cancel();
                                            Toast.makeText(AdminLogin.this,"You are not allowed.",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }else{
                                progressDialog.cancel();
                                passEdtTxt.getText().clear();
                                passEdtTxt.setError("Enter correct password.");
                                passEdtTxt.requestFocus();
                            }
                        }
                    });
                }
            }
        });
    }

    private void adminEnter(){
        isAdminDataAdded = false;
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Admin");
        vel1 = reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    String mail = (String) child.getValue();
                    String crntMail = (String) user.getEmail();
                    if(crntMail.equals(mail)){
                        isAdminDataAdded = true;
                    }
                }
                if(isAdminDataAdded){
                    startActivity(new Intent(AdminLogin.this, AdminMainActivity.class));
                    finish();
                }else{
                    SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    prefEditor.putString("MYSTRLABEL", email);
                    reff.push().setValue(user.getEmail());
                    prefEditor.apply();
                    startActivity(new Intent(AdminLogin.this, AdminMainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void forgotPasswordClicked(){
        email = emailEdtTxt.getText().toString().trim();
        if(email.isEmpty()){
            emailEdtTxt.setError("Enter Email id first.");
            emailEdtTxt.requestFocus();
            return;
        }
        progressDialog.show();
        isAdminDataAdded = false;
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Admin");
        vel = reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    String mail = (String) child.getValue();
                    if(mail.equals(email)){
                        isAdminDataAdded = true;
                        break;
                    }
                }
                if(isAdminDataAdded){
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(AdminLogin.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.cancel();
                                        Toast.makeText(AdminLogin.this,"Password reset email sent to "+email,Toast.LENGTH_SHORT).show();
                                    }else{
                                        progressDialog.cancel();
                                        Toast.makeText(AdminLogin.this,"Error occured",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    progressDialog.cancel();
                    Toast.makeText(AdminLogin.this,"You are entering for first time.Verify your email.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adminVerifyBtn:
                adminVerification();
                break;

            case R.id.adminEnterBtn:
                progressDialog.show();
                FirebaseUser user = mAuth.getCurrentUser();
                user.reload().addOnCompleteListener(AdminLogin.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(user.isEmailVerified()){
                                adminEnter();
                            }else{
                                progressDialog.cancel();
                                Toast.makeText(AdminLogin.this,"Email not verified.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                break;

            case R.id.forgotPassword:
                forgotPasswordClicked();
                break;
        }
    }
}