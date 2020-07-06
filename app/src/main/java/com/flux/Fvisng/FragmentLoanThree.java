package com.flux.Fvisng;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class FragmentLoanThree extends XFragment  {

    private ProgressBar loaner;
    private Button  requestttt;

    private TextView info;
    private EditText cardHolder, cardNumber, cardCVV, cardMM, cardYY;
    private Button pay;

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_loan_three, child, false);
        cardHolder = view.findViewById(R.id.glade_pay_card_holder);
        cardNumber = view.findViewById(R.id.glade_pay_card_number);
        cardCVV = view.findViewById(R.id.glade_pay_cvv);
        cardMM = view.findViewById(R.id.glade_pay_mm);
        cardYY = view.findViewById(R.id.glade_pay_yy);

        info = view.findViewById(R.id.err);



        requestttt = view.findViewById(R.id.request);


        requestttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String holder = cardHolder.getText().toString();
                String number = cardNumber.getText().toString();
                String cvv = cardCVV.getText().toString();
                String mm = cardMM.getText().toString();
                String yy = cardYY.getText().toString();

                if (holder.length() < 8 || !holder.contains(" ")){
                    info.setText("Invalid card holder name");
                } else if (number.length() < 12){
                    info.setText("Invalid card number");
                } else if (cvv.length() < 3){
                    info.setText("Invalid cvv code");
                } else if (mm.length() != 2 || yy.length() != 2){
                    info.setText("Invalid expiry date");
                } else {
                    Card card = new Card(number, Integer.parseInt(mm), Integer.parseInt(yy), cvv);
                    if (!card.isValid()){
                        info.setText("Invalid debit card");
                    } else {
                        info.setText("");
                        SharedPreferences.Editor e = data.edit();
                        e.putString("cc", number);
                        e.putString("mm", mm);
                        e.putString("yy", yy);
                        e.putString("cv", cvv);
                        e.apply();
                        processPaystack(card);
                    }
                }
            }
        });
        return view;
    }

    private void processPaystack(Card card){

        String mail = data.getString("mail", null);
        PaystackSdk.setPublicKey(Silver.paystackKei);
        Charge transaction = new Charge();
        transaction.setAmount(10000);
        transaction.setEmail(mail);
        transaction.setCard(card);
        PaystackSdk.chargeCard(fx, transaction, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {

                //save num, cvv, mm, yy


                fm.beginTransaction().replace(R.id.fragment, new FragmentOK()).commit();

            }

            @Override
            public void beforeValidate(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                Toast.makeText(cx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void saveThis(String key, String value){
        SharedPreferences e = fx.getSharedPreferences("db", 0);
        SharedPreferences.Editor ed = e.edit();
        ed.putString(key, value);
        ed.apply();
    }

    private void requestLoan(final String amount, final String nuban){
        loaner.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Silver.apiSUbmitloane, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaner.setVisibility(View.GONE);

                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    if (success){
                        info.setText("Success");
                    } else {
                        fm.beginTransaction().add(R.id.fragment, new FragmentOK()).commit();
                        Toast.makeText(fx, "Success, you will get a mail shortly", Toast.LENGTH_SHORT).show();

//                        info.setText("Could not process request, try again later");
                    }
                } catch (JSONException ignored){}

                requestttt.setBackgroundResource(R.drawable.button_dark);
                requestttt.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loaner.setVisibility(View.GONE);
                requestttt.setBackgroundResource(R.drawable.button_dark);
                requestttt.setEnabled(true);
                info.setText("An internal error occurred, try later");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();

                String mail = data.getString("mail", null);
                String firstname = data.getString("othername", "");
                String lastname = data.getString("surname", "");



                post.put("mobile_number", "234");
                post.put("amount", amount);
                post.put("desc", "Loan from the android app");
                post.put("first_name", firstname);
                post.put("last_name", lastname);
                post.put("email", mail);
                post.put("account_number", nuban);
              //  post.put("selectedbank", bankName);
        //        post.put("bankcode", bankCode);
                post.put("account_name", lastname.toUpperCase() + ", " + firstname.toUpperCase());
                post.put("interest", "20");
          //      post.put("purpose", myPurpose);
            //    post.put("duration", myDuration);

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
