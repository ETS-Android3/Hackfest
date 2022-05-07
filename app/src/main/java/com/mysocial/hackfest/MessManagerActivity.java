package com.mysocial.hackfest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.appitup.eventCategory.FragmentEvent;
import com.example.appitup.studentFragment.FragmentUpdates;
import com.example.appitup.studentFragment.MessScannerDisplay;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MessManagerActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private String mess,messName;
    Class fra = null;
    Fragment frag = null;
    private NavigationView nvDrawer;
    private FirebaseAuth mAuth;
    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_manager);
        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("isLogged",3);
        myEdit.commit();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_hamburger);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        mAuth = FirebaseAuth.getInstance();
        String myStrValue="Mess Manager";
        View header = nvDrawer.getHeaderView(0);
        TextView tv=header.findViewById(R.id.name);
        tv.setText(myStrValue);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, new FragmentUpdates(this,"notice"))
                .commit();
        Intent intent = getIntent();
        mess = intent.getStringExtra("Mess");
        if(mess.equalsIgnoreCase("null")){
            SharedPreferences sh = getSharedPreferences("MessName", MODE_PRIVATE);
            messName = sh.getString("isMess", "JASPER");
        }
        else{
            messName=mess;
            SharedPreferences srh = getSharedPreferences("MessName",MODE_PRIVATE);
            SharedPreferences.Editor edt = srh.edit();
            edt.putString("isMess",messName);
            edt.commit();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    public void selectDrawerItem(MenuItem menuItem) {
        int k=0;
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.UpdateMessRecords:
                Intent i=new Intent(this, com.example.appitup.UpdateMessMenuActivity.class);
                i.putExtra("Hostel",messName);
                startActivity(i);
                k=1;
                break;
            case R.id.club_updates:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentUpdates(MessManagerActivity.this,"club"))
                        .commit();
                break;
            case R.id.CurrentMessRecords:
                Intent l=new Intent(this, com.example.appitup.CurrentMessMenuActivity.class);
                l.putExtra("Hostel",messName);
                startActivity(l);
                k=1;
                break;
            case R.id.EventUpdates:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentEvent(getSupportFragmentManager(), MessManagerActivity.this))
                        .commit();
                break;
            case R.id.All_notice:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentUpdates(MessManagerActivity.this,"notice"))
                        .commit();
                break;
            case R.id.MessRecords:
                Intent intent1 = new Intent(this , MessScannerDisplay.class);
                intent1.putExtra("Mess",messName);
                startActivity(intent1);
                break;
            case R.id.LogOut:
                SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putInt("isLogged",0);
                myEdit.commit();
                Intent iu=new Intent(MessManagerActivity.this, com.example.appitup.FirstPage.class);
                startActivity(iu);
                finish();
                k=0;
                break;
            case R.id.Open_Maps:// Piyush chutiya hai
                Intent t=new Intent(this, com.example.appitup.MapActivity.class);
                startActivity(t);
                k=1;
                break;
            case R.id.UpdateFullMenu:
                Intent qw=new Intent(this, com.example.appitup.MasterMenuUpdaterActivity.class);
                qw.putExtra("Mess",messName);
                startActivity(qw);
                k=1;
                break;
            case R.id.qrCode:
                Intent qww=new Intent(this,ImageViewActivity.class);
                qww.putExtra("Url","https://firebasestorage.googleapis.com/v0/b/appitup-60933.appspot.com/o/Complaint_Photos%2Fqr.png?alt=media&token=7e842054-5c9d-455e-9865-a1034c7f4c6e");
                startActivity(qww);
                k=1;
                break;
            case R.id.ForgotPassword:
                //Go for forgot password
                Intent intent = new Intent(MessManagerActivity.this, com.example.appitup.ChangePasswordActivity.class);
                intent.putExtra("User","mess");
                startActivity(intent);
                k=0;
                break;

            default:
                k=1;

        }

        mDrawer.closeDrawers();
    }
}