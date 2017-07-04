package com.test.echolips.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.test.echolips.dbUtil.DBManager;


/**
 *
 * @author Xuwenchao
 *
 */
public class BaseDao {

	private DBManager manager;

	private SQLiteDatabase db;
	
	public BaseDao(Context context){
		manager=new DBManager(context);
	}

	protected final void executeUpdate(String sql,Object...bindArgs){
		db=manager.getWritableDatabase();
		if(bindArgs!=null){
			db.execSQL(sql, bindArgs);
		}else{
			db.execSQL(sql);
		}
		db.close();
	}

	protected final List<HashMap<String, String>> executeQuery(String sql,String...bindArgs){ 
		List<HashMap<String, String>> data=new ArrayList<HashMap<String,String>>();
		db=manager.getReadableDatabase();
		Cursor cur=db.rawQuery(sql, bindArgs);

		String[] names=cur.getColumnNames();
		HashMap<String, String> item=null;
		while(cur.moveToNext()){
			item=new HashMap<String, String>();
			for(String name:names){
				String value=cur.getString(cur.getColumnIndex(name));
				item.put(name, value);
			}
			data.add(item);
		}
		cur.close();
		db.close();
		return data;
	}
	
	
}
