package com.flux.Fvisng;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import co.paystack.android.PaystackSdk;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Silver extends Application {

    @Override
    public void onCreate() {
        Fresco.initialize(getApplicationContext());
        PaystackSdk.initialize(getApplicationContext());
        super.onCreate();
    }

    Drawable front, back;

    public Drawable getFront() {
        return front;
    }

    public void setFront(Drawable front) {
        this.front = front;
    }

    public Drawable getBack() {
        return back;
    }

    public void setBack(Drawable back) {
        this.back = back;
    }

    static String twilio = "https://api.twilio.com/2010-04-01/Accounts/AC6b6125d1f4422e8cebedaea18d6400c1/Messages.json";
    static String twilioSID = "AC6b6125d1f4422e8cebedaea18d6400c1";
    static String twilioAUTH = "-----d648f57ad7472694fd51be99500cd5f4";

    static String apiRegister  = "https://resource.fvisng.com/api/auth/register";
    static String apiLogin  = "https://resource.fvisng.com/api/auth/login";
    static String apiUser  = "https://resource.fvisng.com/api/auth/user";
    static String apiBVN  = "https://resource.fvisng.com/api/loans/bvnlookup";
    static String apiSUbmitloane = "https://resource.fvisng.com/api/submitloan";
    static String apiHistory  = "https://resource.fvisng.com/api/user/loanhistory/20";

    static String paystackKei = "pk_test_219fe40f38e54f389a60160061bdcf153f2415d5";
    static String paystackKey = "pk_live_5f002e17e9fb61784f9db7395d4972c54b511a45";


    interface BankNameOnline{
        void onBankNameReady(String name);
    }
    static class getBankName{
        getBankName(BankNameOnline bank, String nuban, String c0de) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://api.nanollite.com/bills/bank?nuban=" + nuban + "&code=" + c0de).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            bank.onBankNameReady("Error getting name");
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String bn = response.body().string();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            bank.onBankNameReady(bn);
                        }
                    });
                }
            });
        }
    }

}