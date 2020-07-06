package com.flux.Fvisng;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentHome extends XFragment{
    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_home, child, false);

        LinearLayout fund = view.findViewById(R.id.fund);

        TextView ba = view.findViewById(R.id.wallet_balance);

        String nn = data.getString("wallet", "0.00");
        ba.setText(String.format("₦%s", nn));


        fund.setOnClickListener(cc->{
            fm.beginTransaction().add(R.id.fragment, new FragmentGladePay()).addToBackStack(null).commit();
        });

        return view;
    }
}