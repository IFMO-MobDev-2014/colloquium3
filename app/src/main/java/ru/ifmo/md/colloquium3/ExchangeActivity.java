package ru.ifmo.md.colloquium3;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ExchangeActivity extends ActionBarActivity {

    public static String INTENT_CURRENCY_ID = "CURRENCY_ID";
    public static String INTENT_RUB_SUM = "RUM_SUM";
    private long moneyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        moneyId = getIntent().getLongExtra(INTENT_CURRENCY_ID, -1);
        Cursor cursor= getContentResolver().query(MoneyProvider.MONEY1_URI, null, DBMoney.CURRENCY1 + " = ?", new String[]{"RUB"}, null);
        cursor.moveToFirst();
        double rubSum = cursor.getDouble(cursor.getColumnIndexOrThrow(DBMoney.SUM1));

//        double rubSum = 1000;
        cursor = getContentResolver().query(MoneyProvider.MONEY1_URI, null, "_id = ?", new String[]{String.valueOf(moneyId)}, null);
        cursor.moveToFirst();
        String currencyName = cursor.getString(cursor.getColumnIndexOrThrow(DBMoney.CURRENCY1));
        double currencyRate = cursor.getDouble(cursor.getColumnIndexOrThrow(DBMoney.SUM1));
        TextView textView1 = (TextView) findViewById(R.id.exchange_currency_name);
        textView1.setText(currencyName);
        textView1 = (TextView) findViewById(R.id.exchange_currency_rate);
        textView1.setText(String.valueOf(currencyRate));
        textView1 = (TextView) findViewById(R.id.exchange_currency2_rate);
        textView1.setText(String.valueOf(rubSum));
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exchange, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buyVoid(View view) {
        TextView textView1 = (TextView) findViewById(R.id.exchange_currency_rate);
        TextView textView2 = (TextView) findViewById(R.id.exchange_currency2_rate);
        double currencySum = Double.parseDouble(textView1.getText().toString());
        double rubSum = Double.parseDouble(textView2.getText().toString());
        Cursor cursor = getContentResolver().query(MoneyProvider.MONEY1_URI, null, "_id = ?", new String[]{String.valueOf(moneyId)}, null);
        cursor.moveToFirst();
        double rate = cursor.getDouble(cursor.getColumnIndexOrThrow(DBMoney.RATE1));
        currencySum += 1;
        rubSum -= rate;
        ContentValues cv = new ContentValues();
        cv.put(DBMoney.SUM1, currencySum);
        getContentResolver().update(MoneyProvider.MONEY1_URI, cv, " _id = ?", new String[]{String.valueOf(moneyId)});
        cv = new ContentValues();
        cv.put(DBMoney.SUM1, rubSum);
        getContentResolver().update(MoneyProvider.MONEY1_URI, cv, DBMoney.CURRENCY1 + "  = ?", new String[]{"RUB"});
        textView1.setText(String.valueOf(currencySum));
        textView2.setText(String.valueOf(rubSum));
    }

    public void sellVoid(View view) {
        TextView textView1 = (TextView) findViewById(R.id.exchange_currency_rate);
        TextView textView2 = (TextView) findViewById(R.id.exchange_currency2_rate);
        double currencySum = Double.parseDouble(textView1.getText().toString());
        double rubSum = Double.parseDouble(textView2.getText().toString());
        Cursor cursor = getContentResolver().query(MoneyProvider.MONEY1_URI, null, "_id = ?", new String[]{String.valueOf(moneyId)}, null);
        cursor.moveToFirst();
        double rate = cursor.getDouble(cursor.getColumnIndexOrThrow(DBMoney.RATE1));
        currencySum -= 1;
        rubSum += rate;
        ContentValues cv = new ContentValues();
        cv.put(DBMoney.SUM1, currencySum);
        getContentResolver().update(MoneyProvider.MONEY1_URI, cv, " _id = ?", new String[]{String.valueOf(moneyId)});
        cv = new ContentValues();
        cv.put(DBMoney.SUM1, rubSum);
        getContentResolver().update(MoneyProvider.MONEY1_URI, cv, DBMoney.CURRENCY1 + "  = ?", new String[]{"RUB"});
        textView1.setText(String.valueOf(currencySum));
        textView2.setText(String.valueOf(rubSum));
    }
}
