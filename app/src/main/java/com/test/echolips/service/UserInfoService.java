package com.test.echolips.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.echolips.bean.EcUser;
import com.test.echolips.xTools.LogUtil;
import com.test.echolips.xTools.PostUtil;
import com.test.echolips.xTools.XBasePath;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 *
 */
public class UserInfoService {

	private static final String loginPath = XBasePath.requestURL + "/info";
	public static int id = 0;
	/**
	 *
	 * @param name
	 * @param password
	 * @return -1:用户名密码错误；0:网络连接失败； >1 正确
	 */
	public int checkLogin(String name, String password, boolean auto){
		Map<String, Object> map = new HashMap<>();
		map.put("nickname", name);
		map.put("password", password);
		String response = null;
		if(auto){
			response = PostUtil.post(map, loginPath, 3000);
		}else{
			response = PostUtil.post(map, loginPath);
		}
		if(response != null){
			Object object = JSON.parseObject(response).get("id");
			if(object == null){
				return -1;
			}else{
				id = (int)object;
				return id;
			}
		}
		return 0;
	}
}
