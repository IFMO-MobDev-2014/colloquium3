package ru.ifmo.md.colloquium3;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Random;


public class MainActivity extends ActionBarActivity {
    Element[] rate = new Element[3];

    double example;

    long usd;
    long eur;
    long gbd;

    Random r = new Random();

    final Uri CURR_URI = Uri.parse("content://ru.ifmo.md.colloquium3.provider/currency");

    final String CURR_NAME = "name";
    final String CURR_RATE = "rate";
    final String CURR_VALUE = "value";

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rate[0] = new Element("usd", 60.0);
        rate[1] = new Element("eur", 80.0);
        rate[2] = new Element("gbd", 100.0);

        SharedPreferences initialPref = getSharedPreferences("INITIAL", 0);
        boolean firsttimer = initialPref.getBoolean("INITIAL", true);
            if (firsttimer) {

                Cursor cursor = getContentResolver().query(CURR_URI, null, null,
                        null, null);
                startManagingCursor(cursor);
                String from[] = { "name", "rate" };
                int to[] = { android.R.id.text1, android.R.id.text2 };
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                        android.R.layout.simple_list_item_2, cursor, from, to);

                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(adapter);
                ContentValues cv = new ContentValues();
                for (int i = 0; i < 3; i++) {

                    rate[i].rate += ((double) r.nextInt(20) / 10) - 1;

                    BigDecimal x = new BigDecimal(rate[i].rate);
                    x = x.setScale(2, BigDecimal.ROUND_HALF_UP);
                    cv.put(CURR_NAME, rate[i].currency);
                    cv.put(CURR_RATE, x.toString());
                    Uri uri = ContentUris.withAppendedId(CURR_URI, i + 1);
                    getContentResolver().update(uri, cv, null, null);
                }
        }
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ContentValues cv = new ContentValues();

                                counter++;
                                if (counter <10) {
                                    for (int i = 0; i < 3; i++) {

                                        rate[i].rate += ((double) r.nextInt(20) / 10) - 1;

                                        BigDecimal x = new BigDecimal(rate[i].rate);
                                        x = x.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        cv.put(CURR_NAME, rate[i].currency);
                                        cv.put(CURR_RATE, x.toString());
                                        Uri uri = ContentUris.withAppendedId(CURR_URI, i + 1);
                                        getContentResolver().update(uri, cv, null, null);
                                    }
                                } else {
                                    counter = 0;
                                    for (int i = 0; i < 3; i++) {

                                        rate[i].rate += r.nextInt(2)-1;

                                        BigDecimal x = new BigDecimal(rate[i].rate);
                                        x = x.setScale(2, BigDecimal.ROUND_HALF_UP);
                                        cv.put(CURR_NAME, rate[i].currency);
                                        cv.put(CURR_RATE, x.toString());
                                        Uri uri = ContentUris.withAppendedId(CURR_URI, i + 1);
                                        getContentResolver().update(uri, cv, null, null);
                                    }
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

}

    private static class Element {
        public final String currency;
        public double rate;

        public Element(String currency, double rate) {
            this.currency = currency;
            this.rate = rate;
        }
    }

    /*private class MyAdapter extends ArrayAdapter<Element> {

        public MyAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2, rate);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Element element = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(android.R.layout.simple_list_item_2, null);
            }
            ((TextView) convertView.findViewById(android.R.id.text1))
                    .setText(element.currency);
            BigDecimal x = new BigDecimal(element.rate);
            x = x.setScale(2, BigDecimal.ROUND_HALF_UP);
            ((TextView) convertView.findViewById(android.R.id.text2))
                    .setText(x.toString());
            return convertView;
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
