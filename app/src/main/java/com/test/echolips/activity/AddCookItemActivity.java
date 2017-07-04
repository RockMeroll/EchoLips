package com.test.echolips.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.test.echolips.R;
import com.test.echolips.bean.EcCookitem;
import com.test.echolips.xTools.LogUtil;
import com.test.echolips.xTools.PictureUtil;
import com.test.echolips.xTools.UploadUtil;
import com.test.echolips.xTools.XBasePath;

import java.io.File;

/**
 *
 * @author Xuwenchao
 *
 */
public class AddCookItemActivity extends BaseActivity implements OnClickListener{
    public static final int RESPONSE_CODE = 0;

    private Intent mainIntent;
    private static String requestURL = XBasePath.requestURL + "pic/upload";
    private ImageView imageView;
    private EditText editText;
    private Button cancelBtn;
    private Button finishBtn;
    private String picPath = null;
    private Bitmap b;
    private ProgressDialog progressDialog = null;
    private Handler handle = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == RESPONSE_CODE){
                LogUtil.v("path", msg.obj.toString());
                LogUtil.v("content", editText.getText().toString());
                progressDialog.dismiss();
                if("error".equals(msg.obj.toString())){
                    Toast.makeText(AddCookItemActivity.this, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                }else{
                    mainIntent.putExtra("cook_item", new EcCookitem(R.mipmap.ic_launcher,
                            android.R.drawable.ic_delete, msg.obj.toString(),
                            picPath, editText.getText().toString()));
                    setResult(RESULT_OK, mainIntent);
                    finish();
                }
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.v("addCookItemActivityCreate", "true");
        setContentView(R.layout.add_cook_item_layout);
        imageView = (ImageView) findViewById(R.id.a_cook_item_image);
        editText = (EditText) findViewById(R.id.a_cook_item_text);
        finishBtn = (Button) findViewById(R.id.a_cook_item_finish);
        finishBtn.setOnClickListener(this);
        imageView.setOnClickListener(this);

        mainIntent = getIntent();
        if(mainIntent.getBooleanExtra("update", false)){
            initUpdate();
        }



    }

    private void initUpdate() {
        EcCookitem ci = (EcCookitem) mainIntent.getSerializableExtra("updatecookmenu");
        LogUtil.v("state", "update..." + ci.toString());
        picPath = ci.getImgLocalUrl();
        imageView.setImageBitmap(PictureUtil.getPictureByUri(AddCookItemActivity.this,
                Uri.parse(picPath)));
        editText.setText(ci.getItemContent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_cook_item, menu);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            picPath = uri.toString();
            try {
                b = PictureUtil.getPictureByUri(AddCookItemActivity.this, uri);
                imageView.setImageBitmap(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a_cook_item_image:
                /***
                 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);


                break;
            case R.id.a_cook_item_finish:
                aCookNameFinish();
                break;
        }
    }
    private void aCookNameFinish(){
        if (picPath == null) {
            Toast.makeText(AddCookItemActivity.this, "请选择图片！", Toast.LENGTH_LONG).show();
        } else {
            try {
                startNetTask(PictureUtil.saveFile(PictureUtil.getPictureByUri(AddCookItemActivity.this, Uri.parse(picPath))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void startNetTask(final File file){
        progressDialog = ProgressDialog.show(AddCookItemActivity.this,
                "请稍等...", "上传中...", true);
        new Thread(new Runnable() {

            @Override
            public void run() {
                LogUtil.v("requestURL", requestURL);
                String path = UploadUtil.uploadFile(file, requestURL);

                Message msg = new Message();
                msg.what = RESPONSE_CODE;
                msg.obj = (path == null) ? "error" : path;
                handle.sendMessage(msg);

            }
        }).start();
    }
}
