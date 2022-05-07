package com.mysocial.hackfest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageViewActivity extends AppCompatActivity {
    String url="";
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        iv=findViewById(R.id.inflated_iv);
        Intent intent = getIntent();
        url=intent.getExtras().get("Url").toString();
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(url)
                .into(iv);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(iv);
        pAttacher.update();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 2000 );
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}