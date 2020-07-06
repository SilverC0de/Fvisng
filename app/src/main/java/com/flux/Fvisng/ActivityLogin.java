package com.flux.Fvisng;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class ActivityLogin extends AppCompatActivity {

    LottieAnimationView load;
    Button in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView recovery = findViewById(R.id.recover);
        TextView register = findViewById(R.id.register);
        in = findViewById(R.id.login);
        load = findViewById(R.id.load);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorLighter));
        if (Build.VERSION.SDK_INT > 22) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        final EditText ma = findViewById(R.id.signInMail);
        final EditText pw = findViewById(R.id.signInPass);

        SharedPreferences d = getSharedPreferences("db", 0);
        String m = d.getString("mail", "");
        ma.setText(m);
        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityRegister.class));
            }
        });

        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myMail = ma.getText().toString().trim();
                final String myPass = pw.getText().toString().trim();
                if (myMail.isEmpty() || myPass.isEmpty()){
                    Toast.makeText(ActivityLogin.this, "Please provide your signin details", Toast.LENGTH_SHORT).show();
                } else {
                    load.setVisibility(View.VISIBLE);
                    in.setBackgroundResource(R.drawable.button_off);
                    in.setText("Loading");
                    in.setEnabled(false);
                    fdf(myMail, myPass);
                }
            }
        });
    }

    private void fdf(final String u, final String p){
        StringRequest request = new StringRequest(Request.Method.POST, Silver.apiLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("silvr", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean ok = obj.getBoolean("success");
                    if (ok){
                        String token = obj.getString("token");
                        SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        e.putString("mail", u);
                        e.putString("token", token);
                        e.apply();

                        updateUser(token);
                    } else {
                        Toast.makeText(ActivityLogin.this, obj.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ignored){
                    Toast.makeText(ActivityLogin.this, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("silvr", error.getMessage() + "");
                load.setVisibility(View.INVISIBLE);
                in.setBackgroundResource(R.drawable.button_dark);
                in.setEnabled(true);
                in.setText("Continue");
                Toast.makeText(ActivityLogin.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();
                post.put("email", u);
                post.put("password", p);
                return post;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).getCache().clear();
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void updateUser(String token) {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(Silver.apiUser).header("Authorization", "Bearer " + token).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        load.setVisibility(View.INVISIBLE);
                        in.setBackgroundResource(R.drawable.button_dark);
                        in.setEnabled(true);
                        in.setText("Continue");
                        Toast.makeText(ActivityLogin.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {

                String json = response.body().string();
                try {
                    JSONObject object = new JSONObject(json);
                    boolean ok = object.getBoolean("success");
                    if (ok){
                        //okk
                        SharedPreferences data = getSharedPreferences("db", 0);
                        SharedPreferences.Editor e = data.edit();

                        e.putString("surname", object.getJSONObject("user").getString("last_name"));
                        e.putString("line", object.getJSONObject("user").getString("phone_number"));
                        e.putString("othername", object.getJSONObject("user").getString("first_name"));
                        e.apply();
                        startActivity(new Intent(getApplicationContext(), ActivityView.class));
                        finish();
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                load.setVisibility(View.INVISIBLE);
                                in.setBackgroundResource(R.drawable.button_dark);
                                in.setEnabled(true);
                                in.setText("Continue");
                                Toast.makeText(ActivityLogin.this, "An error occurred, try later", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException ignored){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            load.setVisibility(View.INVISIBLE);
                            in.setBackgroundResource(R.drawable.button_dark);
                            in.setEnabled(true);
                            in.setText("Continue");
                            Toast.makeText(ActivityLogin.this, "Server error, try later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}