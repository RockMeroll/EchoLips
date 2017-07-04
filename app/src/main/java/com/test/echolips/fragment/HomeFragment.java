package com.test.echolips.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.test.echolips.R;
import com.test.echolips.activity.AddCookMenuActivity;
import com.test.echolips.activity.CookMenuActivity;
import com.test.echolips.activity.MyApplication;
import com.test.echolips.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{

    Context mContext;

    protected void Demo(View paramView) {
        List<TextView> localArrayList = new ArrayList<>();

        localArrayList.add((TextView) paramView.findViewById(R.id.btn_shopping_cart));
        localArrayList.add((TextView) paramView.findViewById(R.id.btn_search));
        localArrayList.add((TextView) paramView.findViewById(R.id.btn_add_cook_menu));
        localArrayList.add((TextView) paramView.findViewById(R.id.btn_rank));
        localArrayList.add((TextView) paramView.findViewById(R.id.btn_category));
        localArrayList.add((TextView) paramView.findViewById(R.id.btn_wiki));
        localArrayList.add((TextView) paramView.findViewById(R.id.btn_top_man));

        View.OnClickListener local1 = new View.OnClickListener() {
            public void onClick(View paramView) {
                Toast.makeText(mContext, paramView.getId() + "", Toast.LENGTH_LONG) .show();
            }
        };

        for(TextView tv:localArrayList){
            tv.setOnClickListener(this);
        }
    }

    protected void initView(LayoutInflater paramLayoutInflater, View root) {
        ViewPager localViewPager = (ViewPager) root.findViewById(R.id.viewpager);
        List<View> localArrayList = new ArrayList<>();

        localArrayList.add(paramLayoutInflater.inflate(R.layout.page1, null));
        localArrayList.add(paramLayoutInflater.inflate(R.layout.page2, null));
        localArrayList.add(paramLayoutInflater.inflate(R.layout.page3, null));
        localViewPager.setAdapter(new myPagerAdapter(localArrayList));

        Demo(root);
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
        View root = paramLayoutInflater.inflate(R.layout.frag_home, paramViewGroup, false);
        this.mContext = getActivity();
        initView(paramLayoutInflater, root);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                MyApplication.startActivity(mContext, SearchActivity.class);
                break;
            case R.id.btn_add_cook_menu:
                MyApplication.startActivity(mContext, AddCookMenuActivity.class);
                break;
        }
    }

    private class myPagerAdapter extends PagerAdapter {
        private List<View> ViewList;

        public myPagerAdapter(List<View> ViewList) {
            this.ViewList = ViewList;
        }

        public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
            paramViewGroup.removeView(ViewList.get(paramInt));
        }

        public int getCount() {
            return this.ViewList.size();
        }

        public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
            paramViewGroup.addView(ViewList.get(paramInt));
            return this.ViewList.get(paramInt);
        }

        public boolean isViewFromObject(View paramView, Object paramObject) {
            return paramView == paramObject;
        }
    }

}