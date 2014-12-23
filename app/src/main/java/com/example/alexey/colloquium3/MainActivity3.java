package com.example.alexey.colloquium3;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity3 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity3);
        String name= getIntent().getStringExtra("name");
        Cursor cursor=getContentResolver().query(provider.CONTENT_URI,null,"( "+provider.PRIOR + " = 2 ) AND ( "+provider.DESK+" = '"+name+"' )",null,null);
        cursor.moveToFirst();
        TextView tv=(TextView) findViewById(R.id.textView3);
        tv.setText(name);
        int time =cursor.getInt(cursor.getColumnIndex("value"));
        tv=(TextView) findViewById(R.id.textView4);
        tv.getText();
        String tim=Integer.toString(time);
        tv.setText(tim);
        tv=(TextView) findViewById(R.id.textView5);
        tv.setText("RUB");
        cursor=getContentResolver().query(provider.CONTENT_URI,null,"( "+provider.PRIOR + " = 2 ) AND ( "+provider.DESK+" = '"+"RUB"+"' )",null,null);
        cursor.moveToFirst();
        time =cursor.getInt(cursor.getColumnIndex("value"));
        tv=(TextView) findViewById(R.id.textView6);
        tv.getText();
        tim=Integer.toString(time);
        tv.setText(tim);
        cursor=getContentResolver().query(provider.CONTENT_URI,null,"( "+provider.PRIOR + " = 1 ) AND ( "+provider.DESK+" = '"+name+"' )",null,null);
        cursor.moveToFirst();
        time =cursor.getInt(cursor.getColumnIndex("value"));
        tv=(TextView) findViewById(R.id.textView7);
        tim=Integer.toString(time);
        tv.setText(tim);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity3, menu);
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
