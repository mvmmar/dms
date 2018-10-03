package com.mad.dms.shops;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mad.dms.database.DMSDatabase;

import java.util.ArrayList;

public class ShopDB extends DMSDatabase {

    public static final String DATABASE_NAME = "DMS.db";
    public static final String TABLE_NAME = "Shop";
    public static final String COL_1 = "S_id";
    public static final String COL_2 = "S_name";
    public static final String COL_3 = "S_customer";
    public static final String COL_4 = "S_address";
    public static final String COL_5 = "S_contact";

    public ShopDB(Context context) {
        super(context);
    }

//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (S_id INTEGER PRIMARY KEY AUTOINCREMENT,S_name TEXT,S_customer TEXT,S_address TEXT,S_contact TEXT)");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
//        onCreate(sqLiteDatabase);
//    }

    public boolean insertShop(Shop shop) {
        ContentValues shopcontentValues = new ContentValues();
        shopcontentValues.put(COL_2, shop.getShopname());
        shopcontentValues.put(COL_3, shop.getShopcustomer());
        shopcontentValues.put(COL_4, shop.getShopaddress());
        shopcontentValues.put(COL_5, shop.getShopcontact());

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long result = sqLiteDatabase.insert(TABLE_NAME, null, shopcontentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateShop(Shop shop) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, shop.getShopname());
        contentValues.put(COL_3, shop.getShopcustomer());
        contentValues.put(COL_4, shop.getShopaddress());
        contentValues.put(COL_5, shop.getShopcontact());

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        int result = sqLiteDatabase.update(TABLE_NAME, contentValues, "S_id =" + shop.getShopid(), null);

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean deleteShop(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        int result = sqLiteDatabase.delete(TABLE_NAME, "S_id =" + id, null);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getshopdata() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);

        return res;
    }
}
