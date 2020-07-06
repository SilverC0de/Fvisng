package com.flux.Fvisng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityOTP extends AppCompatActivity {

    TextView status;
    String vv;
    int sec = 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorLighter));
        if (Build.VERSION.SDK_INT > 22) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);

        final String line = sharedPreferences.getString("line", "");
         vv = String.valueOf(vCode());
        status = findViewById(R.id.status);

        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString("otp", vv);
        e.apply();

        String isVerified = sharedPreferences.getString("verified", "nan");

        status.setText(String.format("OTP will be sent to your mobile number %s shortly", line));
        if (isVerified.equals("ok")){
            startActivity(new Intent(getApplicationContext(), ActivityView.class));
            finish();
        } else {
            try {
                String otp = "Use the verification code " + vv + " to complete your Fvis registration";
                JSONObject body = new JSONObject();
                body.put("content", "hello billions");

                JSONArray arr = new JSONArray();
                arr.put("08104422662");

                body.put("to", arr);
                body.put("from", "sdfdf");
                body.put("binary", false);
                body.put("clientMessageId", "uuid");
                body.put("charset", "UTF-8");

                String url = "https://www.bulksmsnigeria.com/api/v1/sms/create?api_token=4Sekwd691bgAOfhaUIqNfJKrNeQRESfuW9jcoLipTi662wzMFDAlLacqTbEV&from=FvisNG&to=" + line + "&body=" + otp;
                Log.e("silvr", "get----" + url);

                StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("silvr", "sms----" + response);
                        final EditText ee = findViewById(R.id.vCode);
                        Button ok = findViewById(R.id.login);
                        ok.setText("Continue");
                        ok.setBackgroundResource(R.drawable.button_dark);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            //816 128 4937
                            public void onClick(View v) {
                                String cccc = ee.getText().toString().trim();
                                if (vv.equals(cccc)){
                                    SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                                    SharedPreferences.Editor e = sharedPreferences.edit();
                                    e.putString("verified", "ok");
                                    e.apply();

                                    startActivity(new Intent(getApplicationContext(), ActivityView.class));
                                    finish();
                                } else {
                                    status.setText("Invalid Verification code");
                                }
                            }
                        });
                        //otp was sent, the user can enter now
                        new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                sec = sec - 1;
                                status.setText("OTP sent to " + line + ". Didn't receive OTP? Wait for " + sec);
                            }

                            @Override
                            public void onFinish() {
                                status.setText("Resend OTP to " + line);
                                status.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        recreate();
                                    }
                                });
                            }
                        }.start();
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        status.setText("Unable to send OTP, Click to resend OTP to " + line);
                        status.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recreate();
                            }
                        });
                    }
                }){
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return body.toString().getBytes(StandardCharsets.UTF_8);
                    }

                    //                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        HashMap<String, String> post = new HashMap<>();
//                        post.put("username", "fvisng");
//                        post.put("to", "08104422662");
//                        post.put("message", otp);
//                        post.put("from", "AFRICASTKNG");
//                        return post;
//                    }

//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        HashMap<String, String> headre = new HashMap<>();
//                        headre.put("Authorization", "6dbcd4000c9d17a624ad3483b94711d25839f4ed55fda21a98cbc2301511ace5");
//                        headre.put("Content-Type", "application/json");
//                        headre.put("Accept", "application/json");
//                        return headre;
//                    }
                };
                Volley.newRequestQueue(getApplicationContext()).getCache().clear();
                Volley.newRequestQueue(getApplicationContext()).add(request);

            } catch (JSONException ignored){}



//            OkHttpClient client = new OkHttpClient();
//
//
//            RequestBody formBody = new FormBody.Builder()
//                    .add("From", "FVISNG")
//                    .add("To", "+234" + line.substring(1))
//                    .add("Body", body)
//                    .build();
//            Request request = new Request.Builder()
//                    .url(Silver.twilio)
//                    .addHeader("Authorization", Credentials.basic(Silver.twilioSID, Silver.twilioAUTH))
//                    .post(formBody)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException ignored) {
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(ActivityOTP.this, "Unable to send verification code", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull final Response response) {
//                    if (response.isSuccessful()) {
//                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                final EditText ee = findViewById(R.id.vCode);
//                                Button ok = findViewById(R.id.login);
//                                ok.setText("Continue");
//                                ok.setBackgroundResource(R.drawable.button_dark);
//                                ok.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        String cccc = ee.getText().toString().trim();
//                                        if (vv.equals(cccc)){
//                                            SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
//                                            SharedPreferences.Editor e = sharedPreferences.edit();
//                                            e.putString("verified", "ok");
//                                            e.apply();
//
//                                            startActivity(new Intent(getApplicationContext(), ActivityView.class));
//                                            finish();
//                                        } else {
//                                            status.setText("Invalid Verification code");
//                                        }
//                                    }
//                                });
//                                //otp was sent, the user can enter now
//                                status.setText("OTP sent to " + line);
//                            }
//                        }, 4000);
//                    }
//                }
//            });
        }
    }
    private int vCode(){
        Random r = new Random(System.currentTimeMillis());
        return 1000 + r.nextInt(8888);
    }

    public void close(View view) {
        finish();
    }
}
