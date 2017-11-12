package com.chenjunquan.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 仿ViewPager
 * Created by Administrator on 2017/11/12.
 */

public class MyViewPager extends ViewGroup {
    public MyViewPager(Context context) {
        this(context,null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
