package com.flux.Fvisng;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class XFragment extends Fragment {

    Activity fx;
    SharedPreferences data;
    String token;
    Context cx;
    FragmentManager fm;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cx = context;
        fx = getActivity();
        data = cx.getSharedPreferences("db", Context.MODE_PRIVATE);
        token = data.getString("token", "");
        fm = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onFragmentCreate(inflater, container, savedInstanceState);
    }

    public abstract View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root);
}
