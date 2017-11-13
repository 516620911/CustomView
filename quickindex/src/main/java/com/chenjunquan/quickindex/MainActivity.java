package com.chenjunquan.quickindex;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler();
    private ArrayList<Person> persons;
    private IndexAdapter mIndexAdapter;
    TextView tv_word;
    private ListView lv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_word = findViewById(R.id.tv_word);
        IndexView iv_words = findViewById(R.id.iv_words);
        lv_main = (ListView) findViewById(R.id.lv_main);
        iv_words.setOnIndexChangeListener(new IndexView.OnIndexChangeListener() {
            @Override
            public void OnIndexChange(String word) {
                //更新显示的类ToastTextView
                updateWord(word);
                updateListView(word);
            }
        });

        //准备数据
        initData();
        //设置适配器
        mIndexAdapter = new IndexAdapter();
        lv_main.setAdapter(mIndexAdapter);

    }

    //根据点击的索引和集合中的姓名首字母相比较调到指定位置
    private void updateListView(String word) {
        for (int i = 0; i < persons.size(); i++) {
            String listWord = persons.get(i).getPinyin().substring(0, 1);
            if (word.equals(listWord)) {
                lv_main.setSelection(i);
                return;
            }
        }
    }

    private void updateWord(String word) {
        tv_word.setVisibility(View.VISIBLE);
        tv_word.setText(word);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_word.setVisibility(View.GONE);
            }
        }, 2000);
    }

    static class ViewHolder {
        TextView tv_index;
        TextView tv_name;
    }

    class IndexAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public Object getItem(int position) {
            return persons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_main, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String name = persons.get(position).getName();
            String pinyin = persons.get(position).getPinyin().substring(0, 1);
            viewHolder.tv_index.setText(pinyin);
            viewHolder.tv_name.setText(name);
            //取得前一个位置对应的索引首字母  所以当前和前者相同则隐藏
            if (position == 0) {
                viewHolder.tv_index.setVisibility(View.VISIBLE);
            } else {
                String index = persons.get(position - 1).getPinyin().substring(0, 1);
                if (index.equals(pinyin)) {
                    viewHolder.tv_index.setVisibility(View.GONE);
                } else {
                    viewHolder.tv_index.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        }
    }

    private void initData() {

        persons = new ArrayList<>();
        persons.add(new Person("张晓飞"));
        persons.add(new Person("杨光福"));
        persons.add(new Person("胡继群"));
        persons.add(new Person("刘畅"));

        persons.add(new Person("钟泽兴"));
        persons.add(new Person("尹革新"));
        persons.add(new Person("安传鑫"));
        persons.add(new Person("张骞壬"));

        persons.add(new Person("温松"));
        persons.add(new Person("李凤秋"));
        persons.add(new Person("刘甫"));
        persons.add(new Person("娄全超"));
        persons.add(new Person("张猛"));

        persons.add(new Person("王英杰"));
        persons.add(new Person("李振南"));
        persons.add(new Person("孙仁政"));
        persons.add(new Person("唐春雷"));
        persons.add(new Person("牛鹏伟"));
        persons.add(new Person("姜宇航"));

        persons.add(new Person("刘挺"));
        persons.add(new Person("张洪瑞"));
        persons.add(new Person("张建忠"));
        persons.add(new Person("侯亚帅"));
        persons.add(new Person("刘帅"));

        persons.add(new Person("乔竞飞"));
        persons.add(new Person("徐雨健"));
        persons.add(new Person("吴亮"));
        persons.add(new Person("王兆霖"));

        persons.add(new Person("阿三"));
        persons.add(new Person("李博俊"));


        //排序
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person lhs, Person rhs) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });

    }
}
