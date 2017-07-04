package com.test.echolips.activity;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.test.echolips.R;
import com.test.echolips.xTools.LogUtil;
import com.test.echolips.xTools.PictureUtil;

/**
 * 
 * @author Xuwenchao
 *
 */
public class TestActivity extends BaseActivity {
	private ImageView imageView;
	private Bitmap response;
	private Handler handle = new Handler(){
		public void handleMessage(Message msg) {
			LogUtil.v("imageView", imageView == null ? "null" : "not null");
			imageView.setImageBitmap(response);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
		Button testBtn = (Button) findViewById(R.id.test_btn);
		imageView = (ImageView) findViewById(R.id.test_image_view);

		testBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							response = PictureUtil.getUrlBitmap(
									"http://192.168.1.105:8080/image/aff3294b-3603-4210-91cb-5bff5ed230a7.JPEG");
						} catch (Exception e) {
							e.printStackTrace();
						}
						LogUtil.v("responseImg", response == null ? "no":"yes");
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "123";
						handle.sendMessage(msg);
					}
				}).start();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
