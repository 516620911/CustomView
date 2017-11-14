package com.chenjunquan.waterripple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by JunquanChen on 2017/11/14.
 */

public class RippleView extends View {

    private Paint mPaint;
    private int radio;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            radio += 5;
            int alpha = mPaint.getAlpha();
            alpha -= 5;
            if (alpha < 0) {
                alpha = 0;
            }
            mPaint.setAlpha(alpha);
            mPaint.setStrokeWidth(radio / 3);
            invalidate();
            super.handleMessage(msg);
        }
    };
    private float downX;
    private float downY;

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        radio = 5;
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        //样式圆环
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(radio / 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint.getAlpha() > 0 && downX > 0) {
            canvas.drawCircle(downX, downY, radio, mPaint);
            mHandler.sendEmptyMessageDelayed(0, 50);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                initView();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }
}
