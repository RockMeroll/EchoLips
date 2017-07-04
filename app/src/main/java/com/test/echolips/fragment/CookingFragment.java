package com.test.echolips.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.test.echolips.R;
import com.test.echolips.activity.CookMenuActivity;
import com.test.echolips.activity.MyApplication;
import com.test.echolips.adapter.MenuAdapter;
import com.test.echolips.bean.EcCookmenu;
import com.test.echolips.service.UserInfoService;
import com.test.echolips.utils.HttpTest;
import com.test.echolips.utils.PicBitMap;
import com.test.echolips.utils.SavePic;
import com.test.echolips.widget.WaitDialog;

import java.util.ArrayList;
import java.util.List;

public class CookingFragment extends Fragment{
    private static final int SHOW_RESPONSE = 0;
    private int uid;
    private List<EcCookmenu> menu_list;
    private List<String> local_path_list;     //存放所有图片的本地路径
    private MenuAdapter mAdapter;
    private ListView mListView;
    private WaitDialog mDialog;
    private Button startDaohang;

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SHOW_RESPONSE) {
                String response = (String) msg.obj;
                List<EcCookmenu> ml = JSON.parseArray(response, EcCookmenu.class);
                menu_list.clear();
                menu_list.addAll(ml);
                mAdapter.notifyDataSetChanged();
            }
            mDialog.dismiss();
        }
    };

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        LinearLayout root = (LinearLayout) paramLayoutInflater.inflate(R.layout.frag_cooking, null);
        mListView = (ListView) root.findViewById(R.id.list_view_star);
        startDaohang = (Button) root.findViewById(R.id.x_star_start_daohang);
        init();
        //request();

        return root;
    }

    private void init() {
        //// TODO: 2017/1/17
        uid = 1;

        local_path_list = new ArrayList<>();
        menu_list = new ArrayList<>();
        mAdapter = new MenuAdapter(CookingFragment.this.getActivity(), R.layout.menu_layout, menu_list, local_path_list);
        mListView.setAdapter(mAdapter);
        startDaohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContextObject(), "您点击了导航按钮", Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取的一个菜谱实例
                //传对象的属性
                EcCookmenu m = menu_list.get(position);
                Intent intent = new Intent(getActivity(), CookMenuActivity.class);
                //传入一个菜谱列表给下一个活动
                intent.putExtra("menu_cid", m.getId());
                intent.putExtra("menu_uid", m.getUid());
                intent.putExtra("menu_name", m.getCookname());
                intent.putExtra("cookpoint", m.getCookingpoint());//烹饪要点
                intent.putExtra("bitmappath", m.getImgsrc());
                startActivity(intent);
                // finish();
                Toast.makeText(getActivity(), "This is " + m.getCookname(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void request() {
        mDialog = new WaitDialog(this.getActivity());
        mDialog.show();
        local_path_list.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = new HttpTest().postRequest_starcook(UserInfoService.id);
                    //解析
                    List<EcCookmenu> tmp = JSON.parseArray(response, EcCookmenu.class);
                    for (EcCookmenu m : tmp) {
                        //图片路径
                        String path = m.getImgsrc();
                        String local_path = null;
                        //如果本地文件路径不存在
                        //创建本地文件路径
                        if (!SavePic.fileIsExist(path)) {
                            //获取网络图片
                            Bitmap bitmap = new PicBitMap().getPic(path);
                            //获取图片的本地路径
                            local_path = new SavePic().saveFile(bitmap, path).getPath();
                        } else {
                            //本地路径存在
                            local_path = new SavePic().getLocalPath(path);
                        }
                        m.setImgLocalUrl(local_path);
                        local_path_list.add(local_path);
                    }
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = response.toString();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }).start();
    }
    @Override
    public void onResume() {
        super.onResume();
        request();
    }


}