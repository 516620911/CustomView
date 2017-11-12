package com.chenjunquan.myviewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private MyViewPager myViewPager;
    private int[] ids = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myViewPager = findViewById(R.id.myViewPager);
        //添加图片到自定义ViewPager
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView=new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            //添加到MyViewPager
            myViewPager.addView(imageView);
        }
    }
}
