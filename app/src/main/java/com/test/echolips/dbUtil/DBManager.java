package com.test.echolips.dbUtil;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.echolips.xTools.LogUtil;

/**
 * 
 * @author Xuwenchao
 *
 */
public class DBManager extends SQLiteOpenHelper {

	private static final String name=ReadProperties.getDBName();
	private static final int version=ReadProperties.getVersion();

	public DBManager(Context context) {
		super(context, name, null, version);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		String[] sqls=ReadProperties.getCreateSql();
		for (String sql : sqls) {
			db.execSQL(sql);
		}
		String[] initsqls = ReadProperties.getInitSql();
		for(String sql : initsqls){
			db.execSQL(sql);
		}
		LogUtil.v("sql", "sql created and init...");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String[] sqls=ReadProperties.getDropSql();
		for (String sql : sqls) {
			db.execSQL(sql);
		}
		onCreate(db);
	}
}









