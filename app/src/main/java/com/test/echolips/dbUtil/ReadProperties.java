package com.test.echolips.dbUtil;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import com.test.echolips.activity.MyApplication;
import com.test.echolips.xTools.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Xuwenchao
 */
public class ReadProperties extends Properties{

	private static ReadProperties read=null;
	private ReadProperties(){

		AssetManager am = MyApplication.getContextObject().getResources().getAssets();
		InputStream in = null;
		try {
			in = am.open("db.properties");
            LogUtil.v("load", "...");
            load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	public static ReadProperties newInstance(){
		if(read==null){
			read=new ReadProperties();
		}
		return read;
	}
	public static String getDBName(){
		String dbName=newInstance().getProperty("dbname","ec.db");
		return dbName;
	}
	public static int getVersion(){
		String version=newInstance().getProperty("version","1");
		return Integer.parseInt(version);
	}
	public static String[] getCreateSql(){
		String sql=newInstance().getProperty("create_sql","");
		String[] creates=sql.split(";");
		return creates;
	}
	public static String[] getDropSql(){
		String sql=newInstance().getProperty("drop_sql","");
		String[] drops=sql.split(";");
		return drops;
	}
	public static String[] getInitSql(){
		String sql=newInstance().getProperty("init_sql","");
		String[] inits=sql.split(";");
		return inits;
	}
}
