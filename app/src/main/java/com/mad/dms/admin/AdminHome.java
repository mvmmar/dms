package com.mad.dms.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.mad.dms.login.Login;
import com.mad.dms.R;
import com.mad.dms.salesRep.AddSalesRep;
import com.mad.dms.salesRep.SalesRepProfileSettings;
import com.mad.dms.salesRep.ViewSalesRep;

public class AdminHome extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout salesRep;
    private LinearLayout product;
    private LinearLayout shop;
    private LinearLayout order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        this.setTitle("Dashboard");
        salesRep =findViewById(R.id.ADSR);
        salesRep.setOnClickListener(this);

        product =findViewById(R.id.ADProduct);
        product.setOnClickListener(this);

        shop =findViewById(R.id.ADShop);
        shop.setOnClickListener(this);

        order =findViewById(R.id.ADOrder);
        order.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu:
                signOut();
                break;
            case R.id.profSetting:
                Intent i = new Intent(AdminHome.this, AdminProfileSettings.class);
                startActivity(i);
                this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //sign out method
    void signOut() {
        Intent i = new Intent(AdminHome.this, Login.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ADSR:
                Intent i1 = new Intent(AdminHome.this, ViewSalesRep.class);
                startActivity(i1);
                this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                break;
            case R.id.ADProduct:
                Intent i2 = new Intent(AdminHome.this, ViewSalesRep.class);
                startActivity(i2);
                this.overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                break;
            case R.id.ADShop:
                Intent i3 = new Intent(AdminHome.this, SalesRepProfileSettings.class);
                startActivity(i3);
                this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                break;
            case R.id.ADOrder:
                Intent i4 = new Intent(AdminHome.this, AddSalesRep.class);
                startActivity(i4);
                this.overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                break;
        }
    }
}
