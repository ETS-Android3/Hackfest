package com.mysocial.hackfest.studentFragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appitup.R;
import com.example.appitup.classes.MessScanCount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QRCodeScanner extends AppCompatActivity {

    private TextView textView;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private String date;
    private List<MessScanCount> messScanCounts = new ArrayList<>();
    private Boolean isPresent = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int hrs , min ;
    private String hostel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        getTime();

//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        date = simpleDateFormat.format(calendar.getTime());

        hostel = getIntent().getStringExtra("Mess");
       // hostel = "AMBER";
        sharedPreferences = getSharedPreferences("mess", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (!sharedPreferences.getString("date", "").equalsIgnoreCase(date)) {
            editor.putString("date", date);
            editor.putBoolean("breakfast", false);
            editor.putBoolean("lunch", false);
            editor.putBoolean("snacks", false);
            editor.putBoolean("dinner", false);
            editor.commit();
        }

        textView = findViewById(R.id.btnSignUp);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HOSTELS").child(hostel).child("Mess Records");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessScanCount messScanCount = snapshot.getValue(MessScanCount.class);
                messScanCounts.add(messScanCount);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                textView.setText("Scanned Failed");
            } else {
                databaseReference.removeEventListener(childEventListener);
                textView.setText("Scanned Successfully");
                checkList();
                mess_data();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void scan(View view) {
        if (hrs >= 7 && hrs <= 9) {
            if (hrs == 9 && min < 30 && !sharedPreferences.getBoolean("breakfast", true)) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
            } else if (hrs == 9){
                Toast.makeText(this, "You can scan only once during mess hours . Please try again in next mess hours ", Toast.LENGTH_LONG).show();
            }
            if (hrs != 9 && !sharedPreferences.getBoolean("breakfast", true)) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
            } else if ( hrs != 9 ){
                Toast.makeText(this, "You can scan only once during mess hours . Please try again in next mess hours ", Toast.LENGTH_LONG).show();
            }
        } else if (hrs >= 12 && hrs <= 14 && !sharedPreferences.getBoolean("lunch", true)) {
            if (hrs == 14 && min < 30) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
            } else if ( hrs == 14 ){
                Toast.makeText(this, "You can scan only once during mess hours . Please try again in next mess hours ", Toast.LENGTH_LONG).show();
            }
            if (hrs != 14 && !sharedPreferences.getBoolean("lunch", true)) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
            } else if (hrs != 14){
                Toast.makeText(this, "You can scan only once during mess hours . Please try again in next mess hours ", Toast.LENGTH_LONG).show();
            }
        } else if (hrs >= 17 && hrs <= 18 && !sharedPreferences.getBoolean("snacks", true)) {
            if (hrs == 18 && min < 30) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
            } else if ( hrs ==18 ){
                Toast.makeText(this, "You can scan only once during mess hours . Please try again in next mess hours ", Toast.LENGTH_LONG).show();
            }
            if (hrs != 18 && !sharedPreferences.getBoolean("breakfast", true)) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
            } else if ( hrs != 18){
                Toast.makeText(this, "You can scan only once during mess hours . Please try again in next mess hours ", Toast.LENGTH_LONG).show();
            }
        } else if (hrs >= 20 && hrs <= 22 && !sharedPreferences.getBoolean("dinner", true)) {

            if (hrs == 22 && min < 1 && !sharedPreferences.getBoolean("dinner", true)) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
            } else if ( hrs ==22){
                Toast.makeText(this, "You can scan only once during mess hours . Please try again in next mess hours ", Toast.LENGTH_LONG).show();
            }
            if (hrs != 22 && !sharedPreferences.getBoolean("dinner", true)) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.initiateScan();
            } else if (hrs != 22){
                Toast.makeText(this, "You can scan only once during mess hours . Please try again in next mess hours ", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "You can scan only once during mess hours . Please try again in next mess hours ", Toast.LENGTH_LONG).show();
        }

    }

    public void checkList() {
        for (int i = 0; i < messScanCounts.size(); i++) {
            if (messScanCounts.get(i).getDate().equalsIgnoreCase(date)) {
                isPresent = true;
            }
        }
    }

    public void mess_data() {
        if (hrs >= 7 && hrs <= 9) {
            if (hrs == 9 && min < 30) {
                if (isPresent) {
                    UpdateData("breakfast");
                    editor.putBoolean("breakfast", true);
                    editor.commit();
                } else {
                    addData("breakfast");
                    editor.putBoolean("breakfast", true);
                    editor.commit();
                }
            } else if (hrs != 9) {
                if (isPresent) {
                    UpdateData("breakfast");
                    editor.putBoolean("breakfast", true);
                    editor.commit();
                } else {
                    addData("breakfast");
                    editor.putBoolean("breakfast", true);
                    editor.commit();
                }
            }
        }
        if (hrs >= 12 && hrs <= 14) {
            if (hrs == 14 && min < 30) {
                if (isPresent) {
                    UpdateData("lunch");
                    editor.putBoolean("lunch", true);
                    editor.commit();
                } else {
                    addData("lunch");
                    editor.putBoolean("lunch", true);
                    editor.commit();
                }
            } else if (hrs != 14) {
                if (isPresent) {
                    UpdateData("lunch");
                    editor.putBoolean("lunch", true);
                    editor.commit();
                } else {
                    addData("lunch");
                    editor.putBoolean("lunch", true);
                    editor.commit();
                }
            }
        }

        if (hrs >= 17 && hrs <= 18) {
            if (hrs == 18 && min < 30) {
                if (isPresent) {
                    UpdateData("snacks");
                    editor.putBoolean("snacks", true);
                    editor.commit();
                } else {
                    addData("snacks");
                    editor.putBoolean("snacks", true);
                    editor.commit();
                }
            } else if (hrs != 18) {
                if (isPresent) {
                    UpdateData("snacks");
                    editor.putBoolean("snacks", true);
                    editor.commit();
                } else {
                    addData("snacks");
                    editor.putBoolean("snacks", true);
                    editor.commit();
                }
            }
        }
        if (hrs >= 20 && hrs <= 22) {

            if (hrs == 22 && min < 1) {
                if (isPresent) {
                    UpdateData("dinner");
                    editor.putBoolean("dinner", true);
                    editor.commit();
                } else {
                    addData("dinner");
                    editor.putBoolean("dinner", true);
                    editor.commit();
                }
            } else if (hrs != 22) {
                if (isPresent) {
                    UpdateData("dinner");
                    editor.putBoolean("dinner", true);
                    editor.commit();
                } else {
                    addData("dinner");
                    editor.putBoolean("dinner", true);
                    editor.commit();
                }
            }
        }

    }

    public void UpdateData(String s) {

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessScanCount messScanCount = snapshot.getValue(MessScanCount.class);
                if (messScanCount.getDate().equalsIgnoreCase(date)) {
                    String key = snapshot.getKey();
                    switch (s) {
                        case "breakfast":
                            databaseReference.child(key).child("breakfast").setValue(messScanCount.getBreakfast() + 1);
                            break;
                        case "lunch":
                            databaseReference.child(key).child("lunch").setValue(messScanCount.getLunch() + 1);
                            break;
                        case "snacks":
                            databaseReference.child(key).child("snacks").setValue(messScanCount.getSnacks() + 1);
                            break;
                        case "dinner":
                            databaseReference.child(key).child("dinner").setValue(messScanCount.getDinner() + 1);
                            break;
                    }
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
    }

    public void addData(String s) {
        MessScanCount messScanCount = new MessScanCount(0, 0, 0, 0, date);
        switch (s) {
            case "breakfast":
                messScanCount.setBreakfast(messScanCount.getBreakfast() + 1);
                databaseReference.push().setValue(messScanCount);
                break;
            case "lunch":
                messScanCount.setLunch(messScanCount.getLunch() + 1);
                databaseReference.push().setValue(messScanCount);
                break;
            case "snacks":
                messScanCount.setSnacks(messScanCount.getSnacks() + 1);
                databaseReference.push().setValue(messScanCount);
                break;
            case "dinner":
                messScanCount.setDinner(messScanCount.getDinner() + 1);
                databaseReference.push().setValue(messScanCount);
                break;
        }
    }

    public void Back(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getTime() {
        String url = "http://worldtimeapi.org/api/timezone/Asia/Kolkata";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("hhhhhhhhh", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String DateTime = jsonObject.get("datetime").toString();

                    String str[] = DateTime.split("T", 2);
                    date = str[0];

                    String str2[] = str[1].split(":", 3);
                    hrs = Integer.parseInt(str2[0]);
                    min = Integer.parseInt(str2[1]);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("", "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh" + error);
                Toast.makeText(QRCodeScanner.this, "Please try after a minute", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

    }

}



