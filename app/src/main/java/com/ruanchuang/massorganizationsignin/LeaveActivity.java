package com.ruanchuang.massorganizationsignin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
 * Created by joho on 2016/5/27.
 */
public class LeaveActivity extends AppCompatActivity {
    private List<Person> lists;
    private ListView lv_leave;
    private ProgressDialog progressDialog;
    private SharedPreferences sp;
    private static final String TAG = "LeaveActivity";
    private LoadToast lt;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d(TAG, ""+lists.size());
                    if(lists.size()>0){
//                        progressDialog.dismiss();
//                        Toast.makeText(LeaveActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        lt.success();
                    }else {
//                        progressDialog.dismiss();
                        lt.error();
                        Toast.makeText(LeaveActivity.this, "加载失败，请重新加载", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    lv_leave.setAdapter(new MyAdapter());
                    break;
                case 2: //访问服务器异常
                    lt.error();
                    Toast.makeText(LeaveActivity.this, "数据加载失败，找不到服务器", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                    showUpDataDialog();
                    Person p = new Person();
                    p.setGroup("游客");
                    p.setName("游客");
                    p.setPhone("110");
                    p.setSex("男");
                    lists.add(p);
                    lv_leave.setAdapter(new MyAdapter());
                    break;
                case 3: //访问服务器异常
                    Toast.makeText(LeaveActivity.this, "json解析异常", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_leave);
        lt = new LoadToast(this);
        lv_leave = (ListView) findViewById(R.id.lv_leave);
        lists = new ArrayList<Person>();
//        //显示一个进度对话框
//        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("载入数据中");
//        progressDialog.show();
        lt.setText("加载数据中...").setTranslationY(100).show();

        //读取服务器数据
        ReadUtlis.readData("行政",lists,mHandler);
        //设置listview条目的点击事件
        lv_leave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                String phone = tv_phone.getText().toString().trim();
                String name = tv_name.getText().toString().trim();
                sp = getSharedPreferences("user", MODE_PRIVATE);
                String myName = sp.getString("name", "");
                //定义一个Intent，让它能跳转到发短信的界面
                Uri uri = Uri.parse("smsto:"+phone);    //设置短信能够接受的地址
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);        //设置能够响应这个Intent的组件
                intent.putExtra("sms_body", name+"你好，我是"+myName+"，我临时有些事情，想请个假。");     //设置短信的内容
                startActivity(intent);          //开启这个Intent
            }
        });
    }

    //显示一个对话框
    protected void showUpDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框标题
        builder.setTitle("请假的功能介绍");
        //设置对话框内容
        builder.setMessage("这里面是一个ListView，内容是负责请假的人员，当点击ListView的条目时，就会跳转到发短信界面，给相应的人员发送短信");
        //设置积极的按钮
//        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
        //设置消极的按钮
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //让对话框消失
                dialog.dismiss();
            }
        });
        //监听取消按钮
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            //当点击返回的按钮时执行
            @Override
            public void onCancel(DialogInterface dialog) {
                //让对话框消失
                dialog.dismiss();
            }
        });
        builder.show();
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
