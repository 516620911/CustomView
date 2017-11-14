package com.chenjunquan.slidingmenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by JunquanChen on 2017/11/14.
 */

public class SlideLayout extends FrameLayout {

    private View contentView;
    private View menuView;
    private int menuWidth;
    private int contentWidth;
    private int viewHeight;
    private float startX;
    private float startY;
    private Scroller mScroller;

    private float downX;//整个滑动事件的起始值
    private float downY;

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    //布局文件加载完成回调
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i("onFinishInflate", "onFinishInflate");
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //此方法系统会根据布局文件里面设置的属性值计算相应的宽高
        menuWidth = menuView.getMeasuredWidth();
        contentWidth = contentView.getMeasuredWidth();
        viewHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight);
    }

    /**
     * @param event
     * @return true 拦截 执行当前控件的onTouchEvent方法
     * false 不拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = startX = event.getX();
                if(onStateChangeListener != null){
                    onStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                Log.i("onInterceptTouchEvent", endX + "");
                startX = event.getX();
                //滑动总距离
                float totalDistanceX = Math.abs(endX - downX);
                /*此方法和onTouchEvent并不是同时执行
                    在满足拦截条件之后
                        本次滑动事件剩下的动作就会全权交给onTouchEvent方法
                * */
                if (totalDistanceX > 8) {
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = startX = event.getX();
                downY = startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                Log.i("onTouchEvent", endX + "");
                /*
                *手指从右往左滑动
                * X轴的差值为负数(左-右)
                * 此时我们需要的效果是 手机屏幕从左向右移动同样的距离
                * 而手机屏幕从左向右移动同样的距离 此距离向量为正数
                * 所以手指滑动求出的X轴差值distance需要取反-distance
                * 然后根据公式distanceX=getScrollX2(移动完成之后目标)-getScrollX1(移动前的位置)
                * 带入得出 getScrollX2=-distance+getScrollX1
                */

                float distance = endX - startX;

                int toScollX = (int) (getScrollX() - distance);
                Log.i(getScrollX() + "", distance + "");
                if (toScollX < 0) {
                    toScollX = 0;
                } else if (toScollX > menuWidth) {
                    toScollX = menuWidth;
                }
                scrollTo(toScollX, 0);
                startX = event.getX();
                startY = event.getY();
                //滑动总距离
                float totalDistanceX = Math.abs(endX - downX);
                float totalDistanceY = Math.abs(endY - downY);
                if (totalDistanceX > totalDistanceY && totalDistanceX > 8) {
                    //左右滑动距离大于上下滑动 响应侧滑事件 反拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                int totalScrollX = getScrollX();
                if (totalScrollX < menuWidth / 2) {
                    //隐藏右边
                    closeMenu();
                } else {
                    //显示右边
                    openMenu();
                }
                break;
        }
        return true;

    }

    private void openMenu() {
        int distanceX = menuWidth - getScrollX();
        mScroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY(), 500);
        invalidate();
        if(onStateChangeListener != null){
            onStateChangeListener.onOpen(this);
        }
    }

    // distanceX=getScrollX2(移动完成之后目标)-getScrollX1(移动前的位置)
    public void closeMenu() {
        int distanceX = 0 - getScrollX();
        mScroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY(), 500);
        invalidate();
        if(onStateChangeListener != null){
            onStateChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public interface OnStateChangeListener {
        void onClose(SlideLayout slideLayout);

        void onDown(SlideLayout slideLayout);

        void onOpen(SlideLayout slideLayout);
    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }
}
