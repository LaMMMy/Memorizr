package com.bensonius.memorizr.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity
        extends ActionBarActivity
        implements InputFragment.OnInputFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;
    private Toolbar mToolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private InputFragment.OnInputFragmentInteractionListener mListener;
    private ObjectDrawerItem[] mDrawerItems;
    private String[] mDrawerItemTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // array of menu item names
        mDrawerItemTitles = getResources().getStringArray(R.array.menuItems);

        mDrawerItems = new ObjectDrawerItem[3];

        mDrawerItems[0] = new ObjectDrawerItem(R.drawable.ic_action_edit, "Enter Text");
        mDrawerItems[1] = new ObjectDrawerItem(R.drawable.ic_action_new, "Load File");
        mDrawerItems[2] = new ObjectDrawerItem(R.drawable.ic_action_help, "Help");

        // pass to the custom adapter
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
                R.layout.listview_item_row,
                mDrawerItems);

        // drawer specified in activity_main.xml
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // #11 https://www.codeofaninja.com/2014/02/android-navigation-drawer-example.html
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerListView.setAdapter(adapter);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);

            mDrawerToggle = new ActionBarDrawerToggle(this,
                    mDrawerLayout,
                    mToolbar,
                    R.string.drawer_open,
                    R.string.drawer_close) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };

            // Set the drawer toggle as the DrawerListener
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // the code above should get the drawer opening and closing with a swipe.
        // Set the list's click listener -- for use with the app icon
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
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
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new InputFragment();
                break;
            case 1:
                fragment = new LoadFileFragment();
                break;
//            case 2:
//                fragment = new HelpFragment();
//                break;

            default:
                break;
        }

        // Create a new fragment and specify the planet to show based on position
        //Fragment fragment = new InputFragment();
        Bundle args = new Bundle();

        args.putInt(InputFragment.ARG_PARAM1, position);
        fragment.setArguments(args);


        if(fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerListView.setItemChecked(position, true);
            mDrawerListView.setSelection(position);
            getSupportActionBar().setTitle(mDrawerItemTitles[position]);
            // SetTitle(mDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerListView);
        }
        else{
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    public void onInputFragmentInteraction(Uri uri){
    }

    /** Opens up the Activity to actually memorize the entered text. */
    public void MemorizeTextButtonOnButtonClick(View v) {

        TextView myTextView = (TextView)
                findViewById(R.id.fullscreen_content);

        // make an array of all the words so they can be blanked out on the memorize screen
        // where necessary
//        String[] stringToMemorize = myTextView.getText().toString().split(" ");
        String stringToMemorize = myTextView.getText().toString();

        // want to call the Activity for actually memorizing now.
        Intent i = new Intent(this, Memorize.class);
        // add the string array to the intent call.
        i.putExtra("stringToMemorize", stringToMemorize);
        startActivity(i);
    }

    public void SaveFileButtonOnButtonClick(View v) {
        TextView myTextView = (TextView)
                findViewById(R.id.fullscreen_content);

        String filePath = getResources().getString(R.string.defaultFilePath);
        String fileName = "memoryText.xml";
        String data = myTextView.getText().toString();


        FileOutputStream fos;
        try {

            // create a File object for the parent directory
            File dir = new File(filePath);
            // have the object build the directory structure, if needed.
            dir.mkdirs();
            // create a File object for the output file
            File outputFile = new File(dir, fileName);
            // now attach the OutputStream to the file object, instead of a String representation
            fos = new FileOutputStream(outputFile);

            //fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            //default mode is PRIVATE, can be APPEND etc.
            fos.write(data.getBytes());
            fos.close();

            Toast.makeText(getApplicationContext(), fileName + " saved",
                    Toast.LENGTH_LONG).show();


        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // opens up the screen to load a file for text memorize
    public void LoadButtonOnButtonClick(View v) {

    }
}
