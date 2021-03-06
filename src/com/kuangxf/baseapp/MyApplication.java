package com.kuangxf.baseapp;

import java.io.File;

import org.xutils.DbManager;
import org.xutils.x;

import com.kuangxf.baseapp.utils.LogUtil;

import android.app.Application;

public class MyApplication extends Application {
	private static MyApplication instance;
	private static DbManager xdb = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.e("onCreate()");
		instance = this;
		initDB();
	}
	
	public static MyApplication getInstance() {
		LogUtil.d("getInstance()  instance=" + instance);
        return instance;
    }

    //初始化xutil3 数据库
    private DbManager initDB() {
    	LogUtil.e("initDB()");
        xdb = x.getDb(getDBConfig());
        return xdb;
    }

    public static DbManager db() {
        if (xdb == null) {
            xdb = x.getDb(getDBConfig());
        }
        return xdb;
    }

    private static DbManager.DaoConfig getDBConfig() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(AppConfig.DB_NAME)
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbDir(new File(AppConfig.getSDPath())) //
                .setDbVersion(AppConfig.Db_Version)
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager dbManager, int i, int i1) {
                        // TODO: ... 每次更新数据库的时候 增加版本号
//                        try {
//                            dbManager.addColumn(TransactionData.class,"test");
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
                })
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                });
        return daoConfig;
    }
}
