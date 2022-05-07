package com.mysocial.hackfest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mysocial.hackfest.studentFragment.FragmentUpdates;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateAdministratorActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private static final String SERVER_NAME = "time.nist.gov";
    private volatile TimeInfo timeInfo;
    private volatile Long offset;
    private String spClubName="";
    String time;
    Class fra = null;
    Fragment frag = null;
    private Calendar calendar;
    private SimpleDateFormat simpledateformat;
    long tm;
    private String ClubName,club;
    private NavigationView nvDrawer;
    private FirebaseAuth mAuth;
    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_administrator);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("isLogged",6);
        myEdit.commit();
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_hamburger);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        Intent intent = getIntent();
        club = intent.getStringExtra("Club");
        if(club.equalsIgnoreCase("null")){
            SharedPreferences sh = getSharedPreferences("ClubName", MODE_PRIVATE);
            ClubName = sh.getString("isClub", "LCI");
        }
        else {
            switch (club) {
                case "lci":
                    ClubName = "LCI";
                    break;
                case "bookclub":
                    ClubName = "Book Club";
                    break;
                case "chayanikasangh":
                    ClubName = "Chayanika Sangh";
                    break;
                case "litc":
                    ClubName = "LITC";
                    break;
                case "comedyclub":
                    ClubName = "Comedy Club";
                    break;
                case "micdrop":
                    ClubName = "Mic Drop";
                    break;
                case "cyberlabs":
                    ClubName = "Cyber Labs";
                    break;
                case "quizclub":
                    ClubName = "Quiz Club";
                    break;
                case "mailerdaemon":
                    ClubName = "Mailer Daemon";
                    break;
                case "litm":
                    ClubName = "LITM";
                    break;
                case "roboism":
                    ClubName = "RoboIsm";
                    break;
                case "wtc":
                    ClubName = "WTC";
                    break;
                case "tt":
                    ClubName = "Table Tennis Club";
                    break;
                case "badminton":
                    ClubName = "Badminton Club";
                    break;
                case "boxing":
                    ClubName = "Boxing Club";
                    break;
                case "hockey":
                    ClubName = "Hockey Club";
                    break;
                case "cricket":
                    ClubName = "Cricket Club";
                    break;
            }
            SharedPreferences srh = getSharedPreferences("ClubName",MODE_PRIVATE);
            SharedPreferences.Editor edt = srh.edit();
            edt.putString("isClub",ClubName);
            edt.commit();
        }
        String myStrValue="";
        FirebaseUser user = mAuth.getCurrentUser();
        myStrValue=ClubName;
        View header = nvDrawer.getHeaderView(0);
        TextView tv=header.findViewById(R.id.name);
        tv.setText(myStrValue);
        ImageView imageView=header.findViewById(R.id.im_nav_header);
        switch (ClubName) {
            case "LCI":
                imageView.setImageResource(R.drawable.lci);
                break;
            case "Book Club":
                imageView.setImageResource(R.drawable.bookclub);
                break;
            case "Chayanika Sangh":
                imageView.setImageResource(R.drawable.chayanikasangh);
                break;
            case "LITC":
                imageView.setImageResource(R.drawable.litc);
                break;
            case "Comedy Club":
                imageView.setImageResource(R.drawable.comedyclub);
                break;
            case "Mic Drop":
                imageView.setImageResource(R.drawable.micdrop);
                break;
            case "Cyber Labs":
                imageView.setImageResource(R.drawable.cyberlabs);
                break;
            case "Quiz Club":
                imageView.setImageResource(R.drawable.quizclub);
                break;
            case "Mailer Daemon":
                imageView.setImageResource(R.drawable.mailerdaemon);
                break;
            case "LITM":
                imageView.setImageResource(R.drawable.litm);
                break;
            case "RoboIsm":
                imageView.setImageResource(R.drawable.roboism);
                break;
            case "WTC":
                imageView.setImageResource(R.drawable.wtc);
                break;
            case "Table Tennis Club":
                imageView.setImageResource(R.drawable.tt);
                break;
            case "Badminton Club":
                imageView.setImageResource(R.drawable.badminton);
                break;
            case "Boxing Club":
                imageView.setImageResource(R.drawable.boxing);
                break;
            case "Hockey Club":
                imageView.setImageResource(R.drawable.hockey);
                break;
            case "Cricket Club":
                imageView.setImageResource(R.drawable.cricket);
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, new FragmentUpdates(UpdateAdministratorActivity.this,"notice"))
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
                        .replace(R.id.flContent, new FragmentUpdates(UpdateAdministratorActivity.this,"club"))
                        .commit();
                k=1;
                break;
            case R.id.EventUpdates:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentUpdates(UpdateAdministratorActivity.this,"event"))
                        .commit();
                break;
            case R.id.Notice:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, new FragmentUpdates(UpdateAdministratorActivity.this,"notice"))
                        .commit();
                break;
            case R.id.Open_Maps:
                Intent t=new Intent(this, MapActivity.class);
                startActivity(t);
                k=1;
                break;
            case R.id.AddUpdate:
                Intent i=new Intent(this, AddUpdateActivity.class);
                i.putExtra("ClubName",ClubName);
                startActivity(i);
                k=1;
                break;
            case R.id.LogOut:
                SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putInt("isLogged",0);
                myEdit.commit();
                SharedPreferences sh = getSharedPreferences("ClubName",MODE_PRIVATE);
                SharedPreferences.Editor myEd = sharedPreferences.edit();
                myEd.putString("isClub","");
                myEd.commit();
                Intent iuyt=new Intent(UpdateAdministratorActivity.this, SplashScreenActivity.class);
                startActivity(iuyt);
                finish();
                break;
            case R.id.ForgotPassword:
                //Go for forgot password
                Intent intent = new Intent(UpdateAdministratorActivity.this, ChangePasswordActivity.class);
                intent.putExtra("User",club);
                startActivity(intent);
                k=0;
                break;
            default:
                k=1;
        }
        mDrawer.closeDrawers();
    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        return dateFormat.format(cal.getTime());
    }

// String getWebTime() throws IOException {
//     NTPUDPClient timeClient = new NTPUDPClient();
//     InetAddress inetAddress = InetAddress.getByName(SERVER_NAME);
//     TimeInfo timeInfo = timeClient.getTime(inetAddress);
//     long returnTime = timeInfo.getReturnTime();
//     Date time = new Date(returnTime);
//     String tm=time.toString();
//     Toast.makeText(this, "got_time"+tm, Toast.LENGTH_SHORT).show();
//     return  tm;
// }
    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            try {
                NTPUDPClient timeClient = new NTPUDPClient();
                InetAddress inetAddress = InetAddress.getByName(SERVER_NAME);
                TimeInfo timeInfo = timeClient.getTime(inetAddress);
                long returnTime = timeInfo.getReturnTime();
                Date time = new Date(returnTime);
                String tm=time.toString();
                result = tm;
            } catch (IOException e){
                e.printStackTrace();
                result = e.toString();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(UpdateAdministratorActivity.this, "Got time "+result, Toast.LENGTH_SHORT).show();
        }
    }
}
