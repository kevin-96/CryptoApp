package edu.quinnipiac.ser210.cryptousd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String example = getIntent().getStringExtra("coinValue");
        TextView result = (TextView) findViewById(R.id.resultDisplay);
        result.setText(example);
    }
}
