package com.gppdi.ubipri.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.ui.fragments.HomeFragment;
import com.gppdi.ubipri.ui.fragments.SettingsFragment;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import butterknife.InjectView;

public class MainActivity extends BaseActivity implements Drawer.OnDrawerItemClickListener {
    public static final int FRAGMENT_HOME     = 0;
    public static final int FRAGMENT_ENVIRONMENTS  = 1;
    public static final int FRAGMENT_SETTINGS = 2;

    private CharSequence mTitle;
    private Drawer mDrawer;
    @InjectView(R.id.activity_main_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        inflateLayout(R.layout.activity_main);

        //toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_logo));
        setSupportActionBar(toolbar);

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.action_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(FRAGMENT_HOME),
                        new PrimaryDrawerItem().withName(R.string.action_environments).withIcon(FontAwesome.Icon.faw_globe).withIdentifier(FRAGMENT_ENVIRONMENTS),
                        new PrimaryDrawerItem().withName(R.string.action_settings).withIcon(FontAwesome.Icon.faw_gear).withIdentifier(FRAGMENT_SETTINGS)
                )
                .withOnDrawerItemClickListener(this)
                .build();

        // display home fragment
        displayView(FRAGMENT_HOME, R.string.action_home);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDrawer.getCurrentSelection() == mDrawer.getPositionFromIdentifier(FRAGMENT_ENVIRONMENTS)) {
            displayView(FRAGMENT_HOME, R.string.action_home);
            mDrawer.setSelectionByIdentifier(FRAGMENT_HOME);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        menu.findItem(R.id.action_websearch).setVisible(!mDrawer.isDrawerOpen());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle());

                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
        if (drawerItem == null) {
            return true;
        }

        if (drawerItem instanceof Nameable) {
            return displayView(drawerItem.getIdentifier(), ((Nameable) drawerItem).getNameRes());
        }

        return displayView(drawerItem.getIdentifier(), null);
    }

    private boolean displayView(int id, Integer nameRes) {
        Fragment fragment = null;

        switch (id) {
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                break;
            case FRAGMENT_ENVIRONMENTS:
                startActivity(new Intent(this, EnvironmentsActivity.class));
                break;
            case FRAGMENT_SETTINGS:
                fragment = new SettingsFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            if (nameRes != null) {
                toolbar.setTitle(nameRes);
            }

            mDrawer.closeDrawer();
            return true;
        } else {
            Log.e("MainActivity", "Error in creating fragment");
            return false;
        }
    }
}
