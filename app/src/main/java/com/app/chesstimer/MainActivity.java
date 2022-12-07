package com.app.chesstimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.sax.StartElementListener;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 600000;
    private TextView mTextViewCountDown, mTextViewCountDown2;
    //private Button mButtonStartPause,mButtonStartPause2;
    //private Button mButtonReset,mButtonReset2;
    private CountDownTimer mCountDownTimer, mCountDownTimer2;
    private boolean mTimerRunning, mTimerRunning2, mEditing = false;
    long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    long mTimeLeftInMillis2 = START_TIME_IN_MILLIS;
    private long prevTimer1 = START_TIME_IN_MILLIS, prevTimer2 = START_TIME_IN_MILLIS;
    private ImageButton mButtonReset, mButtonPause;
    private ImageButton mButtonEdit, mButtonDone;
    private EditText mEditText1, mEditText2;


    @Override
    protected void onResume() {
        super.onResume();
        updateCountDownText();
        updateCountDownText2();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mTextViewCountDown2 = findViewById(R.id.text_view_countdown2);
        mButtonReset = findViewById(R.id.button_reset);
        mButtonPause = findViewById(R.id.button_pause);
        mButtonEdit = findViewById(R.id.button_edit);
        mButtonDone = findViewById(R.id.button_done);
        mEditText1 = findViewById(R.id.text_edit_countdown);
        mEditText2 = findViewById(R.id.text_edit_countdown2);

        mButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                pauseTimer2();
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
                resetTimer2();
            }
        });
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTimer();
                updateCountDownText();
                updateCountDownText2();

            }
        });
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDone();
                updateCountDownText();
                updateCountDownText2();

            }
        });


        mTextViewCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToTwo();
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.tick2);
                mp.start();
            }
        });
        mTextViewCountDown2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToOne();
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.tick2);
                mp.start();
            }
        });

        updateCountDownText();
        updateCountDownText2();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {0, 500, 20, 500, 100, 500, 20, 500, 100, 500, 20, 500, 100};
                v.vibrate(pattern, -1);
                mTextViewCountDown.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.color3));
            }
        }.start();
        mTimerRunning = true;
        mTextViewCountDown.setBackgroundColor(ContextCompat.getColor(this, R.color.color2));
    }

    private void startTimer2() {
        mCountDownTimer2 = new CountDownTimer(mTimeLeftInMillis2, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis2 = millisUntilFinished;
                updateCountDownText2();
            }

            @Override
            public void onFinish() {
                mTimerRunning2 = false;
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {0, 500, 20, 500, 100, 500, 20, 500, 100, 500, 20, 500, 100};
                v.vibrate(pattern, -1);
                mTextViewCountDown.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.color3));
            }
        }.start();
        mTimerRunning2 = true;
        mTextViewCountDown2.setBackgroundColor(ContextCompat.getColor(this, R.color.color2));
    }

    private void pauseTimer() {
        if (mTimerRunning) {
            mCountDownTimer.cancel();
            mTimerRunning = false;
        }
        mTextViewCountDown.setBackgroundColor(ContextCompat.getColor(this, R.color.color3));
        updateCountDownText();
    }

    private void pauseTimer2() {
        if (mTimerRunning2) {
            mCountDownTimer2.cancel();
            mTimerRunning2 = false;
        }
        mTextViewCountDown2.setBackgroundColor(ContextCompat.getColor(this, R.color.color3));
        updateCountDownText2();

    }

    private void resetTimer() {
        if (mTimerRunning) {
            pauseTimer();
        }
        mTimeLeftInMillis = prevTimer1;
        updateCountDownText();
        //mButtonReset.setVisibility(View.INVISIBLE);
        //mButtonStartPause.setVisibility(View.VISIBLE);
    }

    private void resetTimer2() {
        if (mTimerRunning2) {
            pauseTimer2();
        }
        mTimeLeftInMillis2 = prevTimer2;
        updateCountDownText2();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateCountDownText2() {
        int minutes = (int) (mTimeLeftInMillis2 / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis2 / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown2.setText(timeLeftFormatted);
    }

    private void switchToOne() {
        if (!mTimerRunning) {
            startTimer();
            pauseTimer2();
        }
    }

    private void switchToTwo() {
        if (!mTimerRunning2) {
            startTimer2();
            pauseTimer();
        }
    }

    private void editDone() {
        String time1_str = mEditText1.getText().toString().trim();
        String time2_str = mEditText2.getText().toString().trim();
        if (time1_str.equals("") || time2_str.equals("")) {
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            mTimeLeftInMillis2 = START_TIME_IN_MILLIS;
            prevTimer1 = mTimeLeftInMillis;
            prevTimer2 = mTimeLeftInMillis2;
        } else {
            if (mTimeLeftInMillis / 1000 != stringToMilli(time1_str) / 1000) {
                mTimeLeftInMillis = stringToMilli(time1_str);
                prevTimer1 = mTimeLeftInMillis;
            }
            if (mTimeLeftInMillis2 / 1000 != stringToMilli(time2_str) / 1000) {
                mTimeLeftInMillis2 = stringToMilli(time2_str);
                prevTimer2 = mTimeLeftInMillis2;
            }
        }
        mEditText1.setVisibility(View.INVISIBLE);
        mEditText2.setVisibility(View.INVISIBLE);
        mTextViewCountDown.setVisibility(View.VISIBLE);
        mTextViewCountDown2.setVisibility(View.VISIBLE);
        updateCountDownText();
        updateCountDownText2();
        mButtonDone.setVisibility(View.INVISIBLE);
        mButtonEdit.setVisibility(View.VISIBLE);
    }

    private void editTimer() {
        printToScreen("Tap on a timer to edit");
        pauseTimer();
        pauseTimer2();
        mEditText1.setVisibility(View.VISIBLE);
        mEditText2.setVisibility(View.VISIBLE);
        mTextViewCountDown.setVisibility(View.INVISIBLE);
        mTextViewCountDown2.setVisibility(View.INVISIBLE);
        mEditing = !mEditing;
        mButtonDone.setVisibility(View.VISIBLE);
        mButtonEdit.setVisibility(View.INVISIBLE);
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mEditText1.setText(timeLeftFormatted);
        minutes = (int) (mTimeLeftInMillis2 / 1000) / 60;
        seconds = (int) (mTimeLeftInMillis2 / 1000) % 60;
        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mEditText2.setText(timeLeftFormatted);


    }

    private int stringToMilli(String time_str) {
        int index = time_str.indexOf(":");
        int min = Integer.valueOf(time_str.substring(0, index));
        int sec = Integer.valueOf(time_str.substring(index + 1));
        return (min * 60000 + sec * 1000);
    }

    private void printToScreen(String str) {
        Toast toast = Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG);
        //TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
        /*if( v1 != null) {v1.setGravity(Gravity.CENTER);

         */
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
}
