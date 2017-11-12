package com.chenjunquan.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 仿ViewPager
 * 一.手势识别器
 * 1.实例化
 * 2.把OnTouchEvent()传递给手势识别器
 * 二.Scroller原理
 * 每次移动一小段距离
 * 距离的计算方式为
 * 负责移动逻辑的这段代码执行的微小时间差*平均速度
 * 依托invalidate 会导致onDraw和computeScroll的执行这一特性实现
 * Created by Administrator on 2017/11/12.
 */

public class MyViewPager extends ViewGroup {

    private GestureDetector mGestureDetector;

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(context);
        mScroller = new Scroller(context);
    }

    private void initUI(Context context) {
        //1.实例化并重写监听器方法
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            /**
             * @param e1 按下事件
             * @param e2    松开事件
             * @param distanceX     X移动距离
             * @param distanceY     Y移动距离
             * @return
             */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //X轴可以移动,Y轴固定
                scrollBy((int) distanceX, 0);
                //Log.i(distanceX + "", "" + getScrollX());
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //双击
                return super.onDoubleTap(e);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //遍历孩子,给每个孩子指定在屏幕的坐标位置(以屏幕为基准)(消耗内存?)
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.layout(i * getWidth(), 0, (i + 1) * getWidth(), getHeight());
        }
    }

    private float startX;
    //当前页面的下标位置
    private int currentIndex;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //2.把OnTouchEvent()传递给手势识别器(因为它本身不响应事件)
        super.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录开始位置
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                //当前每次移动新的坐标
                float endX = event.getX();
                int tempIndex = currentIndex;
                if ((startX - endX) > getWidth() / 2) {
                    tempIndex++;
                } else if (-(startX - endX) > getWidth() / 2) {
                    tempIndex--;
                }
                scrollToPager(tempIndex);
                break;
        }
        return true;
    }

    //Android自带的Scroller,可以实现在一段时间内滑动一段距离
    private Scroller mScroller;
    //自定义Scroller
    private MyScroller mMyScroller = new MyScroller();

    /**
     * 屏蔽非法值
     * 滑动到指定页面
     *
     * @param tempIndex
     */
    public void scrollToPager(int tempIndex) {
        if (tempIndex < 0)
            tempIndex = 0;
        if (tempIndex > getChildCount() - 1)
            tempIndex = getChildCount() - 1;
        currentIndex = tempIndex;
        if (mOnPageChangeListener != null)
        mOnPageChangeListener.onChangeRadioButton(currentIndex);
        //需要缓慢移动的距离(getScrollX() 就是当前view的左上角相对于母视图的左上角的X轴偏移量)
        int distance = currentIndex * getWidth() - getScrollX();
        //Android自带的Scroller可以实现渐变滑动
        //Math.abs(distance)可以解决多图片滑动最后一张图的缓冲效果
        mScroller.startScroll(getScrollX(), 0, distance, 0, Math.abs(distance));
        //使用自定义的Scroller实现自动渐变滑动
        //mMyScroller.startScroll(getScrollX(), 0, distance, 0,500);
        //invalidate 会导致onDraw和computeScroll的执行，该方法是空的
        invalidate();
        //立即移动到指定位置
        //scrollTo(currentIndex * getWidth(), getScrollY());


    }

    @Override

    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float curX = mScroller.getCurrX();
            scrollTo((int) curX, 0);
            /**
             * 会导致computeScroll 的执行
             */
            invalidate();
        }
    }

    /**
     * 监听页面的改变
     */
    public interface onPageChangeListener {
        //当前页面下标
        void onChangeRadioButton(int position);
    }

    public onPageChangeListener mOnPageChangeListener;

    //将用户传入的监听对象赋值
    public void setOnPageChangeListener(onPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }
}
