package com.mad.dms;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mad.dms.admin.AdminProfileSettings;
import com.mad.dms.admin.ManageSalesRep;
import com.mad.dms.admin.ViewSalesRep;
import com.mad.dms.orders.OrderMainActivity;
import com.mad.dms.product.ProductView;
import com.mad.dms.salesrep.SalesRepProfileSettings;
import com.mad.dms.shops.ShopViewActivity;
import com.mad.dms.signin.Login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Dashboard");

        if (!Login.getSessionAdmin()) {
            findViewById(R.id.main_view_rep).setVisibility(View.GONE);
        }

    }

    public void setOnClick(View view) {
        int id = view.getId();
        Class menu;
        switch (id) {
            case R.id.main_view_rep:
                menu = ViewSalesRep.class;
                break;
            case R.id.main_view_products:
                menu = ProductView.class;
                break;
            case R.id.main_view_shops:
                menu = ShopViewActivity.class;
                break;
            case R.id.main_view_orders:
                menu = OrderMainActivity.class;
                break;
            default:
                menu = ProductView.class;
        }
        Intent intent = new Intent(MainActivity.this, menu);
        startActivity(intent);
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
                Class profile = Login.getSessionAdmin() ? AdminProfileSettings.class : SalesRepProfileSettings.class;
                Intent i = new Intent(MainActivity.this, profile);
                startActivity(i);
                this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //sign out method
    void signOut() {
        Intent i = new Intent(MainActivity.this, Login.class);
        finishAffinity();
        startActivity(i);
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
}
