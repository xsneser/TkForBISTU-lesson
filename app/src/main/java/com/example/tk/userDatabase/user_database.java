package com.example.tk.userDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tk.dao.FriendInfo;
import com.example.tk.dao.MessageInfo;
import com.example.tk.dao.UserInfo;
import com.example.tk.activity.Plan;

import java.util.ArrayList;
import java.util.List;

public class user_database extends SQLiteOpenHelper {

    // 用户表定义
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";

    // 好友表定义
    public static final String TABLE_FRIEND = "friend";
    public static final String COLUMN_FRIEND_ID = "id";
    public static final String COLUMN_FRIEND_USERNAME = "username";
    public static final String COLUMN_FRIEND_FRIENDNAME = "friendname";

    // 消息表定义
    public static final String TABLE_MESSAGE = "message";
    public static final String COLUMN_MESSAGE_ID = "id";
    public static final String COLUMN_MESSAGE_SENDER = "sender";
    public static final String COLUMN_MESSAGE_RECEIVER = "receiver";
    public static final String COLUMN_MESSAGE_CONTENT = "content";
    public static final String COLUMN_MESSAGE_TIMESTAMP = "timestamp";

    // 计划表定义
    public static final String TABLE_PLAN = "plans";
    public static final String COLUMN_PLAN_ID = "id";
    public static final String COLUMN_PLAN_DATE = "date";
    public static final String COLUMN_PLAN_TIME = "time";
    public static final String COLUMN_PLAN_CONTENT = "content";

    // 创建用户表的SQL语句
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_USERNAME + " TEXT NOT NULL, " +
            COLUMN_USER_PASSWORD + " TEXT NOT NULL);";

    // 创建好友表的SQL语句
    private static final String CREATE_TABLE_FRIEND = "CREATE TABLE " + TABLE_FRIEND + " (" +
            COLUMN_FRIEND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FRIEND_USERNAME + " TEXT NOT NULL, " +
            COLUMN_FRIEND_FRIENDNAME + " TEXT NOT NULL);";

    // 创建消息表的SQL语句
    private static final String CREATE_TABLE_MESSAGE = "CREATE TABLE " + TABLE_MESSAGE + " (" +
            COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MESSAGE_SENDER + " TEXT NOT NULL, " +
            COLUMN_MESSAGE_RECEIVER + " TEXT NOT NULL, " +
            COLUMN_MESSAGE_CONTENT + " TEXT NOT NULL, " +
            COLUMN_MESSAGE_TIMESTAMP + " TEXT NOT NULL);";

    // 创建计划表的SQL语句
    private static final String CREATE_TABLE_PLAN = "CREATE TABLE " + TABLE_PLAN + " (" +
            COLUMN_PLAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PLAN_DATE + " TEXT NOT NULL, " +
            COLUMN_PLAN_TIME + " TEXT NOT NULL, " +
            COLUMN_PLAN_CONTENT + " TEXT NOT NULL);";

