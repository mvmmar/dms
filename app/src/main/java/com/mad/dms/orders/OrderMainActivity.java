package com.mad.dms.orders;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.dms.R;
import com.mad.dms.signin.Login;
import com.mad.dms.utils.FmtHelper;
import com.mad.dms.utils.InputValidator;
import com.mad.dms.utils.RecyclerTouchListener;
import com.mad.dms.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import javax.xml.validation.Validator;

//public class OrderMainActivity extends AppCompatActivity implements OrderMainAdapter.ItemClickListener {
public class OrderMainActivity extends AppCompatActivity {

    private static final int VIEW_ORDER_ACTIVITY_REQUEST_CODE = 1;
    public static final String VIEW_ORDER_EXTRA = "com.mad.dms.orders.VIEW_ORDER_EXTRA";
    public static final String VIEW_ORDER_POS = "com.mad.dms.orders.VIEW_ORDER_POS";
    private ArrayList<Order> orders;
    private OrderMainAdapter adapter;
    private OrderDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderDialog();
            }
        });

        db = new OrderDBHelper(this);
        orders = new ArrayList<>();
        orders.addAll(db.getAllOrders());

        RecyclerView recyclerView = findViewById(R.id.rvOrderMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                clickItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        adapter = new OrderMainAdapter(this, orders);
        recyclerView.setAdapter(adapter);
    }

    private void createOrder(String name, Date date) {
        long id = db.insertOrder(name, date);
        Order o = db.getOrder(id);
        if (o != null) {
            orders.add(0, o);
            adapter.resetSearch(orders);
            adapter.notifyDataSetChanged();
        }
    }

    private static EditText orderDateStr = null;

    private void showOrderDialog() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.order_add_dialog, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderMainActivity.this);
        dialogBuilder.setView(view);
        dialogBuilder
                .setCancelable(true)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int i) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int i) {
                        dialogBox.cancel();
                    }
                });

        final AlertDialog alertDialog = dialogBuilder.create();
        final EditText orderInput = view.findViewById(R.id.order_add_note_input);
        orderDateStr = view.findViewById(R.id.order_add_date_input);

        orderDateStr.setFocusable(false);
        orderDateStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = orderInput.getText().toString();
                String dateStr = orderDateStr.getText().toString();
                String errorText = InputValidator.validateNewOrder(nameStr, dateStr);

                if (errorText.equalsIgnoreCase(InputValidator.VALID)) {
                    Date deliveryDate = FmtHelper.parseShortDate(dateStr);
                    createOrder(nameStr, deliveryDate);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(OrderMainActivity.this, errorText, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public String selected;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if (orderDateStr != null) {
                String dateStr = day + "/" + (month + 1) + "/" + year;
                orderDateStr.setText(dateStr);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIEW_ORDER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int orderPos = data.getIntExtra(OrderViewActivity.VIEW_ORDER_REPLY_POS, 0);

            Order o = db.getOrder(orders.get(orderPos).getId());
            if (o != null) {
                orders.set(orderPos, o);
                adapter.notifyDataSetChanged();
            }

        }
    }

    private void clickItem(final int position) {
        Intent intent = new Intent(OrderMainActivity.this, OrderViewActivity.class);
        intent.putExtra(VIEW_ORDER_EXTRA, adapter.getItem(position).getId());
        intent.putExtra(VIEW_ORDER_POS, position);
        startActivityForResult(intent, VIEW_ORDER_ACTIVITY_REQUEST_CODE);
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Delete"};

        if (Login.getSessionAdmin() || orders.get(position).getStatus() != Order.ORDER_STATUS_CONFIRMED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose option");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        deleteOrder(position);
                    }
                }
            });
            builder.show();
        }
    }

    private void deleteOrder(int position) {
        db.deleteOrder(orders.get(position));
        orders.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }
}
