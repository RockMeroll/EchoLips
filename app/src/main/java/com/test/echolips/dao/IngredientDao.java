package com.test.echolips.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.test.echolips.dbUtil.DBManager;

public class IngredientDao {

	private Context context;
	private DBManager manager;
	private SQLiteDatabase db;
	
	public IngredientDao(Context context) {
		manager = new DBManager(context);
	}
	public List<String> queryLabelsFuzzy(String bindArgs){
		List<String> data=new ArrayList<>();
		db=manager.getReadableDatabase();
		String sql = "select detailname from ec_ingredient where detailname like ?";
		Cursor cur=db.rawQuery(sql, new String[]{"%" + bindArgs + "%"});
		while(cur.moveToNext()){
			String value=cur.getString(cur.getColumnIndex("detailname"));
			data.add(value);
		}
		cur.close();
		db.close();
		return data;
	}
}
