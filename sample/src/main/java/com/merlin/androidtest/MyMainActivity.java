package com.merlin.androidtest.mvp.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.merlin.androidtest.R;
import com.merlin.network.CallBack;
import com.merlin.network.NetworkDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyMainActivity extends AppCompatActivity {
    public static boolean stayTime = false;
    private ViewPager viewPager;

    List<MFragment> fragments;

    private long lastTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        loadData();
        MAdapter adapter = new MAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setVisibility(View.GONE);

        addListener();
    }


    private void addListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0 ; i<fragments.size();i++){
                    if (i!=position){
                        fragments.get(position).cancleHandler();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    if (System.currentTimeMillis() - lastTime >= 500) {
                        stayTime = true;
                    }else {
                        stayTime = false;
                    }
                    lastTime = System.currentTimeMillis();
                }

            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String str = "meixian://com.meixian.shopkeeper?action=login&phone=1234&from=meixian_crm";
//                Uri uri = Uri.parse(str);
//                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                startActivity(intent);
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("app.soyoung://splash/open/1000")));

                NetworkDelegate.getInstance().get(String.class, "http://www.weather.com.cn/data/cityinfo/101010100.html", new HashMap<String, Object>(), new CallBack<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("merlin",response);
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }
                });
            }
        });
    }

    private void loadData() {
        fragments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("position",i+1);
            MFragment fragment =new MFragment() ;
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }



    }

    class MAdapter extends FragmentPagerAdapter {
        public MAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
