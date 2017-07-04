package com.test.echolips.activity;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.test.echolips.R;
import com.test.echolips.service.UserInfoService;

/**
 *
 * @author Xuwenchao
 *
 */
public class WelcomeActivity extends Activity {

	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			Intent intent=new Intent();
			Class cls=null;
			switch (msg.what) {
				case 1:
					cls=MainActivity.class;
					break;
				case 2:
					cls=LoginActivity.class;
					break;
				default:
					break;
			}
			intent.setClass(WelcomeActivity.this, cls);
			startActivity(intent);
			finish();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		Thread t=new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				读取资源文件
				SharedPreferences pre = getSharedPreferences("info", MODE_PRIVATE);
				String name = pre.getString("name", null);
				String password = pre.getString("password", null);
				String auto = pre.getString("auto", null);
				if("true".equals(auto)){
//					用户要使用自动登录功能
					UserInfoService userInfoService=new UserInfoService();
					int flag=userInfoService.checkLogin(name, password, true);
					if(flag > 0){
//						直接进入main页面即可
						handler.sendEmptyMessage(1);
					}else{
						handler.sendEmptyMessage(2);
					}
				}else{
//					表示不需要自动登录，直接进入登录页面即可
					handler.sendEmptyMessage(2);
				}
			}
		});
		t.start();
	}
}
