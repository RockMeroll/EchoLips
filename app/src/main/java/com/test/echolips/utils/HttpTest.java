package com.test.echolips.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.test.echolips.xTools.PostUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpTest {

    //服务器基地址
    //private String url_server = "http://192.168.2.112:8080/";
    private String url_server = "http://www.hyzbi.com:8080/";
    //存放地址的数组
    private String[] part = {"query","cookitems","completeitems","star","querystar"};
    //    private String part;
    private int rows = 10;

    public HttpTest() {

    }

    public String postRequest_starcook(int uid) {

        Map map = new HashMap<String, Object>();
        map.put("uid", uid);
        String res = new PostUtil().post(map, url_server + part[4]);
        return res;
    }

    public String getRequest() {
        Log.v("state", "1111111");
        String response = null;
        HttpGet httpRequest = new HttpGet("http://192.168.2.112:8080/query");
        HttpClient httpClient = new DefaultHttpClient();
        try {
            Log.v("state", "2222222");
            //获取http请求
            //执行请求，获取响应发回的对象
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            Log.v("state", "333333");
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                //response 是返回的json字符串或者字符串数组
                // response 返回
                response = EntityUtils.toString(httpEntity, "utf-8");
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String postRequest(int pagenumber, String name) {
        Map map = new HashMap<String, Object>();
        map.put("cookname", name);
        map.put("page", pagenumber);
        map.put("rows", rows);
        PostUtil pu = new PostUtil();
        return pu.post(map, url_server + part[0]);
    }

    public String postRequest_2(int uid, int cid, int userid) {//访问步骤食材标签服务器
        Map map = new HashMap<String, Object>();
        map.put("uid", uid);
        map.put("cid", cid);
        map.put("userid", userid);
        PostUtil pu = new PostUtil();
        return pu.post(map, url_server + part[2]);
    }


    //收藏
    public String postRequest_star(int uid, int cid) {
        String res = null;
        Map map = new HashMap<String, Object>();
        map.put("uid", uid);
        map.put("cid", cid);
        res = new PostUtil().post(map, url_server + part[3]);
        return res;
    }
}
