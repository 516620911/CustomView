package com.chenjunquan.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 绘制快速索引的字母
 * 1.把26个字母放入数组
 * 2.在onMeasure计算每条的高itemHeight和宽itemWidth,
 * 3.在onDraw和wordWidth,wordHeight,wordX,wordY
 * <p/>
 * 手指按下文字变色
 * 1.重写onTouchEvent(),返回true,在down/move的过程中计算
 * int touchIndex = Y / itemHeight; 强制绘制
 * <p/>
 * 2.在onDraw()方法对于的下标设置画笔变色
 * <p/>
 * 3.在up的时候
 * touchIndex  = -1；
 * 强制绘制
 * Created by Administrator on 2017/11/13.
 */

public class IndexView extends View {
    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private int itemWidth;
    private int itemHeight;
    private Paint mPaint;

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setTextSize(25);
        //粗体
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //单个字母item
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {
            //如果是点击选中的索引字母改变其颜色
            if (touchIndex == i) {
                mPaint.setColor(Color.RED);
            } else {
                mPaint.setColor(0xFF19CAAD);
            }
            String word = words[i];
            Rect rect = new Rect();
            /*!!!返回能完整包裹这个字符串(用指定paint画笔来绘制的)的最小矩形!!!
                   我们可以通过这个方法求某个字符串绘制所占的空间大小
             */
            mPaint.getTextBounds(word, 0, 1, rect);
            //通过上述矩形来确定绘制字符所需要长宽以确定绘制的位置
            int wordWidth = rect.width();
            int wordHeight = rect.height();
            //每个索引字母的左下角X位置为
            float wordX = (itemWidth - wordWidth) / 2;
            //Y位置为
            float wordY = itemHeight / 2 + wordHeight / 2 + itemHeight * i;
            //此方法参照点为View左下角
            canvas.drawText(word, wordX, wordY, mPaint);
        }
    }

    //选中字母的位置
    private int touchIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //按下和移动所处理的逻辑是相同的
            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_MOVE:
                //获取点击的索引
                float y = event.getY();
                int index = (int) (y / itemHeight);
                if (index != touchIndex) {
                    touchIndex = index;
                    //通过重绘来将点击的索引的颜色改变
                    invalidate();
                    if (mOnIndexChangeListener != null && touchIndex < words.length) {
                        mOnIndexChangeListener.OnIndexChange(words[touchIndex]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;

    }

    //字母下标发生变化的回调接口
    public interface OnIndexChangeListener {
        void OnIndexChange(String word);
    }

    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener) {
        mOnIndexChangeListener = onIndexChangeListener;
    }

    private OnIndexChangeListener mOnIndexChangeListener;

}
