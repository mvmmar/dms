package com.mad.dms.admindb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mad.dms.adminclasses.AdminData;
import com.mad.dms.adminclasses.User;
import com.mad.dms.database.DMSDatabase;

import java.util.ArrayList;

public class UserDBHelper extends DMSDatabase {
//    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_NAME = "dms.db";
//    private static final String USER_TABLE = "User";
//    private static final String USER_ID = "UserID";
//    private static final String USER_NAME = "Name";
//    private static final String USER_EMAIL = "Email";
//    private static final String USER_PHONE = "Phone";
//    private static final String USER_PASSWORD = "Password";
//
//    //admin
//    private static final String POSITION = "Position";
//    private static final String ADMIN_TABLE = "Admin";
//    private static final String ADMIN_ID = "AdminID";
//    private static final String ADMIN_EMAIL = "Email";
//    private static final String ADMIN_PASSWORD = "Password";

    public UserDBHelper(Context context) {
        super(context);
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_USER_TABLE =
//                "CREATE TABLE " + USER_TABLE + "(" +
//                        USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                        USER_NAME + " TEXT ," +
//                        USER_EMAIL + " TEXT," +
//                        USER_PASSWORD + " TEXT," +
//                        USER_PHONE + " TEXT)";
//
//        String CREATE_ADMIN_TABLE =
//                "CREATE TABLE " + ADMIN_TABLE + "(" +
//                        ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                        POSITION + " TEXT DEFAULT 'Admin'," +
//                        ADMIN_EMAIL + " TEXT ," +
//                        ADMIN_PASSWORD + " TEXT)";
//
//        String INSERT_ADMIN =
//                "INSERT INTO " + ADMIN_TABLE + "(" + ADMIN_EMAIL + "," + ADMIN_PASSWORD + ")" +
//                        " VALUES('" + AdminData.AdminEmail + "','" + AdminData.AdminPassword + "')";
//
//        db.execSQL(CREATE_USER_TABLE);
//        db.execSQL(CREATE_ADMIN_TABLE);
//        db.execSQL(INSERT_ADMIN);
//    }

//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String SQL_DELETE_USER_TABLE = "DROP TABLE IF EXISTS " + AdminDBKeys.USER_TABLE;
//        String SQL_DELETE_ADMIN_TABLE = "DROP TABLE IF EXISTS " + AdminDBKeys.ADMIN_TABLE;
//
//        db.execSQL(SQL_DELETE_USER_TABLE);
//        db.execSQL(SQL_DELETE_ADMIN_TABLE);
//        onCreate(db);
//    }

    public boolean addInfo(String name, String email, String phone, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AdminDBKeys.USER_NAME, name);
        values.put(AdminDBKeys.USER_EMAIL, email);
        values.put(AdminDBKeys.USER_PHONE, phone);
        values.put(AdminDBKeys.USER_PASSWORD, password);

        long i = db.insert(AdminDBKeys.USER_TABLE, null, values);

        return i != -1;
    }

    // Get User Details
    public ArrayList<User> GetUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM " + AdminDBKeys.USER_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            User user = new User();
            user.setId(cursor.getString(cursor.getColumnIndex(AdminDBKeys.USER_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(AdminDBKeys.USER_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(AdminDBKeys.USER_EMAIL)));
            user.setPhoneNo(cursor.getString(cursor.getColumnIndex(AdminDBKeys.USER_PHONE)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(AdminDBKeys.USER_PASSWORD)));
            userList.add(user);
        }
        cursor.close();
        return userList;
    }

    public String GetUserByEmail(String email) {
        String password = null;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT Password FROM " + AdminDBKeys.USER_TABLE + " WHERE Email ='" + email + "' ", null);
        while (cursor.moveToNext()) {
            password = cursor.getString(cursor.getColumnIndex(AdminDBKeys.USER_PASSWORD));
        }
        cursor.close();
        return password;
    }

    public boolean checkUserEmail(String email) {
        boolean UserEmail = true;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + AdminDBKeys.USER_TABLE + " WHERE Email ='" + email + "' ", null);
        if (cursor.moveToNext()) {
            UserEmail = false;
        }
        cursor.close();
        return UserEmail;
    }

    public boolean UpdateInfo(String id, String name, String email, String phone, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AdminDBKeys.USER_NAME, name);
        values.put(AdminDBKeys.USER_EMAIL, email);
        values.put(AdminDBKeys.USER_PHONE, phone);
        values.put(AdminDBKeys.USER_PASSWORD, password);

        db.update(AdminDBKeys.USER_TABLE, values, "UserID = ?", new String[]{id});
        return true;
    }

    public Integer DeleteInfo(String id) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = AdminDBKeys.USER_ID + " LIKE ? ";
        String[] selectionArg = {id};
        return db.delete(AdminDBKeys.USER_TABLE, selection, selectionArg);
    }

    public boolean UpdateAdmin(String id, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AdminDBKeys.ADMIN_EMAIL, email);
        values.put(AdminDBKeys.ADMIN_PASSWORD, password);

        db.update(AdminDBKeys.ADMIN_TABLE, values, "AdminID = ?", new String[]{id});
        return true;
    }

    public User getAdminData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + AdminDBKeys.ADMIN_TABLE, null);
        User user = new User();

        while (cursor.moveToNext()) {
            user.setPosition(cursor.getString(cursor.getColumnIndex(AdminDBKeys.POSITION)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(AdminDBKeys.USER_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(AdminDBKeys.USER_PASSWORD)));
        }
        return user;
    }

    public Cursor getSalesRepData(String email) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + AdminDBKeys.USER_TABLE + " WHERE Email ='" + email + "' ", null);
        return res;
    }

    public boolean UpdateSalesRep(String LoginEmail, String name, String email, String phone, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AdminDBKeys.USER_NAME, name);
        values.put(AdminDBKeys.USER_EMAIL, email);
        values.put(AdminDBKeys.USER_PHONE, phone);
        values.put(AdminDBKeys.USER_PASSWORD, password);

        db.update(AdminDBKeys.USER_TABLE, values, "Email = ?", new String[]{LoginEmail});
        return true;
    }
}
