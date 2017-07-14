package com.merlin.androidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.merlin.androidtest.R;
import com.merlin.network.CallBack;
import com.merlin.network.NetworkDelegate;

import java.util.HashMap;


public class MyMainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_main);
        addListener();
    }

    private void addListener() {

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkDelegate.getInstance().get(String.class, "http://www.weather.com.cn/data/cityinfo/101010100.html", new HashMap<String, Object>(), new CallBack<String>() {
                    @Override
                    public void onResponse(String response) {
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }
                });
            }
        });
    }


}
