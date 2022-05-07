package com.mysocial.hackfest.faceofcampus;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mysocial.hackfest.databinding.ActivityTakeImageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeImageActivity extends AppCompatActivity {


    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private ActivityTakeImageBinding binding;
    private String URL = "https://faceofcampus.herokuapp.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTakeImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        binding.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (checkAndRequestPermissions(TakeImageActivity.this)){
                    chooseImage(TakeImageActivity.this);
                }
            }
        });

        binding.takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageView.setVisibility(View.INVISIBLE);
                binding.materialCardView.setVisibility(View.INVISIBLE);
                binding.button.setVisibility(View.VISIBLE);
                if (checkAndRequestPermissions(TakeImageActivity.this)){
                    chooseImage(TakeImageActivity.this);
                }
            }
        });

    }


    public static boolean checkAndRequestPermissions(final Activity context) {

        int ExternalStoragePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    // Handled permission Result


    private void chooseImage(Context context) {

        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array

        // create a dialog for showing the optionsMenu

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set the items in builder

        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (optionsMenu[i].equals("Take Photo")) {

                    // Open the camera and get the photo

                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                    //Toast.makeText(getContext(), "h1", Toast.LENGTH_SHORT).show();
                } else if (optionsMenu[i].equals("Choose from Gallery")) {

                    // choose from  external storage

                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);

                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }

            }
        });
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults );
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(TakeImageActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TakeImageActivity.this,
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();

                } else if (ContextCompat.checkSelfPermission(TakeImageActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TakeImageActivity.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    chooseImage(TakeImageActivity.this);
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

//            ImageQueryResponse queryResponseObject = new ImageQueryResponse(this);

            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        api_call(selectedImage);
                        binding.imageView.setImageBitmap(selectedImage);

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(data.getData());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        api_call(selectedImage);
                        binding.imageView.setImageBitmap(selectedImage);

                    }
                    break;
            }

        }
    }


    public void api_call(Bitmap bitmap)
    {

        final String[] responseString = {"check"};

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Checking database. Please Wait..");
        dialog.show();
        binding.imageView.setVisibility(View.INVISIBLE);

        String string = convertToBase64(bitmap);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String name = jsonObject.getString("name");
                    String place = jsonObject.getString("place");
                    String branch = jsonObject.getString("branch");
                    String image = jsonObject.getString("image");
                    String year = jsonObject.getString("year");

                    String txt = year;
                    if(year.equals("1"))txt+="'st year";
                    else if(year.equals("2"))txt+="'nd year";
                    else if(year.equals("3"))txt+="'rd year";
                    else txt+="'th year";


                    responseString[0] = name;
                    binding.nameFetched.setText(name);
                    binding.placeFetched.setText(place);
                    binding.branchFetched.setText(branch+"\n("+txt+")");

                    binding.dataPalate.setVisibility(View.VISIBLE);
                    binding.notFoundPalate.setVisibility(View.INVISIBLE);
                    binding.materialCardView.setVisibility(View.VISIBLE);
                    binding.imageView.setVisibility(View.VISIBLE);
                    binding.button.setVisibility(View.GONE);
//                    binding.materialCardView.animate().alpha(0).start();

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    binding.button.setVisibility(View.VISIBLE);
                    Toast.makeText(TakeImageActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                binding.imageView.setVisibility(View.VISIBLE);
                binding.materialCardView.setVisibility(View.VISIBLE);
                binding.notFoundPalate.setVisibility(View.VISIBLE);
                binding.dataPalate.setVisibility(View.INVISIBLE);
                binding.button.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("image",string);
                return map;
            }
        };

        queue.add(stringRequest);


    }

    private static String convertToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }





}

