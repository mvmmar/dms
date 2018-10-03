package com.mad.dms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mad.dms.admindb.AdminDBKeys;
import com.mad.dms.orders.Order;

public class DMSDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "dms.db";

    private final String SHOP_TABLE_NAME = "Shop";

    public DMSDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AdminDBKeys.CREATE_USER_TABLE);
        db.execSQL(AdminDBKeys.CREATE_ADMIN_TABLE);
        db.execSQL(AdminDBKeys.INSERT_ADMIN);
        db.execSQL("CREATE TABLE Product(P_Id INTEGER PRIMARY KEY AUTOINCREMENT,P_Name TEXT,P_Category TEXT,P_Description TEXT,P_Price REAL,P_Qty INTEGER);");
        db.execSQL("create table " + SHOP_TABLE_NAME + " (S_id INTEGER PRIMARY KEY AUTOINCREMENT,S_name TEXT,S_customer TEXT,S_address TEXT,S_contact TEXT)");
        db.execSQL(Order.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Order.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SHOP_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS Product");
        db.execSQL("DROP TABLE IF EXISTS " + AdminDBKeys.USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AdminDBKeys.ADMIN_TABLE);
        onCreate(db);
    }
}
