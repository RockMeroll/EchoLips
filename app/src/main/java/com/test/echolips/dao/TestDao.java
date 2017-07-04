package com.test.echolips.dao;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

public class TestDao extends BaseDao{

	public TestDao(Context context) {
		super(context);
	}
	public List<HashMap<String, String>> test(){
		return null;
		//return executeQuery("select * from ec_ingredient where mainsort like '%%'", null);
	}

}
