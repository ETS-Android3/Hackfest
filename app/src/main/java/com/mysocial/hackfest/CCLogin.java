package com.mysocial.hackfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class CCLogin extends AppCompatActivity implements View.OnClickListener{

    private EditText token;
    private Button enter;
    private String accessToken,club;
    private ValueEventListener vel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cclogin);

        token = (EditText) findViewById(R.id.token);
        enter = (Button) findViewById(R.id.enterBtn);
        enter.setOnClickListener(this);

        Intent intent = getIntent();
        club = intent.getStringExtra("Club");
        Toast.makeText(CCLogin.this,club,Toast.LENGTH_SHORT).show();
    }

    private void Enter(){

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Tokens");
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        vel = reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    String key = child.getKey();
                    if(key.equals(club)){
                        accessToken = (String) child.getValue();
                        String tokenEntered = token.getText().toString().trim();
                        if(tokenEntered.isEmpty()){
                            token.setError("Enter Token");
                            token.requestFocus();
                            progressDialog.cancel();
                            return;
                        }
                        if(!tokenEntered.equals(accessToken)){
                            token.getText().clear();
                            Toast.makeText(CCLogin.this,"Enter valid Token",Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            return;
                        }
                        Intent intent = new Intent(CCLogin.this,UpdateAdministratorActivity.class); // another activity to be added
                        intent.putExtra("Club",club);
                        progressDialog.cancel();
                        startActivity(intent);
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
        Enter();
    }
}