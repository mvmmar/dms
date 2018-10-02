package com.mad.dms.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mad.dms.admindb.UserDBHelper;
import com.mad.dms.R;
import com.mad.dms.adminclasses.User;

import java.util.ArrayList;

public class ViewSalesRep extends AppCompatActivity {
    private ListView listView;
    public static ArrayList<String> keysList;
    public ArrayList<User> authList;
    public static ArrayAdapter<String> adapter;
    public static ArrayList<String> arrayList;
    public static ArrayList<User> userList;
    private String userID;
    private UserDBHelper udb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sales_rep);
        this.setTitle("Sales Rep List");

        udb = new UserDBHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewSalesRep.this, AddSalesRep.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
            }
        });

        userList = udb.GetUsers();
        arrayList = new ArrayList<>();

        listView = findViewById(R.id.viewSRList);
        adapter = new ArrayAdapter<String>(ViewSalesRep.this, R.layout.sr_info, R.id.listSRDetails, arrayList);
        listView.setAdapter(adapter);

        for (int i = 0; i < userList.size(); i++) {
            arrayList.add("Name : " + userList.get(i).getName() + "\nEmail : " + userList.get(i).getEmail() + "\nPhone No: " + userList.get(i).getPhoneNo());
        }

        adapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ManageSalesRep.setPosition(position);
                Intent i = new Intent(ViewSalesRep.this, ManageSalesRep.class);
                i.putExtra("passEmail", userList.get(position).getEmail());
                i.putExtra("passPassword", userList.get(position).getPassword());
                i.putExtra("passName", userList.get(position).getName());
                i.putExtra("passPhone", userList.get(position).getPhoneNo());
                startActivity(i);

                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ViewSalesRep.this, AdminHome.class);
        startActivity(i);
        this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);

    }


}
