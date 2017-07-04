package com.test.echolips.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

/**
 * Created by RockMeRoLL on 2017/1/10.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        //获取Context
        context = getApplicationContext();
        super.onCreate();
    }

    //返回
    public static Context getContextObject(){
        return context;
    }

    public static void startActivity(Context paramContext,Class<?> clz){
        Intent in = new Intent();
        in.setClass(paramContext,clz);
        paramContext.startActivity(in);
    }

}