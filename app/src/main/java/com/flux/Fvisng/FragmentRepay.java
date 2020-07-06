package com.flux.Fvisng;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentRepay extends XFragment {
    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_repay, child, false);


        return view;
    }
}