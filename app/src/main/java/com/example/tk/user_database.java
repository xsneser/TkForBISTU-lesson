package com.example.tk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class user_database extends SQLiteOpenHelper {
    public user_database(Context context) {
        super(context, "user_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE user(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "paswd TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 添加数据库升级逻辑
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    public void adddata(SQLiteDatabase sqLiteDatabase, String username, String paswd) { // 修正参数名拼写
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("paswd", paswd);
            db.insert("user", null, values);
        } finally {
            db.close();
        }
    }

    public void delete(SQLiteDatabase sqLiteDatabase, int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete("user", "id=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }

    public void update(int id, String username, String paswd) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("paswd", paswd);
            db.update("user", values, "id=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }

    public List<UserInfo> querydata(SQLiteDatabase sqLiteDatabase) {
        List<UserInfo> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query("user", null, null, null, null, null, "id ASC");

            // 检查Cursor是否有效
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // 检查列索引是否有效
                    int idIndex = cursor.getColumnIndex("id");
                    int usernameIndex = cursor.getColumnIndex("username");
                    int paswdIndex = cursor.getColumnIndex("paswd");

                    if (idIndex != -1 && usernameIndex != -1 && paswdIndex != -1) {
                        int id = cursor.getInt(idIndex);
                        String username = cursor.getString(usernameIndex);
                        String paswd = cursor.getString(paswdIndex);
                        list.add(new UserInfo(id, username, paswd));
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 确保资源关闭
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }
}    // 定义UserInfo类
