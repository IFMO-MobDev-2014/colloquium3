package com.example.pva701.colloquium3;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.os.Handler;

import com.example.pva701.colloquium3.provider.CurrencyDbHelper;
import com.example.pva701.colloquium3.provider.QueryManager;

import java.util.Locale;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static class CourseArrayAdapter extends ArrayAdapter <Course>{
        private LayoutInflater inflater;
        public CourseArrayAdapter(Context context) {
            super(context, R.layout.course_view);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public CourseArrayAdapter(Context context, Cursor cursor) {
            super(context, R.layout.course_view);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            while (cursor.moveToNext())
                add(CurrencyDbHelper.CourseCursor.getCourse(cursor));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(R.layout.course_view, parent, false);

            TextView currency = (TextView)convertView.findViewById(R.id.name);
            currency.setText(getItem(position).getName());
            TextView value = (TextView)convertView.findViewById(R.id.value);
            value.setText(" " + String.format(Locale.ENGLISH, "%.02f", getItem(position).getVal()));
            return convertView;
        }
    }

    public static class CourseCursorLoader extends AsyncTaskLoader<Cursor> {
        private Context context;
        public CourseCursorLoader(Context c) {
            super(c);
            context = c;
        }

        @Override
        public Cursor loadInBackground() {
            return QueryManager.get(context).getAllCourses();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CourseCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (adapter == null) {
            adapter = new CourseArrayAdapter(getActivity(), cursor);
            courseList.setAdapter(adapter);
            return;
        }

        adapter.clear();
        while (cursor.moveToNext()) {
            Course course = CurrencyDbHelper.CourseCursor.getCourse(cursor);
            adapter.add(course);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        courseList.setAdapter(null);
        adapter = null;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UpdateService.CURRENCY_UPDATED)
                getLoaderManager().initLoader(0, null, MainFragment.this).forceLoad();
        }
    };

    private ArrayAdapter<Course> adapter;
    private ListView courseList;

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, MainFragment.this).forceLoad();
        UpdateService.setHandler(handler);
    }

    @Override
    public void onStop() {
        super.onStop();
        UpdateService.setHandler(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!UpdateService.isServiceAlarmOn(getActivity()))
            UpdateService.setServiceAlarm(getActivity(), true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        courseList = (ListView)view.findViewById(R.id.course_list);
        if (adapter != null)
            courseList.setAdapter(adapter);
        return view;
    }

}
