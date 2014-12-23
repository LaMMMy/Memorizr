package com.bensonius.memorizr.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class Memorize extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);

        Intent intent = getIntent();

        String incomingString = intent.getExtras().getString("stringToMemorize");
        // the ?<=[\\s] tells the regex to look at things surrounding teh spaces.
        // TODO: learn more about it
        String[] incomingStringArray = incomingString.split("(?<=[\\s])");

        TextView myTextView = (TextView)
                findViewById(R.id.memoryText);

        this.BuildMemorizeText(myTextView, incomingStringArray);
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    // Builds a string with hidden tokens in place.
    private void BuildMemorizeText(TextView textView, final String[] stringToShowArray) {
        int numWordsTotal = stringToShowArray.length;
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
        */
        for(int i = 0; i < numWordsTotal; i++) {

            Random rand = new Random();
            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNum = rand.nextInt((5 - 2) + 1) + 2;

            if(i % randomNum == 0){
                currentWordLength = stringToShowArray[i].trim().length();
                if(currentWordLength > 0) {
                    final String hiddenWord = this.BuildBlankWord(currentWordLength);
                    final int index = i;

                    final SpannableString hiddenWordLink = MakeLinkSpan(hiddenWord, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(Memorize.this, stringToShow[index], Toast.LENGTH_SHORT).show();
                            Toast myToaster = Toast.makeText(Memorize.this, stringToShowArray[index].trim(), Toast.LENGTH_SHORT);
                            myToaster.setGravity(Gravity.TOP | Gravity.LEFT, 100, 100);
                            myToaster.show();
                        }
                    });

                    hiddenWordLink.setSpan(new ForegroundColorSpan(Color.parseColor(getResources().getString(R.string.memorizr_colourPrimaryDark))),
                            0,
                            hiddenWordLink.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    hiddenWordLink.setSpan(new BackgroundColorSpan(Color.parseColor(getResources().getString(R.string.memorizr_colourPrimaryDark))),
                            0,
                            hiddenWordLink.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    textView.append(hiddenWordLink);
                    textView.append(" ");
                }
            }
            else {
                textView.append(stringToShowArray[i]);
            }

           // textView.append(" ");
        }

        MakeLinksFocusable(textView);
    }

    // basically just builds a string of consecutive underscores that are the length of the word it's replacing.
    private String BuildBlankWord(int wordLength){
        return StringUtils.leftPad("", wordLength, '_');
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
