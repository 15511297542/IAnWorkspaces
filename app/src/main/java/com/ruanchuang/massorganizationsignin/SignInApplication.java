package com.ruanchuang.massorganizationsignin;

import android.app.Application;

/**
 * Created by joho on 2016/5/31.
 */
public class SignInApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        //SDKInitializer.initialize(getApplicationContext());
    }

}