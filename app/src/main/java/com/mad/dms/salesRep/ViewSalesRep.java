package com.mad.dms.salesRep;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.dms.R;

import java.util.ArrayList;

public class ViewSalesRep extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> keysList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_rep);
        this.setTitle("Sales Rep List");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewSalesRep.this, AddSalesRep.class);
                startActivity(i);
            }
        });

        listView = findViewById(R.id.viewSRList);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        adapter = new ArrayAdapter<String>(ViewSalesRep.this, R.layout.sr_info, R.id.listSRDetails, arrayList);
        listView.setAdapter(adapter);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                userID = dataSnapshot.getKey();
                keysList.add(userID);
                arrayList.add("Full Name : " + user.getFullname() + "\nEmail : " + user.getEmail() + "\nPhoneNo : " + user.getPhoneNo());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //User string = dataSnapshot.getValue(String.class);
                //arrayList.remove(string);
                keysList.remove(dataSnapshot.getKey());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSalesRep.this);
                builder.setMessage("Are you sure you want to delete Sales rep?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // deleteSalesRep(userID);
                                String key = keysList.get(position);
                                mDatabase.child(key).removeValue();

                                String item = adapter.getItem(position);
                                adapter.remove(item);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
    }

    private void deleteSalesRep(String userId) {
        DatabaseReference deleteUser = mDatabase.child(userId);
        deleteUser.removeValue();
    }

}
