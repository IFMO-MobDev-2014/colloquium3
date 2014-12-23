package com.example.alexey.colloquium3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends Activity implements AppReceiver.Receiver {

    public static int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!provider.isTableExists(provider.db,provider.TABLE_NAME))
        {
            provider.add_table(provider.TABLE_NAME);
            ContentValues cv= new ContentValues();
            cv.put("desk","GBR");
            cv.put("time",0);
            cv.put("prior",1);
            cv.put("value",75);
            getContentResolver().insert(provider.CONTENT_URI, cv);
            cv= new ContentValues();
            cv.put("desk","USD");
            cv.put("time",0);
            cv.put("prior",1);
            cv.put("value",54);
            getContentResolver().insert(provider.CONTENT_URI, cv);
            cv= new ContentValues();
            cv.put("desk","EUR");
            cv.put("time",0);
            cv.put("prior",1);
            cv.put("value",65);
            getContentResolver().insert(provider.CONTENT_URI, cv);
            cv= new ContentValues();
            cv.put("desk","USD");
            cv.put("time",0);
            cv.put("prior",2);
            cv.put("value",0);
            getContentResolver().insert(provider.CONTENT_URI, cv);
            cv= new ContentValues();
            cv.put("desk","EUR");
            cv.put("time",0);
            cv.put("prior",2);
            cv.put("value",0);
            getContentResolver().insert(provider.CONTENT_URI, cv);
            cv= new ContentValues();
            cv.put("desk","GBR");
            cv.put("time",0);
            cv.put("prior",2);
            cv.put("value",0);
            getContentResolver().insert(provider.CONTENT_URI,cv);
            cv= new ContentValues();
            cv.put("desk","RUB");
            cv.put("time",0);
            cv.put("prior",2);
            cv.put("value",10000);
            getContentResolver().insert(provider.CONTENT_URI, cv);
            cv= new ContentValues();
            cv.put("desk","time");
            cv.put("time",0);
            cv.put("prior",0);
            cv.put("value",0);
            getContentResolver().insert(provider.CONTENT_URI,cv);
        }
        Cursor cursor=getContentResolver().query(provider.CONTENT_URI,null,provider.DESK + " = 'time'",null,null);
        cursor.moveToFirst();
        time =cursor.getInt(cursor.getColumnIndex("time"));
        update_lv();
        Intent intent=new Intent(MainActivity.this,IServise.class);
        startService(intent);

    }

    void update_lv()
    {
        ListView lv= (ListView) findViewById(R.id.listView);
        lv.setAdapter(new SimpleCursorAdapter(this,
                R.layout.item,
                getContentResolver().query(provider.CONTENT_URI, null, "prior = 1", null, null),
                new String[]{"desk","value"},
                new int[]{R.id.textView,R.id.textView2}));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv= (TextView) view.findViewById(R.id.textView);
                String name= tv.getText().toString();
                Intent intent=new Intent(MainActivity.this,MainActivity3.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

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
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        update_lv();
    }
}
