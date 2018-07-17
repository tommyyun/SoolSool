package com.example.q.soolsool;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static String id="on";
    public static boolean loggedIn = true;

    private static class MainHandler extends Handler {
        WeakReference<MainActivity> activity;

        MainHandler(MainActivity _activity) {
            activity = new WeakReference<>(_activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==0) {
                activity.get().findViewById(R.id.splash_image).setVisibility(View.GONE);
                activity.get().findViewById(R.id.main_layout).setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView daericall = (ImageView) findViewById(R.id.daericall);
        daericall.setOnClickListener(this);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(0);
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);


        final Handler handler = new MainHandler(this);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.arg1=0;
                handler.sendMessage(msg);
            }
        }.start();

    }

    @Override
    public void onClick(View v){
        Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:1577-1577"));
        startActivity(callIntent);
    }


    class TabAdapter extends FragmentPagerAdapter {


        TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0 : return new Tab1();
                case 1 : return new Tab2();
                case 2 : return new Tab3();
            }
            return null;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0 : return "Rooms";
                case 1 : return "Chats";
                case 2 : return "LogIn";
            }
            return null;
        }


        @Override
        public int getCount() {
            return 3;
        }
    }
}
