package com.merlin.androidtest.mvp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.merlin.androidtest.R;

/**
 * Created by zhouyang on 2017/3/10.
 */

public class MFragment extends Fragment {
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                Log.e("Merlin", "fragment is work!" + mPosition);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    int mPosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            mPosition = getArguments().getInt("position");
        }
    }

    private static Handler handler = new Handler();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser || !MyMainActivity.stayTime) {
//            handler.removeCallbacks(runnable);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item,container,false);
        TextView textView = (TextView) view.findViewById(R.id.item_tv);

        loadData();
        textView.setText(String.valueOf(mPosition));
        return view;
    }

    private void loadData() {
        handler.postDelayed(runnable, 500);
    }

    public void cancleHandler() {
//        handler.removeCallbacks(runnable);
    }
}
