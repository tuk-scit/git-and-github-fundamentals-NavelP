package com.example.vicmakmanager;

import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VicmakSalesAdapter extends FragmentPagerAdapter {

    ArrayList<String> tab_names = new ArrayList<>();
    RelativeLayout relativeLayout;

    public VicmakSalesAdapter(@NonNull FragmentManager fm, RelativeLayout relativeLayout) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.relativeLayout = relativeLayout;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new VicmakSales(relativeLayout);

            case 1:
                return new VinnySales(relativeLayout);

            case 2:
                return new TotalSales();

            case 3:
                return new Credits();
        }

        return null;
    }

    @Override
    public int getCount() {
        return tab_names.size();
    }

    public void add(){
        tab_names.add("Vicmak2");
        tab_names.add("Vinny");
        tab_names.add("Total");
        tab_names.add("Credits");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab_names.get(position);
    }
}
