package com.mysocial.hackfest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appitup.studentFragment.MessScannerDisplay;

public class MessLoginSelecterActivity extends AppCompatActivity implements View.OnClickListener {
    private String mess;
    private int isLogin;// 0 for using login activity 1 for using going to mess Records

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_login_selecter);

        Intent in = getIntent();
        isLogin = in.getIntExtra("isLogin", 5);
        findViewById(R.id.jasper).setOnClickListener(this);
        findViewById(R.id.Emarald).setOnClickListener(this);
        findViewById(R.id.diamond).setOnClickListener(this);
        findViewById(R.id.Topaz).setOnClickListener(this);
        findViewById(R.id.Rosaline).setOnClickListener(this);
        findViewById(R.id.Ruby).setOnClickListener(this);
//        findViewById(R.id.Sapphire).setOnClickListener(this);
        findViewById(R.id.amber).setOnClickListener(this);
        findViewById(R.id.Opal).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String mess = "";
        switch (v.getId()) {
            case R.id.jasper:
                mess = "JASPER";
                break;
            case R.id.amber:
                mess = "AMBER";
                break;
            case R.id.diamond:
                mess = "DIAMOND";
                break;
            case R.id.Emarald:
                mess = "EMARALD";
                break;
            case R.id.Opal:
                mess = "OPAL";
                break;
            case R.id.Topaz:
                mess = "TOPAZ";
                break;
            case R.id.Rosaline:
                mess = "ROSALINE";
                break;
            case R.id.Ruby:
                mess = "RUBY";
                break;
//            case R.id.Sapphire:
//                mess = "SAPPHIRE";
//                break;
        }
        if (isLogin == 0) {
            Intent intent = new Intent(MessLoginSelecterActivity.this, LoginActivity.class);
            intent.putExtra("Mess", mess);
            intent.putExtra("Accesser Name", "mess");
            startActivity(intent);
        }
        else {
            Intent inte = new Intent(MessLoginSelecterActivity.this, MessScannerDisplay.class);
            inte.putExtra("Mess", mess);
            startActivity(inte);
        }
    }
}