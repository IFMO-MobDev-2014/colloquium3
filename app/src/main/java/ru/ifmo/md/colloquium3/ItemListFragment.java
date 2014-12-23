package ru.ifmo.md.colloquium3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import ru.ifmo.md.colloquium3.DummyContent;

public class ItemListFragment extends ListFragment {
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final String LOG_TAG = "itemlistfragment";
    private static final Uri DB_URI1 = Uri.parse("content://ru.ifmo.md.colloquium3.providers.cur/cur");
    private static final Uri DB_URI2 = Uri.parse("content://ru.ifmo.md.colloquium3.providers.money/money");
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    public ItemListFragment() {
    }

    DummyContent.DummyItem mItem;
    MyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "itemlistfragment");
        Cursor cur1 = getActivity().getContentResolver().query(DB_URI1, null, null, null, null);
        Cursor cur2 = getActivity().getContentResolver().query(DB_URI2, null, null, null, null);
        if (cur1.getCount() == 0) {
            for (int i = 0; i < DummyContent.ITEMS.size(); i++) {
                mItem = DummyContent.ITEMS.get(i);
//                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                Log.d(LOG_TAG, "add " + mItem.content);
                ContentValues cv1 = new ContentValues();
                cv1.put(MyCurDB.CURRENCY, mItem.content);
                cv1.put(MyCurDB.VALUE, 50.0 + (Math.random() % 20) * 10);
                ContentValues cv2 = new ContentValues();
                cv2.put(MyMoneyDB.CURRENCY, mItem.content);
                cv2.put(MyMoneyDB.VALUE, 0.0);
                getActivity().getContentResolver().insert(DB_URI1, cv1);
                getActivity().getContentResolver().insert(DB_URI2, cv2);
            }
            ContentValues cv2 = new ContentValues();
            cv2.put(MyMoneyDB.CURRENCY, "RUB");
            cv2.put(MyMoneyDB.VALUE, 10000.0);
            getActivity().getContentResolver().insert(DB_URI2, cv2);
        }
        getActivity().startService(new Intent(getActivity(), MyService.class));
        Log.d(LOG_TAG, "set Service");
        adapter = new MyAdapter(getActivity(), cur1);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

}
