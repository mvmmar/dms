package com.mad.dms.product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "DMS.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="CREATE TABLE Product(P_Id INTEGER PRIMARY KEY AUTOINCREMENT,P_Name TEXT,P_Category TEXT,P_Description TEXT,P_Price REAL,P_Qty INTEGER);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query="DROP TABLE IF EXISTS Product";
        onCreate(sqLiteDatabase);

    }

    public boolean insertProduct(Product product){
        ContentValues contentValues = new ContentValues();
        contentValues.put("P_Name",product.getProductName());
        contentValues.put("P_Category",product.getCategory());
        contentValues.put("P_Description",product.getDescription());
        contentValues.put("P_Price",product.getPrice());
        contentValues.put("P_Qty",product.getQuantity());

        SQLiteDatabase db= getWritableDatabase();
        long result=db.insert("Product",null,contentValues);

        return result != -1;
    }

    public boolean deleteProduct(int id){
        SQLiteDatabase db= getReadableDatabase();

        int result=db.delete("Product","P_Id ="+id,null);
        return result != -1;
    }

    public boolean updateProduct(Product product){
        ContentValues contentValues = new ContentValues();
        contentValues.put("P_Name",product.getProductName());
        contentValues.put("P_Category",product.getCategory());
        contentValues.put("P_Description",product.getDescription());
        contentValues.put("P_Price",product.getPrice());
        contentValues.put("P_Qty",product.getQuantity());

        SQLiteDatabase db= getReadableDatabase();

        int result=db.update("Product",contentValues,"P_Id ="+product.getProductId(),null);

        return result != -1;
    }

    public Cursor displayProducts(){
        SQLiteDatabase db=getWritableDatabase();

        String query="SELECT * FROM Product";
        Cursor cursor = db.rawQuery(query,null);

        return cursor;

    }
}
