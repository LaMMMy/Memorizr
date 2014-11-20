package com.bensonius.memorizr.app;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends BaseActivity {
    private String[] drawerListViewItems;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_drawer);
        setContentView(R.layout.activity_main);

        // array of menu item names
        drawerListViewItems = getResources().getStringArray(R.array.menuItems);
        // drawer specified in activity_main.xml
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // the listview in activity_main
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerListViewItems));

        //actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.drawable.memorizr_logo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_drawer);

            toolbar.setTitle("Title");
            toolbar.setSubtitle("Sub");
            toolbar.setLogo(R.drawable.ic_launcher);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // App Icon
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // the code above should get the drawer opening and closing with a swipe.
        // Set the list's click listener -- for use with the app icon
        // drawerListView.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override protected int getLayoutResource() {
        return R.layout.activity_main;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.memorize, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // opens up the screen to manually type in text to memorize
    public void EnterTextButtonOnButtonClick(View v) {

        //TextView myTextView = (TextView)
        //        findViewById(R.id.enter_text_button);

        //String[] stringToMemorize = myTextView.getText().toString().split(" ");

        Intent i = new Intent(this, EnterText.class);
        //i.putExtra("stringToMemorize", stringToMemorize);
        startActivity(i);
    }

    // opens up the screen to load a file for text memorize
    public void LoadButtonOnButtonClick(View v) {

    }
}
