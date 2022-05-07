package com.mysocial.hackfest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mysocial.hackfest.eventCategory.FragmentEvent;
import com.mysocial.hackfest.studentFragment.FragmentUpdates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OuterEventUpdatesActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    Class fra = null;
    Fragment frag = null;
    private Calendar calendar;
    private SimpleDateFormat simpledateformat;
    long tm;
    private String Date,club;
    private NavigationView nvDrawer;
    private FirebaseAuth mAuth;
    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("isLogged",4);
        myEdit.commit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outer_event_updates);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_hamburger);
        getSupportActionBar().setTitle("");
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        mAuth = FirebaseAuth.getInstance();
        String myStrValue="";
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            myStrValue=user.getEmail().toString();
        }
        else{
            myStrValue="Piyush_Pyasa_Rocks@gmail.com";
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, new FragmentUpdates(OuterEventUpdatesActivity.this,"notice"))
                .commit();
        club="IIT";
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
            case R.id.All_updates:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentEvent(getSupportFragmentManager(), OuterEventUpdatesActivity.this))
                        .commit();
                k=1;
                break;
            case R.id.ClubUpdates:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentUpdates(OuterEventUpdatesActivity.this,"club"))
                        .commit();
                break;
            case R.id.Notice:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentUpdates(OuterEventUpdatesActivity.this,"notice"))
                        .commit();
                break;
            case R.id.Open_Maps:
                Intent t=new Intent(this,MapActivity.class);
                startActivity(t);
                k=1;
                break;
            case R.id.AddUpdate:
                Intent i=new Intent(this, AddNoticeActivity.class);
                startActivity(i);
                k=1;
                break;
            case R.id.LogOut:
                SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putInt("isLogged",0);
                myEdit.commit();
                Intent igh=new Intent(OuterEventUpdatesActivity.this,SplashScreenActivity.class);
                startActivity(igh);
                finish();
                k=0;
                break;
            case R.id.ForgotPassword:
                //Go for forgot password
                Intent intent = new Intent(OuterEventUpdatesActivity.this,ChangePasswordActivity.class);
                intent.putExtra("User","event");
                startActivity(intent);
                k=0;
                break;
            default:
                k=1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentUpdates(OuterEventUpdatesActivity.this,"notice"))
                        .commit();
        }
        mDrawer.closeDrawers();
    }private long getTime() throws Exception {
        String url = "https://time.is/Unix_time_now";
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
        String[] tags = new String[] {
                "div[id=time_section]",
                "div[id=clock0_bg]"
        };
        Elements elements= doc.select(tags[0]);
        for (int i = 0; i <tags.length; i++) {
            elements = elements.select(tags[i]);
        }
        return Long.parseLong(elements.text());
    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        return dateFormat.format(cal.getTime());
    }

}
