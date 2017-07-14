package com.merlin.androidtest.mvp.presenter;

import java.util.HashMap;

/**
 * Created by zhouyang on 2017/3/6.
 */

public class MainIteractor {

    public void loadData(int a ,int b,MainLoadListener loadListener){

        HashMap<String,Integer> map = new HashMap();
        map.put("a",a);


    }

    public void pay(){

    }

    interface MainLoadListener{
        void success(String s);
        void requestFailiure(String s);
    }
}
