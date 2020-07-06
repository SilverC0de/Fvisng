package com.flux.Fvisng;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class FragmentGladePay extends XFragment {


    private static String apiChargeCard = "";

    private TextView info;
    private EditText cardAmount, cardHolder, cardNumber, cardCVV, cardMM, cardYY;
    private Button pay;

    ///ire fir i




    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_glade_pay, child, false);

        final String ip = Utils.getIPAddress(true);

        Log.e("silvr", "---------" + ip);

        cardHolder = view.findViewById(R.id.glade_pay_card_holder);
        cardAmount = view.findViewById(R.id.glade_pay_amount);
        cardNumber = view.findViewById(R.id.glade_pay_card_number);
        cardCVV = view.findViewById(R.id.glade_pay_cvv);
        cardMM = view.findViewById(R.id.glade_pay_mm);
        cardYY = view.findViewById(R.id.glade_pay_yy);
        info = view.findViewById(R.id.glade_info);

        pay = view.findViewById(R.id.gladepay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String holder = cardHolder.getText().toString();
                String number = cardNumber.getText().toString().trim();
                String cvv = cardCVV.getText().toString().trim();
                String mm = cardMM.getText().toString().trim();
                String yy = cardYY.getText().toString().trim();

                String amount  = cardAmount.getText().toString().trim();

                if (holder.length() < 8 || !holder.contains(" ")){
                    info.setText("Invalid card holder name");
                } else if (number.length() < 12){
                    info.setText("Invalid card number");
                } else if (cvv.length() < 3){
                    info.setText("Invalid cvv code");
                } else if (mm.length() != 2 || yy.length() != 2){
                    info.setText("Invalid expiry date");
                }
                else if (amount.length() < 2){
                    info.setText("Enter a valid amount");
                } else {
                    info.setText("");
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
                        processPaystack(card, amount);
                    }
                }
            }
        });
        return view;
    }

    private void processPaystack(Card card, String amount){

        String mail = data.getString("mail", null);
        PaystackSdk.setPublicKey(Silver.paystackKei);
        Charge transaction = new Charge();
        transaction.setAmount(Integer.parseInt(amount) * 100);
        transaction.setEmail(mail);
        transaction.setCard(card);
        PaystackSdk.chargeCard(fx, transaction, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                //ok
                SharedPreferences.Editor e = data.edit();
                e.putString("wallet", amount);
                e.apply();
                fm.beginTransaction().replace(R.id.fragment, new FragmentWallet()).commit();
                Toast.makeText(cx, "You have successfully funded your wallet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void beforeValidate(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                info.setText(error.getMessage());
            }
        });
    }

    /*
    private void chargeCard(String holder, String number, String cvv, String mm, String yy, String ip){
        String mail = data.getString("mail", null);

        String lastname = holder.substring(0, holder.indexOf(" ")); //get the surname
        String firstname = holder.substring(holder.indexOf(" ") + 1); //get the other name

        try {
            JSONObject card = new JSONObject();
            card.put("card_no", number);
            card.put("expiry_month", mm);
            card.put("expiry_year", yy);
            card.put("ccv", cvv);

            JSONObject user = new JSONObject();
            user.put("firstname", firstname);
            user.put("lastname", lastname);
            user.put("email", mail);
            user.put("ip", ip);
            user.put("fingerprint", "cccvxbxbxb");

            final JSONObject object = new JSONObject();
            object.put("action", "initiate");
            object.put("paymentType", "card");
            object.put("user", user);
            object.put("card", card);
            object.put("amount", "50");
            object.put("country", "NG");
            object.put("currency", "NGN");

            StringRequest request = new StringRequest(Request.Method.POST, apiChargeCard, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public byte[] getBody() {
                    return object.toString().getBytes(StandardCharsets.UTF_8);
                }

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> header = new HashMap<>();
                    header.put("MID", "GP0000001");
                    header.put("KEY", "123456789");
                    header.put("Content-Type", "application/json");
                    return header;
                }
            };

        } catch (JSONException ignored){}
    }

     */

}
