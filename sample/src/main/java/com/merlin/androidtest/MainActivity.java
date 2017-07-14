package com.merlin.androidtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.merlin.network.CallBack;
import com.merlin.network.NetworkDelegate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * User: Merlin
 * Date: 2017-2-16
 * Desc:
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 处理排序的标识位
     */
    private boolean mSwitch = true;

    private ListView mListView;

    private MAdapter adapter;

    private List<String> mContentData;

    private Button mSwitchBtn;

    private SimpleDraweeView simpleDraweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentData = new ArrayList<>();
        test();
        initView();
        addListener();



    }


    private void test() {
        String str = "";
        System.out.println(TextUtils.isEmpty(str));
        if(TextUtils.isEmpty(str)){
            Log.e("merlin","true_____");
        }else {
            Log.e("merlin","false_____");
        }



        String url = "http://ww3.sinaimg.cn/large/610dc034jw1f6m4aj83g9j20zk1hcww3.jpg";
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.sdv_1);
        ImageLoader.loadImage(simpleDraweeView, url);

        ImageLoader.loadImage((SimpleDraweeView) findViewById(R.id.sdv_2),url);
    }

    private void initView() {

        mListView = (ListView) findViewById(R.id.list_view);
        adapter = new MAdapter(this);
        mListView.setAdapter(adapter);

        mSwitchBtn = (Button) findViewById(R.id.title_btn_right);


    }

    private void initData() {
        for (int i = 0; i <= 450; i++) {
            BigInteger bigInteger = fib(i);
            if (bigInteger.compareTo(new BigInteger("362880")) == 1) {
                BigDecimal bigDecimal = new BigDecimal(bigInteger, new MathContext(5, RoundingMode.HALF_UP));
                mContentData.add(bigDecimal.toEngineeringString());
            } else {
                mContentData.add(bigInteger.toString());
            }
        }
    }

    private void addListener() {
        Lettle l = new Lettle();
        l.mL = 'm';
        Log.e("merlin__ml1", String.valueOf(l.mL));
        f(l);
        Log.e("merlin", String.valueOf(l.mL));


//        initData();
//
//        mSwitchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                resoveList();
//                adapter.setData(mContentData);
//            }
//        });
    }

    private BigInteger fib(int n) {
        if (0 == n) return new BigInteger("0");
        if (1 == n || 2 == n) return new BigInteger("1");
        BigInteger a = new BigInteger("1"), b = new BigInteger("1");
        BigInteger ret = new BigInteger("0");
        for (int i = 3; i <= n; ++i) {
            ret = a.add(b);
            b = a;
            a = ret;
        }
        return ret;
    }

    private void resoveList() {

        Collections.reverse(mContentData);
    }

    private void f(Lettle l) {
        l.mL = 'z';
    }

    public static class Lettle {
        char mL;
    }

}
