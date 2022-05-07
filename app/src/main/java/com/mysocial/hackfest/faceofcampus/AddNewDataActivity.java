package com.mysocial.hackfest.faceofcampus;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mysocial.hackfest.databinding.ActivityAddNewDataActivtiyBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddNewDataActivity extends AppCompatActivity {



    private ActivityAddNewDataActivtiyBinding binding;
    private String URL = "https://facereco23.herokuapp.com/update";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewDataActivtiyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int update = getIntent().getIntExtra("update",-1);

        if(update == 0){
            binding.imageView.setImageBitmap(getIntent().getParcelableExtra("image"));
        }


        binding.uploadNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.nameU.getText().toString();
                String place = binding.placeU.getText().toString();
                String branch = binding.branchU.getText().toString();
                String year = binding.yearU.getText().toString();
                String foInterest = binding.fieldOfInterestU.getText().toString();
                Bitmap image = binding.imageView.getDrawingCache();


                if(name.isEmpty()){
                    binding.nameU.setError("Enter the field");
                    binding.nameU.requestFocus();
                    return;
                }
                if(place.isEmpty()){
                    binding.placeU.setError("Enter the field");
                    binding.placeU.requestFocus();
                    return;
                }
                if(branch.isEmpty()){
                    binding.branchU.setError("Enter the field");
                    binding.branchU.requestFocus();
                    return;
                }
                if(year.isEmpty()){
                    binding.yearU.setError("Enter the field");
                    binding.yearU.requestFocus();
                    return;
                }
                if(foInterest.isEmpty()){
                    binding.fieldOfInterestU.setError("Enter the field");
                    binding.fieldOfInterestU.requestFocus();
                    return;
                }

                // uploading new data to server
                uploadData(name, image, place,branch,year,foInterest);

            }
        });



    }

    private void uploadData(String name, Bitmap image, String place, String branch, String year, String foInterest) {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Checking database. Please Wait..");
        dialog.show();
        binding.imageView.setVisibility(View.INVISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("Hogaya");
                    if(status.equals("OK")){
                        dialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("image", convertToBase64(image));
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name",name);
                    jsonObject.put("place",place);
                    jsonObject.put("branch", branch);
                    jsonObject.put("year",year);
                    jsonObject.put("studying",foInterest);

                    map.put("details",jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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