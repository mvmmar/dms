package com.mad.dms.orders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mad.dms.database.DMSDatabase;
import com.mad.dms.product.Product;
import com.mad.dms.signin.Login;
import com.mad.dms.utils.FmtHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class OrderDBHelper extends DMSDatabase {

//    // DB version
//    private static final int DATABASE_VERSION = 6;
//
//    // DB name
//    private static final String DATABASE_NAME = "dms.orders.db";

    OrderDBHelper(Context context) {
        super(context);
    }

    // Creating table
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(Order.CREATE_TABLE);
//    }

    // Upgrading DB
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table
//        db.execSQL("DROP TABLE IF EXISTS " + Order.TABLE_NAME);
//
//        // Recreate tables
//        onCreate(db);
//    }

    // Insert Note
    public int insertOrder(String name, Date date) {
        // get DB
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Order.COLUMN_NAME, name);
        values.put(Order.COLUMN_ACCEPTED, FmtHelper.toSQLDate(date));

        if (Login.userId > -1) {
            values.put(Order.COLUMN_USER, Login.userId);
        }

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

    public int updateOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Order.COLUMN_STATUS, order.getStatus());
        values.put(Order.COLUMN_NAME, order.getName());
        values.put(Order.COLUMN_ACCEPTED, order.getSQLAcceptedDate());
        values.put(Order.COLUMN_SHOP, order.getShop_id());

        String selection = Order.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(order.getId()) };

        return db.update(Order.TABLE_NAME, values, selection, selectionArgs);
    }

    private Order getOrderFromCursor(Cursor cursor, Order order) {
        order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Order.COLUMN_ID)));
        order.setName(cursor.getString(cursor.getColumnIndexOrThrow(Order.COLUMN_NAME)));
        order.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(Order.COLUMN_STATUS)));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(Order.COLUMN_DATE));
        String delivery = cursor.getString(cursor.getColumnIndexOrThrow(Order.COLUMN_ACCEPTED));
        int repId = cursor.getInt(cursor.getColumnIndexOrThrow(Order.COLUMN_USER));

        order.setDate(date);
        order.setSQLAcceptedDate(delivery);
        order.setUserId(repId);
        return order;
    }

    private Order getOrderFromCursor(Cursor cursor) {
        Order order = new Order();
        return getOrderFromCursor(cursor, order);
    }

    public Order getOrder(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = Order.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
                Order.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Order order = new Order();

        if (cursor != null) {
            cursor.moveToFirst();
            order = getOrderFromCursor(cursor, order);
            cursor.close();
        }

        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sortOrder = Order.COLUMN_DATE + " DESC";
        String selection = null;
        String[] selectionArgs = null;

        if (Login.userId > 0) {
            selection = Order.COLUMN_USER + " = ?";
            selectionArgs = new String[]{String.valueOf(Login.userId)};
        }

        Cursor cursor = db.query(
                Order.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()) {
            Order order = getOrderFromCursor(cursor);
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

    private int wipeProducts(SQLiteDatabase db, int order) {
        String where = Order.COLUMN_REL_ORDER + " = ?";
        String[] whereArgs = {String.valueOf(order)};
        return db.delete(Order.REL_TABLE, where, whereArgs);
    }

    public int insertProducts(int order, ArrayList<OrderProducts> productList) {
        // get DB
        int inserted = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println("INSERTING PRODUCTS: " + productList.size());
        try {
            wipeProducts(db, order);
            for (OrderProducts p : productList) {
                ContentValues values = new ContentValues();
                values.put(Order.COLUMN_REL_ORDER, order);
                values.put(Order.COLUMN_REL_PROD, p.getProductId());
                inserted = (int) db.insert(Order.REL_TABLE, null, values);
                if (inserted > 0) {
                    System.out.print("INSERTED ORDER: " + order + " PROD: " + p.getProductId());
                } else {
                    System.out.print("COULD NOT INSERT ORDER: " + order + " PROD: " + p.getProductId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            inserted = -1;
        }


        db.close();
        return inserted;
    }

    public ArrayList<OrderProducts> getAllOrderProducts() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("Product", null, null, null, null, null, null);

        ArrayList<OrderProducts> products = new ArrayList<>();
        while (cursor.moveToNext()) {
            OrderProducts product = new OrderProducts(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getInt(5));
            product.setProductId(cursor.getInt(0));
            product.setSelected(false);
            products.add(product);
        }

        cursor.close();
        return products;
    }

    public ArrayList<Product> getOrderProducts(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        final String MY_QUERY = "SELECT * FROM order_products o, Product p WHERE o.o_id=? AND o.p_id=p.P_Id";
        Cursor cursor = db.rawQuery(MY_QUERY, new String[]{String.valueOf(id)});

        ArrayList<Product> products = new ArrayList<>();

        while (cursor.moveToNext()) {
            Product product = new Product();
            product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("P_Id")));
            product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("P_Name")));
            product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("P_Price")));
            product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("P_Category")));
            products.add(product);
        }

        cursor.close();
        return products;
    }
}
