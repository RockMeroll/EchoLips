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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.test.echolips.R;
import com.test.echolips.bean.EcCookingredient;
import com.test.echolips.bean.EcCookmenu;
import com.test.echolips.xTools.LogUtil;
import com.test.echolips.xTools.PictureUtil;
import com.test.echolips.xTools.UploadUtil;
import com.test.echolips.xTools.XBasePath;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Xuwenchao
 */
public class AddCookMenuActivity extends BaseActivity implements OnClickListener {
    protected static final int REQUEST_ADD_LABEL_CODE = 0;
    protected static final int REQUEST_ADD_INGREDIENT_CODE = 2;
    private static final int REQUEST_ADD_FINISH_CODE = 103;

    protected static final String REQUEST_URL = XBasePath.requestURL + "pic/upload";
    protected static final int RESPONSE_CODE = 1;
    private String picPath = null;
    private EditText name;
    private EditText introduction;
    private Spinner mealsNum;
    private Spinner mealsHard;
    private Spinner mealsTime;
    private EditText cookPoint;
    private ArrayList<String> labels = null;
    private ArrayList<EcCookingredient> ingredients = null;
    private ImageView imageView;
    private Button created;
    private Button addLabel;
    private Button addIngredient;
    private ProgressDialog progressDialog;
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == RESPONSE_CODE) {
                LogUtil.v("path", msg.obj.toString());
                if (!msg.obj.toString().equals("error")) {

                    EcCookmenu cookMenu = new EcCookmenu(name.getText().toString(), picPath, msg.obj.toString(),
                            introduction.getText().toString(), mealsNum.getSelectedItem().toString(),
                            mealsHard.getSelectedItem().toString(), mealsTime.getSelectedItem().toString(),
                            cookPoint.getText().toString(), labels);
                    LogUtil.v("cookMenu", cookMenu.toString());

                    Intent intent = new Intent(AddCookMenuActivity.this, CookItemListActivity.class);
                    intent.putExtra("cookMenu", cookMenu);
                    intent.putExtra("ingredients", ingredients);
                    startActivityForResult(intent, REQUEST_ADD_FINISH_CODE);
                    //startActivity(intent);
                } else {
                    Toast.makeText(AddCookMenuActivity.this, "网络错误,请重试...", Toast.LENGTH_SHORT).show();
                }
            }
            progressDialog.dismiss();
        }

        ;
    };

    private void init() {
        TextView btn_back = (TextView)findViewById(R.id.title_bar_btn_back);
        btn_back.setOnClickListener(this);

        name = (EditText) findViewById(R.id.cook_menu_name);
        introduction = (EditText) findViewById(R.id.cook_introduction);
        mealsNum = (Spinner) findViewById(R.id.cook_meals_num);
        mealsTime = (Spinner) findViewById(R.id.cook_meals_time);
        mealsHard = (Spinner) findViewById(R.id.cook_meals_hard);
        cookPoint = (EditText) findViewById(R.id.cook_menu_cook_point);
        imageView = (ImageView) findViewById(R.id.cook_menu_img);
        imageView.setOnClickListener(this);
        created = (Button) findViewById(R.id.created);
        addIngredient = (Button) findViewById(R.id.add_ingredient);
        addIngredient.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCookMenuActivity.this, IngredientSelectActivity.class);
                startActivityForResult(intent, REQUEST_ADD_INGREDIENT_CODE);
            }
        });
        addLabel = (Button) findViewById(R.id.add_label);
        addLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCookMenuActivity.this, LabelsSelectActivity.class);
                startActivityForResult(intent, REQUEST_ADD_LABEL_CODE);
            }
        });
        created.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加各种条件判定
                if (picPath == null) {
                    Toast.makeText(AddCookMenuActivity.this, "请添加图片", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if(name.getText().toString().trim().length() == 0){
                            Toast.makeText(AddCookMenuActivity.this, "请输入菜谱名", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(introduction.getText().toString().trim().length() == 0){
                            Toast.makeText(AddCookMenuActivity.this, "请输入菜谱介绍", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(labels != null && labels.size() == 0){
                            Toast.makeText(AddCookMenuActivity.this, "请输入至少一个标签", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(ingredients != null && ingredients.size() == 0){
                            Toast.makeText(AddCookMenuActivity.this, "请输入所用食材", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(cookPoint.getText().toString().trim().length() == 0){
                            Toast.makeText(AddCookMenuActivity.this, "请输入烹饪要点", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(labels == null || ingredients == null){
                            Toast.makeText(AddCookMenuActivity.this, "请输入标签或食材", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startNetTask(PictureUtil.saveFile(PictureUtil.getPictureByUri(AddCookMenuActivity.this, Uri.parse(picPath))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void startNetTask(final File file) {
        progressDialog = ProgressDialog.show(AddCookMenuActivity.this,
                "请稍等...", "上传中...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = UploadUtil.uploadFile(file, REQUEST_URL);
                Message msg = new Message();
                msg.what = RESPONSE_CODE;
                msg.obj = path == null ? "error" : path;
                handle.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_cook_menu_layout);


        init();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_cook_menu, menu);
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
        if (requestCode == REQUEST_ADD_INGREDIENT_CODE) {
            if (resultCode == RESULT_OK) {
                ingredients = (ArrayList<EcCookingredient>) data.getSerializableExtra("ingredients");
                addIngredient(ingredients);
            }
        } else if (requestCode == REQUEST_ADD_LABEL_CODE) {
            if (resultCode == RESULT_OK) {
                labels = (ArrayList<String>) data.getSerializableExtra("labels");
                addLabel(labels);
                LogUtil.v("AddCookMenuActivityLabels", labels.toString());
            }
        } else if(requestCode == REQUEST_ADD_FINISH_CODE){
            if (resultCode == RESULT_OK) {
                //跳转
                Intent intent = new Intent();
                intent.setClass(this,CompletedActivity.class);
                startActivity(intent);
                finish();

            }
        }else if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            picPath = uri.toString();
            try {
                Bitmap b = PictureUtil.getPictureByUri(AddCookMenuActivity.this, uri);
                LogUtil.v("xuri", uri.toString());
                LogUtil.v("xbitmap", b == null ? "null" : "not null");
                imageView.setImageBitmap(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }


    private void addIngredient(ArrayList<EcCookingredient> param){
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout root = (LinearLayout) this.findViewById(R.id.ingredient_linearLayout);
        root.removeAllViews();
        for(EcCookingredient i:param){
            ViewGroup ll = (ViewGroup) inflater.inflate(R.layout.label_layout, null);
            TextView tv = (TextView) ll.findViewById(R.id.label_name);
            tv.setText(i.getIngredientname());
            root.addView(ll);
        }
    }

    private void addLabel(ArrayList<String> param){
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout root = (LinearLayout) this.findViewById(R.id.label_linearLayout);
        root.removeAllViews();
        for(String i:param){
            ViewGroup ll = (ViewGroup) inflater.inflate(R.layout.label_layout, null);
            TextView tv = (TextView) ll.findViewById(R.id.label_name);
            tv.setText(i);
            root.addView(ll);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cook_menu_img:
                /***
                 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;
            case R.id.title_bar_btn_back:
                this.finish();
                break;

            default:
                break;
        }
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
}
