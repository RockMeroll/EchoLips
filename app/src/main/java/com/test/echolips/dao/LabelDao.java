package com.test.echolips.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.test.echolips.dbUtil.DBManager;


public class LabelDao {

	private Context context;
	private DBManager manager;
	private SQLiteDatabase db;
	
	public LabelDao(Context context) {
		manager = new DBManager(context);
	}
	public List<String> queryLabelsFuzzy(String bindArgs){
		List<String> data=new ArrayList<>();
		db=manager.getReadableDatabase();
		String sql = "select detailname from ec_labelsortcode where detailname like ? or typename like ?";
		Cursor cur=db.rawQuery(sql, new String[]{"%" + bindArgs + "%", "%" + bindArgs + "%"});
		while(cur.moveToNext()){
			String value=cur.getString(cur.getColumnIndex("detailname"));
			data.add(value);
		}
		cur.close();
		db.close();
		return data;
	}
}
