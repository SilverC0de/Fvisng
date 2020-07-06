package com.flux.Fvisng;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentHistory extends XFragment{

    ListView listView ;
    private ImageView emp ;
    private ProgressBar roller;

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_history, child, false);

        listView = view.findViewById(R.id.list);
        roller = view.findViewById(R.id.roller);

        emp = view.findViewById(R.id.empty);
        final List<MyList> myLists = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().header("Authorization", "Bearer " + token).url(Silver.apiHistory).build();
        Log.e("silvr", token);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String json = response.body().string();

                Log.e("silvr", json);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject obj = new JSONObject(json);

                            JSONArray arr = obj.getJSONObject("data").getJSONArray("data");
                            if (arr.length() == 0){
                                roller.setVisibility(View.GONE);
                                emp.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < arr.length(); i++){
                                    JSONObject  o = arr.getJSONObject(i);
                                    String amount = o.getString("amount");
                                    String interest = o.getString("interest");
                                    String final_amount = o.getString("final_amount");
                                    String duration = o.getString("duration");
                                    String ref = o.getString("ref");
                                    String purpose = o.getString("purpose");
                                    String approved_date = o.getString("approved_date");
                                    String repayment_date = o.getString("repayment_date");
                                    String status = o.getString("status");

                                    myLists.add(new MyList(amount,interest + "%",final_amount,ref, purpose, status, duration + " Days", approved_date, repayment_date));
                                }
                                roller.setVisibility(View.GONE);
                                XAdapter adapter = new XAdapter(fx, cx, R.layout.xml_history, myLists, fm);
                                listView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            roller.setVisibility(View.GONE);
                            emp.setImageResource(R.drawable.ic_wireless_error);
                            emp.setVisibility(View.VISIBLE);
                            Toast.makeText(fx, "Could not connect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



        return view;
    }


    private static class XAdapter extends ArrayAdapter<MyList> {

        int layout;
        Context cx;
        Activity fx;
        List<MyList> list;
        FragmentManager fm;

        XAdapter(Activity fx, @NonNull Context context, int resource, @NonNull List<MyList> objects, FragmentManager fm) {
            super(context, resource, objects);
            this.fx = fx;
            this.cx = context;
            this.layout = resource;
            this.list = objects;
            this.fm = fm;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Holder holder;
            if (convertView == null) {
                holder = new Holder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);


                holder.purpose = convertView.findViewById(R.id.fvis_purpose);
                holder.status = convertView.findViewById(R.id.fvis_status);
                holder.amount = convertView.findViewById(R.id.fvis_amount);
                holder.interest = convertView.findViewById(R.id.fvis_interest);
                holder.start = convertView.findViewById(R.id.fvis_start);
                holder.end = convertView.findViewById(R.id.fvis_end);
                holder.duration = convertView.findViewById(R.id.fvis_duration);
                holder.repay = convertView.findViewById(R.id.fvis_repay);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.purpose.setText(list.get(position).getPurpose());
            holder.status.setText(list.get(position).getStatus());
            holder.amount.setText(list.get(position).getAmount());
            holder.interest.setText(list.get(position).getInterest());
            holder.start.setText(list.get(position).getApproved_date());
            holder.end.setText(list.get(position).getRepayment_date());
            holder.duration.setText(list.get(position).getDuration());
            if (list.get(position).getStatus().equals("active")){
                holder.repay.setBackgroundResource(R.drawable.button_dark);
                holder.repay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //FragmentRepay repay = new FragmentRepay();

                        //load kejfkn
                        //sdfef

                        holder.repay.setText("Please Wait");
                        holder.repay.setBackgroundResource(R.drawable.button_off);
                        holder.repay.setEnabled(false);
                        chargeUser(fx, cx, list.get(position).getFinal_amount());

                        //fm.beginTransaction().add(R.id.fragment, repay).addToBackStack(null).commit();
                    }
                });
            } else {
                holder.repay.setText("Loan not active");
                holder.repay.setTextColor(cx.getResources().getColor(R.color.colorTextHint));
            }

            return convertView;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    static class Holder{
        TextView purpose, status, amount, interest, start, end, duration, repay;
    }


    private static void chargeUser(Activity fx, Context cx, String amount){
        SharedPreferences data = cx.getSharedPreferences("db", 0);

        String cc = data.getString("cc", "");
        String mm = data.getString("mm", "");
        String yy = data.getString("yy", "");
        String cv = data.getString("cv", "");

        Card card = new Card(cc, Integer.parseInt(mm), Integer.parseInt(yy), cv);

        if (cc.isEmpty()){
            Toast.makeText(cx, "Please fund your wallet to continue", Toast.LENGTH_SHORT).show();
        } else {

            PaystackSdk.setPublicKey(Silver.paystackKei);
            Charge transaction = new Charge();
            transaction.setAmount(Integer.parseInt(amount) * 100);
            transaction.setEmail(data.getString("mail", null));
            transaction.setCard(card);
            PaystackSdk.chargeCard(fx, transaction, new Paystack.TransactionCallback() {
                @Override
                public void onSuccess(Transaction transaction) {
                    //ok
                    StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Silver.apiLogin, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("silvr", response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                boolean ok = obj.getBoolean("success");
                                if (ok){

                                    //ok
                                } else {
                                    Toast.makeText(cx, obj.getString("error"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException ignored){
                                Toast.makeText(cx, "An error occurred", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(cx, "Check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() {
                            String ref = String.valueOf(System.currentTimeMillis());
                            HashMap<String, String> post = new HashMap<>();
                            post.put("final_amount", amount);
                            post.put("message", "Loan repayment");
                            post.put("status", "true");
                            post.put("ref", ref);
                            return post;
                        }
                    };
                    request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    Volley.newRequestQueue(cx).getCache().clear();
                    Volley.newRequestQueue(cx).add(request);
//                    SharedPreferences.Editor e = data.edit();
//                    e.putString("wallet", amount);
//                    e.apply();
//                    fm.beginTransaction().replace(R.id.fragment, new FragmentWallet()).commit();
//                    Toast.makeText(cx, "You have successfully funded your wallet", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void beforeValidate(Transaction transaction) {

                }

                @Override
                public void onError(Throwable error, Transaction transaction) {


                }
            });
        }
    }
}