package com.chenjunquan.myviewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private MyViewPager myViewPager;
    private int[] ids = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6};
    private RadioGroup rg_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //隐藏标题栏
        getSupportActionBar().hide();
        myViewPager = findViewById(R.id.myViewPager);
        //添加图片到自定义ViewPager
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            //添加到MyViewPager
            myViewPager.addView(imageView);
        }
        View view = View.inflate(getApplicationContext(), R.layout.test, null);
        myViewPager.addView(view,2);
        //给ViewPager头部添加RadioGroup
        rg_main = findViewById(R.id.rg_main);
        for (int i = 0; i < myViewPager.getChildCount(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            //默认选中第一个
            if (i == 0)
                radioButton.setChecked(true);
            //把radioButton添加到radioGroup中
            rg_main.addView(radioButton);
        }
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //checkedId取值范围0-myViewPager.getChildCount()-1
                myViewPager.scrollToPager(checkedId);
            }
        });
        //设置自定义监听器监听页面的改变然后修改radioButton的check
        myViewPager.setOnPageChangeListener(new MyViewPager.onPageChangeListener() {
            @Override
            public void onChangeRadioButton(int position) {
                rg_main.check(position);
            }
        });
    }
}


