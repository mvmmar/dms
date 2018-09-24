package com.mad.dms.orders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderDBHelper extends SQLiteOpenHelper {

    // DB version
    private static final int DATABASE_VERSION = 1;

    // DB name
    private static final String DATABASE_NAME = "orders";

    OrderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Order.CREATE_TABLE);
    }

    // Upgrading DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table
        db.execSQL("DROP TABLE IF EXISTS " + Order.TABLE_NAME);

        // Recreate tables
        onCreate(db);
    }

    // Insert Note
    public int insertOrder(String name) {
        // get DB
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Order.COLUMN_NAME, name);

        int id = (int) db.insert(Order.TABLE_NAME, null, values);

        db.close();
        return id;
    }

    private int updateStatus(long id, int status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Order.COLUMN_STATUS, status);

        String selection = Order.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        return db.update(Order.TABLE_NAME, values, selection, selectionArgs);
    }

    public int acceptOrder(long id) {
        return updateStatus(id, Order.ORDER_STATUS_CONFIRMED);
    }

    public int denyOrder(long id) {
        return updateStatus(id, Order.ORDER_STATUS_DENIED);
    }

    public Order getOrder(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                Order.COLUMN_ID,
                Order.COLUMN_NAME,
                Order.COLUMN_STATUS,
                Order.COLUMN_DATE,
        };
        String selection = Order.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
                Order.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Order order = new Order();

        if (cursor != null) {
            cursor.moveToFirst();
            order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Order.COLUMN_ID)));
            order.setName(cursor.getString(cursor.getColumnIndexOrThrow(Order.COLUMN_NAME)));
            order.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(Order.COLUMN_STATUS)));
            order.setDate(cursor.getString(cursor.getColumnIndexOrThrow(Order.COLUMN_DATE)));
            cursor.close();
        }

        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                Order.COLUMN_ID,
                Order.COLUMN_NAME,
                Order.COLUMN_STATUS,
                Order.COLUMN_DATE,
        };
        String sortOrder = Order.COLUMN_DATE + " DESC";

        Cursor cursor = db.query(
                Order.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()) {
            Order order = new Order();
            order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Order.COLUMN_ID)));
            order.setName(cursor.getString(cursor.getColumnIndexOrThrow(Order.COLUMN_NAME)));
            order.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(Order.COLUMN_STATUS)));
            order.setDate(cursor.getString(cursor.getColumnIndexOrThrow(Order.COLUMN_DATE)));
            orders.add(order);
        }

        cursor.close();
        return orders;
    }

    public void deleteOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        String where = Order.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(order.getId()) };
        db.delete(Order.TABLE_NAME, where, whereArgs);
    }
}
