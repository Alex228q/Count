package com.example.sharedpreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewOpinion0;
    private TextView textViewOpinion1;
    private TextView textViewOpinion2;
    private TextView textViewOpinion3;
    private TextView textViewScore;
    private TextView textViewTimer;
    private TextView textViewQuestion;

    private String question;
    private int correctAnswer;
    private int correctAnswerPosition;
    private boolean isPositive;
    private final int min = 1;
    private final int max = 100;

    private int countOfCorrectAnswers = 0;
    private boolean gameOver = false;

    private final ArrayList<TextView> options = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        options.add(textViewOpinion0);
        options.add(textViewOpinion1);
        options.add(textViewOpinion2);
        options.add(textViewOpinion3);
        playNext();

        for (TextView option : options) {
            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!gameOver) {
                        TextView textView = (TextView) v;
                        int answer = Integer.parseInt(textView.getText().toString());
                        if (answer == correctAnswer) {
                            countOfCorrectAnswers++;
                            playNext();
                        } else {
                            playNext();
                        }
                    }
                }
            });
        }
        CountDownTimer timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(getTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                gameOver = true;
                SharedPreferences preferences =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("record", 0);
                if (countOfCorrectAnswers >= max) {
                    preferences.edit().putInt("record", countOfCorrectAnswers).apply();
                }
                startActivity(ScoreActivity.newIntent(MainActivity.this, countOfCorrectAnswers));
                finish();
            }
        };
        timer.start();
    }

    private void initViews() {
        textViewOpinion0 = findViewById(R.id.textView1);
        textViewOpinion1 = findViewById(R.id.textView2);
        textViewOpinion2 = findViewById(R.id.textView3);
        textViewOpinion3 = findViewById(R.id.textView4);
        textViewScore = findViewById(R.id.textViewScore);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewQuestion = findViewById(R.id.textViewQuestion);
    }

    private void generateQuestion() {
        int a = (int) (Math.random() * (max - min + 1) + min);
        int b = (int) (Math.random() * (max - min + 1) + min);
        int mark = (int) (Math.random() * 2);
        isPositive = mark == 1;

        if (isPositive) {
            correctAnswer = a + b;
            question = String.format("%s + %s", a, b);
        } else {
            correctAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        textViewQuestion.setText(question);
        correctAnswerPosition = (int) (Math.random() * 4);
    }

    private int generateWrongAnswer() {
        int result;
        do {
            result = (int) (Math.random() * max * 2 + 1) - (max - min);
        } while (result == correctAnswer);
        return result;
    }

    private void playNext() {

        generateQuestion();
        for (int i = 0; i < options.size(); i++) {
            if (i == correctAnswerPosition) {
                options.get(i).setText(Integer.toString(correctAnswer));
            } else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String result = String.format("%s", countOfCorrectAnswers);
        textViewScore.setText(result);
    }

    private String getTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}