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
import com.mysocial.hackfest.classes.Updates;

public class SingleClubUpdateDisplay extends AppCompatActivity {

    private TextView heading, content, club_name, fb_link, insta_link, web_link, other_link;
    private ImageView imageView, img_view, pdf_image;
    private TextView link1, link2, link3, link4;
    private Updates updates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_club_update_display);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();

        heading = findViewById(R.id.title);
        content = findViewById(R.id.message);
        club_name = findViewById(R.id.club_name);
        imageView = findViewById(R.id.club_image);
        img_view = findViewById(R.id.image_view);
        pdf_image = findViewById(R.id.pdf_view);
        fb_link = findViewById(R.id.fb_link);
        insta_link = findViewById(R.id.insta_link);
        web_link = findViewById(R.id.web_link);
        other_link = findViewById(R.id.other_link);

        link1 = findViewById(R.id.link1);
        link2 = findViewById(R.id.link2);
        link3 = findViewById(R.id.link3);
        link4 = findViewById(R.id.link4);

        updates = (Updates) getIntent().getSerializableExtra("object");
        String club_notice = getIntent().getStringExtra("club_notice");

        if (club_notice.equalsIgnoreCase("notice")) {
            link1.setText("link 1 ");
            link2.setText("link 2 ");
            link3.setText("link 3 ");
            link4.setText("link 4 ");
        }

        heading.setText(updates.getTitle());
        content.setText(updates.getContent());
        if (club_notice.equalsIgnoreCase("club")) {
            club_name.setText(updates.getClubName());
            switch (updates.getClubName()) {
                case "LCI":
                    imageView.setBackgroundResource(R.drawable.lci);
                    break;
                case "Book Club":
                    imageView.setBackgroundResource(R.drawable.bookclub);
                    break;
                case "Chayanika Sangh":
                    imageView.setBackgroundResource(R.drawable.chayanikasangh);
                    break;
                case "LITC":
                    imageView.setBackgroundResource(R.drawable.litc);
                    break;
                case "Comedy Club":
                    imageView.setBackgroundResource(R.drawable.comedyclub);
                    break;
                case "Mic Drop":
                    imageView.setBackgroundResource(R.drawable.micdrop);
                    break;
                case "Cyber Labs":
                    imageView.setBackgroundResource(R.drawable.cyberlabs);
                    break;
                case "Quiz Club":
                    imageView.setBackgroundResource(R.drawable.quizclub);
                    break;
                case "Mailer Daemon":
                    imageView.setBackgroundResource(R.drawable.mailerdaemon);
                    break;
                case "LITM":
                    imageView.setBackgroundResource(R.drawable.litm);
                    break;
                case "RoboIsm":
                    imageView.setBackgroundResource(R.drawable.roboism);
                    break;
                case "WTC":
                    imageView.setBackgroundResource(R.drawable.wtc);
                    break;
                case "Table Tennis Club":
                    imageView.setBackgroundResource(R.drawable.tt);
                    break;
                case "Badminton Club":
                    imageView.setBackgroundResource(R.drawable.badminton);
                    break;
                case "Boxing Club":
                    imageView.setBackgroundResource(R.drawable.boxing);
                    break;
                case "Hockey Club":
                    imageView.setBackgroundResource(R.drawable.hockey);
                    break;
                case "Cricket Club":
                    imageView.setBackgroundResource(R.drawable.cricket);
                    break;
                default:
                    break;
            }
        } else {
            club_name.setText("IIT DHANBAD");
            imageView.setBackgroundResource(R.drawable.iit_ism_logo);
        }

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
                            Intent intent = new Intent(SingleClubUpdateDisplay.this, ViewpdfActivity.class);
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