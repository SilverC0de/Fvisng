package com.flux.Fvisng;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.navigation.NavigationView;

/* Drawer activity created by silver 2020 */
/* Use this activity as the main drawer activity */

public class ActivityView extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView nav;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorLighter));
        if (Build.VERSION.SDK_INT > 22) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initializeDrawer();
        bottomNavigation();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentHome()).commit();
    }

    private void initializeDrawer(){

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        nav = findViewById(R.id.navigation_view); //get footer from here
        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.app_name, R.string.app_name);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable menu = ResourcesCompat.getDrawable(getResources(), R.drawable.menu, getApplicationContext().getTheme());
        toggle.setHomeAsUpIndicator(menu);
        toggle.setToolbarNavigationClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));


        nav.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.nav_item_wallet:
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentWallet()).addToBackStack(null).commit();
                    break;
                case R.id.nav_item_repay:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentRepay()).commit();
                    break;
                case R.id.nav_item_bills:
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentVTU()).addToBackStack(null).commit();
                    break;
                case R.id.nav_item_transfer:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentTransfer()).commit();
                    break;
                case R.id.nav_item_spin:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentGame()).commit();
                    break;
                case R.id.nav_item_ref:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentRef()).commit();
                    break;
                case R.id.nav_item_sett:
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentSetting()).addToBackStack(null).commit();
                    break;
                case R.id.nav_item_lohout:
                    SharedPreferences data = getSharedPreferences("db", 0);
                    SharedPreferences.Editor e = data.edit();
                    e.putBoolean("online", false);
                    e.apply();

                    startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                    finish();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        nav.setItemIconTintList(null);
    }

    private void bottomNavigation(){
        final ImageView home = findViewById(R.id.home);
        final ImageView loan = findViewById(R.id.loan);
        final ImageView history = findViewById(R.id.history);
        final ImageView account = findViewById(R.id.profile);
        final SimpleDraweeView avi = findViewById(R.id.avi);


        SharedPreferences data = getSharedPreferences("db", 0);
        avi.setImageURI(data.getString("avi", ""));

        avi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentProfile()).addToBackStack(null).commit();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setImageResource(R.drawable.ic_home_active);
                loan.setImageResource(R.drawable.ic_wallet_inactive);
                history.setImageResource(R.drawable.ic_activities_inactive);
                account.setImageResource(R.drawable.ic_profile_inactive);

                getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentHome()).addToBackStack(null).commit();
            }
        });

        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setImageResource(R.drawable.ic_home_inactive);
                loan.setImageResource(R.drawable.ic_wallet_active);
                history.setImageResource(R.drawable.ic_activities_inactive);
                account.setImageResource(R.drawable.ic_profile_inactive);

                getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentLoan()).addToBackStack(null).commit();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setImageResource(R.drawable.ic_home_inactive);
                loan.setImageResource(R.drawable.ic_wallet_inactive);
                history.setImageResource(R.drawable.ic_activities_active);
                account.setImageResource(R.drawable.ic_profile_inactive);

                getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentHistory()).addToBackStack(null).commit();
            }
        });

//        account.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                home.setImageResource(R.drawable.ic_home_inactive);
//                loan.setImageResource(R.drawable.ic_wallet_inactive);
//                history.setImageResource(R.drawable.ic_activities_inactive);
//                account.setImageResource(R.drawable.ic_profile_active);
//
//                getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentMore()).addToBackStack(null).commit();
//            }
//        });
    }
}