package com.test.echolips.utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;


public class SavePic {

    public static String filepath;
    public static File file;

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
    public SavePic(){
        file = Environment.getExternalStorageDirectory();
        filepath = file + "/pic";
        File f = new File(filepath);
        if(!f.exists()){
            f.mkdir();
        }
    }
    public String returenPath(){
        return filepath;
    }


    public static File saveFile(Bitmap bm) throws Exception{

//        //最后一个/的位置
//        int last = path.lastIndexOf("/");
//        String str;
//        str = path.substring(last);
        File outputImage = new File(file + "/pic", UUID.randomUUID().toString() + ".jpg");
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

    /**
     * 判断路径是否存在
     * @param path
     * @return
     */
    public static boolean fileIsExist(String path){
        int last = path.lastIndexOf("/");//aaaa/aa

        File outputImage = new File(file + "/pic", path.substring(last + 1));
        try {
            if (outputImage.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据网络路径获取本地路径
     * @param path
     * @return
     */
    public static String getLocalPath(String path){
        int last = path.lastIndexOf("/");//aaaa/aa
        File outputImage = new File(file + "/pic", path.substring(last + 1));
        try {
            if (outputImage.exists()) {
                return outputImage.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File saveFile(Bitmap bm, String path) throws Exception{
        Log.v("SavePicfilePath", path);
        int last = path.lastIndexOf("/");
        File outputImage = new File(file + "/pic", path.substring(last + 1));
        try {
            if (outputImage.exists()) {
                Log.v("SavePicfile", path.substring(last + 1));
                return outputImage;
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
}