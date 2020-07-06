package com.flux.Fvisng;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FragmentLoanTwo extends XFragment implements AdapterView.OnItemSelectedListener {

    private ProgressBar loaner;
    private Button  requestttt;
    private TextView errr;
    private String bankC0de, bankName, percentage, duration;

    private EditText bank_nuban, bank_holder;
    private boolean isEmployed = false;

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_loan_two, child, false);

        EditText loan_amount = view.findViewById(R.id.loan_amount);
        EditText loan_bvn = view.findViewById(R.id.loan_bvn);
        bank_holder = view.findViewById(R.id.loan_holder);
        bank_nuban = view.findViewById(R.id.loan_nuban);
        Spinner loan_duration = view.findViewById(R.id.loan_duration);
        Spinner loan_bank = view.findViewById(R.id.loan_bank);

        errr = view.findViewById(R.id.err);



        requestttt = view.findViewById(R.id.request);

        ArrayAdapter<CharSequence> purpos_adapter = ArrayAdapter.createFromResource(cx, R.array.duration, R.layout.xml_spinner);
        purpos_adapter.setDropDownViewResource(R.layout.xml_spinner);
        loan_duration.setAdapter(purpos_adapter);
        loan_duration.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> purpose_adapter = ArrayAdapter.createFromResource(cx, R.array.bank_names, R.layout.xml_spinner);
        purpose_adapter.setDropDownViewResource(R.layout.xml_spinner);
        loan_bank.setAdapter(purpose_adapter);
        loan_bank.setOnItemSelectedListener(this);


        requestttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = loan_amount.getText().toString();
                String nuban = bank_nuban.getText().toString();
                String bvn = loan_bvn.getText().toString();
                String holder = bank_holder.getText().toString();


                if (amount.isEmpty()){
                    errr.setText("Enter a valid amount");
                } else if (nuban.isEmpty()){
                    errr.setText("Enter valid account name");
                } else if (bvn.length() != 11) {
                    errr.setText("Enter your valid BVN number");
                } else {
                    errr.setText("");
                    requestttt.setBackgroundResource(R.drawable.button_off);
                    requestttt.setEnabled(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Dialog dl = new Dialog(fx);
                            dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dl.setContentView(R.layout.xml_more);
                            dl.setCancelable(false);
                            Button yes = dl.findViewById(R.id.yes);
                            Button no = dl.findViewById(R.id.no);

                            TextView inf = dl.findViewById(R.id.lwdlk);
                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    inf.setText("Please wait...");
                                    dl.setCancelable(false);
                                    requestLoan(amount, duration, percentage, nuban, bankC0de, bankName, holder, bvn, dl);
                                }
                            });
                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dl.cancel();
                                    requestttt.setBackgroundResource(R.drawable.button_dark);
                                    requestttt.setEnabled(true);
                                }
                            });
                            dl.show();
                        }
                    }, 2800);
                    //open new page
                }
            }
        });
        return view;
    }

    private void saveThis(String key, String value){
        SharedPreferences e = fx.getSharedPreferences("db", 0);
        SharedPreferences.Editor ed = e.edit();
        ed.putString(key, value);
        ed.apply();
    }

    private void requestLoan(String amount, String duration, String percent, String nuban, String bankcode, String bankname, String name, String bvn, Dialog dl){
        //loaner.setVisibility(View.VISIBLE);


        XUpload multipartRequest = new XUpload(Request.Method.POST, Silver.apiSUbmitloane, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                Log.e("silvr", resultResponse);

                try {
                    JSONObject object = new JSONObject(resultResponse);
                    boolean success = object.getBoolean("success");
                    if (success){
                        dl.cancel();
                        fm.beginTransaction().add(R.id.fragment, new FragmentLoanThree()).addToBackStack(null).commit();
                    } else {
                        dl.cancel();
                        errr.setText("Could not process request, try again later");
                    }
                } catch (JSONException ignored){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(cx, "error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Bundle bb = getArguments();


                HashMap<String, String> post = new HashMap<>();

                String mail = data.getString("mail", null);
                String firstname = data.getString("othername", "");
                String lastname = data.getString("surname", "");

                post.put("amount", amount);
                post.put("purpose", "Business");
                post.put("duration", duration);
                post.put("interest", percent);
                post.put("relativename", bb.getString("relative_name"));
                post.put("employ_status", bb.getString("employment"));
                post.put("bankcode", bankcode);
                post.put("bankname", bankname);
                post.put("account_number", nuban);
                post.put("account_name", name);

                post.put("relativenumber", bb.getString("relative_phone"));
                post.put("employ_company", bb.getString("company_name"));
                post.put("employ_name", bb.getString("employee_name"));
                post.put("employ_number", bb.getString("employee_phone"));
                post.put("salary", bb.getString("salary"));


//                post.put("mobile_number", "234");
//                post.put("amount", amount);
//                post.put("desc", "Loan from the android app");
//                post.put("first_name", firstname);
//                post.put("last_name", lastname);
//                post.put("email", mail);
//                post.put("account_number", nuban);
//                post.put("selectedbank", bankName);
//                post.put("bankcode", bankcode);
//                post.put("account_name", lastname.toUpperCase() + ",  " + firstname.toUpperCase());
//                post.put("interest", percent);
//                post.put("purpose", "Business");
//                post.put("duration", duration);

                return post;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView

//                post.put("idback", bb.getString("front"));
//                post.put("idfront", bb.getString("back"));
                Drawable x = ((Silver)fx.getApplication()).getFront();
                Drawable y = ((Silver)fx.getApplication()).getBack();

                params.put("idfront", new DataPart("idfront", AppHelper.getFileDataFromDrawable(cx, x), "image/jpeg"));
                params.put("idback", new DataPart("idback", AppHelper.getFileDataFromDrawable(cx, y), "image/jpeg"));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> post = new HashMap<>();
                post.put("Authorization", "Bearer " + token);
                return post;
            }
        };




        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(cx).getCache().clear();
        Volley.newRequestQueue(cx).add(multipartRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.loan_duration:
                String[] d = {"", "5", "14", "28"};
                String[] p = {"", "5", "8", "15"};
                percentage = p[position];
                duration = d[position];
                break;
            case R.id.loan_bank:
                String[] arr = cx.getResources().getStringArray(R.array.bank_codes);
                bankC0de = arr[position];
                bankName = parent.getItemAtPosition(position).toString();
                new Silver.getBankName(new Silver.BankNameOnline() {
                    @Override
                    public void onBankNameReady(String name) {
                        bank_holder.setText(name);
                    }
                }, bank_nuban.getText().toString(), bankC0de);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Sketch Project Studio
     * Created by Angga on 12/04/2016 14.27.
     */
    public static class AppHelper {


        /**
         * Turn drawable resource into byte array.
         * @param context parent context
         * @param id      drawable resource id
         * @return byte array
         */
        public static byte[] getFileDataFromDrawable(Context context, int id) {
            Drawable drawable = ContextCompat.getDrawable(context, id);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }

        /**
         * Turn drawable into byte array.
         *
         * @param drawable data
         * @return byte array
         */
        public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
