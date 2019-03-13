package edu.quinnipiac.ser210.cryptousd;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] coins = new String[] {"BTC", "ETH", "XRP", "BCH", "LTC",};
    private String[] currency = new String[] {"USD", "EUR"};
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    String coinValue;
    String currencyValue, res;


    Handler handler = new Handler();

    boolean userSelect = false;
    private String url1 = "https://bravenewcoin-v1.p.rapidapi.com/ticker?show="; // after goes price
    private String url2 = "&coin=";// after goes coin


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_copy:
                Toast.makeText(this, "Value Copied to ClipBoard", Toast.LENGTH_SHORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner coinSpinner = (Spinner)findViewById(R.id.coinSpinner);
        final Spinner currencySpinner = (Spinner)findViewById(R.id.currencySpinner);
        Button priceButton = (Button)findViewById(R.id.getPriceButton);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter<String> coinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, coins);
        coinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coinSpinner.setAdapter(coinAdapter);

        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currency);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);

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
          //  Log.d("result", res);
    }

    private class FetchCoinValue extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection =null;
            BufferedReader reader =null;
            String coinValue = null;

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

                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("coinValue",result);

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

}

























