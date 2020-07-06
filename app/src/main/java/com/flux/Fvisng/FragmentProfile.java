package com.flux.Fvisng;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;

public class FragmentProfile extends XFragment{

    SimpleDraweeView avi;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent uri) {
        if (requestCode == 22 && resultCode == Activity.RESULT_OK) {

            avi.setImageURI(uri.getData());
            SharedPreferences.Editor e = data.edit();
            e.putString("avi", uri.getData().toString());
            e.apply();
//
//            String avi_name = String.valueOf(System.currentTimeMillis());
//            String avi_link = "https://cdn.eronville.com/avi/" + avi_name + ".jpeg";
//            String[] projection = {MediaStore.Images.Media.DATA};
//            Cursor cursor = cx.getContentResolver().query(uri.getData(), projection, null, null, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                int column = cursor.getColumnIndexOrThrow(projection[0]);
//                avi_path = cursor.getString(column);
//                cursor.close();
//            }
//            avi_progress.setVisibility(View.VISIBLE);
//
//            saveData(XClass.avi, avi_link);
//            XClass.updateUser("avi", avi_link, mail);
//            XClass.upload(fx, avi_progress, avi_path, avi, avi_name, "avi");
        }
        super.onActivityResult(requestCode, resultCode, uri);
    }

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle root) {
        View view = inflater.inflate(R.layout.fragment_profile, child, false);

         avi = view.findViewById(R.id.avatar);
        final Button save = view.findViewById(R.id.profile_toggle);
        final EditText surname = view.findViewById(R.id.profile_surname);
        final EditText othername = view.findViewById(R.id.profile_othername);
        EditText mailll = view.findViewById(R.id.profile_mail);
        final EditText line = view.findViewById(R.id.profile_number);

        String sur = data.getString("surname", "");
        String othe = data.getString("othername", "");
        String li = data.getString("line", "");
        String mal = data.getString("mail", "");

        surname.setText(sur);
        othername.setText(othe);
        mailll.setText(mal);
        line.setText(li);

        avi.setImageURI(data.getString("avi", ""));
        mailll.setEnabled(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mySurname = surname.getText().toString();
                String myOther = othername.getText().toString();
                String myLine = line.getText().toString();

                SharedPreferences.Editor e = data.edit();
                e.putString("surname", mySurname);
                e.putString("line", myLine);
                e.putString("othername", myOther);
                e.apply();
                save.setText("Saved");
            }
        });

        avi.setOnClickListener(v -> {
            Intent addImage = new Intent(Intent.ACTION_GET_CONTENT);
            addImage.setType("image/*");
            startActivityForResult(addImage, 22);
        });


        return view;
    }
}
