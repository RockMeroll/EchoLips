package com.test.echolips.activity;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.test.echolips.R;
import com.test.echolips.service.UserInfoService;

/**
 *
 * @author Xuwenchao
 *
 */
public class LoginActivity extends BaseActivity {

	private static final int LOGIN_CODE = 0;
	private EditText editLoginName;
	private EditText editPassword;
	private CheckBox checkAutoLogin;
	private CheckBox checkRecord;
	private Button btnLogin;

	private UserInfoService userInfoService;
	private Handler handle = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == LOGIN_CODE){
				int flag = (int)msg.obj;
				if(flag > 0){
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}else if(flag == 0){
//					登录，弹出对话框提示用户
					Toast.makeText(LoginActivity.this, "网络错误，请重试...", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(LoginActivity.this, "用户名或者密码错误...", Toast.LENGTH_LONG).show();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		findView();
		setListener();
		checkRecord();
	}
	private void checkRecord() {
		SharedPreferences pre = getSharedPreferences("info", MODE_PRIVATE);
		if("true".equals(pre.getString("record", null))){
			editLoginName.setText(pre.getString("name", ""));
			editPassword.setText(pre.getString("password", ""));
			checkRecord.setChecked(true);
			checkAutoLogin.setChecked("true".equals(pre.getString("auto", null)));
		}
	}
	private void setListener() {
		this.btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = editLoginName.getText().toString();
				String password = editPassword.getText().toString();
				if(name == null || password == null) {
					Toast.makeText(LoginActivity.this, "用户名或密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}else if(name.length() == 0 || password.length() == 0){
					Toast.makeText(LoginActivity.this, "用户名或密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				startNetWork(name, password);
			}
		});
	}
	private void startNetWork(final String name, final String password){
		new Thread(new Runnable() {
			@Override
			public void run() {
				int flag = userInfoService.checkLogin(name, password, false);
				if(flag > 0){
					SharedPreferences pre = getSharedPreferences("info", MODE_PRIVATE);
					SharedPreferences.Editor edit=pre.edit();
					edit.clear();
					edit.putString("name", name);
					edit.putString("password", password);
					if(checkRecord.isChecked()){
						edit.putString("record", "true");
					}
//					判断用户是否需要实现自动登录
					if(checkAutoLogin.isChecked()){
						edit.putString("auto", "true");
					}
					edit.commit();
				}
				Message msg = new Message();
				msg.what = LOGIN_CODE;
				msg.obj = flag;
				handle.sendMessage(msg);
			}
		}).start();
	}
	private void findView() {
		this.userInfoService=new UserInfoService();
		this.editLoginName = (EditText) findViewById(R.id.login_edit_name);
		this.editPassword = (EditText) findViewById(R.id.password_edit_name);
		this.checkAutoLogin = (CheckBox) findViewById(R.id.login_check_auto);
		this.btnLogin = (Button) findViewById(R.id.login_button_login);
		this.checkRecord = (CheckBox) findViewById(R.id.login_check_record);
	}

}
