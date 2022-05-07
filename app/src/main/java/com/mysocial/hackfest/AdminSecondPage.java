package com.mysocial.hackfest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appitup.AdminSignIn.AdminLogin;

public class AdminSecondPage extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_second_page);

        ImageButton btn_compl = (ImageButton) findViewById(R.id.btn_compl);
        ImageButton btn_health = (ImageButton) findViewById(R.id.health_care);
        ImageButton btn_notice = (ImageButton) findViewById(R.id.btn_notice);
        btn_compl.setOnClickListener(this);
        btn_notice.setOnClickListener(this);
        btn_health.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_compl:
                startActivity(new Intent(AdminSecondPage.this, AdminLogin.class));
                break;
            case R.id.btn_notice:
                Intent intent = new Intent(AdminSecondPage.this,LoginActivity.class);
                intent.putExtra("Accesser Name","notice");
                startActivity(intent);
                break;
            case R.id.health_care:
                Intent intent3 = new Intent(AdminSecondPage.this, LoginActivity.class);
                intent3.putExtra("Accesser Name", "health");
                startActivity(intent3);
                break;
        }
    }
}