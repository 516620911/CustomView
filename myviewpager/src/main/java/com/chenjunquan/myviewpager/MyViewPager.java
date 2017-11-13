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
 * 三.这个ViewPager的触摸事件执行流程
     * 1.手按下 按下这一事件传递 到onInterceptTouchEvent()方法用于判断是否拦截
     * 2.然后传给mGestureDetector.onTouchEvent(event)方法 此类辅助判断手势类型(双击,滑动,长按)
     * 3.switch语句执行后,手按下这一事件结束
     * 4.接着手滑动 这一事件传递流程同上
     * 5.当传递给手势识别器之后,识别器判断这两次事件为滑动事件
     * 6.执行滑动事件方法 将ViewPager滑动
     * (我们的ViewPager其实是一次性把几张图片合并成一个大图片 只是屏幕大小固定所以我们只能看到固定大小的位置)
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
        //手势识别器只是用来识别手势的类型
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

    //测量子View,否则无法显示子View
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
        }
        int size = MeasureSpec.getSize(widthMeasureSpec);

        int mode = MeasureSpec.getMode(widthMeasureSpec);

        System.out.println(size + "   :   " + mode);
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

    /**
     * ViewGroup特有的拦截方法
     *
     * @param event
     * @return true 表示拦截点击事件强制触发当前控件的onTouchEvent()方法
     * false 表示正常继续传递给子View
     */
    private float downX;
    private float downY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //好像这样也可以啊????
        //onTouchEvent(ev);
        /*改良方案? 根据不同的滑动方向判断是否拦截
        * 左右滑动 拦截 返回true 即让onTouchEvent()执行可以滑动ViewPager
        * 上下滑动 不拦截 返回false 即让子View消费事件
        *  */
        mGestureDetector.onTouchEvent(event);
        boolean result = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录开始位置
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //结束位置
                float endX = event.getX();
                float endY = event.getY();
                float distanceX = Math.abs(endX - downX);
                float distanceY = Math.abs(endY - downY);
                //左右滑动
                if (distanceX > distanceY && distanceX > 10)
                    result = true;
                /*downX=endX;
                downY=endY;*/
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return result;
    }

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
