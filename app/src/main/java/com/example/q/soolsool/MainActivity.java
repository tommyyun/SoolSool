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
        final TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {
                return;
            }

            @Override
            public void onPageSelected(int i) {
                System.out.println(i+"selected");
                switch(i) {
                    case 0 : {
                        tabAdapter.reCreate1();
                        System.out.println(11);
                        break;
                    }
                    case 1 : {
                        tabAdapter.reCreate2();
                        System.out.println(22);
                        break;
                    }
                    case 2 : {
                        tabAdapter.reCreate3();
                        System.out.println(33);
                        break;
                    }
                    default : break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                return;
            }
        });


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

        private Tab1 tab1 = new Tab1();
        private Tab2 tab2 = new Tab2();
        private Tab3 tab3 = new Tab3();

        TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0 : return tab1;
                case 1 : return tab2;
                case 2 : return tab3;
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

        public void reCreate1() {
        }

        public void reCreate2() {
            tab2 = new Tab2();
        }

        public void reCreate3() {
            tab3 = new Tab3();
        }


        @Override
        public int getCount() {
            return 3;
        }
    }
}
