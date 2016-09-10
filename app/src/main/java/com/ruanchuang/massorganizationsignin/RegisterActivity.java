package com.ruanchuang.massorganizationsignin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ruanchuang.massorganizationsignin.designlibrary.R;
import com.runachuang.massorganizationsignin.utils.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by joho on 2016/5/27.
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText et_username,et_pwd,et_pwd2,et_name,et_group,et_sex,et_phone,et_qq,et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();
    }

    private void initUI() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd2 = (EditText) findViewById(R.id.et_pwd2);
        et_name = (EditText) findViewById(R.id.et_name);
        et_group = (EditText) findViewById(R.id.et_group);
        et_sex = (EditText) findViewById(R.id.et_sex);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_qq = (EditText) findViewById(R.id.et_qq);
        et_email = (EditText) findViewById(R.id.et_email);
    }

    public void click (View view){
        final String userName = et_username.getText().toString().trim();
        final String pwd = et_pwd.getText().toString().trim();
        String pwd2 = et_pwd2.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String group = et_group.getText().toString().trim();
        String sex = et_sex.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String qq = et_qq.getText().toString().trim();
        String email = et_email.getText().toString().trim();

        if(!userName.equals("") && !pwd.equals("") && !pwd2.equals("") && !name.equals("") && !group.equals("") && !sex.equals("") && !phone.equals("") && !qq.equals("") && !email.equals("")){
            if(pwd.equals(pwd2)){
                MyUser myUser = new MyUser();
                myUser.setUsername(userName);
                myUser.setPassword(pwd);
                myUser.setName(name);
                myUser.setGroup(group);
                myUser.setSex(sex);
                myUser.setMobilePhoneNumber(phone);
                myUser.setQq(qq);
                myUser.setEmail(email);
                myUser.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null){
                            Toast.makeText(getApplicationContext(),"注册成功,请到邮箱中激活账号",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("userName",userName);
                            intent.putExtra("pwd",pwd);
                            setResult(1,intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"注册失败,"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
        }
    }

}
