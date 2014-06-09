package com.bensonius.memorizr.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.helloworld.app.R;


public class Memorize extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);

        Intent intent = getIntent();
        String[] stringToMemorize = intent.getExtras().getStringArray("stringToMemorize");

        TextView myTextView = (TextView)
                findViewById(R.id.memoryText);

        String textToMemorize = this.BuildMemorizeText(stringToMemorize);

        // initial test to display memory text
        myTextView.setText(textToMemorize);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.memorize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Builds a string with hidden tokens in place.
    // TODO: store array of hidden words for later retrieval.
    private String BuildMemorizeText(String[] stringToShow) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < stringToShow.length; i++) {
            if(i % 4 == 0){
                sb.append("________ ");
            }
            else {
                sb.append(stringToShow[i]);
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}
