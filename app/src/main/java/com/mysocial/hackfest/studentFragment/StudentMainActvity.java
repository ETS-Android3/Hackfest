package com.mysocial.hackfest.studentFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mysocial.hackfest.ClubInfoActivity;
import com.mysocial.hackfest.CurrentMessMenuActivity;
import com.mysocial.hackfest.EditProfileActivity;
import com.mysocial.hackfest.HealthActivity;
import com.mysocial.hackfest.ImageViewActivity;
import com.mysocial.hackfest.MapActivity;
import com.mysocial.hackfest.R;
import com.mysocial.hackfest.SplashScreenActivity;
import com.mysocial.hackfest.ViewpdfActivity;
import com.mysocial.hackfest.classes.StudentsInfo;
import com.mysocial.hackfest.eventCategory.FragmentEvent;
import com.mysocial.hackfest.faceofcampus.TakeImageActivity;
import com.mysocial.hackfest.library.LibraryCountDisplay;
import com.mysocial.hackfest.lostNFound.LostNFoundActivity;

import org.jetbrains.annotations.NotNull;

public class StudentMainActvity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private String Email,timeTableUrl;
    private ProgressDialog progressDialog;
    private String year,branch,section,hostel;
    private ValueEventListener vel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_zone);
        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("isLogged",2);
        myEdit.commit();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Student Info");
        Email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        getStudentDetails(Email);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching your profile ...");
        progressDialog.show();
        Log.d("Year ",year+section);
        getTimeTableUrl();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.parseColor("#FFFFFF"));

        // setting default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FragmentUpdates(StudentMainActvity.this, "notice"))
                .commit();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.public_timeline:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentPublicTimeLine(getSupportFragmentManager(), StudentMainActvity.this))
                                .commit();
                        break;

                    case R.id.add_a_complaint:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentAddAComplaint(StudentMainActvity.this))
                                .commit();
                        break;
                    case R.id.my_complaints:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentMyComplaints(getSupportFragmentManager(), StudentMainActvity.this))
                                .commit();
                        break;

                    case R.id.club_updates:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentUpdates(StudentMainActvity.this, "club"))
                                .commit();
                        break;
                    case R.id.event_updates:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentEvent(getSupportFragmentManager(),StudentMainActvity.this))
                                .commit();
                        break;

                    case R.id.colledge_noticeboard:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new FragmentUpdates(StudentMainActvity.this, "notice"))
                                .commit();
                        break;
                    case R.id.mess_scanner:
                        Intent intent = new Intent(StudentMainActvity.this, QRCodeScanner.class);
                        intent.putExtra("Mess",hostel);
                        startActivity(intent);
                        break;
                    case R.id.club_info:
                        Intent intnt = new Intent(StudentMainActvity.this, ClubInfoActivity.class);
                        startActivity(intnt);
                        break;
                    case R.id.colledge_maps:
                        Intent intent1 = new Intent(StudentMainActvity.this, MapActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.mess_daily_schedule:
                        Intent intent2 = new Intent(StudentMainActvity.this, CurrentMessMenuActivity.class);
                        intent2.putExtra("Hostel",hostel);
                        startActivity(intent2);
                        break;
                    case R.id.healthExperts:
                        Intent intent23 = new Intent(StudentMainActvity.this, HealthActivity.class);
                        startActivity(intent23);
                        break;
                    case R.id.mess_weeklly_schedule:
                        String url="";
                        switch (hostel){
                            case "JASPER":
                                url="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/PDF%2F1622974568826.pdf?alt=media&token=10e6a7e0-5c1d-47c3-a22d-3f00ca7b3b3e";
                                break;
                            case "AMBER":
                                url="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/PDF%2F1622974568826.pdf?alt=media&token=10e6a7e0-5c1d-47c3-a22d-3f00ca7b3b3e";
                                break;
                            case "RUBY":
                                url="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/PDF%2F1622974568826.pdf?alt=media&token=10e6a7e0-5c1d-47c3-a22d-3f00ca7b3b3e";
                                break;
                            case "ROSALINE":
                                url="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/PDF%2F1622974568826.pdf?alt=media&token=10e6a7e0-5c1d-47c3-a22d-3f00ca7b3b3e";
                                break;
                            case "EMARALD":
                                url="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/PDF%2F1622974568826.pdf?alt=media&token=10e6a7e0-5c1d-47c3-a22d-3f00ca7b3b3e";
                                break;
                            case "DIAMOND":
                                url="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/PDF%2F1622974568826.pdf?alt=media&token=10e6a7e0-5c1d-47c3-a22d-3f00ca7b3b3e";
                                break;
                            case "OPAL":
                                url="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/PDF%2F1622974568826.pdf?alt=media&token=10e6a7e0-5c1d-47c3-a22d-3f00ca7b3b3e";
                                break;
                            case "TOPAZ":
                                url="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/PDF%2F1622974568826.pdf?alt=media&token=10e6a7e0-5c1d-47c3-a22d-3f00ca7b3b3e";
                                break;
                            case "SAPPHIRE":
                                url="https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/PDF%2F1622974568826.pdf?alt=media&token=10e6a7e0-5c1d-47c3-a22d-3f00ca7b3b3e";
                                break;
                        }
                        Intent in = new Intent(StudentMainActvity.this, ViewpdfActivity.class);
                        in.putExtra("url", url);
                        startActivity(in);
                        break;
                    case R.id.time_table:
                        Intent iny = new Intent(StudentMainActvity.this, ImageViewActivity.class);
                        iny.putExtra("Url",timeTableUrl);
                        startActivity(iny);
                        break;

                    case R.id.lost_n_found:

                        startActivity(new Intent(StudentMainActvity.this , LostNFoundActivity.class));

                        break;

                    case R.id.college_faces:

                        startActivity(new Intent(StudentMainActvity.this , TakeImageActivity.class));

                        break;

                    case R.id.library_capacity:

                        startActivity(new Intent(StudentMainActvity.this , LibraryCountDisplay.class));

                        break;

                    case R.id.ForgotPassword:
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        String crntemail = mAuth.getCurrentUser().getEmail();
                        mAuth.sendPasswordResetEmail(crntemail)
                                .addOnCompleteListener(StudentMainActvity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(StudentMainActvity.this, "Password reset mail sent.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(StudentMainActvity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        break;
                    case R.id.log_out:

                        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putInt("isLogged",0);
                        myEdit.commit();
                        Intent i = new Intent(StudentMainActvity.this, SplashScreenActivity.class);
                        startActivity(i);
                        finish();

                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                StudentsInfo studentsInfo = snapshot.getValue(StudentsInfo.class);
                if (studentsInfo.getEmail().equalsIgnoreCase(Email)) {

                    // accessing the navigation view header

                    View header = navigationView.getHeaderView(0);
                    TextView name = header.findViewById(R.id.name);
                    name.setText(studentsInfo.getName());
                    TextView roll_No = header.findViewById(R.id.roll_No);
                    roll_No.setText(studentsInfo.getAdmNo());
                    hostel=studentsInfo.getHostel();
                    ImageView imageView = header.findViewById(R.id.im_nav_header);

                    if (!studentsInfo.getProfilePicUri().equalsIgnoreCase("")) {
                        Glide.with(imageView)
                                .asBitmap()
                                .load(studentsInfo.getProfilePicUri())
                                .circleCrop()
                                .into(imageView);
                    }
                    TextView edit_profile = header.findViewById(R.id.edit_profile);
                    edit_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(StudentMainActvity.this, EditProfileActivity.class);
                            startActivity(i);
                            finish();
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
        SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("isLogged", 2);
        prefEditor.commit();
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }
    void getStudentDetails(String email){
        String yr=email.substring(0,2);
        String roll=email.substring(4,8);
        int u=email.indexOf(".");
        String bnc=email.substring(9,u);
        Log.d("Detail",yr+" "+roll+" "+bnc);
        if(yr.equalsIgnoreCase("20"))year="First";
        else if(yr.equalsIgnoreCase("19"))year="Second";
        else if(yr.equalsIgnoreCase("18"))year="Third";
        else  year="Fourth";
        branch=bnc;
        int rn=Integer.valueOf(roll);
        if(rn<=282)section="AB";
        else if(rn<=564)section="CD";
        else if(rn<=846)section="EF";
        else section="GH";
    }
    String getTimeTableUrl(){
        String timeTable="";
        if(year.equalsIgnoreCase("First")){
        timeTable=compare();
        }
        else{
            branch="First";
            section="AB";
            timeTable=compare();
        }
        return  timeTable;
    }
    String compare(){
        final String[] res = {""};
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("TimeTable");
        vel = reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    String key = child.getKey();
                    if(key.equals(year+section)){
                        res[0] = (String) child.getValue();
                        timeTableUrl=res[0];
                        progressDialog.cancel();
                        Log.d("Image Url",res[0]);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        return  res[0];
    }


}