package com.merlin.androidtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyang on 2017/2/16.
 */

public class MAdapter extends BaseAdapter {
    private List<String> mContentData;
    private LayoutInflater inflater;

    public MAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContentData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                mContentData.add("0");
            } else if (i == 1) {
                mContentData.add("1");
            } else {
                mContentData.add(new BigInteger(mContentData.get(i - 1)).add(new BigInteger(mContentData.get(i - 2))).toString());
            }
        }
    }

    public void setData(List<String> data){
        mContentData.clear();
        mContentData.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return 450;
    }

    @Override
    public Object getItem(int position) {
        return mContentData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        final int p = position;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (p == position && p > mContentData.size() - 1) {
            BigInteger bigInteger = new BigDecimal(mContentData.get(position - 1)).add(new BigDecimal(mContentData.get(position - 2))).toBigInteger();
            if (bigInteger.compareTo(new BigInteger("362880")) == 1) {
                BigDecimal bigDecimal = new BigDecimal(bigInteger, new MathContext(5, RoundingMode.HALF_UP));
                mContentData.add(bigDecimal.toEngineeringString());
            } else {
                mContentData.add(bigInteger.toString());
            }
        }
        holder.tv.setText(mContentData.get(position));
        return convertView;
    }

    static class Holder {
        private TextView tv;
    }

}
