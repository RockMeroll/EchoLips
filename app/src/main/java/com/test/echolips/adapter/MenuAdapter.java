package com.test.echolips.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.test.echolips.R;
import com.test.echolips.activity.SearchActivity;
import com.test.echolips.bean.EcCookmenu;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class MenuAdapter extends ArrayAdapter<EcCookmenu>{
    private int resId;
    private List<String> localPathAll;


    public MenuAdapter(Context context, int textViewId, List<EcCookmenu> list){
        super(context,textViewId,list);
        //布局Id
        this.resId = textViewId;
    }

    public MenuAdapter(Context context, int textViewId, List<EcCookmenu> list, List<String> pathAll){
        super(context,textViewId,list);
        //布局Id
        this.resId = textViewId;
        this.localPathAll = pathAll;
    }
    public View getView(int position , View convertView, ViewGroup parent){
        //获取某一个位置的实例
        EcCookmenu menu = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resId,null);
            //控件
            viewHolder.textView = (TextView)convertView.findViewById(R.id.cook_menu_name);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.image_view);
            viewHolder.textView_diff = (TextView)convertView.findViewById(R.id.cook_menu_label_diff);
            viewHolder.textView_name = (TextView)convertView.findViewById(R.id.cook_menu_author);
            viewHolder.textView_time = (TextView)convertView.findViewById(R.id.cook_menu_label_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(menu.getCookname());
        viewHolder.textView_name.setText(menu.getName());
        viewHolder.textView_diff.setText(menu.getHard());
        viewHolder.textView_time.setText(menu.getTime());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap bitmap= BitmapFactory.decodeFile(localPathAll.get(position), options);
        viewHolder.imageView.setImageBitmap(bitmap);
        return convertView;
    }
    class ViewHolder{
        TextView textView;
        ImageView imageView;
        TextView textView_diff;
        TextView textView_name;
        TextView textView_time;

    }

}
