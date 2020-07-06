package com.flux.Fvisng;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FragmentLoan extends XFragment implements AdapterView.OnItemSelectedListener {

    private ProgressBar loaner;
    private Button  requestttt;
    private TextView errr;
    private String maritalStatus;
    ImageView oje;
    ImageView ojex, ojey;
    boolean ojeOK = false, ojeOKX =false, ojeOKY = false;
    private String bankName, bankCode, myPurpose, myDuration, employment, ojea, ojeb;

    private EditText loan_company_name,  loan_employee_name, loan_employee_phone;
    private boolean isEmployed = false;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(fx, "Allow access to your camera first", Toast.LENGTH_SHORT).show();
                fx.onBackPressed();
            }
            return;

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent uri) {
        if (requestCode == 11) {
            Bitmap image = (Bitmap) uri.getExtras().get("data");
            oje.setImageBitmap(image);
            ojeOK = true;
        }
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {


            ojex.setImageURI(uri.getData());

            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = cx.getContentResolver().query(uri.getData(), projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column = cursor.getColumnIndexOrThrow(projection[0]);
                ojea = cursor.getString(column);
                cursor.close();
            }
            ojeOKX = true;

            File imgFile = new  File(ojea);

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ojex.setImageDrawable(new BitmapDrawable(cx.getResources(), myBitmap));

            }
        }
        if (requestCode == 6 && resultCode == Activity.RESULT_OK) {


            ojey.setImageURI(uri.getData());

            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = cx.getContentResolver().query(uri.getData(), projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column = cursor.getColumnIndexOrThrow(projection[0]);
                ojeb = cursor.getString(column);
                cursor.close();
            }
            ojeOKY = true;
            File imgFile = new  File(ojeb);

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ojey.setImageDrawable(new BitmapDrawable(cx.getResources(), myBitmap));

            }

        }
        super.onActivityResult(requestCode, resultCode, uri);
    }

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_loan, child, false);

        EditText loan_address = view.findViewById(R.id.loan_address);
        EditText loan_relative_name = view.findViewById(R.id.loan_relative_name);
        EditText loan_relative_phone = view.findViewById(R.id.loan_relative_number);
        loan_company_name = view.findViewById(R.id.loan_company_name);
        loan_employee_name = view.findViewById(R.id.loan_company_owner);
        loan_employee_phone = view.findViewById(R.id.loan_company_phone);
        EditText loan_nin = view.findViewById(R.id.loan_nin);
        EditText loan_salary = view.findViewById(R.id.loan_salary);
        Spinner loan_marital = view.findViewById(R.id.loan_marital);
        Spinner loan_employment = view.findViewById(R.id.loan_employment);

        oje = view.findViewById(R.id.oje); //sets imageview as the bitmap
        ojex = view.findViewById(R.id.ojex); //sets imageview as the bitmap
        ojey = view.findViewById(R.id.ojey); //sets imageview as the bitmap

        LinearLayout avvv = view.findViewById(R.id.camera);
        LinearLayout avvvx = view.findViewById(R.id.camerax);
        LinearLayout avvvy = view.findViewById(R.id.cameray);
        errr = view.findViewById(R.id.err);

        String firstname = data.getString("othername", "");
        String lastname = data.getString("surname", "");

        //namee.setText(lastname.toUpperCase() + ", " + firstname.toUpperCase());


        ActivityCompat.requestPermissions(fx,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                1);

        avvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 11);
            }
        });
        avvvx.setOnClickListener(v->{
            Intent addImage = new Intent(Intent.ACTION_GET_CONTENT);
            addImage.setType("image/*");
            startActivityForResult(addImage, 5);
        });
        avvvy.setOnClickListener(v->{
            Intent addImage = new Intent(Intent.ACTION_GET_CONTENT);
            addImage.setType("image/*");
            startActivityForResult(addImage, 6);
        });
        requestttt = view.findViewById(R.id.request);

        ArrayAdapter<CharSequence> purpose_adapter = ArrayAdapter.createFromResource(cx, R.array.marital, R.layout.xml_spinner);
        purpose_adapter.setDropDownViewResource(R.layout.xml_spinner);
        loan_marital.setAdapter(purpose_adapter);
        loan_marital.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> duration_adapter = ArrayAdapter.createFromResource(cx, R.array.employ, R.layout.xml_spinner);
        duration_adapter.setDropDownViewResource(R.layout.xml_spinner);
        loan_employment.setAdapter(duration_adapter);
        loan_employment.setOnItemSelectedListener(this);



        requestttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bb = new Bundle();
                String address = loan_address.getText().toString();
                String relative_name = loan_relative_name.getText().toString();
                String relative_phone = loan_relative_phone.getText().toString();
                String company_name = loan_company_name.getText().toString();
                String employee_name = loan_employee_name.getText().toString();
                String employee_phone = loan_employee_phone.getText().toString();
                String nin = loan_nin.getText().toString();
                String salary = loan_salary.getText().toString();


                errr.setText("");
                if (!ojeOK) {
                    errr.setText("Grant access to your camera and take your picture");
                }
                if (!ojeOKX) {
                    errr.setText("Grant access to storage and select front of your ID card");
                }
                if (!ojeOKY) {
                    errr.setText("Grant access to storage and select back of your ID card");
                } else if (address.isEmpty()){
                    errr.setText("Enter full address");
                } else if (relative_name.length() < 8){
                    errr.setText("Enter relative name in full");

                } else if (relative_phone.length() < 8){
                    errr.setText("Enter relative phone number");

                } else if (company_name.isEmpty() && isEmployed){
                    errr.setText("Enter company name");

                } else if (employee_name.isEmpty() && isEmployed){
                    errr.setText("Enter employee name");

                } else if (employee_phone.length() < 8 && isEmployed){
                    errr.setText("Enter employee phone number");

                } else if (nin.isEmpty()){
                    errr.setText("Enter ID card number");

                } else if (salary.length() < 4) {
                    errr.setText("Enter your actual monthly income");

                } else {

                    FragmentLoanTwo two = new FragmentLoanTwo();
                    bb.putString("address", address);
                    bb.putString("relative_name", relative_name);
                    bb.putString("relative_phone", relative_phone);
                    bb.putString("employment", employment);
                    bb.putString("company_name", company_name);
                    bb.putString("employee_name", employee_name);
                    bb.putString("employee_phone", employee_phone);
                    bb.putString("nin", nin);
//                    bb.putString("front", ojea);
//                    bb.putString("back", ojeb);
                    bb.putString("salary", salary);
                    bb.putString("marital", maritalStatus);
                    ((Silver)getActivity().getApplication()).setFront(ojex.getDrawable());
                    ((Silver)getActivity().getApplication()).setBack(ojey.getDrawable());

                    two.setArguments(bb);
                    fm.beginTransaction().add(R.id.fragment, two).addToBackStack(null).commit();
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
                        errr.setText("Success");
                    } else {
                        fm.beginTransaction().add(R.id.fragment, new FragmentOK()).commit();
                        Toast.makeText(fx, "Success, you will get a mail shortly", Toast.LENGTH_SHORT).show();

//                        errr.setText("Could not process request, try again later");
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
                errr.setText("An internal error occurred, try later");
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
                post.put("selectedbank", bankName);
                post.put("bankcode", bankCode);
                post.put("account_name", lastname.toUpperCase() + ", " + firstname.toUpperCase());
                post.put("interest", "20");
                post.put("purpose", myPurpose);
                post.put("duration", myDuration);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.loan_employment:
                isEmployed = position == 2;
                employment = parent.getItemAtPosition(position).toString();
                if (position == 2){
                    loan_company_name.setVisibility(View.VISIBLE);
                    loan_employee_name.setVisibility(View.VISIBLE);
                    loan_employee_phone.setVisibility(View.VISIBLE);
                } else {
                    loan_company_name.setVisibility(View.GONE);
                    loan_employee_name.setVisibility(View.GONE);
                    loan_employee_phone.setVisibility(View.GONE);
                }
                break;
            case R.id.loan_marital:
                maritalStatus = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
