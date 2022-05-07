package com.mysocial.hackfest.StudentSignIn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mysocial.hackfest.R;
import com.mysocial.hackfest.classes.StudentsInfo;
import com.mysocial.hackfest.studentFragment.StudentMainActvity;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name,mailId,password, admissionNo ;
    private TextView signupBtn , signInBtn ;
    private Spinner spinner ;
    private String email,pass,sName,hostel;
    private DatabaseReference studentInfoDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.editTxtName);
        mailId = findViewById(R.id.editTxtMail);
        password = findViewById(R.id.editTxtPassword);
        admissionNo = findViewById(R.id.editTxtAdmi);
        signupBtn = findViewById(R.id.button_signup);
        signInBtn = findViewById(R.id.goToLogin);
        spinner = findViewById(R.id.spinner);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        studentInfoDatabase = FirebaseDatabase.getInstance().getReference().child("Student Info");

        String[] items_spinner3 = new String[]{"Select Hostel","JASPER","AMBER","DIAMOND","EMARALD","OPAL","TOPAZ","ROSALINE","RUBY","SAPPHIRE"};
        ArrayAdapter<String> adapterSpinner3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_spinner3);
        spinner.setAdapter(adapterSpinner3);

        signInBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
    }

    private Boolean checkEmpty(){
        email = mailId.getText().toString().trim().toLowerCase();
        pass = password.getText().toString().trim();
        sName = name.getText().toString().trim();

        if(email.isEmpty()){
            mailId.setError("Please enter your Email ID");
            mailId.requestFocus();
            return false;
        }

        if(sName.isEmpty()){
            name.setError("Please enter your name");
            name.requestFocus();
            return false;
        }

        if ( admissionNo.getText().toString().isEmpty() )
        {
            admissionNo.setError("Please enter your admission No");
            admissionNo.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mailId.setError("Please enter a valid Email ID");
            mailId.requestFocus();
            return false;
        }

        if(!email.contains(".iitism.ac.in")){
            mailId.setError("Please enter the institute's Email ID");
            mailId.requestFocus();
            return false;
        }

        if(pass.isEmpty()){
            password.setError("Please set a password");
            password.requestFocus();
            return false;
        }

        if(pass.length()<6){
            password.setError("Password should be of atleast 6 characters.");
            password.requestFocus();
            return false;
        }

        if(spinner.getSelectedItem() == null || spinner.getSelectedItem().toString().equals("Select Hostel")){
            TextView errorText = (TextView)spinner.getSelectedView();
            errorText.requestFocus();
            errorText.setError("");
            errorText.setText("Hostel is Required");
            return false;
        } else
            hostel = spinner.getSelectedItem().toString();
            return true ;
    }


    private void uploadToDatabase(String email, String studentName) {
        StudentsInfo studentData = new StudentsInfo();
        studentData.setEmail(email);
        studentData.setHostel(hostel);
        studentData.setName(studentName);
        studentData.setAdmNo(admissionNo.getText().toString());
        studentInfoDatabase.push().setValue(studentData);
        Intent intent = new Intent(SignUpActivity.this, StudentMainActvity.class);
        startActivity(intent);
        finish();
        progressDialog.cancel();
    }

    private void createUser ()
    {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success
                            Toast.makeText(SignUpActivity.this,"Sign Up Successful",Toast.LENGTH_SHORT).show();
                            uploadToDatabase(email , sName);
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this,"Error occurred, try Sign In.",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.goToLogin:
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.delete();
                }
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
                break;

            case R.id.button_signup:

                progressDialog.show();
                if ( checkEmpty() )
                {
                    createUser();
                }
                break;
        }
    }
}