package com.mysocial.hackfest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText token;
    private ImageButton enter;
    private String mess="";
    private String accessToken, accesser;
    private LinearLayout bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        token = (EditText) findViewById(R.id.tokenEdtTxt);
        enter = (ImageButton) findViewById(R.id.btn_enter);
        enter.setOnClickListener(this);
        bg = (LinearLayout) findViewById(R.id.loginbg);

        Intent intent = getIntent();
        accesser = intent.getStringExtra("Accesser Name");
        switch (accesser){
            case "notice":
                bg.setBackgroundResource(R.drawable.clubloginbg);
                break;
            case "mess":
                bg.setBackgroundResource(R.drawable.mess1login);
                mess=intent.getStringExtra("Mess");
                break;
            case "event":
                bg.setBackgroundResource(R.drawable.eventmanager_login);
                break;
            case "health":
                bg.setBackgroundResource(R.drawable.health_background_photo);
                break;
        }
    }

    private void Enter(){
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Tokens");
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    String key = child.getKey();
                    if(accesser.equals("mess")){
                        for(DataSnapshot child1 : child.getChildren()){
                            if(child1.getKey().equals(mess)){
                                accessToken = (String) child1.getValue();
                                String tokenEntered = token.getText().toString().trim();
                                if(tokenEntered.isEmpty()){
                                    token.setError("Enter Token");
                                    token.requestFocus();
                                    progressDialog.cancel();
                                    return;
                                }
                                if(!tokenEntered.equals(accessToken)){
                                    token.getText().clear();
                                    Toast.makeText(LoginActivity.this,"Enter valid Token",Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                    return;
                                }
                                Intent intent;
                                intent = new Intent(LoginActivity.this,MessManagerActivity.class);
                                intent.putExtra("Mess",mess);
                                intent.putExtra("Accesser",accesser);
                                progressDialog.cancel();
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    if(key.equals(accesser) && !accesser.equals("mess")){
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
                            Toast.makeText(LoginActivity.this,"Enter valid Token",Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            return;
                        }
                        Intent intent;
                        switch (accesser){
                            case "notice":
                                intent = new Intent(LoginActivity.this,NoticeActivity.class);
                                break;
                            case "event":
                                intent = new Intent(LoginActivity.this, OuterEventUpdatesActivity.class);
                                break;
                            case "health":
                                intent = new Intent(LoginActivity.this, HealthManagerActivity.class);
                                break;
                            default:
                                intent = new Intent(LoginActivity.this,LoginActivity.class);
                                break;
                        }
                        intent.putExtra("Accesser",accesser);
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