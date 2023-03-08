package com.example.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class ScoreActivity extends AppCompatActivity {
    private static final String EXTRA_RESULT = "result";
    private TextView textViewResult;
    private Button buttonRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int record = preferences.getInt("record", 0);
        textViewResult = findViewById(R.id.textViewResult);
        buttonRestart = findViewById(R.id.buttonRestartGame);
        int result = getIntent().getIntExtra(EXTRA_RESULT, 0);
        String formatResult = String.format(Locale.getDefault(),
                "Your result: %d\nThe best result is: %d", result, record);
        textViewResult.setText(formatResult);

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.newIntent(ScoreActivity.this));
                finish();
            }
        });
    }

    public static Intent newIntent(Context context, int result) {
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra(EXTRA_RESULT, result);
        return intent;
    }
}