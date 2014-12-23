package ru.ifmo.md.colloquium3;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView courcesList = (ListView)findViewById(R.id.cources_list);

        String[] Columns = new String[]{MyContentProvider.COLUMN_COURCE_NAME, MyContentProvider.COLUMN_CURRENT_COURCE};
        int[] elements = new int[]{R.id.cource_name_textview, R.id.curr_cource_textview};

        cursor = new SimpleCursorAdapter(this, R.layout.element, null, Columns, elements, 0);

        courcesList.setAdapter(cursor);

        Intent i = new Intent(this, CourceUpdateService.class);
        startService(i);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] Columns = new String[]{MyContentProvider.COLUMN_ID, MyContentProvider.COLUMN_COURCE_NAME, MyContentProvider.COLUMN_CURRENT_COURCE, MyContentProvider.COLUMN_COUNT};
        return new CursorLoader(this, MyContentProvider.TABLE_COURCES_URI, Columns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
