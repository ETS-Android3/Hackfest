package com.mysocial.hackfest.faceofcampus;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageQueryResponse {


    private String URL = "https://faceofcampus.herokuapp.com/predict";
    Context context;

    ImageQueryResponse(Context context){
        this.context = context;

    }

    public String api_call(Bitmap bitmap)
    {

        final String[] responseString = {"check"};

        String string = convertToBase64(bitmap);

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String name = jsonObject.getString("name");
                    String place = jsonObject.getString("place");
                    String branch = jsonObject.getString("branch");
                    String image = jsonObject.getString("image");
                    String year = jsonObject.getString("year");

                    String txt ="";
                    if(year.equals("1"))txt+="'st";
                    else if(year.equals("2"))txt+="'nd";
                    else if(year.equals("3"))txt+="'rd";
                    else txt+="'th";

                    responseString[0] = name;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseString[0] = "error";
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


        return responseString[0];

    }



    private static String convertToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }


}
