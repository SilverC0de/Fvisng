package com.flux.Fvisng;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

public class FragmentSetting extends XFragment {

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_settings, child, false);


        SwitchCompat sw = view.findViewById(R.id.cmera_switch);
        SwitchCompat ex = view.findViewById(R.id.str_switch);

        if (ContextCompat.checkSelfPermission(cx, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) sw.setChecked(true);
        if (ContextCompat.checkSelfPermission(cx, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ex.setChecked(true);
        sw.setEnabled(false);
        ex.setEnabled(false);
        return view;
    }
}
