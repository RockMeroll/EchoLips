package com.test.echolips.activity;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.test.echolips.R;
import com.test.echolips.adapter.MenuAdapter;
import com.test.echolips.bean.EcCookmenu;
import com.test.echolips.utils.HttpTest;
import com.test.echolips.utils.PicBitMap;
import com.test.echolips.utils.SavePic;
import com.test.echolips.widget.WaitDialog;
import com.test.echolips.xTools.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private WaitDialog mWaitDialog;

    //输入历史
    private List<String> list_history                   = new ArrayList<String>();
    public static final int SHOW_RESPONSE               = 0;
    //查询测试数组
    private String[] str                                = {"土豆丝","yyyy","yhhf","fswer","巧克喜"};
    //存放menu对象的list
    private List<EcCookmenu> menuList                         = new ArrayList<EcCookmenu>();
    public  List<String> pathAll;//存放所有主图的list
    private AutoCompleteTextView autoCompleteTextView;
    private ListView listView ;
    // 标记上次滑动位置，初始化默认为0
    private int lastVisibleItemPosition                 = 0;
    // 标记是否滑动
    private boolean scrollFlag                          = false;

    //传入服务器的页数
    private int page = 1;
    private String cook_name = null;
    private MenuAdapter ma;
    private String filepath;

    private TabHost myTabHost;

    //新建handler对象，接收Massage

    public Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == SHOW_RESPONSE){
                String response = (String)msg.obj;
                menuList.addAll(JSON.parseArray(response,EcCookmenu.class));
                ma.notifyDataSetChanged();
            }
            mWaitDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        myTabHost = (TabHost) findViewById(R.id.tabHost);
        myTabHost.setup();//实例化了tabWidget和tabContent
        myTabHost.addTab(myTabHost.newTabSpec("菜谱").setIndicator("菜谱").setContent(R.id.tab_menu));
        myTabHost.addTab(myTabHost.newTabSpec("专辑").setIndicator("专辑").setContent(R.id.tab_ablum));
        myTabHost.addTab(myTabHost.newTabSpec("达人").setIndicator("达人").setContent(R.id.tab_top_man));

        pathAll = new ArrayList<String>();
        //0.获取控件实例
        TextView button = (TextView)findViewById(R.id.btn_search);
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        listView= (ListView)findViewById(R.id.menu_view);
        //1.相似搜素适配器
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,str);
        autoCompleteTextView.setAdapter(arrayAdapter);

        //ma = new MenuAdapter(SearchActivity.this,R.layout.menu_layout,menuList);
        ma = new MenuAdapter(SearchActivity.this,R.layout.menu_layout,menuList, pathAll);
        listView.setAdapter(ma);


        //2.点击按钮事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入的菜单名称
                cook_name = autoCompleteTextView.getText().toString();

                if(cook_name.length() == 0){
                    Toast.makeText(SearchActivity.this,"请输入搜索内容！",Toast.LENGTH_LONG).show();
                    return;
                }

                mWaitDialog = new WaitDialog(SearchActivity.this);
                mWaitDialog.show();
                ma.clear();
                if (!cook_name.equals("")) {
                    //新线程http请求
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                        try {
                            //http请求实例
                            HttpTest ht = new HttpTest();
                            //返回http请求
                            String responses = ht.postRequest(1, cook_name);
                            menuList.clear();
                            pathAll.clear();
                            //response存放的是请求发回的json字符数组
                            List<EcCookmenu> currentList = JSON.parseArray(responses, EcCookmenu.class);
                            for (EcCookmenu mt : currentList) {
                                //图片网络路径
                                String path = mt.getImgsrc();
                                String picpath = null;
                                if (!SavePic.fileIsExist(path)) {
                                    Bitmap bitmap = new PicBitMap().getPic(path);
                                    picpath = new SavePic().saveFile(bitmap, path).getPath();
                                } else {
                                    picpath = SavePic.getLocalPath(path);
                                }
                                mt.setImgLocalUrl(picpath);
                                pathAll.add(picpath);
                            }
                            Message mes = new Message();
                            mes.what = SHOW_RESPONSE;
                            mes.obj = responses.toString();
                            handler.sendMessage(mes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        }
                    }).start();
                }
            }
        });

        //3.上滑
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case ListView.OnScrollListener.SCROLL_STATE_IDLE:
                        scrollFlag = false;
                        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {

                            page++;
                            Log.v("state", String.valueOf(page));
                            mWaitDialog = new WaitDialog(SearchActivity.this);
                            mWaitDialog.show();


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //http请求实例
                                        HttpTest ht = new HttpTest();
                                        //获取http请求,返回recv
                                        String responses = ht.postRequest(page, cook_name.toString());

                                        List<EcCookmenu> currentList = JSON.parseArray(responses, EcCookmenu.class);

                                        for (EcCookmenu mt : currentList) {
                                            String path = mt.getImgsrc();
                                            String picpath = null;
                                            if (!SavePic.fileIsExist(path)) {
                                                Bitmap bitmap = new PicBitMap().getPic(path);
                                                picpath = new SavePic().saveFile(bitmap, path).getPath();
                                            } else {
                                                picpath = SavePic.getLocalPath(path);
                                            }
                                            mt.setImgLocalUrl(picpath);
                                            pathAll.add(picpath);
                                        }
                                        Message mes = new Message();
                                        mes.what = SHOW_RESPONSE;
                                        mes.obj = responses.toString();
                                        handler.sendMessage(mes);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                        if (listView.getLastVisiblePosition() == 0) {
                            Toast.makeText(SearchActivity.this, "滚动到顶部", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case ListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        //滚动时
                        scrollFlag = true;
                        Toast.makeText(SearchActivity.this, "正在滚动", Toast.LENGTH_LONG).show();
                        break;
                    case ListView.OnScrollListener.SCROLL_STATE_FLING:
                        //惯性滑动
                        scrollFlag = true;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //滑动
                if (scrollFlag) {
                    if (firstVisibleItem > lastVisibleItemPosition) {
                        //上滑
                        Toast.makeText(SearchActivity.this, "上滑", Toast.LENGTH_SHORT).show();
                    } else if (firstVisibleItem < lastVisibleItemPosition) {
                        Toast.makeText(SearchActivity.this, "下滑", Toast.LENGTH_SHORT).show();
                    }
                    //更新位置
                    lastVisibleItemPosition = firstVisibleItem;
                }
            }
        });


        //4.点击菜单名称
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取的一个菜谱实例
                //传对象的属性
                EcCookmenu m = menuList.get(position);
                Intent intent = new Intent(SearchActivity.this, CookMenuActivity.class);
                //传入一个菜谱列表给下一个活动
                intent.putExtra("menu_cid", m.getId());
                intent.putExtra("menu_uid", m.getUid());
                intent.putExtra("menu_name", m.getCookname());
                intent.putExtra("cookpoint", m.getCookingpoint());//烹饪要点
                intent.putExtra("bitmappath", m.getImgsrc());
                startActivity(intent);
               // finish();
                Toast.makeText(SearchActivity.this, "This is " + m.getCookname(), Toast.LENGTH_SHORT).show();
            }
        });


        TextView tv = (TextView) findViewById(R.id.title_bar_btn_back);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });


    }
}
