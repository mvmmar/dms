package com.mad.dms.admindb;

import com.mad.dms.adminclasses.AdminData;

public class AdminDBKeys {
    public static final String USER_TABLE = "User";
    public static final String USER_ID = "UserID";
    public static final String USER_NAME = "Name";
    public static final String USER_EMAIL = "Email";
    public static final String USER_PHONE = "Phone";
    public static final String USER_PASSWORD = "Password";

    //admin
    public static final String POSITION = "Position";
    public static final String ADMIN_TABLE = "Admin";
    public static final String ADMIN_ID = "AdminID";
    public static final String ADMIN_EMAIL = "Email";
    public static final String ADMIN_PASSWORD = "Password";

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + "(" +
                    USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    USER_NAME + " TEXT ," +
                    USER_EMAIL + " TEXT," +
                    USER_PASSWORD + " TEXT," +
                    USER_PHONE + " TEXT)";

    public static final String CREATE_ADMIN_TABLE =
            "CREATE TABLE " + ADMIN_TABLE + "(" +
                    ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    POSITION + " TEXT DEFAULT 'Admin'," +
                    ADMIN_EMAIL + " TEXT ," +
                    ADMIN_PASSWORD + " TEXT)";

    public static final String INSERT_ADMIN =
            "INSERT INTO " + ADMIN_TABLE + "(" + ADMIN_EMAIL + "," + ADMIN_PASSWORD + ")" +
                    " VALUES('" + AdminData.AdminEmail + "','" + AdminData.AdminPassword + "')";
}
