package com.ruanchuang.massorganizationsignin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ruanchuang.massorganizationsignin.designlibrary.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/9/10/010.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private AlertDialog dialog;
    private View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUI();
    }

    private void initUI() {
        Button bt_loation = (Button) findViewById(R.id.bt_loation);
        Button bt_setpwd = (Button) findViewById(R.id.bt_setpwd);
        Button bt_exit = (Button) findViewById(R.id.bt_exit);
        bt_loation.setOnClickListener(this);
        bt_setpwd.setOnClickListener(this);
        bt_exit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_loation:
                startActivity(new Intent(getApplicationContext(),SignLocationActivity.class));
                break;
            case R.id.bt_setpwd:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                dialog = builder.create();
                mView = View.inflate(this, R.layout.dialog_setpwd_view, null);
                dialog.setView(mView, 0, 0, 0, 0);
                final EditText et_oldpwd = (EditText) mView.findViewById(R.id.et_oldpwd);
                final EditText et_newpwd = (EditText) mView.findViewById(R.id.et_newpwd);
                Button bt_submit = (Button) mView.findViewById(R.id.bt_submit);
                Button bt_cancel = (Button) mView.findViewById(R.id.bt_cancel);
                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String oldpwd = et_oldpwd.getText().toString().trim();
                        final String newpwd = et_newpwd.getText().toString().trim();
                        // 如果内容不为空
                        if(!TextUtils.isEmpty(oldpwd) && !TextUtils.isEmpty(newpwd)){
                            BmobUser.updateCurrentUserPassword(oldpwd, newpwd, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(getApplicationContext(),"密码修改成功",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"密码修改失败",Toast.LENGTH_SHORT).show();
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
            case R.id.bt_exit:
                sp = getSharedPreferences("user", MODE_PRIVATE);
                sp.edit().clear().commit();
                BmobUser.logOut();
                Intent intent = new Intent(getApplicationContext(), LognInActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
