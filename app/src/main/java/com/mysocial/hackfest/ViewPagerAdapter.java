package com.mysocial.hackfest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appitup.classes.Data;
import com.example.appitup.complaintCategory.AcademicFragment;
import com.example.appitup.complaintCategory.AdministrativeFragment;
import com.example.appitup.complaintCategory.HostelFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private Context context;
    private List<Data> complaintList;
    private ArrayList<Data> academicComplaintList = new ArrayList<Data>();
    private ArrayList<Data> administrativeComplaintList = new ArrayList<Data>();
    private ArrayList<Data> hostelComplaintList = new ArrayList<Data>();
    private String studentAdmin;


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context, List<Data> complaintList , String studentAdmin) {
        super(fm, behavior);
        this.tabCount = behavior;
        this.context = context;
        this.complaintList = complaintList;
        this.studentAdmin = studentAdmin;
        complaintSort();
    }

    @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        return POSITION_NONE;
    }

    public void complaintSort() {
        for (int i = 0; i < complaintList.size(); i++) {

            Data data = complaintList.get(i);
            if (data.getCategory().equalsIgnoreCase("academic"))
            {
                academicComplaintList.add(data);
            }
            if (data.getCategory().equalsIgnoreCase("administrative"))
            {
                administrativeComplaintList.add(data);
            }
            if (data.getCategory().equalsIgnoreCase("hostel"))
            {
                hostelComplaintList.add(data);
            }
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AcademicFragment(context, academicComplaintList,studentAdmin);
            case 1:
                return new HostelFragment(context,hostelComplaintList,studentAdmin);
            case 2:
                return new AdministrativeFragment(context,administrativeComplaintList,studentAdmin);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Academic";
            case 1:
                return "Hostel";
            case 2:
                return "Administrative";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
