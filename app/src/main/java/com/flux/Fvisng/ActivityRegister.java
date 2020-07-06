package com.flux.Fvisng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ActivityRegister extends AppCompatActivity {

    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText surname = findViewById(R.id.registerSurname);
        final EditText othername = findViewById(R.id.registerOthername);
        final EditText mail = findViewById(R.id.registerMail);
        final EditText phone = findViewById(R.id.registerNumber);
        final EditText password = findViewById(R.id.registerPass);
        final EditText passwordx = findViewById(R.id.registerPassword);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorLighter));
        if (Build.VERSION.SDK_INT > 22) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ImageView close = findViewById(R.id.close);
        register = findViewById(R.id.register);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final String mySurname = surname.getText().toString().trim();
                        final String myOthername = othername.getText().toString().trim();
                        final String myMail = mail.getText().toString().trim();
                        final String myPass = password.getText().toString().trim();
                        final String myPassword = passwordx.getText().toString().trim();
                        final String myNumber = phone.getText().toString().trim();

                        if (mySurname.isEmpty() || myOthername.isEmpty()) {
                            Toast.makeText(ActivityRegister.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        } else if (myMail.isEmpty()) {
                            Toast.makeText(ActivityRegister.this, "Invalid mail address", Toast.LENGTH_SHORT).show();
                        } else if (!myPass.equals(myPassword)) {
                            Toast.makeText(ActivityRegister.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        } else if (myPassword.isEmpty()) {
                            Toast.makeText(ActivityRegister.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                        } else if(myNumber.length() != 11){
                            Toast.makeText(ActivityRegister.this, "Phone number should be in local format", Toast.LENGTH_SHORT).show();
                        }else {
                            register.setBackgroundResource(R.drawable.button_off);
                            register.setEnabled(false);
                            reg(mySurname, myOthername, myMail, myNumber, myPass);
                        }
                    }
                }, 2000);
            }
        });
    }

    /*

    StringRequest request = new StringRequest(Request.Method.POST, Silver.apiRegister, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

        };
     */

    private void reg(final String firstName, final String lastName, final String email, final String phone, final String password){
        StringRequest request = new StringRequest(Request.Method.POST, Silver.apiRegister, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("silvr", "regg--" +response);
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean stat = obj.getBoolean("success");
                    if (stat){
                        SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        e.putString("line", phone);
                        e.putString("verified", "nan");
                        e.putString("surname", firstName);
                        e.putString("othername", lastName);
                        e.putString("mail", email);
                        e.putString("wallet", "0.00");
                        e.putString("avi", "");
                        e.apply();




                        startActivity(new Intent(getApplicationContext(), ActivityOTP.class));
                        finish();
                    } else {
                        register.setBackgroundResource(R.drawable.button_dark);
                        register.setEnabled(true);
                        Toast.makeText(ActivityRegister.this, obj.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    Toast.makeText(ActivityRegister.this, "Unable to sign in, try later", Toast.LENGTH_SHORT).show();
                }
                Log.e("silvr", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityRegister.this, "Could not connect", Toast.LENGTH_SHORT).show();
                register.setBackgroundResource(R.drawable.button_dark);
                register.setEnabled(true);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();
                post.put("first_name", firstName);
                post.put("last_name", lastName);
                post.put("email", email);
                post.put("phone_number", phone);
                post.put("password", password);
                post.put("password_confirmation", password);
                return post;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).getCache().clear();
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

}
