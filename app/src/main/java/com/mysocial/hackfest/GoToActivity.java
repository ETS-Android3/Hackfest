package com.mysocial.hackfest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GoToActivity extends AppCompatActivity {
  public   ListView lv_places;
    public ArrayList<String> list_places = new ArrayList<String>();
    public ArrayAdapter adpt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to);
        lv_places = (ListView) findViewById(R.id.lv_places);
        adpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_places);
        SharedPreferences sharedPreferences = getSharedPreferences("MapShared",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        lv_places.setAdapter(adpt);
        adpt.clear();
        Set<String> set = new HashSet<String>();
        set.add("Central Library");
        set.add("Swimming Pool");
        set.add("Library Garden");
        set.add("Upper Ground");
        set.add("CHW Office");
        set.add("National Cadet Corps");
        set.add("Nescafe, Academic Complex, IIT Dhanbad");
        set.add("India Post IIT (ISM)");
        set.add("Senior Academic Hostel");
        set.add("New Lecture Hall Complex");
        set.add("Admin Block");
        set.add("Rosaline Hostel");
        adpt.addAll(set);
        adpt.notifyDataSetChanged();
        lv_places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String requestToUser = ((TextView)view).getText().toString();
                myEdit.putString("position", getLatitudeLongitude(position));
                myEdit.commit();
//                Intent i=new Intent(GoToActivity.this, MapActivity.class);
//                startActivity(i);
                finish();
            }
        });
    }

    public String getLatitudeLongitude(int position){
        if(position==0)return "23.81675071001958,86.43933895727457";
        if(position==1)return "23.81427891415240,86.44234663919941";
        if(position==2)return "23.81214195667855,86.43930962577218";
        if(position==3)return "23.81159720138982,86.44219624437763";
        if(position==4)return "23.81773279618223,86.43793604747039";
        if(position==5)return "23.81604988913945,86.44223350497512";
        if(position==6)return "23.81443269968689,86.44118069743269";
        if(position==7)return "23.81185569582762,86.44182042441837";
        if(position==8)return "23.81221075076774,86.44320920483243";
        if(position==9)return "23.81364390929464,86.44504913606133";
        if(position==10)return "23.81312981657197,86.44096545510405";
        if(position==11)return "23.81673844925432,86.44179958763975";
        else return "23.81463798228032,86.44120950167581";
   }

}