package com.flux.Fvisng;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentWallet extends XFragment {
    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_wallet, child, false);

        TextView ba = view.findViewById(R.id.wallet_balance);

        String nn = data.getString("wallet", "0.00");
        ba.setText(String.format("₦%s", nn));


        return view;
    }
}