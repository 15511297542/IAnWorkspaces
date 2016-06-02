package com.ruanchuang.massorganizationsignin;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
}

