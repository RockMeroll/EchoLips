package com.test.echolips.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.echolips.R;
import com.test.echolips.bean.EcCookitem;
import com.test.echolips.xTools.PictureUtil;

import java.io.FileNotFoundException;
import java.util.List;


public class CookItemAdapter extends ArrayAdapter<EcCookitem> {

    private int resourceId;
    private List<EcCookitem> items;
    private List<Bitmap> bitmaps;
    private Context context;

    public CookItemAdapter(Context context, int resource, List<EcCookitem> objects) {
        super(context, resource, objects);
        resourceId = resource;
        items = objects;
        this.context = context;
    }
    public CookItemAdapter(Context context, int resource, List<EcCookitem> objects,
                           List<Bitmap> bitmaps) {
        super(context, resource, objects);
        resourceId = resource;
        items = objects;
        this.context = context;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public EcCookitem getItem(int arg0) {
        return items.get(arg0);
    }
    public Bitmap getBitmap(int index){
        return bitmaps.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void remove(int arg0) {
        items.remove(arg0);
        bitmaps.remove(arg0);
        this.notifyDataSetChanged();
    }

    public void insert(EcCookitem item, int arg0, Bitmap bm) {
        items.add(arg0, item);
        bitmaps.add(arg0, bm);
        this.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EcCookitem pt = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.cook_item_image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.cook_item_text);
            viewHolder.delView = (ImageView) convertView.findViewById(R.id.cook_item_del);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String picPath = pt.getImgLocalUrl();
        if (picPath == null) {
            viewHolder.imgView.setImageResource(pt.getImgId());
        } else {
//            viewHolder.imgView.setImageBitmap(PictureUtil.thumbnailBitmap(bm));
            viewHolder.imgView.setImageBitmap(bitmaps.get(position));
        }
        viewHolder.textView.setText(pt.getItemContent());
        //viewHolder.delView.setImageResource(pt.getDelId());
        return convertView;
    }

    class ViewHolder {
        ImageView imgView;
        TextView textView;
        ImageView delView;
    }
}
