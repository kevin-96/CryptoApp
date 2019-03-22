/*
 * This class is in charged of the first activity, it has all the info to receive the JSON
 * and it has all the backend methods used in the main activity
 *
 * Created by: Kevin Sangurima
 * Last Updated: 03/22/19
 *
 */

package edu.quinnipiac.ser210.cryptousd;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] coins = new String[] {"BTC", "ETH", "XRP", "BCH", "LTC", "ZEC", "XLM", "XMR", "TRX", "DASH",
            "NEO", "ETC", "DOGE", "NANO", "SC"};
    private String[] currency = new String[] {"USD", "EUR", "CAD", "JPY", "MXN", "CNY"};
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String coinValue;
    private String currencyValue, res;
    private ShareActionProvider shareActionProvider;
    private Handler handler = new Handler();
    private String url1 = "https://bravenewcoin-v1.p.rapidapi.com/ticker?show="; // after goes price
    private String url2 = "&coin=";// after goes coin
    private ConstraintLayout currentLayout; // Stores current layout

    //This block of code is for when the user clicks on one of the options of the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){

            case R.id.action_copy:
                Toast.makeText(this, "No Value to Copy", Toast.LENGTH_SHORT);
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //gets both spinners and finds the layout
        final Spinner coinSpinner = findViewById(R.id.coinSpinner);
        final Spinner currencySpinner = findViewById(R.id.currencySpinner);
        currentLayout = findViewById(R.id.main_layout);
        //finds the toolbar and sets the support for it to be used in this activity
        Toolbar toolbar = findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        //add the values from the coin array to the first spinner
        ArrayAdapter<String> coinAdapter = new ArrayAdapter<String>(this,  R.layout.spinner_item, coins);
        coinAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        coinSpinner.setAdapter(coinAdapter);
        //add the values of the currency array to the second spinner
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, currency);
        currencyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
        // Stores the value selected on the first spinner related to the cryptocurrency
        coinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                coinValue = (String) parent.getItemAtPosition(position);
                Log.d("coin", coinValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Stores the value selected on the second spinner related to the currency
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyValue = (String) parent.getItemAtPosition(position);
                Log.d("currency", currencyValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
            new FetchCoinValue().execute(coinValue, currencyValue);
    }

    private class FetchCoinValue extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection =null;
            BufferedReader reader =null;

            try {
                URL url = new URL(url1 + params[1]
                        + url2 + params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key","006da6b0d5msh97aaea208d3f61fp1a7561jsn5292b28eff9a");

                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                if (in == null) {
                    return null;
                }
                reader  = new BufferedReader(new InputStreamReader(in));
                // call getBufferString to get the string from the buffer.

                String coinValueJsonString = getBufferStringFromBuffer(reader).toString();

                // call a method to parse the json data and return a string.
                res =  handler.getCoinValue(coinValueJsonString);
                Log.d("Price",res);

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }finally{
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try{
                        reader.close();
                    }catch (IOException e){
                        Log.e(LOG_TAG,"Error" + e.getMessage());
                        return null;
                    }
                }
            }

            return res;
        }

        protected void onPostExecute(String result){
            if (result != null){
                Log.d(LOG_TAG, result);
                // Creates an intent and send the value obtained from the JSON as well as the information of what currencies they are
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("coinValue",result);
                intent.putExtra("coin", coinValue);
                intent.putExtra("currency", currencyValue);
                startActivity(intent);

            }
        }
        private StringBuffer getBufferStringFromBuffer(BufferedReader br) throws Exception{
            StringBuffer buffer = new StringBuffer();

            String line;
            while((line = br.readLine()) != null){
                buffer.append(line + '\n');
            }

            if (buffer.length() == 0)
                return null;

            return buffer;
        }
    }
    // Populates the toolbar with menu items
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent(res);
        return super.onCreateOptionsMenu(menu);
    }
    // Method used to pass the string to be used with the share menu item
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }
}

























