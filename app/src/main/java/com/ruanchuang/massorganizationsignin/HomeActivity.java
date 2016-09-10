package com.ruanchuang.massorganizationsignin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;
import com.ruanchuang.massorganizationsignin.designlibrary.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CoordinatorLayout rootLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private NavigationView navigation;
    private Button bt_xingzheng;
    private Button bt_php;
    private Button bt_shipin;
    private Button bt_android;
    private Button bt_java;
    private Button bt_net;
    private Button bt_meigong;
    private String mName;
    private String mSex;
    private String mGroup;
    private String mQq;
    private String mPhone;
    private SharedPreferences sp;

    private static Boolean isExit = false;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        mName = sp.getString("name", "");
        mSex = sp.getString("sex", "");
        mGroup = sp.getString("group", "");
        mQq = sp.getString("qq", "");
        mPhone = sp.getString("phone", "");

        FilterMenuLayout layout3 = (FilterMenuLayout) findViewById(R.id.filter_menu3);
        attachMenu3(layout3);

        initInstances();
        //初始化控件
        initUI();
        //设置点击事件
        click();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mName = sp.getString("name", "");
        mSex = sp.getString("sex", "");
        mGroup = sp.getString("group", "");
        mQq = sp.getString("qq", "");
        mPhone = sp.getString("phone", "");

        mTv.setText(mName);
    }

    private FilterMenu attachMenu3(FilterMenuLayout layout){
        return new FilterMenu.Builder(this)
                .addItem(R.drawable.ic_action_location_2) //签到
                .addItem(R.drawable.ic_action_info)  //个人信息
                .addItem(R.drawable.ic_action_io)  //退出当前账号
                .attach(layout)
                .withListener(listener)
                .build();
    }

    //设置菜单点击事件
    FilterMenu.OnMenuChangeListener listener = new FilterMenu.OnMenuChangeListener() {
        @Override
        public void onMenuItemClick(View view, int position) {
            switch (position){
                case 0: //签到
                    Intent intent1 = new Intent(HomeActivity.this, SignActivity.class);
                    startActivity(intent1);
                    break;
                case 1:  //个人信息
                    //显示对话框
                    showUpDataDialog();
                    break;
                case 2:  //退出当前账号
                    sp = getSharedPreferences("user", MODE_PRIVATE);
                    sp.edit().clear().commit();
                    BmobUser.logOut();
                    Intent intent = new Intent(HomeActivity.this, LognInActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }

        @Override
        public void onMenuCollapse() {

        }

        @Override
        public void onMenuExpand() {

        }
    };

    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("社团通讯录");
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //在侧滑栏设置用户姓名
        navigation = (NavigationView) findViewById(R.id.navigation);
        View view = navigation.inflateHeaderView(R.layout.nav_header);
        mTv = (TextView) view.findViewById(R.id.tv_menuname);
        mTv.setText(mName);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navItem1: //个人信息
                        //显示对话框
//                        showUpDataDialog();
                        startActivity(new Intent(getApplicationContext(),InfoUser.class));
                        break;
                    case R.id.navItem2: //签到
                        Intent intent1 = new Intent(HomeActivity.this, SignActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navItem3: //请假
                        Intent intent2 = new Intent(HomeActivity.this, LeaveActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navItem4: //设置
                        Intent intent3 = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navItem5: //关于我们
                        Intent intent4 = new Intent(HomeActivity.this, AboutWeActivity.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }

    //监听按钮点击
    private void click() {
        bt_xingzheng.setOnClickListener(this);
        bt_php.setOnClickListener(this);
        bt_shipin.setOnClickListener(this);
        bt_android.setOnClickListener(this);
        bt_java.setOnClickListener(this);
        bt_net.setOnClickListener(this);
        bt_meigong.setOnClickListener(this);
    }

    //设置点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_xingzheng:
                Intent intent4 = new Intent(this, XingZhengActivity.class);
                startActivity(intent4);
                break;
            case R.id.bt_php:
                Intent intent5 = new Intent(this, PHPActivity.class);
                startActivity(intent5);
                break;
            case R.id.bt_shipin:
                Intent intent6 = new Intent(this, ShiPinActivity.class);
                startActivity(intent6);
                break;
            case R.id.bt_android:
                Intent intent7 = new Intent(this, AndroidActivity.class);
                startActivity(intent7);
                break;
            case R.id.bt_java:
                Intent intent8 = new Intent(this, JavaActivity.class);
                startActivity(intent8);
                break;
            case R.id.bt_net:
                Intent intent9 = new Intent(this, NetActivity.class);
                startActivity(intent9);
                break;
            case R.id.bt_meigong:
                Intent intent0 = new Intent(this, MeiGongActivity.class);
                startActivity(intent0);
                break;
        }
    }

    //显示一个对话框
    protected void showUpDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框标题
        builder.setTitle("我的信息");
        //设置对话框内容
        builder.setMessage("姓名：" + mName + "\n" + "性别：" + mSex + "\n" + "所在组别：" + mGroup + "\n" + "qq号：" + mQq + "\n" + "电话：" + mPhone);

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


    //初始化控件
    private void initUI() {
        bt_xingzheng = (Button) findViewById(R.id.bt_xingzheng);
        bt_php = (Button) findViewById(R.id.bt_php);
        bt_shipin = (Button) findViewById(R.id.bt_shipin);
        bt_android = (Button) findViewById(R.id.bt_android);
        bt_java = (Button) findViewById(R.id.bt_java);
        bt_net = (Button) findViewById(R.id.bt_net);
        bt_meigong = (Button) findViewById(R.id.bt_meigong);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

