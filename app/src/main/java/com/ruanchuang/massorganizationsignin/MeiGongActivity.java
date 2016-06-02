package com.ruanchuang.massorganizationsignin;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruanchuang.massorganizationsignin.designlibrary.R;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joho on 2016/5/26.
 */
public class MeiGongActivity extends AppCompatActivity {
    private List<Person> lists;
    private ListView lv_meigong;
    private ProgressDialog progressDialog;
    private static final String TAG = "MeiGongActivity";
    private LoadToast lt;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d(TAG, ""+lists.size());
                    if(lists.size()>0){
//                        progressDialog.dismiss();
//                        Toast.makeText(MeiGongActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        lt.success();
                    }else {
//                        progressDialog.dismiss();
                        lt.error();
                        Toast.makeText(MeiGongActivity.this, "加载失败，请重新加载", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    lv_meigong.setAdapter(new MyAdapter());
                    break;
                case 2: //访问服务器异常
                    lt.error();
                    Toast.makeText(MeiGongActivity.this, "数据加载失败，找不到服务器", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                    Person p = new Person();
                    p.setGroup("游客");
                    p.setName("游客");
                    p.setPhone("110");
                    p.setSex("女");
                    lists.add(p);
                    lv_meigong.setAdapter(new MyAdapter());
                    break;
                case 3: //访问服务器异常
                    Toast.makeText(MeiGongActivity.this, "json解析异常", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                    lt.error();
                    finish();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meigong);
        lt = new LoadToast(this);
        lv_meigong = (ListView) findViewById(R.id.lv_meigong);
        lists = new ArrayList<Person>();
//        //显示一个进度对话框
//        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("载入数据中");
//        progressDialog.show();
        lt.setText("加载数据中...").setTranslationY(100).show();

        //读取服务器数据
        ReadUtlis.readData("美工组",lists,mHandler);
        //设置listview条目的点击事件
        lv_meigong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                String phone = tv_phone.getText().toString().trim();
                String name = tv_name.getText().toString().trim();
                //将文字复制到系统粘贴板
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("text",phone);
                cm.setPrimaryClip(data);
                Toast.makeText(MeiGongActivity.this, "已将"+name+"的电话复制到粘贴板", Toast.LENGTH_SHORT).show();
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
                view = View.inflate(getApplicationContext(), R.layout.item, null);
            } else {
                view = convertView;
            }

            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tv_group = (TextView) view.findViewById(R.id.tv_group);
            ImageView iv_sex = (ImageView) view.findViewById(R.id.iv_sex);

            Person person = lists.get(position);
            String sex = person.getSex();
            if ("男".equals(sex)) {
                iv_sex.setImageResource(R.drawable.icon_male);
            }
            if ("女".equals(sex)) {
                iv_sex.setImageResource(R.drawable.icon_female);
            }
            tv_name.setText(person.getName());
            tv_phone.setText(person.getPhone());
            tv_group.setText(person.getGroup());

            return view;
        }

    }
}
