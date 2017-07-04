package com.test.echolips.xTools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

import com.test.echolips.activity.*;

/**
 * @author Xuwenchao
 */
public class PictureUtil {

    /**
     *
     *
     * @param bm
     * @paramfileName
     * @throws Exception
     */
    public static File saveFile(Bitmap bm) throws Exception {
        File outputImage = new File(Environment.getExternalStorageDirectory() + "/pic", UUID.randomUUID().toString() + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputImage));
        bm.compress(Bitmap.CompressFormat.JPEG, 30, bos);
        bos.flush();
        bos.close();
        return outputImage;
    }

    public static Bitmap thumbnailBitmap(Context context, String path){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse(path)),
                    null, options);
            int picWidth  = options.outWidth;
            int picHeight = options.outHeight;
            options.inSampleSize = 0;
            if(picWidth > picHeight){
                if(picWidth > 80)
                    options.inSampleSize = picWidth/80;
            }else{
                if(picHeight > 80)
                    options.inSampleSize = picHeight/80;
            }

            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse(path)),
                    null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getPictureByUri(Context context, Uri uri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri)
                    , null, options);

            int picWidth  = options.outWidth;
            int picHeight = options.outHeight;

            WindowManager windowManager =
                    (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            int screenWidth = display.getWidth();
            int screenHeight = display.getHeight();
            options.inSampleSize = 0;
            if(picWidth > picHeight){
                if(picWidth > screenWidth)
                    options.inSampleSize = picWidth/screenWidth;
            }else{
                if(picHeight > screenHeight)
                    options.inSampleSize = picHeight/screenHeight;
            }
            options.inJustDecodeBounds = false;
            Bitmap b = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri)
                    , null, options);
            LogUtil.v("bitmap", "bitmap");
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getUrlBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
}