    // 构造函数
    public user_database(Context context) {
        super(context, "user_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_FRIEND);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_PLAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAN);
        onCreate(db);
    }

    // ===================== 好友表操作方法 =====================
    public void add_f(SQLiteDatabase sqLiteDatabase, String ui, String fn) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FRIEND_USERNAME, ui);
            values.put(COLUMN_FRIEND_FRIENDNAME, fn);
            db.insert(TABLE_FRIEND, null, values);
        } finally {
            db.close();
        }
    }

    public void updatePrimaryKey(SQLiteDatabase db, String oldId, String newId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, newId);
    }

    public void delete_f(SQLiteDatabase sqLiteDatabase, int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete(TABLE_FRIEND, "id=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }

    public void up_f(int id, String ui, String fn) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FRIEND_USERNAME, ui);
            values.put(COLUMN_FRIEND_FRIENDNAME, fn);
            db.update(TABLE_FRIEND, values, "id=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }

    /**
     * 查询所有好友
     */
    public List<FriendInfo> query_f(SQLiteDatabase sqLiteDatabase) {
        List<FriendInfo> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_FRIEND, null, null, null, null, null, "id ASC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
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
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    // ===================== 消息表操作方法 =====================
    public void insertMessage(String sender, String receiver, String content, String timestamp) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MESSAGE_SENDER, sender);
            values.put(COLUMN_MESSAGE_RECEIVER, receiver);
            values.put(COLUMN_MESSAGE_CONTENT, content);
            values.put(COLUMN_MESSAGE_TIMESTAMP, timestamp);
            db.insert(TABLE_MESSAGE, null, values);
        } finally {
            db.close();
        }
    }

    public List<MessageInfo> queryMessages(String sender, String receiver) {
        List<MessageInfo> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_MESSAGE, null,
                    COLUMN_MESSAGE_SENDER + "=? AND " + COLUMN_MESSAGE_RECEIVER + "=? OR " +
                            COLUMN_MESSAGE_SENDER + "=? AND " + COLUMN_MESSAGE_RECEIVER + "=?",
                    new String[]{sender, receiver, receiver, sender}, null, null, COLUMN_MESSAGE_TIMESTAMP + " ASC");

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_MESSAGE_ID);
                int senderIndex = cursor.getColumnIndex(COLUMN_MESSAGE_SENDER);
                int receiverIndex = cursor.getColumnIndex(COLUMN_MESSAGE_RECEIVER);
                int contentIndex = cursor.getColumnIndex(COLUMN_MESSAGE_CONTENT);
                int timestampIndex = cursor.getColumnIndex(COLUMN_MESSAGE_TIMESTAMP);

                do {
                    int id = cursor.getInt(idIndex);
                    String messageSender = cursor.getString(senderIndex);
                    String messageReceiver = cursor.getString(receiverIndex);
                    String messageContent = cursor.getString(contentIndex);
                    String messageTimestamp = cursor.getString(timestampIndex);
                    list.add(new MessageInfo(id, messageSender, messageReceiver, messageContent, messageTimestamp));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    // ===================== 用户表操作方法 =====================
    /**
     * 添加用户
     */
    public void adddata(SQLiteDatabase sqLiteDatabase, String ID, String username, String paswd) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, ID);
            values.put(COLUMN_USER_USERNAME, username);
            values.put(COLUMN_USER_PASSWORD, paswd);
            db.insert(TABLE_USER, null, values);
        } finally {
            db.close();
        }
    }

    /**
     * 删除用户
     */
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

            if (cursor != null && cursor.moveToFirst()) {
                do {
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
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    // ===================== 计划表操作方法 =====================
    /**
     * 添加计划
     */
// user_database.java
    public void addPlan(SQLiteDatabase sqLiteDatabase, String date, String time, String content) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAN_DATE, date);
            values.put(COLUMN_PLAN_TIME, time);
            values.put(COLUMN_PLAN_CONTENT, content);
            db.insert(TABLE_PLAN, null, values);
        } finally {
            db.close();
        }
    }
    /**
     * 删除计划
     */
    public void deletePlan(int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete(TABLE_PLAN, "id=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }

    /**
     * 查询指定日期的计划
     */
    // user_database.java
    // user_database.java
    /**
     * 查询指定日期的计划
     */
    public List<Plan> queryPlansByDate(String date) {
        List<Plan> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_PLAN, null, COLUMN_PLAN_DATE + "=?", new String[]{date}, null, null, "id ASC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(COLUMN_PLAN_ID);
                    int dateIndex = cursor.getColumnIndex(COLUMN_PLAN_DATE);
                    int timeIndex = cursor.getColumnIndex(COLUMN_PLAN_TIME);
                    int contentIndex = cursor.getColumnIndex(COLUMN_PLAN_CONTENT);

                    if (idIndex != -1 && dateIndex != -1 && timeIndex != -1 && contentIndex != -1) {
                        int id = cursor.getInt(idIndex);
                        String planDate = cursor.getString(dateIndex);
                        String time = cursor.getString(timeIndex);
                        String content = cursor.getString(contentIndex);
                        list.add(new Plan(id, planDate, time, content));
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

}