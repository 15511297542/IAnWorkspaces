package com.ruanchuang.massorganizationsignin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ruanchuang.massorganizationsignin.designlibrary.R;
import com.runachuang.massorganizationsignin.utils.LocationDB;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by joho on 2016/5/29.
 */
public class SignLocationActivity extends AppCompatActivity {
    private ListView lv_loaction;
    private List<Loactionbeen> lists;
    private SharedPreferences sp;
    protected LoadToast lt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signlocation);
        lv_loaction = (ListView) findViewById(R.id.lv_loaction);
        lists = new ArrayList<Loactionbeen>();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        String username = sp.getString("username","");

        lt = new LoadToast(this);
        lt.setText("加载数据中...").setTranslationY(100).show();

        BmobQuery<LocationDB> query = new BmobQuery<LocationDB>();
        //查询LocationDB叫 username 的数据
        query.addWhereEqualTo("userName", username);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<LocationDB>() {
            @Override
            public void done(List<LocationDB> object, BmobException e) {
                if(e==null){
                    Log.i("bmob","查询成功：共"+object.size()+"条数据。");
                    for (LocationDB locationDB : object) {
                        Loactionbeen location = new Loactionbeen();
                        location.setAddress(locationDB.getLocation());
                        lists.add(location);
                    }
                    lt.success();
                    lv_loaction.setAdapter(new MyAdapter());
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    lt.error();
                }
            }
        });
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

            Loactionbeen loactionbeen = lists.get(position);
            String ads = loactionbeen.getAddress();
            tv_location.setText(ads);

            return view;
        }

    }
}
