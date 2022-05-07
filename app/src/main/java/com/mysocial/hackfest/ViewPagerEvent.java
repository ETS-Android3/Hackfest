package com.mysocial.hackfest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appitup.classes.Updates;
import com.example.appitup.eventCategory.FragmentEventUpdates;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerEvent extends FragmentStatePagerAdapter {

    private int tabCount;
    private Context context;
    private List<Updates> updatesList;
    private ArrayList<Updates> ismEventList = new ArrayList<Updates>();
    private ArrayList<Updates> nonIsmEventList = new ArrayList<Updates>();

    public ViewPagerEvent(@NonNull @NotNull FragmentManager fm, int behavior , Context context , List<Updates> updates) {
        super(fm, behavior);
        this.tabCount = behavior;
        this.context = context;
        this.updatesList = updates;
        sort_List();
    }

    @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        return POSITION_NONE;
    }

    public void  sort_List()
    {
       for ( int i=0 ; i<updatesList.size() ; i++ )
       {
           if (updatesList.get(i).getIsmEvent())
           {
               ismEventList.add(updatesList.get(i));
           }
           else
           {
               nonIsmEventList.add(updatesList.get(i));
           }
       }
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0 : return new FragmentEventUpdates( context , ismEventList );
            case 1 : return new FragmentEventUpdates( context , nonIsmEventList );
            default:
                return null;
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0 : return "ISM Events";
            case 1 : return "Non ISM Events";
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
