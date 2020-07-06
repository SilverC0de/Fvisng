package com.flux.Fvisng;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentVTU extends XFragment implements AdapterView.OnItemSelectedListener {

    int type = 0;
    String serviceID = "0", variationCode = "0", billerIDx;
    EditText one, two;
    Spinner dropper;
    Button go;

    static String fixedApi = "http://sandbox.vtpass.com/api/payfix";

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_vtu, child, false);

        Spinner purpose = view.findViewById(R.id.vtu_service);
        one = view.findViewById(R.id.vtu_one);
        two = view.findViewById(R.id.vtu_two);
        dropper = view.findViewById(R.id.vtu_dropper);

        ArrayAdapter<CharSequence> purpose_adapter = ArrayAdapter.createFromResource(cx, R.array.vtu_service, R.layout.xml_spinner);
        purpose_adapter.setDropDownViewResource(R.layout.xml_spinner);
        purpose.setAdapter(purpose_adapter);
        purpose.setOnItemSelectedListener(this);

        go = view.findViewById(R.id.vtuuuuuu);
        ImageView close = view.findViewById(R.id.close);


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go.setBackgroundResource(R.drawable.button_off);
                go.setEnabled(false);

                if (type == 1){
                    //airtime
                    String amount = one.getText().toString();
                    String phone = two.getText().toString();
                    buy(serviceID, amount, phone, false);
                } else if (type == 2){
                    //data
                    String amount = one.getText().toString();
                    String phone = two.getText().toString();
                    buy(serviceID, variationCode, amount, true);
                } else if (type == 3){
                    //tv
                    String amount = one.getText().toString();
                    String phone = two.getText().toString();
                    buy(serviceID, variationCode, amount, true);
                } else if (type == 4){
                    //light
                    String amount = one.getText().toString();
                    String phone = two.getText().toString();
                    buy(serviceID, variationCode, amount, true);
                }
            }
        });

        return view;
    }

    private void buy(String service, String variation, String biller, boolean flexible){
        //for airtime
        RequestBody body;
        String api;
        if (flexible){
            api = "http://sandbox.vtpass.com/api/payflexi";
            body = new FormBody.Builder()
                    .add("serviceID", service)
                    .add("request_id", String.valueOf(System.currentTimeMillis()))
                    .add("phone", biller)
                    .add("variation_code", variation)
                    .add("billersCode", biller)
                    .build();
        } else {
            api = "http://sandbox.vtpass.com/api/payfix";
            body = new FormBody.Builder()
                    .add("serviceID", service)
                    .add("request_id", String.valueOf(System.currentTimeMillis()))
                    .add("amount", variation)
                    .add("phone", biller)
                    .build();
        }
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder().header("Authorization", Credentials.basic("sandbox@vtpass.com", "sandbox")).url(api).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(fx, "Request Done", Toast.LENGTH_SHORT).show();
                        go.setBackgroundResource(R.drawable.button_dark);
                        go.setEnabled(true);

                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Log.e("silvr", "------vt" + json);
                try {
                    JSONObject obj = new JSONObject(json);
                    String status = obj.getString("code");

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Dialog dl = new Dialog(fx);
                            dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dl.setContentView(R.layout.xml_ok);
                            TextView txt = dl.findViewById(R.id.info);
                            txt.setText("Your action has been completed");
                            dl.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    fm.beginTransaction().replace(R.id.fragment, new FragmentHome()).commit();
                                }
                            });
                            dl.show();
                        }
                    });



//                    if (status.equals("000")){
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(fx, "Request Has Been Completed", Toast.LENGTH_SHORT).show();
//                                go.setBackgroundResource(R.drawable.button_dark);
//                                go.setEnabled(true);
//                            }
//                        });
//                    } else {
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(fx, "Request Completed", Toast.LENGTH_SHORT).show();
//                                go.setBackgroundResource(R.drawable.button_dark);
//                                go.setEnabled(true);
//                            }
//                        });
//                    }
                } catch (JSONException  ignored){}
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.vtu_service:
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                dropper.setVisibility(View.VISIBLE);
                if (position == 1){
                    //airtime
                    //phone, amount
                    one.setHint("Amount");
                    two.setHint("Phone Number");


                    ArrayAdapter<CharSequence> purpose_adapter = ArrayAdapter.createFromResource(cx, R.array.netwoek, R.layout.xml_spinner);
                    purpose_adapter.setDropDownViewResource(R.layout.xml_spinner);
                    dropper.setAdapter(purpose_adapter);
                    dropper.setOnItemSelectedListener(this);


                    type = 1;
                } else if(position == 2){
                    //show
                    //data
                    one.setHint("Phone Number");
                    two.setVisibility(View.GONE);


                    ArrayAdapter<CharSequence> purpose_adapter = ArrayAdapter.createFromResource(cx, R.array.data, R.layout.xml_spinner);
                    purpose_adapter.setDropDownViewResource(R.layout.xml_spinner);
                    dropper.setAdapter(purpose_adapter);
                    dropper.setOnItemSelectedListener(this);



                    type = 2;
                } else if (position == 3){
                    //tv
                    one.setHint("SmartCard Number");
                    two.setVisibility(View.GONE);

                    ArrayAdapter<CharSequence> purpose_adapter = ArrayAdapter.createFromResource(cx, R.array.tv, R.layout.xml_spinner);
                    purpose_adapter.setDropDownViewResource(R.layout.xml_spinner);
                    dropper.setAdapter(purpose_adapter);
                    dropper.setOnItemSelectedListener(this);

                    type = 3;

                } else if (position == 4){
                    //phcn
                    //card number
                    one.setHint("Meter Number");
                    two.setVisibility(View.GONE);
                    ArrayAdapter<CharSequence> purpose_adapter = ArrayAdapter.createFromResource(cx, R.array.nepa, R.layout.xml_spinner);
                    purpose_adapter.setDropDownViewResource(R.layout.xml_spinner);
                    dropper.setAdapter(purpose_adapter);
                    dropper.setOnItemSelectedListener(this);

                    type = 4;
                } else {
                    one.setVisibility(View.GONE);
                    two.setVisibility(View.GONE);
                    dropper.setVisibility(View.GONE);
                }
                break;
            case R.id.vtu_dropper:
                if (type == 1){
                    //its airtime
                    serviceID = cx.getResources().getStringArray(R.array.netwoek)[position];

                } else if(type == 2){
                    //show
                    //data
                    String[] dataServiceCode = {"mtn-data", "airtel-data", "glo-data", "etisalat-data"};
                    String[] service = {"", "", "", ""};

                    //serviceID = dataServiceCode[position];
                } else if (type == 3){
                    //tv
                    String[] tvServiceCode = {"dstv", "gotv", "startimes"};

                    //serviceID = tvServiceCode[position];

                } else if (type == 4){
                    //phcn
                    //card number
                    String[] lightServiceCode = {"ikeja-electric",
                            "eko-electric", "kano-electric", "portharcourt-electric", "jos-electric", "ibadan-electric"};

                    //serviceID = lightServiceCode[position];
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
