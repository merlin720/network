package com.merlin.network.internal.utils;

import android.content.Context;

/**
 * User: Simon
 * Date: 2016/1/20
 * Desc:
 */
public class Config {

    private static Context CONTEXT;

    private static boolean DEBUG = true;


    public static  void setDebug(boolean debug){
        DEBUG = debug;
    }

    public static boolean isDebuggable(){
        return DEBUG;
    }

    public static void setContext(Context context){
        CONTEXT = context;
    }

    public static Context getContext() {
        return CONTEXT;
    }


}
