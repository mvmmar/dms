package com.mad.dms.orders;

import com.mad.dms.product.Product;

public class OrderProducts extends Product {
    private boolean selected;

    public OrderProducts(String string, String string1, String string2, double aDouble, int anInt) {
        super(string, string1, string2, aDouble, anInt);
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
