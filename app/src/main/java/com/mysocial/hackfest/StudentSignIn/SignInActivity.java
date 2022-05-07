package com.mysocial.hackfest.StudentSignIn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.mysocial.hackfest.studentFragment.StudentMainActvity;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView newAccount;
    private Button btnlogin;
    private EditText emailEdtTxt, passwordEdtTxt;
    private FirebaseAuth mAuth;
    private ValueEventListener vel;
    private boolean isDataAdded;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnlogin = (Button) findViewById(R.id.loginBtn);
        emailEdtTxt = (EditText) findViewById(R.id.mailIdSignIn);
        passwordEdtTxt = (EditText) findViewById(R.id.passwordSignIn);
        newAccount = (TextView) findViewById(R.id.gotosignup);
        btnlogin.setOnClickListener(this);
        newAccount.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        mAuth = FirebaseAuth.getInstance();
    }

    private void loginStudent() {
        String email = emailEdtTxt.getText().toString().trim().toLowerCase();
        String pass = passwordEdtTxt.getText().toString().trim();

        if (email.isEmpty()) {
            emailEdtTxt.setError("Enter your email Id.");
            emailEdtTxt.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            passwordEdtTxt.setError("Enter your password");
            passwordEdtTxt.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdtTxt.setError("Please enter a valid Email ID");
            emailEdtTxt.requestFocus();
            return;
        }
        if (!email.contains(".iitism.ac.in")) {
            emailEdtTxt.setError("Enter correct email.");
            emailEdtTxt.requestFocus();
            return;
        }


        progressDialog.show();
        btnlogin.setOnClickListener(null);
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        isDataAdded = false;
                        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Student Info");
                        vel = reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    String mail = (String) child.child("email").getValue();
                                    String crntMail = user.getEmail();
                                    if (crntMail.equals(mail)) {
                                        isDataAdded = true;
                                        break;
                                    }
                                }
                                if (!isDataAdded) {
                                    Toast.makeText(SignInActivity.this, "You need to Sign Up first.", Toast.LENGTH_SHORT).show();
                                    emailEdtTxt.getText().clear();
                                    passwordEdtTxt.getText().clear();
                                    user.delete();
                                } else {
                                    startActivity(new Intent(SignInActivity.this, StudentMainActvity.class));
                                    reff.removeEventListener(vel);
                                    finish();
                                }
                                reff.removeEventListener(vel);


                                progressDialog.cancel();

                                btnlogin.setOnClickListener(SignInActivity.this::onClick);
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(SignInActivity.this, "Email not verified.", Toast.LENGTH_SHORT).show();

                        progressDialog.cancel();

                        btnlogin.setOnClickListener(SignInActivity.this::onClick);
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Error occurred, try Sign Up.", Toast.LENGTH_SHORT).show();


                    progressDialog.cancel();

                    btnlogin.setOnClickListener(SignInActivity.this::onClick);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gotosignup:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
                break;

            case R.id.loginBtn:
                loginStudent();
                break;
        }
    }
}