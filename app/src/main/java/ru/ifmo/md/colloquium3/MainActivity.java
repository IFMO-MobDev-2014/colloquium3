package ru.ifmo.md.colloquium3;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ValutaListFragment()).commit();
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
        if (id == android.R.id.home) {
            Fragment selectedValutaFragment = getFragmentManager().findFragmentByTag(SelectedValutaFragment.BUNDLE_KEY);
            if (selectedValutaFragment != null) {
                getFragmentManager().beginTransaction().remove(selectedValutaFragment).commit();
                getFragmentManager().popBackStack();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
