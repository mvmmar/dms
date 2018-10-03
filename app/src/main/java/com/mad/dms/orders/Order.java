package com.mad.dms.orders;

import com.mad.dms.adminclasses.User;
import com.mad.dms.admindb.UserDBHelper;
import com.mad.dms.utils.FmtHelper;

import java.util.Date;

public class Order {
    private int id;
    private String name;
    private int status;
    private Date date;
    private Date accepted_date;
    private int userId;
    //    private User salesRep;
    private int shop_id;

    public static final int ORDER_STATUS_DENIED = -1;
    public static final int ORDER_STATUS_PENDING = 0;
    public static final int ORDER_STATUS_CONFIRMED = 1;

    public static final String TABLE_NAME = "orders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DATE = "created_date";
    public static final String COLUMN_ACCEPTED = "delivery_date";
    public static final String COLUMN_SHOP = "shop";
    public static final String COLUMN_USER = "sales_rep";

    public static final String REL_TABLE = "order_products";
    public static final String COLUMN_REL_ORDER = "o_id";
    public static final String COLUMN_REL_PROD = "p_id";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_STATUS + " INTEGER DEFAULT 0, "
                    + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + COLUMN_ACCEPTED + " DATE, "
                    + COLUMN_SHOP + " INTEGER, "
                    + COLUMN_USER + " INTEGER DEFAULT NULL, "
                    + "FOREIGN KEY(" + COLUMN_USER + ") REFERENCES Product(P_Id) "
                    + ")";

    public static final String CREATE_REL_TABLE =
            "CREATE TABLE " + REL_TABLE + " ("
                    + COLUMN_REL_ORDER + " INTEGER, "
                    + COLUMN_REL_PROD + " INTEGER, "
                    + " FOREIGN KEY(" + COLUMN_REL_ORDER + ") REFERENCES orders(id),"
                    + "FOREIGN KEY(" + COLUMN_REL_PROD + ") REFERENCES Product(P_Id), "
                    + "PRIMARY KEY(o_id, p_id) "
                    + ")";

    public Order() {}

    public Order(String orderName) {
        this.id = (int) (Math.random() * 100);
        this.name = orderName;
        this.status = ORDER_STATUS_PENDING;
        this.date = null;
        this.accepted_date = null;
        this.shop_id = -1;
        this.userId = -1;
//        this.salesRep = new User("Admin", "Admin", "dms@gmail.com", "", "0123456789");
    }

    public Order(String name, int status) {
        this.id = (int) (Math.random() * 100);
        this.name = name;
        this.status = status;
        this.date = null;
        this.accepted_date = null;
        this.shop_id = -1;
        this.userId = -1;
//        this.salesRep = new User("Admin", "Admin", "dms@gmail.com", "", "0123456789");
    }

    public Order(String name, int status, Date date) {
        this.id = (int) (Math.random() * 100);
        this.name = name;
        this.status = status;
        this.date = date;
        this.accepted_date = null;
        this.shop_id = -1;
        this.userId = -1;
//        this.salesRep = new User("Admin", "Admin", "dms@gmail.com", "", "0123456789");
    }

    public Order(int id, String name, int status, String date) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.date = FmtHelper.parseSQLDate(date);
        this.accepted_date = null;
        this.shop_id = -1;
        this.userId = -1;
//        this.salesRep = new User("Admin", "Admin", "dms@gmail.com", "", "0123456789");
    }

    // Order ID
    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }

    // Order name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Order status
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        switch (this.status) {
            case Order.ORDER_STATUS_CONFIRMED:
                return "Confirmed";
            case Order.ORDER_STATUS_PENDING:
                return "Pending";
            default:
                return "Denied";
        }
    }

    // Created Date
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setDate(String date) {
        this.date = FmtHelper.parseSQLDate(date);
    }

    // Accepted Date
    public Date getAcceptedDate() { return accepted_date; }
    public String getFmtAcceptedDate() { return FmtHelper.formatShortDate(accepted_date); }
    public void setAcceptedDate(Date accepted_date) { this.accepted_date = accepted_date; }
    public void setAcceptedDate(String accepted_date) { this.accepted_date = FmtHelper.parseShortDate(accepted_date); }
    public void setSQLAcceptedDate(String accepted_date) { this.accepted_date = FmtHelper.parseSQLDate(accepted_date); }
    public String getSQLAcceptedDate() { return FmtHelper.toSQLDate(accepted_date); }

    // Shop ID
    public int getShop_id() { return shop_id; }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    // User ID
    public void setUserId(int id) {
        this.userId = id;
    }

    public int getUserId() {
        return userId;
    }

//    public void setSalesRep(User user) { this.salesRep = user; }
//    public User getSalesRep() { return this.salesRep; }

    public String toString() {
        return "ID: " + this.id + ", "
                + "Name: " + this.name + ", "
                + "Date: " + this.getDate().toString()
                + "Accepted: " + this.getAcceptedDate().toString()
                + "Status: " + this.getStatus();
    }
}
