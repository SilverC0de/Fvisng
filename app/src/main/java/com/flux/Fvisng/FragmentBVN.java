package com.flux.Fvisng;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentBVN extends XFragment {

    private ProgressBar loaner;
    private Button  bvnnnn;
    private TextView errr;

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_bvn, child, false);

        loaner = view.findViewById(R.id.loaner);
        final EditText bvn = view.findViewById(R.id.bvnn);
        bvnnnn = view.findViewById(R.id.bvnnnnnnnn);
        errr = view.findViewById(R.id.err);

        ImageView close = view.findViewById(R.id.close);

         close.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 fx.onBackPressed();
             }
         });

        bvnnnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bv = bvn   .getText()         .toString();
                if (bv.length() != 11){
                    errr.setText("Invalid BVN");
                } else {
                    errr.setText("");
                    bvnnnn.setBackgroundResource(R.drawable.button_off);
                    bvnnnn.setEnabled(false);
                    checkBVN(bv);
                }
            }
        });

        return view;
    }

    private void checkBVN(final String bv){
        loaner.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Silver.apiBVN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaner.setVisibility(View.GONE);

                Log.e("silvr", "okkk" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    if (success){
                        fm.beginTransaction().add(R.id.fragment, new FragmentLoan()).addToBackStack(null).commit();
                    } else {
                        errr.setText("Could not process request, try again later");
                    }
                } catch (JSONException ignored){}

                bvnnnn.setBackgroundResource(R.drawable.button_dark);
                bvnnnn.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loaner.setVisibility(View.GONE);
                bvnnnn.setBackgroundResource(R.drawable.button_dark);
                bvnnnn.setEnabled(true);
                errr.setText("An internal error occurred, try later");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();

                post.put("bvn", bv);
                return post;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> post = new HashMap<>();
                post.put("Authorization", "Bearer " + token);
                return post;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(request);
    }
}
