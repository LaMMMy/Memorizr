package com.bensonius.memorizr.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

public class Memorize extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);

        Intent intent = getIntent();
        String[] incomingString = intent.getExtras().getStringArray("stringToMemorize");

        TextView myTextView = (TextView)
                findViewById(R.id.memoryText);

        this.BuildMemorizeText(myTextView, incomingString);
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
    private void BuildMemorizeText(TextView textView, final String[] stringToShow) {
        int numWordsTotal = stringToShow.length;
        int numBlankedWords = numWordsTotal / 4;
        if(numBlankedWords % 4 > 0) {
            numBlankedWords += numBlankedWords % 4;
        }

        String[] blankedWords = new String[numBlankedWords];

        int currentWordLength;
        int blankCount = 0;

        textView.setText("");

        /*
        the words of the text are passed as an array of Strings
        go through each one and append individually to the TextView
        TODO: make sure it can handle very big chunk of text.
        */
        for(int i = 0; i < numWordsTotal; i++) {
            if(i % 4 == 0){
                currentWordLength = stringToShow[i].length();
                String hiddenWord = this.BuildBlankWord(currentWordLength);
                final int index = i;

                final SpannableString hiddenWordLink = MakeLinkSpan(hiddenWord, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(Memorize.this, stringToShow[index], Toast.LENGTH_SHORT).show();
                        Toast myToaster = Toast.makeText(Memorize.this, stringToShow[index], Toast.LENGTH_SHORT);
                        myToaster.setGravity(Gravity.TOP|Gravity.LEFT, 100, 100);
                        myToaster.show();
                    }
                });

                textView.append(hiddenWordLink);

                // add the word to those that are blanked out
                blankedWords[blankCount] = stringToShow[i];
                blankCount++;
            }
            else {
                textView.append(stringToShow[i]);
            }

            textView.append(" ");
        }

        MakeLinksFocusable(textView);
    }

    // basically just builds a string of consecutive underscores that are the length of the word it's replacing.
    private String BuildBlankWord(int wordLength){
        String blank = StringUtils.leftPad("", wordLength, '_') + " ";
        return blank;
    }

/*
 * Methods used above.
 */
    private SpannableString MakeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);

        // use the custom class to make a clickable string.
        link.setSpan(new ClickableString(listener, text, true), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        return link;
    }

    private void MakeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();

        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    // Custom class that makes strings clickable
    // Stolen from http://stackoverflow.com/a/9970195/506366
    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;
        private String mHiddenValue;
        private boolean mIsHidden;

        public ClickableString(View.OnClickListener listener, CharSequence hiddenValue, boolean isHidden) {
            mListener = listener;
            mHiddenValue = hiddenValue.toString();
            mIsHidden = isHidden;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
