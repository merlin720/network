package com.merlin.androidtest;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.merlin.network.NetworkMgr;

/**
 * Created by zhouyang on 2017/2/28.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {


        NetworkMgr.getInstance().init(this,true);

        super.onCreate();
    }
}
