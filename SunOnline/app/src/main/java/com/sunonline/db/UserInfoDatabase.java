package com.sunonline.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 存放用户信息的表，数据库中的信息和UserInfo中的信息一致
 * Created by duanjigui on 2016/7/25.
 */
public class UserInfoDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="userinfo.db";
    public static final int VERSION=1;
    public static final String TABLE_NAME="user";
    public UserInfoDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       String sql="create table "+TABLE_NAME+"(user_id int primary key ," +
               "usernickName varchar(20)," +
               "userMobile varchar(20)," +
               "userEmail varchar(20)," +
               "userAvatar varchar(20)," +
               "islogin boolean default false)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if "+TABLE_NAME+"exists";
        db.execSQL(sql);
        this.onCreate(db);
    }

}
