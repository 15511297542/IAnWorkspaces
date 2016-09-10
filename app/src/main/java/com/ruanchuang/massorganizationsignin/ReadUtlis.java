package com.ruanchuang.massorganizationsignin;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.runachuang.massorganizationsignin.utils.MyUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by joho on 2016/5/29.
 */
public class ReadUtlis {
    //读取服务器中的数据
    public static void readData(final String lvgroup, final List<Person> lists, final Handler mHandler) {
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
                            String fuwugroup = jsonObject.getString("group");
                            if (lvgroup.equals(fuwugroup)) {
                                String name = jsonObject.getString("name");
                                String sex = jsonObject.getString("sex");
                                String group = jsonObject.getString("group");
                                String phone = jsonObject.getString("phone");
                                //把数据封装的javabean
                                Person person = new Person();
                                person.setName(name);
                                person.setSex(sex);
                                person.setGroup(group);
                                person.setPhone(phone);
                                //把javabean对象加入到集合
                                lists.add(person);
                            }
                        }
                        message.what = 1;
                    }
                } catch (IOException e) {
                    message.what = 2;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what = 3;
                    e.printStackTrace();
                }finally {
                    mHandler.sendMessage(message);
                }

            }
        }.start();
    }

    public static void queryData(String lvgroup, final List<Person> lists,final CallBack cb){
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("group", lvgroup);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object, BmobException e) {
                if(e==null){
                    for (MyUser myUser : object) {
                        //把数据封装的javabean
                        Person person = new Person();
                        person.setName(myUser.getName());
                        person.setSex(myUser.getSex());
                        person.setGroup(myUser.getGroup());
                        person.setPhone(myUser.getMobilePhoneNumber());
                        //把javabean对象加入到集合
                        lists.add(person);
                        cb.success();
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    cb.error();
                }
            }
        });
    }

    public interface CallBack{
        public abstract void success();
        public abstract void error();
    }
}

