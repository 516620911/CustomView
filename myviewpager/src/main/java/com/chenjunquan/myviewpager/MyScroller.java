package com.chenjunquan.myviewpager;

import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Administrator on 2017/11/12.
 */

public class MyScroller {
    private float startY;
    private float startX;
    private int distanceX;
    private int distanceY;
    private long startTime;
    private long totalTime = 500;
    //是否移动完成
    private boolean isFinish;
    /**
     * 得到坐标
     */
    public float getCurrX() {
        return currX;
    }
    private float currX;

    /**
     * @duration 滑动时间
     */
    public void startScroll(float startX, float startY, int distanceX, int distanceY, int duration) {
        this.startY = startY;
        this.startX = startX;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.startTime = SystemClock.uptimeMillis();//系统开机时间
        this.isFinish = false;
        if (duration != 0)
            totalTime = duration;
    }

    /**
     * 计算本次移动的目标点
     * @return true正在移动 false移动结束
     */
    public boolean computeScrollOffset() {
        if (isFinish)
            return false;
        long endTime = SystemClock.uptimeMillis();
        Log.i("endTime",endTime+"");
        //移动一小段所花的时间
        long duration = endTime - startTime;
        if (duration < totalTime) {
            //还没有结束移动
            //计算平均速度
            float a = distanceX / totalTime;
            //移动一小段的距离=代码执行时间差*平均速度
            float sdistance = duration * distanceX / totalTime;
            //移动结束后的坐标
            currX=startX+sdistance;

        } else {
            //时间超过规定时间则强制移动到目标点结束整个移动
            isFinish = true;
            currX=startX+distanceX;
        }
        return true;
    }
}