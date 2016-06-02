package com.ruanchuang.massorganizationsignin;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by joho on 2016/5/29.
 */
public class StreamUtils {
    public static String streamToString(InputStream is) {
        //先将读取的内容缓存  然后一次性转换成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //读流操作
        //每次读的大小
        byte[] buffer = new byte[1024];
        //记录读取内容的临时变量
        int temp = -1;
        try {
            while((temp = is.read(buffer))!=-1){
                bos.write(buffer,0,temp);
            }
            //返回读取的数据
            return bos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                is.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;
    }
}
