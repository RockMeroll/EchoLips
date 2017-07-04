package com.test.echolips.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.echolips.R;
import com.test.echolips.bean.EcCookingredient;
import com.test.echolips.bean.EcCookitem;
import com.test.echolips.bean.EcCooklabel;
import com.test.echolips.bean.EcCookmainlabel;
import com.test.echolips.service.UserInfoService;
import com.test.echolips.utils.HttpTest;
import com.test.echolips.utils.PicBitMap;
import com.test.echolips.utils.SavePic;
import com.test.echolips.widget.WaitDialog;
import com.test.echolips.xTools.LogUtil;
import com.test.echolips.xTools.PictureUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CookMenuActivity extends BaseActivity {

    private WaitDialog mWaitDialog;

    public static final int SHOW_RESPONSE = 0;

    private List<EcCookitem> cooklist;
    private List<EcCookingredient> foodlist;
    private List<EcCooklabel> labelList;
    private List<EcCookmainlabel> mainLabelList;

    private TextView textView_title;
    private ImageView imageView; //顶栏图片
    private TextView btnStar;
    private ImageView pic;        //Avatar
    private Bitmap bitmap;
    private String name;
    private String Point;
    private boolean isStar;

    public List<String> pathList;//步骤图片Local路径的列表

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    //response存json数组
                    String response = (String) msg.obj;

                    JSONObject jsonObject = JSON.parseObject(response);
                    JSONArray result_step = jsonObject.getJSONArray("cimList");
                    JSONArray result_food = jsonObject.getJSONArray("ciList");

                    JSONArray result_label = jsonObject.getJSONArray("clList");
                    JSONArray result_main_label = jsonObject.getJSONArray("cmlList");

                    foodlist = JSON.parseArray(result_food.toJSONString(), EcCookingredient.class);
                    labelList = JSON.parseArray(result_label.toJSONString(), EcCooklabel.class);
                    mainLabelList = JSON.parseArray(result_main_label.toJSONString(), EcCookmainlabel.class);

                    String star = (String)jsonObject.get("star");
                    Log.v("star",star);
                    if(star.equals("true")){
                        btnStar.setEnabled(false);
                    }


                    //add Ingredent
                    LinearLayout IngredentLayout = (LinearLayout) findViewById(R.id.food_list_layout);
                    addIngredentItem(IngredentLayout, foodlist);

                    LinearLayout StepsLayout = (LinearLayout) findViewById(R.id.step_list_layout);
                    addSteps(StepsLayout, cooklist);

                    LinearLayout LabelLayout = (LinearLayout) findViewById(R.id.label_linearLayout);
                    addMainLabel(LabelLayout, mainLabelList);
                    addLabel(LabelLayout, labelList);

                    setPoint(Point);

                    imageView.setImageBitmap(bitmap);
                    textView_title.setText(name);
                    mWaitDialog.dismiss();
                    break;
            }
        }
    };

    //真实菜谱
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cook_menu);

        pic = (ImageView) findViewById(R.id.cook_menu_avatat);
        imageView = (ImageView) findViewById(R.id.cook_menu_pic);

        textView_title = (TextView) findViewById(R.id.text_view_title);

