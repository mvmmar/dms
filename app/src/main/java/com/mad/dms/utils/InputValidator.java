package com.mad.dms.utils;

import android.text.TextUtils;

import java.util.Date;

public class InputValidator {

    public static final String VALID = "VALID";

    public static String validateNewOrder(String nameStr, String dateStr) {
        String errorText = "Error!";
        try {
            if (!TextUtils.isEmpty(nameStr)) {
                if (nameStr.length() > 3) {
                    if (!TextUtils.isEmpty(dateStr)) {
                        Date deliveryDate = FmtHelper.parseShortDate(dateStr);
                        if (!deliveryDate.before(new Date())) {
                            return "VALID";
                        } else {
                            errorText = "Date has already passed!";
                        }
                    } else {
                        errorText = "Please enter a date!";
                    }
                } else {
                    errorText = "Name is too short! Must be longer than 3 characters";
                }
            } else {
                errorText = "Please enter a name!";
            }
        } catch (Exception e) {
            errorText = "Error: " + e;
            e.printStackTrace();
        }
        return errorText;
    }
}
