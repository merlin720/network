package com.merlin.androidtest;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by zhouyang on 2017/2/21.
 */

public class MService  extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private MyHanlder hanlder = new MyHanlder(this);

    private static class MyHanlder extends Handler{
        WeakReference<MService> weakReference ;
        public MyHanlder(MService mService){
            weakReference = new WeakReference<MService>(mService);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MService mService = weakReference.get();
            if (mService!=null){

            }
        }
    }
}
