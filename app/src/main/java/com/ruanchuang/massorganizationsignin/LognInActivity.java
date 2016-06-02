package com.ruanchuang.massorganizationsignin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ruanchuang.massorganizationsignin.designlibrary.R;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class LognInActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_pwd;
    private CheckBox cb_rember;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private String userName;
    private String mPwd;
    private String name;
    private String sex;
    private String group;
    private String qq;
    private String phone;
    private ProgressDialog progressDialog;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private static Boolean isExit = false;

    protected LoadToast lt;

        private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1: //账号密码匹配成功
                    //检查是否勾选记住密码
                    checkUp();
//                    Toast.makeText(LognInActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                    lt.success();
                    //跳转到主界面
                    enterHome();
                    break;
                case 2: //访问服务器异常
                    Toast.makeText(LognInActivity.this, "找不到服务器,您可以以游客方式登陆", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                    lt.error();
                    lt.error();
                    et_username.setText("123");
                    et_pwd.setText("123");
                    if(userName.equals("123") && mPwd.equals("123")){
                        checkUp();
                        sp = getSharedPreferences("user", MODE_PRIVATE);
                        edit = sp.edit();
                        edit.putString("name", "游客");
                        edit.putString("group", "游客");
                        edit.commit();
                        Toast.makeText(LognInActivity.this, "无法访问到服务器，您将以游客的方式登陆", Toast.LENGTH_SHORT).show();
                        enterHome();
                    }
                    break;
                case 3: //访问服务器异常
                    Toast.makeText(LognInActivity.this, "json解析异常", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                    lt.error();
                    break;
                case 4: //找不到符合条件的
                    Toast.makeText(LognInActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                    lt.error();
                    break;
            }
        }

        ;
    };


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

        initToolbar();
        initInstances();
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

    //读取服务器中的数据
    public void readData() {
        new Thread() {
            @Override
            public void run() {
                Message message = Message.obtain();
                try {
                    URL url = new URL("http://192.168.0.105:8080/ruanchuang.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    connection.setRequestMethod("GET");
                    if (connection.getResponseCode() == 200) {
                        // 以流的形式将数据取下来
                        InputStream is = connection.getInputStream();
                        // 将流转化成字符串
                        String json = StreamUtils.streamToString(is);
                        Log.d("SingInActivity", json);
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String number = jsonObject.getString("number");
                            String pwd = jsonObject.getString("pwd");
                            if (userName.equals(number) && mPwd.equals(pwd)) {
                                name = jsonObject.getString("name");
                                sex = jsonObject.getString("sex");
                                group = jsonObject.getString("group");
                                qq = jsonObject.getString("qq");
                                phone = jsonObject.getString("phone");
                                sp = getSharedPreferences("user", MODE_PRIVATE);
                                edit = sp.edit();
                                edit.putString("name", name);
                                edit.putString("sex", sex);
                                edit.putString("group", group);
                                edit.putString("qq", qq);
                                edit.putString("phone", phone);
                                edit.commit();
                                message.what = 1;
                                return;
                            }
                        }
                        message.what = 4;
                    }
                } catch (IOException e) {
                    message.what = 2;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what = 3;
                    e.printStackTrace();
                } finally {
                    mHandler.sendMessage(message);
                }

            }
        }.start();
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

            //读取服务器数据
            readData();
        }
    }

    //设置注册的文字点击事件
    public void tv_click(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    //检查是否勾选记住密码
    private void checkUp() {
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

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(LognInActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("登陆");
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

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

}