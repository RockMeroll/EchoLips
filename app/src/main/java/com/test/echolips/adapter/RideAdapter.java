package com.test.echolips.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.echolips.R;
import com.test.echolips.bean.EcCookitem;

import java.util.List;

/**
 * Created by 14439 on 2017-1-10.
 */

public class RideAdapter extends ArrayAdapter<EcCookitem> {
    int resId;
    String picpath;
    List<String> pathList;
    public RideAdapter(Context context, int textViewId, List<EcCookitem> list){
        super(context,textViewId,list);
        resId = textViewId;
    }
    public RideAdapter(Context context, int textViewId,
                            List<EcCookitem> list, List<String> pathList){
        super(context,textViewId,list);
        resId = textViewId;
        this.pathList = pathList;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        EcCookitem cookItem = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resId,null);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.image_view_ride);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.text_view_ride);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(String.valueOf(cookItem.getStep()));
        List<String> list = pathList;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap bitmap= BitmapFactory.decodeFile(list.get(position), options);
        viewHolder.imageView.setImageBitmap(bitmap);
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
