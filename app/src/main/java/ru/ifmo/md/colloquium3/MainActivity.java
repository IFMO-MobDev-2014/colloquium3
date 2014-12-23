package ru.ifmo.md.colloquium3;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", "44");
        contentValues.put("url", "USD");

        getContentResolver().insert(Uri.parse("content://ru.ifmo.md.colloquium3.feeds/channels"), contentValues);

        contentValues.put("url", "EUR");
        contentValues.put("name", "65");
        getContentResolver().insert(Uri.parse("content://ru.ifmo.md.colloquium3.feeds/channels"), contentValues);

        contentValues.put("url", "GBP");
        contentValues.put("name", "70");

        getContentResolver().insert(Uri.parse("content://ru.ifmo.md.colloquium3.feeds/channels"), contentValues);

        setListAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, new String[]{"name", "url"}, new int[]{android.R.id.text1, android.R.id.text2}, 0));
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                /*Intent intent = new Intent(MainActivity.this, RSSActivity.class);
                intent.putExtra("extra_channel_id", id);
                startActivity(intent);*/
            }
        });
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                getContentResolver().delete(Uri.parse("content://ru.ifmo.md.colloquium3.feeds/channels"), "" + id, null);
                return true;
            }
        });
        RSSIntentService.start(this, getIntent().getLongExtra("extra_channel_id", -1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if (item.getItemId() == R.id.addDialogItem) {
            RSSDialog RSSDialog = new RSSDialog();
            getFragmentManager().beginTransaction().add(RSSDialog, "").commit();
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, Uri.parse("content://ru.ifmo.md.colloquium3.feeds/channels"), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((CursorAdapter) getListAdapter()).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter) getListAdapter()).swapCursor(null);
    }
}
