package com.ruanchuang.massorganizationsignin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ruanchuang.massorganizationsignin.designlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joho on 2016/5/29.
 */
public class SettingActivity extends AppCompatActivity {
    private ListView lv_loaction;
    private List<Loaction> lists;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        lv_loaction = (ListView) findViewById(R.id.lv_loaction);
        lists = new ArrayList<Loaction>();

        int length = sp.getInt("length",0);
        if(length == 0){
            Loaction loaction = new Loaction();
            loaction.setAddress("没有签到记录");
            lists.add(loaction);
        }
        for(int i=0;i<length;i++) {
            String address = sp.getString("address"+i,"");
            Loaction loaction = new Loaction();
            loaction.setAddress(address);
            lists.add(loaction);
        }
    }

    public void click_loaction(View view){
        lv_loaction.setAdapter(new MyAdapter());
    }

    //定义listview的数据适配器
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.item_location, null);
            } else {
                view = convertView;
            }

            TextView tv_location = (TextView) view.findViewById(R.id.tv_location);

            Loaction loaction = lists.get(position);
            String ads = loaction.getAddress();
            tv_location.setText(ads);

            return view;
        }

    }
}
