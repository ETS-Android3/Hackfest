package com.mysocial.hackfest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appitup.classes.Updates;

public class SingleEventUpdateDisplay extends AppCompatActivity {

    private TextView heading, content, fb_link, insta_link, web_link, other_link;
    private ImageView  img_view, pdf_image;
    private Updates updates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event_update_display);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        heading = findViewById(R.id.title);
        content = findViewById(R.id.message);
        img_view = findViewById(R.id.image_view);
        pdf_image = findViewById(R.id.pdf_view);
        fb_link = findViewById(R.id.fb_link);
        insta_link = findViewById(R.id.insta_link);
        web_link = findViewById(R.id.web_link);
        other_link = findViewById(R.id.other_link);

        updates = (Updates) getIntent().getSerializableExtra("object");

        heading.setText(updates.getTitle());
        content.setText(updates.getContent());

        if (!updates.getImageUrl().equalsIgnoreCase("Noting here")) {

            Glide.with(img_view)
                    .asBitmap()
                    .load(updates.getImageUrl())
                    .into(img_view);
        }


        fb_link.setText(updates.getFbLink());
        insta_link.setText(updates.getInstaLink());
        web_link.setText(updates.getWebLink());
        other_link.setText(updates.getOtherLink());

        progressDialog.cancel();
    }
    public  void open ( View view )
    {
        if (!updates.getPdfUrl().equalsIgnoreCase("")) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setMessage("What would you like to do ?")
                    .setNegativeButton("View", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SingleEventUpdateDisplay.this, ViewpdfActivity.class);
                            intent.putExtra("url", updates.getPdfUrl());
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updates.getPdfUrl()));
                            startActivity(intent);
                        }
                    });

            alert.show();
        }
        else{
            Toast.makeText(this, "No PDF Attached", Toast.LENGTH_SHORT).show();
        }
    }

    public void zoomImage ( View view )
    {
        if (!updates.getImageUrl().equalsIgnoreCase("Noting here")) {
            Intent i = new Intent(this, ImageViewActivity.class);
            i.putExtra("Url", updates.getImageUrl());
            startActivity(i);
        }
    }

    public void Back(View view) {
        finish();
    }
}