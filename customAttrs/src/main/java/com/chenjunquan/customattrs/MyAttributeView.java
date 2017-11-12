package com.chenjunquan.customattrs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/11/12.
 */

public class MyAttributeView extends View {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private int age;
    private String name;
    private Bitmap bg;

    public MyAttributeView(Context context) {
        this(context, null, 0);
    }

    public MyAttributeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyAttributeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性三种方式
        //1.通过名称空间和属性名
        if (attrs != null) {
            String my_age = attrs.getAttributeValue(NAMESPACE, "my_age");
            String my_name = attrs.getAttributeValue(NAMESPACE, "my_name");
            String my_bg = attrs.getAttributeValue(NAMESPACE, "my_bg");
        }
        //2.循环遍历
        //3.系统工具
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyAttributeView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.MyAttributeView_my_name:
                    name =typedArray.getString(index);
                    break;
                case R.styleable.MyAttributeView_my_age:
                    age =typedArray.getInt(index,0);
                    break;
                case R.styleable.MyAttributeView_my_bg:
                    Drawable drawable = typedArray.getDrawable(index);
                    bg =((BitmapDrawable) drawable).getBitmap();
                    break;
            }

        }
        // 记得回收
        typedArray.recycle();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setTextSize(20);
        paint.setAntiAlias(true);
        canvas.drawText(name+age,50,50,paint);
        canvas.drawBitmap(bg,100,100,paint);
    }
}
