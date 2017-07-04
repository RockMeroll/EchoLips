package com.test.echolips.activity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.test.echolips.R;
import com.test.echolips.adapter.CookItemAdapter;
import com.test.echolips.bean.EcCookingredient;
import com.test.echolips.bean.EcCookitem;
import com.test.echolips.bean.EcCookmenu;
import com.test.echolips.service.UserInfoService;
import com.test.echolips.xTools.BeanToMapUtil;
import com.test.echolips.xTools.LogUtil;
import com.test.echolips.xTools.PictureUtil;
import com.test.echolips.xTools.PostUtil;
import com.test.echolips.xTools.XBasePath;

import com.mobeta.android.dslv.*;

/**
 * @author Xuwenchao
 */
public class CookItemListActivity extends BaseActivity {
    private static final int REQUEST_ADD_CODE = 101;
    private static final int REQUEST_UPDATE_CODE = 102;
    private static final int RESPONSE_CODE = 200;

    private ProgressDialog progressDialog;
    protected static final String RequestURL = XBasePath.requestURL + "test";
    private List<EcCookitem> cookItemList = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();
    private DragSortListView listView;
    private CookItemAdapter adapter;
    private Button addBtn;
    private Button finishItemBtn;
    private EcCookmenu cookMenu;
    private ArrayList<EcCookingredient> cookIngredient;
    private int listIndex = -1;
    private Handler handle = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == RESPONSE_CODE) {
                if ("error".equals(msg.obj)) {
                    Toast.makeText(CookItemListActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CookItemListActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                }
            }
            progressDialog.dismiss();
            setResult(RESULT_OK);
            finish();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.v("addCookItemListActivity", "true");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cook_item_list_layout);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        cookMenu = (EcCookmenu) intent.getSerializableExtra("cookMenu");
        cookIngredient = (ArrayList<EcCookingredient>) intent.getSerializableExtra("ingredients");
        //cookItemList
        LogUtil.v("ingredients", cookIngredient.toString());

        addBtn = (Button) findViewById(R.id.add_item_btn);
        finishItemBtn = (Button) findViewById(R.id.finish_item_btn);
        listView = (DragSortListView) findViewById(R.id.cook_item_list);
        adapter = new CookItemAdapter(CookItemListActivity.this, R.layout.cook_item_adapter_layout, cookItemList, bitmaps);
        listView.setAdapter(adapter);
        listView.setDragEnabled(true); //设置是否可拖动

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                EcCookitem pt = cookItemList.get(pos);
                listIndex = pos;
                Intent intent = new Intent(CookItemListActivity.this, AddCookItemActivity.class);
                //false : 添加 ； true : 更新
                intent.putExtra("update", true);
                intent.putExtra("updatecookmenu", pt);
                startActivityForResult(intent, REQUEST_UPDATE_CODE);
                //Toast.makeText(CookItemListActivity.this, pt.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        // 监听器在手机拖动停下的时候触发
        listView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {// from to 分别表示 被拖动控件原位置 和目标位置
                if (from != to) {
                    EcCookitem item = (EcCookitem) adapter.getItem(from);// 得到listview的适配器
                    Bitmap bm = adapter.getBitmap(from);
                    adapter.remove(from);// 在适配器中”原位置“的数据。
                    adapter.insert(item, to, bm);
                }
            }
        });
        // 删除监听器，点击左边差号就触发。删除item操作。
        listView.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int which) {
                Toast.makeText(CookItemListActivity.this, "remove", Toast.LENGTH_SHORT);
                adapter.remove(which);
            }
        });
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CookItemListActivity.this, AddCookItemActivity.class);
                //false : 添加 ； true : 更新
                intent.putExtra("update", false);
                startActivityForResult(intent, REQUEST_ADD_CODE);
            }
        });
        finishItemBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, Object> map = BeanToMapUtil.CookMenuToMap(cookMenu);
                map.putAll(BeanToMapUtil.CookItemToMap(cookItemList));
                map.putAll(BeanToMapUtil.CookIngredientToMap(cookIngredient));
                map.put("uid", UserInfoService.id);
                LogUtil.v("CookItemListUid", Integer.toString(UserInfoService.id));
                progressDialog = ProgressDialog.show(CookItemListActivity.this,
                        "请稍等...", "上传中...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = PostUtil.post(map, RequestURL);
                        LogUtil.v("response", response == null ? "error" : response);
                        Message msg = new Message();
                        msg.what = RESPONSE_CODE;
                        msg.obj = response;
                        handle.sendMessage(msg);
                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cook_item_list, menu);
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
        switch (requestCode) {
            case REQUEST_ADD_CODE:
                if (resultCode == RESULT_OK) {
                    EcCookitem cookItem = (EcCookitem) data.getSerializableExtra("cook_item");
                    cookItemList.add(cookItem);
                    bitmaps.add(PictureUtil.thumbnailBitmap(CookItemListActivity.this, cookItem.getImgLocalUrl()));
                    adapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_UPDATE_CODE:
                if (resultCode == RESULT_OK) {
                    EcCookitem cookItem = (EcCookitem) data.getSerializableExtra("cook_item");
                    cookItemList.set(listIndex, cookItem);
                    bitmaps.set(listIndex, PictureUtil.thumbnailBitmap(CookItemListActivity.this, cookItem.getImgLocalUrl()));
                    adapter.notifyDataSetChanged();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
