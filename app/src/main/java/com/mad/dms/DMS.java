package com.mad.dms;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class DMS extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
