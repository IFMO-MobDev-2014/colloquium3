package ru.ifmo.md.colloquium3;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;


public class MainActivity extends Activity {
    public static final String[] CURRENCIES = {"USD","EUR","GBR"};
    public int[] COURSES = {7500,10000,12000};
    public ArrayList<CurrencyType> currencyArrayList;
    private DB myBD;
    private Cursor cursor;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        myBD = new DB(this);
        myBD.open();
        currencyArrayList = new ArrayList<CurrencyType>();
        CurrencyType timeCurr = new CurrencyType(CURRENCIES[0],COURSES[0]);
        currencyArrayList.add(timeCurr);
        timeCurr = new CurrencyType(CURRENCIES[1],COURSES[1]);
        currencyArrayList.add(timeCurr);
        timeCurr = new CurrencyType(CURRENCIES[2],COURSES[2]);
        currencyArrayList.add(timeCurr);
        MyAdapter myAdapter = new MyAdapter(this, currencyArrayList);
        listView.setAdapter(myAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Random rand = new Random();
                        Thread.sleep(1000);
                        CurrencyType timeCurr=currencyArrayList.get(0);
                        timeCurr.cost=timeCurr.cost+rand.nextInt(1);
                        currencyArrayList.set(0,timeCurr);
                        timeCurr=currencyArrayList.get(1);
                        timeCurr.cost=timeCurr.cost+rand.nextInt(1);
                        currencyArrayList.set(0,timeCurr);
                        timeCurr=currencyArrayList.get(2);
                        timeCurr.cost=timeCurr.cost+rand.nextInt(1);
                        currencyArrayList.set(0,timeCurr);
                    } catch (Exception e) {}
                }
            }
        }).start();
    }

}
