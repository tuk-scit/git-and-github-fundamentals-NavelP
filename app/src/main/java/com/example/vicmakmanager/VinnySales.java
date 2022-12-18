package com.example.vicmakmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VinnySales extends Fragment {

    RelativeLayout relativeLayout;

    public VinnySales(RelativeLayout relativeLayout){
        this.relativeLayout = relativeLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new SalesInitializer("vinny", getContext(), relativeLayout).onCreateView(inflater, container, savedInstanceState);
    }
}
