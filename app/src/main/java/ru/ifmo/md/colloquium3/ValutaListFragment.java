package ru.ifmo.md.colloquium3;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;

/**
 * Created by german on 22.12.14.
 */
public class ValutaListFragment extends Fragment {
    public static final String BUNDLE_KEY = "valuta_list_fragment";
    private HashMap<String, View> map = new HashMap<>();

    private void updateList(LinearLayout valutasView, Cursor cursor) {
        if (cursor.getCount() == 0) {
            return;
        }
        valutasView.removeAllViews();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {
            final String name = cursor.getString(cursor.getColumnIndexOrThrow(MoneyContentProvider.VALUTA_NAME));
            double value = cursor.getDouble(cursor.getColumnIndexOrThrow(MoneyContentProvider.VALUTA_VALUE));
            View view = new ValutaViewBuilder(getActivity()).
                        withValutaName(name).
                        withValutaValue(value).
                        withClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentManager fm = getActivity().getFragmentManager();
                                fm.beginTransaction().
                                        replace(R.id.fragment_container, new SelectedValutaFragment(name), SelectedValutaFragment.BUNDLE_KEY).
                                        addToBackStack(BUNDLE_KEY).commit();
                            }
                        }).
                        create(valutasView);
            map.put(name, view);
            valutasView.addView(view);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.valuta_list_fragment, null);
        LinearLayout valutasView = (LinearLayout) view.findViewById(R.id.valuta_container_layout);

        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = MoneyManager.getAllData(resolver);
        updateList(valutasView, cursor);

//        valutasView.addView(
//                new ValutaViewBuilder(getActivity()).
//                        withValutaName("USD").
//                        withValutaValue(54.55).
//                        withClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                FragmentManager fm = getActivity().getFragmentManager();
//                                fm.beginTransaction().
//                                        replace(R.id.fragment_container, new SelectedValutaFragment("USD", 54.55), SelectedValutaFragment.BUNDLE_KEY).
//                                        addToBackStack(BUNDLE_KEY).commit();
//                            }
//                        }).
//                        create(valutasView));
//
//        valutasView.addView(
//                new ValutaViewBuilder(getActivity()).
//                        withValutaName("EUR").
//                        withValutaValue(65).
//                        withClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                FragmentManager fm = getActivity().getFragmentManager();
//                                fm.beginTransaction().
//                                        replace(R.id.fragment_container, new SelectedValutaFragment("EUR", 65), SelectedValutaFragment.BUNDLE_KEY).
//                                        addToBackStack(BUNDLE_KEY).commit();
//                            }
//                        }).
//                        create(valutasView));


        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Current rate");
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        return view;
    }
}
