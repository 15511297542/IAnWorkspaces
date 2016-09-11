package com.ruanchuang.massorganizationsignin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ruanchuang.massorganizationsignin.designlibrary.R;
import com.runachuang.massorganizationsignin.utils.MyUser;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LognInActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_pwd;
    private CheckBox cb_rember;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private String userName;
    private String mPwd;
    private static Boolean isExit = false;
    protected LoadToast lt;
    private final String tag = "LognInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lognin);
        lt = new LoadToast(this);

        //初始化控件
        initUI();
        //检测是否记住了密码 如果是就填充
        checkUpDown();
        //检查是否已经登陆
        checkEnter();

    }

    //检查是否已经登陆
    private void checkEnter() {
        sp = getSharedPreferences("user", MODE_PRIVATE);
        if (sp.getBoolean("check", false)) {
            enterHome();
        }
    }

    //进入主界面
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //检测是否记住了密码 如果是就填充
    private void checkUpDown() {
        sp = getSharedPreferences("user", MODE_PRIVATE);
        if (sp.getBoolean("check", false)) {
            et_username.setText(sp.getString("userName", ""));
            et_pwd.setText(sp.getString("pwd", ""));
            cb_rember.setChecked(sp.getBoolean("check", false));
        }else {
            et_username.setText(sp.getString("userName", ""));
        }
    }

    //设置按钮点击事件
    public void click(View view) {
        userName = et_username.getText().toString().trim();
        mPwd = et_pwd.getText().toString().trim();
        if (userName.equals("") || mPwd.equals("")) {
            Toast.makeText(LognInActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
//            //显示一个进度对话框
//            progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("登陆中");
//            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                //当点击返回的按钮时执行
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                }
//            });
//            progressDialog.show();

            lt.setText("登陆中...").setTranslationY(100).show();

            MyUser myUser = new MyUser();
            myUser.setUsername(userName);
            myUser.setPassword(mPwd);
            myUser.login(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if(e==null){
                        Toast.makeText(LognInActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        checkUp(userName,mPwd);
                        lt.success();
                        enterHome();
                        //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                        MyUser user = BmobUser.getCurrentUser(MyUser.class);
                        sp = getSharedPreferences("user", MODE_PRIVATE);
                        edit = sp.edit();
                        edit.putString("username",user.getUsername());
                        edit.putString("name", user.getName());
                        edit.putString("sex", user.getSex());
                        edit.putString("group", user.getGroup());
                        edit.putString("qq", user.getQq());
                        edit.putString("phone", user.getMobilePhoneNumber());
                        edit.commit();

                        Log.i(tag,"姓名："+user.getName());
                        Log.i(tag,"性别："+user.getSex());
                        Log.i(tag,"组别："+user.getGroup());
                        Log.i(tag,"电话："+user.getMobilePhoneNumber());
                        Log.i(tag,"qq："+user.getQq());

                    }else{
                        lt.error();
                        Toast.makeText(LognInActivity.this, "登录失败,用户名或密码不正确", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    //设置注册的文字点击事件
    public void tv_click(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent,1);
    }

    //设置忘记密码的文字点击事件
    public void tv_unpwd(View view){
        startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            String username = data.getStringExtra("userName");
            String pwd = data.getStringExtra("pwd");
            et_username.setText(username);
            et_pwd.setText(pwd);
        }
    }

    //检查是否勾选记住密码
    private void checkUp(String userName,String mPwd) {
        if (cb_rember.isChecked()) {
            sp = getSharedPreferences("user", MODE_PRIVATE);
            edit = sp.edit();
            edit.putString("userName", userName);
            edit.putString("pwd", mPwd);
            edit.putBoolean("check", true);
            edit.commit();
        } else {
            sp = getSharedPreferences("user", MODE_PRIVATE);
            edit = sp.edit();
            edit.putString("userName", userName);
            edit.putBoolean("check", false);
            edit.commit();
        }
    }

    //初始化控件
    private void initUI() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        cb_rember = (CheckBox) findViewById(R.id.cb_rember);
    }

    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exitBy2Click();
        }
        return false;
    }

    //双击退出程序
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

}