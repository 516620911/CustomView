package com.chenjunquan.youkumenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 1.构造方法事例类
 * 2.测量 measure(int,int)->重写onMeasure()
 * 如果是继承Viewgroup还要测量孩子控件以及layout布局指定位置
 * 绘制draw->onDraw
 * Created by Administrator on 2017/11/12.
 */

public class MyToggleButton extends View {

    private Bitmap backgroundBitmap;
    private Bitmap sildingBitmap;
    private int mSlidLeftMax;
    private Paint paint;
    private boolean isOpen = false;
    //距离左边的值
    private int slideLeft;
    //点击事件是否生效
    private boolean isEnableClick = true;

    public MyToggleButton(Context context) {
        this(context, null);
    }

    public MyToggleButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToggleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        sildingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        mSlidLeftMax = backgroundBitmap.getWidth() - sildingBitmap.getWidth();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnableClick) {
                    //点击事件生效
                    isOpen = !isOpen;
                    flushView();
                }
            }
        });

    }

    private float startX;
    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = startX = event.getX();
                isEnableClick = true;
                break;
            case MotionEvent.ACTION_MOVE:

                float endX = event.getX();
                //计算偏移量
                float distanceX = endX - startX;
                //
                slideLeft += distanceX;
                //防止越界
                if (slideLeft < 0)
                    slideLeft = 0;
                else if (slideLeft > mSlidLeftMax)
                    slideLeft = mSlidLeftMax;
                //刷新
                invalidate();
                //还原位置
                startX = event.getX();
                if (Math.abs(endX - lastX) > 5) {
                    //认为滑动事件生效 点击事件屏蔽
                    isEnableClick = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                //滑动一点点的判断
                if (!isEnableClick) {
                    //点击事件不生效 滑动事件生效
                    if (slideLeft > mSlidLeftMax / 2)
                        isOpen = true;
                    else
                        isOpen = false;
                    flushView();
                }
                break;
        }
        return true;
    }

    private void flushView() {
        if (isOpen)
            slideLeft = mSlidLeftMax;
        else
            slideLeft = 0;
        //重新绘制
        invalidate();
    }

    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        canvas.drawBitmap(sildingBitmap, slideLeft, 0, paint);
    }
}
