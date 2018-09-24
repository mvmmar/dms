package com.mad.dms.orders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mad.dms.R;

public class OrderAddActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.mad.dms.orders.NEW_ORDER_REPLY";
    private EditText mOrderName;
    private OrderDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add);
        db = new OrderDBHelper(this);

        mOrderName = findViewById(R.id.orderAddNameInput);

        final Button button = findViewById(R.id.orderAddButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mOrderName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String name = mOrderName.getText().toString();
                    int id = db.insertOrder(name);
                    replyIntent.putExtra(EXTRA_REPLY, id);
                    // replyIntent.putExtra(EXTRA_REPLY, name);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

    }
}
