package com.example.tk.userDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tk.dao.FriendInfo;
import com.example.tk.dao.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class user_database extends SQLiteOpenHelper {

    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";

    public static final String TABLE_FRIEND = "friend";
    public static final String COLUMN_FRIEND_ID = "id";
    public static final String COLUMN_FRIEND_USERNAME = "username";
    public static final String COLUMN_FRIEND_FRIENDNAME = "friendname";

    public static final String TABLE_MESSAGE = "message";
    public static final String COLUMN_MESSAGE_ID = "id";
    public static final String COLUMN_MESSAGE_SENDER = "sender";
    public static final String COLUMN_MESSAGE_RECEIVER = "receiver";
    public static final String COLUMN_MESSAGE_CONTENT = "content";
    public static final String COLUMN_MESSAGE_TIMESTAMP = "timestamp";

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_USERNAME + " TEXT NOT NULL, " +
            COLUMN_USER_PASSWORD + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_FRIEND = "CREATE TABLE " + TABLE_FRIEND + " (" +
            COLUMN_FRIEND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FRIEND_USERNAME + " TEXT NOT NULL, " +
            COLUMN_FRIEND_FRIENDNAME + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_MESSAGE = "CREATE TABLE " + TABLE_MESSAGE + " (" +
            COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MESSAGE_SENDER + " TEXT NOT NULL, " +
            COLUMN_MESSAGE_RECEIVER + " TEXT NOT NULL, " +
            COLUMN_MESSAGE_CONTENT + " TEXT NOT NULL, " +
            COLUMN_MESSAGE_TIMESTAMP + " TEXT NOT NULL);";
    public user_database(Context context) {
        super(context, "user_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_FRIEND);
        db.execSQL(CREATE_TABLE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        onCreate(db);
    }
    UserInfo ui = new UserInfo();
    public void add_f(SQLiteDatabase sqLiteDatabase, String fn){
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(COLUMN_FRIEND_USERNAME, ui.getUsername());
            values.put(COLUMN_FRIEND_FRIENDNAME, fn);
            db.insert(TABLE_FRIEND, null, values);
        } finally {
            db.close();
        }

    }
    public void delete_f(SQLiteDatabase sqLiteDatabase, int id){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.delete(TABLE_FRIEND, "id=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }
    public void up_f(int id, String un, String fn){
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(COLUMN_FRIEND_USERNAME, ui.getUsername());
            values.put(COLUMN_FRIEND_FRIENDNAME, fn);
            db.update(TABLE_FRIEND, values, "id=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }
    public List<FriendInfo> query_f(SQLiteDatabase sqLiteDatabase) {
        List<FriendInfo> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_FRIEND, null, null, null, null, null, "id ASC");

            // 检查Cursor是否有效
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // 检查列索引是否有效
                    int idIndex = cursor.getColumnIndex(COLUMN_FRIEND_ID);
                    int usernameIndex = cursor.getColumnIndex(COLUMN_FRIEND_USERNAME);
                    int friendIndex = cursor.getColumnIndex(COLUMN_FRIEND_FRIENDNAME);

                    if (idIndex != -1 && usernameIndex != -1 && friendIndex != -1) {
                        int id = cursor.getInt(idIndex);
                        String username = cursor.getString(usernameIndex);
                        String friendname = cursor.getString(friendIndex);
                        list.add(new FriendInfo(id, username, friendname));
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
    public void adddata(SQLiteDatabase sqLiteDatabase, String username, String paswd) { // 修正参数名拼写
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_USERNAME, username);
            values.put(COLUMN_USER_PASSWORD, paswd);
            db.insert(TABLE_USER, null, values);
        } finally {
            db.close();
        }
    }

    public void delete(SQLiteDatabase sqLiteDatabase, int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete(TABLE_USER, "id=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }

    public void update(int id, String username, String paswd) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_USERNAME, username);
            values.put(COLUMN_USER_PASSWORD, paswd);
            db.update(TABLE_USER, values, "id=?", new String[]{String.valueOf(id)});
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
                    int idIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                    int usernameIndex = cursor.getColumnIndex(COLUMN_USER_USERNAME);
                    int paswdIndex = cursor.getColumnIndex(COLUMN_USER_PASSWORD);

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
