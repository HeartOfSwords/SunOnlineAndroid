package com.sunonline.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sunonline.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanjigui on 2016/7/25.
 */
public class DButil {

    private Context context;

    private UserInfoDatabase userInfoDatabase;

    public DButil(Context context) {
        this.context = context;
        userInfoDatabase=new UserInfoDatabase(context);
    }

    /**
     * 插入信息
     * @param userInfo
     * @param is_login
     * @return
     */
    public boolean insertUserInfo(UserInfo userInfo,Boolean is_login){
        SQLiteDatabase sqLiteDatabase=userInfoDatabase.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("user_id",userInfo.getUser_id());
        values.put("userAvatar",userInfo.getUserAvatar());
        values.put("userEmail",userInfo.getUserEmail());
        values.put("userMobile",userInfo.getUserMobile());
        values.put("usernickName",userInfo.getUsernickName());
        values.put("islogin",is_login);
        Long count= sqLiteDatabase.insert(UserInfoDatabase.TABLE_NAME, null, values);
        sqLiteDatabase.close();
        if (count==1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 根据用户的手机号来查询用户的具体信息
     * 主要用于用户登陆时查询
     * @param user_phone
     * @return
     */
    public UserInfo fetchInfoByPhone(String user_phone){
        List<UserInfo> list=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=userInfoDatabase.getWritableDatabase();
        Cursor cursor= sqLiteDatabase.query(UserInfoDatabase.TABLE_NAME,
                new String[]{"user_id", "userAvatar", "userEmail", "userMobile", "usernickName"},
                "userMobile=?",
                new String[]{user_phone},
                null,
                null,
                null);
        for (cursor.moveToFirst();cursor.isLast();cursor.moveToNext()){
            UserInfo userInfo=new UserInfo();
            userInfo.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
            userInfo.setUserAvatar(cursor.getString(cursor.getColumnIndex("userAvatar")));
            userInfo.setUserMobile(cursor.getString(cursor.getColumnIndex("userMobile")));
            userInfo.setUserEmail(cursor.getString(cursor.getColumnIndex("userEmail")));
            userInfo.setUsernickName(cursor.getString(cursor.getColumnIndex("usernickName")));
            list.add(userInfo);
        }
        sqLiteDatabase.close();
        if (list.size()==1){
            return list.get(0); //只返回第一个查询到的结果
        }else {
            return null;
        }

    }

    /**
     * 再插入数据之前，先判断用户是否存在
     * @param userInfo
     * @return
     */
    public boolean userIsExists(UserInfo userInfo){
        SQLiteDatabase sqLiteDatabase=userInfoDatabase.getWritableDatabase();
        Cursor cursor= sqLiteDatabase.query(UserInfoDatabase.TABLE_NAME,
                new String[]{"user_id", "userAvatar", "userEmail", "userMobile", "usernickName"},
                "user_id=?",
                new String[]{String.valueOf(userInfo.getUser_id())},
                null,
                null,
                null);
        int count=cursor.getCount();
        sqLiteDatabase.close();
        if (count==1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改登陆的状态位
     * @param is_login 状态信息
     * @param userInfo
     * @return
     */
    public boolean updateLoginStatus(Boolean is_login,UserInfo userInfo){
        SQLiteDatabase sqLiteDatabase=userInfoDatabase.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("islogin",is_login);
        int row_count=sqLiteDatabase.update(UserInfoDatabase.TABLE_NAME, contentValues, "user_id=?", new String[]{String.valueOf(userInfo.getUser_id())});
        sqLiteDatabase.close();
        if (row_count==1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改全部信息
     * @param is_login
     * @param userInfo
     * @return
     */
    public boolean updateUserInfo(Boolean is_login,UserInfo userInfo){
        SQLiteDatabase sqLiteDatabase=userInfoDatabase.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("userAvatar",userInfo.getUserAvatar());
        contentValues.put("userEmail",userInfo.getUserEmail());
        contentValues.put("userMobile",userInfo.getUserMobile());
        contentValues.put("usernickName",userInfo.getUsernickName());
        contentValues.put("islogin",is_login);
        int row_count=sqLiteDatabase.update(UserInfoDatabase.TABLE_NAME,contentValues,"user_id=?",new String[]{String.valueOf(userInfo.getUser_id())});
        sqLiteDatabase.close();
        if (row_count==1){
            return true;
        }else {
            return false;
        }
    }

}
