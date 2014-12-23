package ru.ifmo.md.colloquium3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class StoreActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Intent starter = getIntent();
        final String curr = starter.getStringExtra("curr");
        setTitle(curr);

        Button buy = (Button) findViewById(R.id.buy);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView cur1 = (TextView) findViewById(R.id.cur1);
                cur1.setText(curr);

                TextView count = (TextView) findViewById(R.id.cnt);
                double cnt = Double.parseDouble(count.getText().toString());

                Toast.makeText(StoreActivity.this, "Bought " + cnt + " " + curr + " for " + cnt * getRate(curr) + "rubles", Toast.LENGTH_LONG).show();

            }
        });
    }

    public double getRate(String curr) {
        Cursor cursor = getContentResolver().query(RateContentProvider.RATES_CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();

        do {
            String curCur = cursor.getString(1);
            double rate = cursor.getDouble(2);
            cursor.close();
            return rate;
        } while (cursor.moveToNext());
    }
}
