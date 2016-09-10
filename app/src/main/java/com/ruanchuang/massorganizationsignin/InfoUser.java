package com.ruanchuang.massorganizationsignin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruanchuang.massorganizationsignin.designlibrary.R;
import com.runachuang.massorganizationsignin.utils.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/9/10/010.
 */
public class InfoUser extends AppCompatActivity implements View.OnClickListener{
    private String mName;
    private String mSex;
    private String mGroup;
    private String mQq;
    private String mPhone;
    private SharedPreferences sp;
    private AlertDialog dialog;
    private View mView;
    private Context mContext;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_group;
    private TextView tv_phone;
    private TextView tv_qq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infouser);
        mContext = this;

        sp = getSharedPreferences("user", MODE_PRIVATE);
        mName = sp.getString("name", "");
        mSex = sp.getString("sex", "");
        mGroup = sp.getString("group", "");
        mQq = sp.getString("qq", "");
        mPhone = sp.getString("phone", "");

        initDialog();

        initUI();

        initBt();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mName = sp.getString("name", "");
        mSex = sp.getString("sex", "");
        mGroup = sp.getString("group", "");
        mQq = sp.getString("qq", "");
        mPhone = sp.getString("phone", "");

        tv_name.setText("姓名："+mName);
        tv_sex.setText("性别："+mSex);
        tv_group.setText("组别："+mGroup);
        tv_phone.setText("电话："+mPhone);
        tv_qq.setText("QQ："+mQq);
    }

    private void initDialog() {
        // 自定义对话框 要用dialog.setView(mView); 来设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();

        // 将一个xml转换成一个view对象
        mView = View.inflate(this, R.layout.dialog_view, null);
        // 兼容低版本 去掉对话框的内边距
        dialog.setView(mView, 0, 0, 0, 0);
    }

    private void initBt() {
        Button bt_name = (Button) findViewById(R.id.bt_name);
        Button bt_sex = (Button) findViewById(R.id.bt_sex);
        Button bt_group = (Button) findViewById(R.id.bt_group);
        Button bt_phone = (Button) findViewById(R.id.bt_phone);
        Button bt_qq = (Button) findViewById(R.id.bt_qq);

        bt_name.setOnClickListener(this);
        bt_sex.setOnClickListener(this);
        bt_group.setOnClickListener(this);
        bt_phone.setOnClickListener(this);
        bt_qq.setOnClickListener(this);
    }

    private void initUI() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_group = (TextView) findViewById(R.id.tv_group);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_qq = (TextView) findViewById(R.id.tv_qq);

        tv_name.setText("姓名："+mName);
        tv_sex.setText("性别："+mSex);
        tv_group.setText("组别："+mGroup);
        tv_phone.setText("电话："+mPhone);
        tv_qq.setText("QQ："+mQq);
    }

    @Override
    public void onClick(View view) {
        TextView tv_title = (TextView) mView.findViewById(R.id.tv_title);
        final EditText tv_in = (EditText) mView.findViewById(R.id.tv_in);
        Button bt_submit = (Button) mView.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) mView.findViewById(R.id.bt_cancel);

        switch (view.getId()){
            case R.id.bt_name:
                tv_title.setText("修改姓名");
                tv_in.setHint("请输入姓名");
                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String content = tv_in.getText().toString().trim();
                        // 如果内容不为空
                        if(!TextUtils.isEmpty(content)){
                            MyUser newUser = new MyUser();
                            newUser.setName(content);
                            BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                            newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(getApplicationContext(),"更新用户信息成功",Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor edit = sp.edit();
                                        edit.putString("name",content);
                                        edit.commit();
                                        tv_name.setText("姓名："+content);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"更新用户信息失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // 隐藏对话框
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"输入的内容不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.bt_sex:
                tv_title.setText("修改性别");
                tv_in.setHint("请输入性别");
                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String content = tv_in.getText().toString().trim();
                        // 如果内容不为空
                        if(!TextUtils.isEmpty(content)){
                            MyUser newUser = new MyUser();
                            newUser.setSex(content);
                            BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                            newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(getApplicationContext(),"更新用户信息成功",Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor edit = sp.edit();
                                        edit.putString("sex",content);
                                        edit.commit();
                                        tv_sex.setText("性别："+content);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"更新用户信息失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // 隐藏对话框
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"输入的内容不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.bt_group:
                tv_title.setText("修改组别");
                tv_in.setHint("请输入组别");
                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String content = tv_in.getText().toString().trim();
                        // 如果内容不为空
                        if(!TextUtils.isEmpty(content)){
                            MyUser newUser = new MyUser();
                            newUser.setGroup(content);
                            BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                            newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(getApplicationContext(),"更新用户信息成功",Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor edit = sp.edit();
                                        edit.putString("group",content);
                                        edit.commit();
                                        tv_sex.setText("组别："+content);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"更新用户信息失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // 隐藏对话框
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"输入的内容不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.bt_phone:
                tv_title.setText("修改电话");
                tv_in.setHint("请输入电话号码");
                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String content = tv_in.getText().toString().trim();
                        // 如果内容不为空
                        if(!TextUtils.isEmpty(content)){
                            MyUser newUser = new MyUser();
                            newUser.setMobilePhoneNumber(content);
                            BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                            newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(getApplicationContext(),"更新用户信息成功",Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor edit = sp.edit();
                                        edit.putString("phone",content);
                                        edit.commit();
                                        tv_sex.setText("电话："+content);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"更新用户信息失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // 隐藏对话框
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"输入的内容不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.bt_qq:
                tv_title.setText("修改QQ");
                tv_in.setHint("请输入QQ号码");
                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String content = tv_in.getText().toString().trim();
                        // 如果内容不为空
                        if(!TextUtils.isEmpty(content)){
                            MyUser newUser = new MyUser();
                            newUser.setQq(content);
                            BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                            newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(getApplicationContext(),"更新用户信息成功",Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor edit = sp.edit();
                                        edit.putString("qq",content);
                                        edit.commit();
                                        tv_sex.setText("QQ："+content);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"更新用户信息失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            // 隐藏对话框
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"输入的内容不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }

}
