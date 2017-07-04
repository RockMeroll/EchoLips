package com.test.echolips.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.test.echolips.xTools.PictureUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 14439 on 2017-1-9.
 */

public class PicBitMap {
    public Bitmap getPic(String path)throws IOException{
//        URL url = new URL("http://192.168.2.112:8080/image/56150810-9431-4a51-a597-d6b6312089ce.JPEG");
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        }
        return null;
    }
}
