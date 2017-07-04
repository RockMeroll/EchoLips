package com.test.echolips.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.test.echolips.R;
import com.test.echolips.fragment.CookingFragment;
import com.test.echolips.fragment.FriendsFragment;
import com.test.echolips.fragment.HomeFragment;
import com.test.echolips.fragment.UserFragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity
{
    private List<TextView> mBottomBtn;
    private FragmentManager mFragmentManager;
    private List<Fragment> mFragments;

    private long exitTime = 0;

    private void ResetBottomBtn(){
        for(TextView btn:mBottomBtn){
            if (btn != null)
                btn.setEnabled(true);
        }
    }

    private void hideFragment(FragmentTransaction paramFragmentTransaction) {
        for(Fragment localFragment:mFragments){
            if (localFragment != null)
                paramFragmentTransaction.hide(localFragment);
        }
    }

    private void initView(){
        //底栏按钮
        mBottomBtn = new ArrayList<>();
        TextView BtnHome = (TextView)findViewById(R.id.btn_home);
        TextView BtnCooking = (TextView)findViewById(R.id.btn_cook);
        TextView BtnFriends  = (TextView)findViewById(R.id.btn_friend);
        TextView BtnUser  = (TextView)findViewById(R.id.btn_user);

        mBottomBtn.add(BtnHome);
        mBottomBtn.add(BtnCooking);
        mBottomBtn.add(BtnFriends);
        mBottomBtn.add(BtnUser);
        BtnHome.setEnabled(false);

        for(TextView tv:mBottomBtn){
            tv.setOnClickListener(new BottomBtnOnClickListener());
        }
    }

    //MIUI下设置状态栏文字颜色
    public static boolean setMiuiStatusBarDarkMode(Activity paramActivity, boolean paramBoolean) {
        Class localClass1 = paramActivity.getWindow().getClass();
        try{
            Class localClass2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int i = localClass2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(localClass2);
            Class[] arrayOfClass = new Class[2];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            Method localMethod = localClass1.getMethod("setExtraFlags", arrayOfClass);
            Window localWindow = paramActivity.getWindow();
            Object[] arrayOfObject = new Object[2];
            if (paramBoolean);
            for (int j = i; ; j = 0)
            {
                arrayOfObject[0] = Integer.valueOf(j);
                arrayOfObject[1] = Integer.valueOf(i);
                localMethod.invoke(localWindow, arrayOfObject);
                return true;
            }
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return false;
    }

    protected void initFragment(){
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new CookingFragment());
        mFragments.add(new FriendsFragment());
        mFragments.add(new UserFragment());

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction localFragmentTransaction = this.mFragmentManager.beginTransaction();

        for (Fragment localFragment: mFragments){
            if (localFragment != null)
                localFragmentTransaction.add(R.id.fl_content, localFragment);
        }
        localFragmentTransaction.commit();
    }

    protected void onCreate(Bundle paramBundle){
        super.onCreate(paramBundle);

        try {
            setMiuiStatusBarDarkMode(this, true);
        }catch (Exception e){
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        initFragment();
        initView();
        showFragment(0);
    }

    protected void showFragment(int paramInt){
        FragmentTransaction localFragmentTransaction = this.mFragmentManager.beginTransaction();
        hideFragment(localFragmentTransaction);
        localFragmentTransaction.show(mFragments.get(paramInt));
        localFragmentTransaction.commit();
    }

    private class BottomBtnOnClickListener implements View.OnClickListener {

        public void onClick(View paramView){
            ResetBottomBtn();
            paramView.setEnabled(false);
            switch (paramView.getId()){
                case R.id.btn_home:
                    showFragment(0);
                    break;
                case R.id.btn_cook:
                    showFragment(1);
                    break;
                case R.id.btn_friend:
                    showFragment(2);
                    break;
                case R.id.btn_user:
                    showFragment(3);
                    break;
                default:
                    showFragment(0);
                    break;
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}