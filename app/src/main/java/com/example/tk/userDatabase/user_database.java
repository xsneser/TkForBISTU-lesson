package com.example.tk.userDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tk.dao.FriendInfo;
import com.example.tk.dao.TaskInfo;
import com.example.tk.dao.UserInfo;

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

    // 任务表定义（新增）
    public static final String TABLE_TASK = "task";
    public static final String COLUMN_TASK_DATE = "date";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_TASK_START_TIME = "start_time";
    public static final String COLUMN_TASK_END_TIME = "end_time";
    public static final String COLUMN_TASK_NAME = "task_name";
    public static final String COLUMN_TASK_CREATOR = "creator";
    public static final String COLUMN_TASK_CREATOR_ID = "creator_id";

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

    // 创建任务表的SQL语句（新增）
    private static final String CREATE_TABLE_TASK = "CREATE TABLE " + TABLE_TASK + " (" +
            COLUMN_TASK_DATE + " TEXT NOT NULL, " +
            COLUMN_TASK_ID + " TEXT NOT NULL UNIQUE, " +
            COLUMN_TASK_START_TIME + " TEXT NOT NULL, " +
            COLUMN_TASK_END_TIME + " TEXT NOT NULL, " +
            COLUMN_TASK_NAME + " TEXT NOT NULL, " +
            COLUMN_TASK_CREATOR + " TEXT NOT NULL, " +
            COLUMN_TASK_CREATOR_ID + " INTEGER NOT NULL);";

    // 构造函数
    public user_database(Context context) {
        super(context, "user_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_FRIEND);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_TASK); // 创建任务表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK); // 删除任务表
        onCreate(db);
    }

    // 用户信息实例（用于好友表操作）
    UserInfo ui = new UserInfo();

    // ===================== 好友表操作方法 =====================
    /**
     * 添加好友
     */
    public void add_f(SQLiteDatabase sqLiteDatabase, String fn) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FRIEND_USERNAME, ui.getUsername());
            values.put(COLUMN_FRIEND_FRIENDNAME, fn);
            db.insert(TABLE_FRIEND, null, values);
        } finally {
            db.close();
        }
    }

    /**
     * 删除好友
     */
    public void delete_f(SQLiteDatabase sqLiteDatabase, int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete(TABLE_FRIEND, "id=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }

    /**
     * 更新好友信息
     */
    public void up_f(int id, String un, String fn) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FRIEND_USERNAME, ui.getUsername());
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

    // ===================== 用户表操作方法 =====================
    /**
     * 添加用户
     */
    public void adddata(SQLiteDatabase sqLiteDatabase, String username, String paswd) {
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

    /**
     * 更新用户信息
     */
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

    /**
     * 查询所有用户
     */
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

    // ===================== 任务表操作方法（新增） =====================
    /**
     * 添加任务
     */
    public void addTask(SQLiteDatabase sqLiteDatabase, String date, String taskId,
                        String startTime, String endTime, String taskName,
                        String creator, int creatorId) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TASK_DATE, date);
            values.put(COLUMN_TASK_ID, taskId);
            values.put(COLUMN_TASK_START_TIME, startTime);
            values.put(COLUMN_TASK_END_TIME, endTime);
            values.put(COLUMN_TASK_NAME, taskName);
            values.put(COLUMN_TASK_CREATOR, creator);
            values.put(COLUMN_TASK_CREATOR_ID, creatorId);
            db.insert(TABLE_TASK, null, values);
        } finally {
            db.close();
        }
    }

    /**
     * 删除任务
     */
    public void deleteTask(SQLiteDatabase sqLiteDatabase, String taskId) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete(TABLE_TASK, COLUMN_TASK_ID + "=?", new String[]{taskId});
        } finally {
            db.close();
        }
    }

    /**
     * 更新任务信息
     */
    public void updateTask(String taskId, String date, String startTime,
                           String endTime, String taskName, String creator, int creatorId) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TASK_DATE, date);
            values.put(COLUMN_TASK_START_TIME, startTime);
            values.put(COLUMN_TASK_END_TIME, endTime);
            values.put(COLUMN_TASK_NAME, taskName);
            values.put(COLUMN_TASK_CREATOR, creator);
            values.put(COLUMN_TASK_CREATOR_ID, creatorId);
            db.update(TABLE_TASK, values, COLUMN_TASK_ID + "=?", new String[]{taskId});
        } finally {
            db.close();
        }
    }

    /**
     * 查询所有任务
     */
    public List<TaskInfo> queryAllTasks(SQLiteDatabase sqLiteDatabase) {
        List<TaskInfo> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_TASK, null, null, null, null, null, COLUMN_TASK_DATE + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int dateIndex = cursor.getColumnIndex(COLUMN_TASK_DATE);
                    int taskIdIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
                    int startTimeIndex = cursor.getColumnIndex(COLUMN_TASK_START_TIME);
                    int endTimeIndex = cursor.getColumnIndex(COLUMN_TASK_END_TIME);
                    int taskNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                    int creatorIndex = cursor.getColumnIndex(COLUMN_TASK_CREATOR);
                    int creatorIdIndex = cursor.getColumnIndex(COLUMN_TASK_CREATOR_ID);

                    if (dateIndex != -1 && taskIdIndex != -1 && startTimeIndex != -1 &&
                            endTimeIndex != -1 && taskNameIndex != -1 && creatorIndex != -1 &&
                            creatorIdIndex != -1) {
                        String date = cursor.getString(dateIndex);
                        String taskId = cursor.getString(taskIdIndex);
                        String startTime = cursor.getString(startTimeIndex);
                        String endTime = cursor.getString(endTimeIndex);
                        String taskName = cursor.getString(taskNameIndex);
                        String creator = cursor.getString(creatorIndex);
                        int creatorId = cursor.getInt(creatorIdIndex);
                        list.add(new TaskInfo(date, taskId, startTime, endTime, taskName, creator, creatorId));
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

    /**
     * 根据创建人ID查询任务
     */
    public List<TaskInfo> queryTasksByCreatorId(SQLiteDatabase sqLiteDatabase, int creatorId) {
        List<TaskInfo> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(TABLE_TASK, null,
                    COLUMN_TASK_CREATOR_ID + "=?",
                    new String[]{String.valueOf(creatorId)},
                    null, null, COLUMN_TASK_DATE + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int dateIndex = cursor.getColumnIndex(COLUMN_TASK_DATE);
                    int taskIdIndex = cursor.getColumnIndex(COLUMN_TASK_ID);
                    int startTimeIndex = cursor.getColumnIndex(COLUMN_TASK_START_TIME);
                    int endTimeIndex = cursor.getColumnIndex(COLUMN_TASK_END_TIME);
                    int taskNameIndex = cursor.getColumnIndex(COLUMN_TASK_NAME);
                    int creatorIndex = cursor.getColumnIndex(COLUMN_TASK_CREATOR);
                    int creatorIdIndex = cursor.getColumnIndex(COLUMN_TASK_CREATOR_ID);

                    if (dateIndex != -1 && taskIdIndex != -1 && startTimeIndex != -1 &&
                            endTimeIndex != -1 && taskNameIndex != -1 && creatorIndex != -1 &&
                            creatorIdIndex != -1) {
                        String date = cursor.getString(dateIndex);
                        String taskId = cursor.getString(taskIdIndex);
                        String startTime = cursor.getString(startTimeIndex);
                        String endTime = cursor.getString(endTimeIndex);
                        String taskName = cursor.getString(taskNameIndex);
                        String creator = cursor.getString(creatorIndex);
                        list.add(new TaskInfo(date, taskId, startTime, endTime, taskName, creator, creatorId));
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