package com.mysocial.hackfest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.appitup.eventCategory.FragmentEvent;
import com.example.appitup.studentFragment.FragmentUpdates;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NoticeActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    Class fra = null;
    Fragment frag = null;
    private NavigationView nvDrawer;
    private FirebaseAuth mAuth;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("isLogged", 5);
        myEdit.commit();
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_hamburger);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        mAuth = FirebaseAuth.getInstance();
        String myStrValue = "";
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            myStrValue = "Notice Manager";
        } else {
            myStrValue = "Someone@gmail.com";
        }
        View header = nvDrawer.getHeaderView(0);
        TextView tv = header.findViewById(R.id.name);
        tv.setText(myStrValue);
        ImageView iv = header.findViewById(R.id.im_nav_header);
        iv.setImageResource(R.drawable.iit_ism_logo);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, new FragmentUpdates(NoticeActivity.this, "notice"))
                .commit();
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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, new FragmentUpdates(NoticeActivity.this, "notice"))
                .commit();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        int k = 0;
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.All_notice:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentUpdates(NoticeActivity.this, "notice"))
                        .commit();
                k = 1;
                break;
            case R.id.club_updates:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentUpdates(NoticeActivity.this, "club"))
                        .commit();
                break;
            case R.id.AddNotice:
                Intent i = new Intent(this, com.example.appitup.AddNoticeActivity.class);
                startActivity(i);
                k = 1;
                break;
            case R.id.EventUpdates:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentEvent(getSupportFragmentManager(), NoticeActivity.this))
                        .commit();
                break;
            case R.id.MessRecords:
                Intent intent = new Intent(this, com.example.appitup.MessLoginSelecterActivity.class);
                intent.putExtra("isLogin", 2);
                startActivity(intent);
                break;

            case R.id.LogOut:
                SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putInt("isLogged", 0);
                myEdit.commit();
                Intent igh = new Intent(NoticeActivity.this, com.example.appitup.FirstPage.class);
                startActivity(igh);
                finish();
                // Logout here
                k = 0;
                break;
            case R.id.Open_Maps:
                Intent t = new Intent(this, com.example.appitup.MapActivity.class);
                startActivity(t);
                k = 1;
                break;
            case R.id.ForgotPassword:
                //Go for forgot password
                Intent intent1 = new Intent(NoticeActivity.this, com.example.appitup.ChangePasswordActivity.class);
                intent1.putExtra("User", "notice");
                startActivity(intent1);
                k = 0;
                break;

            default:
                k = 1;
        }

        mDrawer.closeDrawers();
    }
}