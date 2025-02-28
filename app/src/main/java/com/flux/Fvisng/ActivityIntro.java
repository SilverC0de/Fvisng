package com.flux.Fvisng;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ActivityIntro extends AppCompatActivity {

    ViewPager slide;
    Button skip, next;
    LinearLayout layout;
    int[] slides;
    TextView[] dots;
    SlideAdapter slideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        slide = findViewById(R.id.view_pager);
        layout = findViewById(R.id.layoutDots);
        skip = findViewById(R.id.btn_skip);
        next = findViewById(R.id.btn_next);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorLighter));
        if (Build.VERSION.SDK_INT > 22) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        slides = new int[]{
                R.layout.slide1,
                R.layout.slide2,
                R.layout.slide3,
                R.layout.slide4
        };





        slideAdapter = new SlideAdapter();
        slide.setAdapter(slideAdapter);
        slide.addOnPageChangeListener(slideListener);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem();
                if (current < slides.length) {
                    slide.setCurrentItem(current);
                } else {
                    startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                    finish();
                }
            }
        });
    }



    private void addBottomDots(int currentPage) {
        dots = new TextView[slides.length];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        layout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            dots[i] = new TextView(this);
            dots[i].setText(String.format("%s", Html.fromHtml("  " + getResources().getString(R.string.bullet)) + "  "));
            dots[i].setTextSize(22);
            dots[i].setGravity(Gravity.CENTER);
            dots[i].setLayoutParams(params);
            dots[i].setTextColor(colorsInactive[currentPage]);
            layout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem() {
        return slide.getCurrentItem() + 1;
    }

    ViewPager.OnPageChangeListener slideListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == slides.length - 1) {
                next.setText("Start");
                skip.setVisibility(View.GONE);
                //no pages are left
            } else {
                next.setText("Next");
                skip.setVisibility(View.VISIBLE);
                //some pages are left
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}
        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };

    public class SlideAdapter extends PagerAdapter {

        SlideAdapter() {}

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(slides[position], container, false);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return slides.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {

            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}