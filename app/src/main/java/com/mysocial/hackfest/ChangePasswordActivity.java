package com.mysocial.hackfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText oldpass, newpass;
    private ImageButton changeBtn;
    private String correctPass,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldpass = (EditText) findViewById(R.id.oldTokenEdtTxt);
        newpass = (EditText) findViewById(R.id.newTokenEdtTxt);
        changeBtn = (ImageButton) findViewById(R.id.changePassBtn);
        changeBtn.setOnClickListener(this);
        Intent intent = getIntent();
        user = intent.getStringExtra("User");
    }

    private void ChangeToken(){
        ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        String old = oldpass.getText().toString().trim();
        String newer = newpass.getText().toString().trim();
        if(old.isEmpty()){
            oldpass.setError("Enter old password");
            oldpass.requestFocus();
            oldpass.getText().clear();
            progressDialog.cancel();
            return;
        }
        if(newer.isEmpty()){
            newpass.setError("Enter new password");
            newpass.requestFocus();
            newpass.getText().clear();
            progressDialog.cancel();
            return;
        }
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Tokens");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    String key = child.getKey();
                    if(key.equals(user)){
                        correctPass = (String) child.getValue();
                        if(!old.equals(correctPass)){
                            Toast.makeText(ChangePasswordActivity.this,"Old password is incorrect.",Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            return;
                        }
                        child.getRef().setValue(newer);
                        Toast.makeText(ChangePasswordActivity.this,"Password changed Successfully",Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        ChangeToken();
    }
}