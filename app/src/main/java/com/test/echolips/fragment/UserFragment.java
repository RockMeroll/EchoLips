package com.test.echolips.fragment;

import com.test.echolips.activity.LoginActivity;
import com.test.echolips.activity.MyApplication;
import com.test.echolips.xTools.LogUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.test.echolips.R;
import com.test.echolips.service.UserInfoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import static android.content.Context.MODE_PRIVATE;

public class UserFragment extends Fragment {
    private ListView mListView;

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> localArrayList = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("icon", R.drawable.q_collect_download);
        map1.put("title", "我的收藏");
        localArrayList.add(map1);

        map1 = new HashMap<>();
        map1.put("icon", R.drawable.q_track);
        map1.put("title", "我的足迹");
        localArrayList.add(map1);


        map1 = new HashMap<>();
        map1.put("icon", R.drawable.q_advise);
        map1.put("title", "意见反馈");
        localArrayList.add(map1);

        map1 = new HashMap<>();
        map1.put("icon", R.drawable.q_other_app);
        map1.put("title", "推荐悦时给朋友");
        localArrayList.add(map1);

        map1 = new HashMap<>();
        map1.put("icon", R.drawable.q_app_mark);
        map1.put("title", "APP市场打分");
        localArrayList.add(map1);

        return localArrayList;
    }
    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {

        View root = paramLayoutInflater.inflate(R.layout.frag_user, paramViewGroup, false);
        return init(root);
    }

    private View init(View view){
        mListView = ((ListView) view.findViewById(R.id.user_page_menu));
        SimpleAdapter localSimpleAdapter = new SimpleAdapter(
                getActivity(), getData(), R.layout.user_menu_item,
                new String[]{"icon", "title"},
                new int[]{R.id.icon, R.id.title});
        mListView.setAdapter(localSimpleAdapter);
        TextView username = (TextView) view.findViewById(R.id.fragment_user_name);
        SharedPreferences pre = MyApplication.getContextObject().getSharedPreferences("info", MODE_PRIVATE);
        username.setText("    " + pre.getString("name", "游客"));
        TextView user_set = (TextView) view.findViewById(R.id.user_set);
        user_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

}