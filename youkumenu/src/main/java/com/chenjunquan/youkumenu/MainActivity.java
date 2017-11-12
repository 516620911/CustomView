package com.chenjunquan.youkumenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenjunquan.youkumenu.utils.DensityUtil;
import com.chenjunquan.youkumenu.utils.Tools;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout level3;
    private ImageView icon_menu;
    private RelativeLayout level2;
    private RelativeLayout level1;
    private ImageView icon_home;
    private ViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    /**
     * 是否显示第一级圆环
     */
    private boolean isShowLevel1 = true;
    /**
     * 是否显示第二级圆环
     */
    private boolean isShowLevel2 = true;
    /**
     * 是否显示第三级圆环
     */
    private boolean isShowLevel3 = true;
    // 图片资源ID
    private final int[] imageIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e};
    private final String[] imageDescriptions = {
            "尚硅谷波河争霸赛！",
            "凝聚你我，放飞梦想！",
            "抱歉没座位了！",
            "7月就业名单全部曝光！",
            "平均起薪11345元"
    };
    private ArrayList<ImageView> mImageViews;

    /**
     * 上一次高亮显示的位置
     */
    private int prePositon = 0;
    /**
     * 是否已经滚动
     */
    private boolean isDragging = false;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //自动滑动
            int item = viewpager.getCurrentItem() + 1;
            viewpager.setCurrentItem(item);
            mHandler.sendEmptyMessageDelayed(0, 4000);
            super.handleMessage(msg);
        }
    };
    //下拉框
    private PopupWindow mPopupWindow;
    private EditText et_input;
    private ImageView iv_down_arrow;
    private ListView listview;
    private ArrayList<String> msgs;
    private MypopupAdapter mMypopupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        level3 = (RelativeLayout) findViewById(R.id.level3);
        level2 = (RelativeLayout) findViewById(R.id.level2);
        level1 = (RelativeLayout) findViewById(R.id.level1);
        icon_home = (ImageView) findViewById(R.id.icon_home);
        icon_menu = (ImageView) findViewById(R.id.icon_menu);
        tv_title = findViewById(R.id.tv_title);
        ll_point_group = findViewById(R.id.ll_point_group);
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        //设置点击事件
        icon_home.setOnClickListener(myOnClickListener);
        icon_menu.setOnClickListener(myOnClickListener);
        level1.setOnClickListener(myOnClickListener);
        level2.setOnClickListener(myOnClickListener);
        level3.setOnClickListener(myOnClickListener);

        mImageViews = new ArrayList<>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            mImageViews.add(imageView);

            //添加点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            int px=DensityUtil.dip2px(getApplicationContext(),8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(px, px);
            if (i == 0) {
                //第一个默认点击即press红色
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                //设置间隔
                params.leftMargin = 8;
            }

            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        MyPageAdapter myPageAdapter = new MyPageAdapter();
        viewpager.setAdapter(myPageAdapter);
        //设置中间位置解决不能往左滑的问题
        //保证mImageViews的整数倍
        int middle = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mImageViews.size();
        middle = mImageViews.size() * 50;
        viewpager.setCurrentItem(middle);

        tv_title.setText(imageDescriptions[prePositon]);
        mHandler.sendEmptyMessageDelayed(0, 3000);
        //设置监听ViewPager页面的改变
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 页面正在被滑动
             * @param position 当前页面的位置
             * @param positionOffset 滑动页面的百分比
             * @param positionOffsetPixels 滑动的像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Log.i("onPageScrolled", "onPageScrolled");

            }

            /**
             * 完全滑动到某个页面
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                //Log.i("onPageSelected", "onPageSelected");
                //循环
                position = position % mImageViews.size();
                //设置文本信息
                tv_title.setText(imageDescriptions[position]);
                //设置点的状态变化
                //上一个变灰
                ll_point_group.getChildAt(prePositon).setEnabled(false);
                prePositon = position;
                ll_point_group.getChildAt(position).setEnabled(true);
            }

            /**
             * 页面滚动状态开始变化(滑动->静止 静止->滑动 静止->拖拽)
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                    /*SCROLL_STATE_IDLE：空闲状态
                    SCROLL_STATE_DRAGGING：滑动状态
                    SCROLL_STATE_SETTLING：滑动后自然沉降的状态*/
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    //手动拖拽的时候不让他自动换页
                    mHandler.removeCallbacksAndMessages(null);
                    isDragging = true;
                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
                    isDragging = false;
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(0, 4000);
                }
            }
        });


        //下拉框
        iv_down_arrow = findViewById(R.id.down_arrow);
        et_input = findViewById(R.id.et_input);
        iv_down_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow == null) {
                    mPopupWindow = new PopupWindow(MainActivity.this);
                    mPopupWindow.setWidth(et_input.getWidth());
                    int height= DensityUtil.dip2px(getApplicationContext(),500);
                    mPopupWindow.setHeight(height);
                    mPopupWindow.setContentView(listview);
                    mPopupWindow.setFocusable(true);

                }
                mPopupWindow.showAsDropDown(et_input, 0, 0);
            }
        });
        msgs=new ArrayList<>();
        for(int i=0;i<50;i++){
            msgs.add(i+"123456789abcdefghijk");
        }
        listview=new ListView(this);
        mMypopupAdapter = new MypopupAdapter();
        listview.setAdapter(mMypopupAdapter);
        listview.setBackgroundResource(R.drawable.listview_background);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg=msgs.get(position);
                et_input.setText(msg);
                if(mPopupWindow!=null&&mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                    mPopupWindow=null;
                }
            }
        });
    }

    class MyOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.level1:
                    Toast.makeText(getApplicationContext(), "level1", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.level2:
                    Toast.makeText(getApplicationContext(), "level2", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.level3:
                    Toast.makeText(getApplicationContext(), "level3", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.icon_home://home图标
                    //如果三级菜单和二级菜单显示则都隐藏
                    if (isShowLevel2) {
                        //隐藏二级菜单
                        isShowLevel2 = false;
                        Tools.hideViewbyObjectAnimator(level2);
                        if (isShowLevel3) {
                            //隐藏三级菜单
                            isShowLevel3 = false;
                            Tools.hideViewbyObjectAnimator(level3, 200);
                        }
                    } else {
                        //如果都是隐藏则显二级菜单
                        isShowLevel2 = true;
                        Tools.showViewbyObjectAnimator(level2);
                    }
                    break;
                case R.id.icon_menu:
                    if (isShowLevel3) {
                        //隐藏
                        isShowLevel3 = false;
                        Tools.hideViewbyObjectAnimator(level3);
                    } else {
                        isShowLevel3 = true;
                        Tools.showViewbyObjectAnimator(level3);
                    }
                    break;
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //如果全部菜单都显示,则全部隐藏
            if (isShowLevel1) {
                isShowLevel1 = false;
                Tools.hideViewbyObjectAnimator(level1);
                if (isShowLevel2) {
                    //隐二级菜单
                    isShowLevel2 = false;
                    Tools.hideViewbyObjectAnimator(level2, 200);
                    if (isShowLevel3) {
                        isShowLevel3 = false;
                        Tools.hideViewbyObjectAnimator(level2, 400);
                    }
                }
            } else {
                //如果一级,二级菜单是隐藏就显示
                isShowLevel1 = true;
                Tools.showViewbyObjectAnimator(level1);
                isShowLevel2 = true;
                Tools.showViewbyObjectAnimator(level2, 200);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //无限循环滚动次数
            //return mImageViews.size();
            return 1000;
        }

        /**
         * 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
         *
         * @param view   页面
         * @param object instantiateItem()返回的值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可0
         *
         * @param container ViewPager自身
         * @param position  实例化位置
         * @return
         */
        //相当于getView()预加载
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //Log.i("instantiateItem", "instantiateItem" + position);
            //循环 去模
            ImageView imageView = mImageViews.get(position % mImageViews.size());
            container.addView(imageView);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Log.i("ACTION_DOWN", "ACTION_DOWN");
                            //手指按下移除消息发送
                            mHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_MOVE:

                            break;
                        case MotionEvent.ACTION_CANCEL:
                            Log.i("ACTION_CANCEL", "ACTION_CANCEL");
                            /*mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendEmptyMessageDelayed(0, 4000);*/
                            break;
                        case MotionEvent.ACTION_UP:
                            //Log.i("ACTION_UP", "ACTION_UP");
                            //重新启动消息
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendEmptyMessageDelayed(0, 4000);
                            break;
                    }
                    //返回true代表事件被消费完了
                    return false;
                }
            });

            imageView.setTag(position % mImageViews.size());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    String text = imageDescriptions[position];
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
            });
            return imageView;
        }

        /**
         * PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
         *
         * @param container viewpager
         * @param position  释放的位置
         * @param object    要释放的页面()
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i("destroyItem", "destroyItem" + position + object);
        }
    }

    private class MypopupAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public Object getItem(int position) {
            return msgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView  == null){
                convertView = View.inflate(MainActivity.this,R.layout.item_main,null);
                viewHolder = new ViewHolder();
                viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到数据
            final String msg = msgs.get(position);
            viewHolder.tv_msg.setText(msg);

            //设置删除
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.从集合中删除
                    msgs.remove(msg);
                    //2.刷新ui-适配器刷新
                    mMypopupAdapter.notifyDataSetChanged();//getCount()-->getView();

                }
            });
            return convertView;
        }

    }
    static class ViewHolder{
        TextView tv_msg;
        ImageView iv_delete;
    }
}
