package com.test.echolips.xTools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author Xuwenchao
 *
 */
public class PostUtil {
	private static final int TIME_OUT = 10 * 1000;
	private static final String CHARSET = "utf-8";

	public static String post(Map<String, Object> map, String RequestURL) {
		String response = null;
		HttpURLConnection conn = null;
		try {
				HttpClient httpClient = new DefaultHttpClient();
				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
				httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
				HttpPost httpPost = new HttpPost(RequestURL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for(String key : map.keySet()){
					Object val = map.get(key);
					if(val instanceof ArrayList){
						for(String s : (ArrayList<String>)val){
							params.add(new BasicNameValuePair(key, s));
						}
					}else{
						params.add(new BasicNameValuePair(key, val.toString()));
					}
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
				httpPost.setEntity(entity);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				LogUtil.v("responsestate", Integer.toString(httpResponse.getStatusLine().getStatusCode()));
				if (httpResponse.getStatusLine().getStatusCode() == 200) {

					HttpEntity httpEntity = httpResponse.getEntity();
					response = EntityUtils.toString(httpEntity, "utf-8");
				}
				return response;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(conn != null){
				conn.disconnect();
			}
		}
	}

	public static String post(Map<String, Object> map, String RequestURL, int time) {
		String response = null;
		HttpURLConnection conn = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, time);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, time);
			HttpPost httpPost = new HttpPost(RequestURL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for(String key : map.keySet()){
				Object val = map.get(key);
				if(val instanceof ArrayList){
					for(String s : (ArrayList<String>)val){
						params.add(new BasicNameValuePair(key, s));
					}
				}else{
					params.add(new BasicNameValuePair(key, val.toString()));
				}
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			LogUtil.v("responsestate", Integer.toString(httpResponse.getStatusLine().getStatusCode()));
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				HttpEntity httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity, "utf-8");
			}
			return response;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(conn != null){
				conn.disconnect();
			}
		}
	}
}