/*      //步骤列表：添加适配器存放
        listView_steps = (ListView)findViewById(R.id.steps_list_view);
        //食材列表：添加适配器存放显示
        listView_food = (ListView)findViewById(listView);
        imageView = (ImageView)findViewById(R.id.pic);*/

        pathList = new ArrayList<>();
        cooklist = new ArrayList<>();
        foodlist = new ArrayList<>();
        labelList = new ArrayList<>();
        Intent intent = getIntent();
        final int cid = intent.getIntExtra("menu_cid", -1);
        final int uid = intent.getIntExtra("menu_uid", -1);
        name = intent.getStringExtra("menu_name");
        Point = intent.getStringExtra("cookpoint");
        final String path = intent.getStringExtra("bitmappath");

        TextView button = (TextView) findViewById(R.id.q_bottom_btn_cook);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入下一个活动
                if(cooklist.size() != 0){
                    Intent intent = new Intent(CookMenuActivity.this, SpeechActivity.class);
                    //传入序列化的list给下一个活动
                    intent.putExtra("steps", (Serializable) cooklist);
                    intent.putExtra("pic", (Serializable) pathList);
                    intent.putExtra("cook_name",name);
                    Log.v("name",name);
                    LogUtil.v("cookma", pathList.toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CookMenuActivity.this,"此菜谱没有步骤！",Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView back_button = (TextView) findViewById(R.id.title_bar_btn_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CookMenuActivity.this.finish();
            }
        });


        //star_button
        btnStar = (TextView)findViewById(R.id.bottom_btn_star);
        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStar.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String star_result =
                                new HttpTest().postRequest_star(UserInfoService.id,cid);
                        if(star_result.equals("true")){
                            //新收藏成功
                            Log.v("star","true");
                            //change color
                        }
                        else if(star_result.equals("false")){
                            //已经收藏
                            Log.v("star","false");
                        }
                    }
                }).start();
            }
        });

        mWaitDialog = new WaitDialog(this);
        mWaitDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpTest ht = new HttpTest();
                    //传入cid，uid,返回json数据
                    String res = ht.postRequest_2(uid, cid, UserInfoService.id);
                    Log.v("state", String.valueOf(uid));
                    Log.v("state", String.valueOf(cid));
                    //主图
                    PicBitMap pbm = new PicBitMap();
                    bitmap = pbm.getPic(path);

                    JSONObject jsonObject = JSON.parseObject(res);
                    JSONArray result_step = jsonObject.getJSONArray("cimList");

                    cooklist = JSON.parseArray(result_step.toJSONString(), EcCookitem.class);

                    //++++++++++++++++++++++++++

                    for (EcCookitem ci : cooklist) {
                        String p = ci.getPictureurl();
                        String picpath = null;
                        if(!SavePic.fileIsExist(p)){
                            Bitmap bitmap = new PicBitMap().getPic(p);
                            picpath = new SavePic().saveFile(bitmap, p).getPath();
                        }else{
                            picpath = SavePic.getLocalPath(p);
                        }
                        ci.setImgLocalUrl(picpath);
                        pathList.add(picpath);
                    }
                    Message msg = new Message();
                    msg.what = SHOW_RESPONSE;
                    msg.obj = res.toString();
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void addIngredentItem(ViewGroup root, List<EcCookingredient> text) {
        for (EcCookingredient t : text) {
            TextView tv = (TextView) this.getLayoutInflater().inflate(R.layout.ingredentitem, null);
            String str = "                              ";
            tv.setText(str.substring(0, str.length() - t.getIngredientname().length() * 4)
                    + t.getIngredientname() + "               /               "
                    + t.getNum() + t.getCompanyname());
            root.addView(tv);
        }
    }


    protected void addLabel(ViewGroup root, List<EcCooklabel> text) {
        for (EcCooklabel t : text) {
            FrameLayout tv = (FrameLayout) this.getLayoutInflater().inflate(R.layout.label_layout, null);
            ((TextView)tv.findViewById(R.id.label_name)).setText(t.getLabelname());
            root.addView(tv);
        }
    }

    protected void addMainLabel(ViewGroup root, List<EcCookmainlabel> text) {
        for (EcCookmainlabel t : text) {
            FrameLayout frameLayout = (FrameLayout) this.getLayoutInflater().inflate(R.layout.label_layout, null);
            ((TextView) frameLayout.findViewById(R.id.label_name)).setText(t.getLabelname());
            root.addView(frameLayout);
        }
    }

    protected void addSteps(ViewGroup root, List<EcCookitem> text) {
        for (EcCookitem t : text) {
            FrameLayout ll = (FrameLayout) this.getLayoutInflater().inflate(R.layout.step_layout, null);

            TextView tVText = (TextView) ll.findViewById(R.id.text_step);
            ImageView tVPic = (ImageView) ll.findViewById(R.id.pic);

            tVText.setText(t.getItemcontent());
            LogUtil.v("cookmenuactivity", t.getImgLocalUrl());
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 5;
            Bitmap bitmap = BitmapFactory.decodeFile(t.getImgLocalUrl(), opts);
//          Bitmap bitmap =
//                    PictureUtil.thumbnailBitmap(CookMenuActivity.this, t.getImgLocalUrl());
            tVPic.setImageBitmap(bitmap);

            root.addView(ll);
        }
    }

    protected void setPoint(String str) {
        ((TextView) this.findViewById(R.id.steps_points)).setText(str);
    }
}
