package com.test.echolips.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.test.echolips.xTools.ActivityCollector;
import com.test.echolips.xTools.LogUtil;

import java.lang.reflect.Method;

/**
 * Created by RockMeRoLL on 2017/1/15.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle paramBundle) {

        try {
            setMiuiStatusBarDarkMode(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate(paramBundle);
        LogUtil.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    //MIUI下设置状态栏文字颜色
    public static boolean setMiuiStatusBarDarkMode(Activity paramActivity, boolean paramBoolean) {
        Class localClass1 = paramActivity.getWindow().getClass();
        try {
            Class localClass2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int i = localClass2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(localClass2);
            Class[] arrayOfClass = new Class[2];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            Method localMethod = localClass1.getMethod("setExtraFlags", arrayOfClass);
            Window localWindow = paramActivity.getWindow();
            Object[] arrayOfObject = new Object[2];
            if (paramBoolean) ;
            for (int j = i; ; j = 0) {
                arrayOfObject[0] = Integer.valueOf(j);
                arrayOfObject[1] = Integer.valueOf(i);
                localMethod.invoke(localWindow, arrayOfObject);
                return true;
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}

