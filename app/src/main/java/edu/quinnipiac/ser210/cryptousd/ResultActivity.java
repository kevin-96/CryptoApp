/*
 * This class is in charged of the backend code for the result activity, it displays the value obtained
 * from the JSON and it also has the code to change the background color as well as the code for the
 * share options button on the toolbar.
 *
 * Created by: Kevin Sangurima
 * Last Updated: 03/22/19
 *
 */

package edu.quinnipiac.ser210.cryptousd;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    private Double finalValue;
    private Handler handler = new Handler();
    private String finalString;
    private ShareActionProvider shareActionProvider;
    private ConstraintLayout currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // Finds view for layout and toolbar
        currentLayout = findViewById(R.id.result_layout);
        Toolbar toolbar = findViewById(R.id.include);
        // Set support for the toolbar to function on this activity
        setSupportActionBar(toolbar);
        // Enables the back to previous activity button on the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Retrieves all the strings passed in intents from the main activity
        String value = getIntent().getStringExtra("coinValue");
        String coin = getIntent().getStringExtra("coin");
        String currency = getIntent().getStringExtra("currency");

        /*
         * // This is Commented out because it will be used in a future version of the app which
         * // will be worked on after the semester is over
         *
         * // Converts the string into a double so it can be truncated to 2 decimal places
         *
         *  finalValue = Math.floor((Double.parseDouble(value)) * 100) / 100;
         *
         */

        // Finds the view for the textview and then changes the text to show the result needed
        TextView result = findViewById(R.id.resultDisplay);
        finalString = "The price of 1 " + coin + " is " + value + " " + currency;
        result.setText(finalString);
    }
    //This block of code is for when the user clicks on one of the options of the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_copy:
                Toast.makeText(this, "Price Copied to ClipBoard", Toast.LENGTH_SHORT).show();
                ClipboardManager clipBoard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                clipBoard.setText(finalString);
                return true;
            case R.id.help_menu:
                handler.dialogBox(this);
                return true;
            case R.id.originalButton:
                handler.changeBackgroundColor(currentLayout, Color.rgb(57,61,72));
                return true;
            case R.id.blackButton:
                handler.changeBackgroundColor(currentLayout, Color.BLACK);
                return true;
            case R.id.blueButton:
                handler.changeBackgroundColor(currentLayout, Color.BLUE);
                return true;
            case R.id.yellowButton:
                handler.changeBackgroundColor(currentLayout, Color.YELLOW);
                return true;
            case R.id.redButton:
                handler.changeBackgroundColor(currentLayout, Color.RED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_share);

        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareActionIntent(finalString);

        return true;
    }
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }


}
