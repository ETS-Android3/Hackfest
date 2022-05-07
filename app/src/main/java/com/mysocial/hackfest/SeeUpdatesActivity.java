package com.mysocial.hackfest;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.mysocial.hackfest.classes.Updates;

public class SeeUpdatesActivity extends AppCompatActivity {

    private TextView tv_fb_link,tv_insta_link,tv_web_link,tv_other_link,tv_content,tv_title;
    private String imgUrl,pdfUrl;
    private ImageView image_view,pdf_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_updates);
        Updates updt=(Updates)getIntent().getSerializableExtra("object");
        image_view=findViewById(R.id.image_view);
        pdf_view=findViewById(R.id.pdf_view);
        tv_content=findViewById(R.id.message);
        tv_title=findViewById(R.id.title);
        imgUrl=updt.getImageUrl();
        pdfUrl=updt.getPdfUrl();
        if(updt.getClubName().equalsIgnoreCase("Notice")){
            CardView cv=findViewById(R.id.cv_collaspable);
            cv.setVisibility(GONE);
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();
        if(!updt.getImageUrl().equalsIgnoreCase("")){
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(updt.getImageUrl())
                    .into(image_view);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    uploadData(updt);
                }
            }, 2000 );
        }
        else{
            uploadData(updt);
        }
        if(updt.getPdfUrl().equalsIgnoreCase("")){
            pdf_view.setVisibility(GONE);
            progressDialog.cancel();
        }
        else{
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(updt.getPdfUrl())
                    .into(pdf_view);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000 );
        }

    }

    public void iv_clicked(View view) {
        if(!imgUrl.equalsIgnoreCase("")) {
            Intent i = new Intent(this, ImageViewActivity.class);
            i.putExtra("Url", imgUrl);
            startActivity(i);
        }
        else{
            image_view.setClickable(false);
        }
    }

    public void pdf_clicked(View view) {
        if(!pdfUrl.equalsIgnoreCase("")) {
            Intent i = new Intent(this, ImageViewActivity.class);
            i.putExtra("Url", pdfUrl);
            startActivity(i);
        }
        else{
            image_view.setClickable(false);
        }
    }
    void uploadData(Updates updt){
        if(updt.getClubName().equalsIgnoreCase("Notice")){
         tv_content.setText(updt.getContent());
         tv_title.setText(updt.getTitle());
        }
        else{
            tv_fb_link=findViewById(R.id.fb_link);
            tv_insta_link=findViewById(R.id.insta_link);
            tv_web_link=findViewById(R.id.web_link);
            tv_other_link=findViewById(R.id.web_link);
            tv_content.setText(updt.getContent());
            tv_title.setText(updt.getTitle());
            tv_other_link.setText(updt.getOtherLink());
            tv_fb_link.setText(updt.getFbLink());
            tv_insta_link.setText(updt.getInstaLink());
            tv_web_link.setText(updt.getWebLink());

        }
    }
}