package com.mad.dms.orders;

import com.mad.dms.utils.FmtHelper;

import java.util.Date;

public class Order {
    private int id;
    private String name;
    private int status;
    private Date date;

    public static final int ORDER_STATUS_DENIED = -1;
    public static final int ORDER_STATUS_PENDING = 0;
    public static final int ORDER_STATUS_CONFIRMED = 1;

    public static final String TABLE_NAME = "orders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DATE = "date";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_STATUS + " INTEGER DEFAULT 0,"
                + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    public Order() {}

    public Order(String orderName) {
        this.id = (int) (Math.random() * 100);
        this.name = orderName;
        this.status = ORDER_STATUS_PENDING;
        this.date = null;
    }

    public Order(String name, int status) {
        this.id = (int) (Math.random() * 100);
        this.name = name;
        this.status = status;
        this.date = null;
    }

    public Order(String name, int status, Date date) {
        this.id = (int) (Math.random() * 100);
        this.name = name;
        this.status = status;
        this.date = date;
    }

    public Order(int id, String name, int status, String date) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.date = FmtHelper.parseSQLDate(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date) {
        this.date = FmtHelper.parseSQLDate(date);
    }
}
