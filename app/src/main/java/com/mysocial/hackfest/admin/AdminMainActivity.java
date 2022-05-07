package com.mysocial.hackfest.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mysocial.hackfest.R;
import com.mysocial.hackfest.SplashScreenActivity;
import com.mysocial.hackfest.classes.Data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("isLogged",1);
        myEdit.commit();
        mAuth=FirebaseAuth.getInstance();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.getDrawerArrowDrawable().setColor(Color.parseColor("#FFFFFF"));
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        String myStrValue = "";
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            myStrValue = user.getEmail().toString();
        } else {
            myStrValue = "someone63@gmail.com";
        }

        View header = navigationView.getHeaderView(0);
        TextView tv = header.findViewById(R.id.name);
        tv.setText(myStrValue);

        SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("isLogged", 1);
        prefEditor.commit();

        // setting default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FragmentAdminAllComplaints(getSupportFragmentManager(), AdminMainActivity.this, "All"))
                .commit();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        ArrayList<Data> tmpList = new ArrayList<Data>();

        switch (item.getItemId()) {
            case R.id.Resolved:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentAdminAllComplaints(getSupportFragmentManager(), AdminMainActivity.this, "Resolved"))
                        .commit();
                break;

            case R.id.Unseen:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentAdminAllComplaints(getSupportFragmentManager(), AdminMainActivity.this, "Unseen"))
                        .commit();
                break;
            case R.id.Current:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentAdminAllComplaints(getSupportFragmentManager(), AdminMainActivity.this, "Processing"))
                        .commit();
                break;
            case R.id.All:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentAdminAllComplaints(getSupportFragmentManager(), AdminMainActivity.this, "All"))
                        .commit();

                break;

            case R.id.club_updates:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentUpdates(AdminMainActivity.this, "club"))
                        .commit();
                break;

            case R.id.colledge_noticeboard:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentUpdates(AdminMainActivity.this, "notice"))
                        .commit();
                break;

            case R.id.mess_scanner:
                Intent intent = new Intent(this , MessLoginSelecterActivity.class);
                intent.putExtra("isLogin",2);
                startActivity(intent);
                break;

            case R.id.event_updates:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FragmentEvent(getSupportFragmentManager(),AdminMainActivity.this))
                        .commit();
                break;

            case R.id.colledge_maps:
                Intent intent1 = new Intent(AdminMainActivity.this, MapActivity.class);
                startActivity(intent1);
                break;

            case R.id.ForgotPassword:

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String crntemail = mAuth.getCurrentUser().getEmail();
                mAuth.sendPasswordResetEmail(crntemail)
                        .addOnCompleteListener(AdminMainActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminMainActivity.this, "Password reset mail sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AdminMainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.LogOut:

                SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putInt("isLogged",0);
                myEdit.commit();
                Intent i = new Intent(this, SplashScreenActivity.class);
                startActivity(i);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
       finishAffinity();
    }
}