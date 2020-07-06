package com.flux.Fvisng;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class FragmentOK extends XFragment {
    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_ok, child, false);

        Button end = view.findViewById(R.id.end);

        final ImageView home = fx.findViewById(R.id.home);
        final ImageView loan = fx.findViewById(R.id.loan);
        final ImageView history = fx.findViewById(R.id.history);
        final ImageView account = fx.findViewById(R.id.profile);

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setImageResource(R.drawable.ic_home_active);
                loan.setImageResource(R.drawable.ic_wallet_inactive);
                history.setImageResource(R.drawable.ic_activities_inactive);
                account.setImageResource(R.drawable.ic_profile_inactive);


                try {
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment)).commit();

                }catch (NullPointerException ignored){}

            }
        });
        return view;
    }
}
