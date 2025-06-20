package com.qppd.carmonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qppd.carmonitoring.Classes.Device;
import com.qppd.carmonitoring.Classes.Timer;
import com.qppd.carmonitoring.Libs.Firebasez.FirebaseRTDBHelper;
import com.qppd.carmonitoring.Libs.Functionz.FunctionClass;

import java.util.HashMap;
import java.util.Map;

public class SleepActivity extends AppCompatActivity implements View.OnClickListener {

    private FunctionClass functionClass = new FunctionClass(this);

    private FirebaseRTDBHelper<Device> deviceFirebaseRTDBHelper = new FirebaseRTDBHelper<>("carmonitoring");
    private FirebaseRTDBHelper<Timer> timerFirebaseRTDBHelper = new FirebaseRTDBHelper<>("carmonitoring");

    private ImageButton btnBack;
    private RadioGroup radioGroup;
    private Button btnStart;
    private DonutProgress donutProgress;

    private DatabaseReference databaseReference;
    private long selectedTimeInMillis = 0;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        functionClass.setActionbar(getSupportActionBar(), 0, "", 0);

        // Initialize Views
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        radioGroup = findViewById(R.id.radioGroup);
        btnStart = findViewById(R.id.btnStart);
        donutProgress = findViewById(R.id.dntProducts);

        // Initialize Firebase
        deviceFirebaseRTDBHelper.getRef().child("device/sleep_duration");

        // Load previously selected time from Firebase
        loadSelectedTime();
        loadUnfinishedTimer();

        // RadioGroup selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_5:
                    selectedTimeInMillis = 5 * 60 * 1000;
                    break;
                case R.id.radio_10:
                    selectedTimeInMillis = 10 * 60 * 1000;
                    break;
                case R.id.radio_15:
                    selectedTimeInMillis = 15 * 60 * 1000;
                    break;
                case R.id.radio_30:
                    selectedTimeInMillis = 30 * 60 * 1000;
                    break;
                case R.id.radio_60:
                    selectedTimeInMillis = 60 * 60 * 1000;
                    break;
            }
            saveSelectedTime();
        });

        // Start Button click
        btnStart.setOnClickListener(v -> {
            if (selectedTimeInMillis > 0) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("device_status", 2);

                // Use the FirebaseHelper instance to update the contact
                deviceFirebaseRTDBHelper.update("device", updates, new FirebaseRTDBHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });


                startTimer(selectedTimeInMillis);
                btnStart.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(this, "Please select a time first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUnfinishedTimer() {
        timerFirebaseRTDBHelper.getRef().child("timer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Timer timer = snapshot.getValue(Timer.class);
                    if(timer.getStatus() == 1){
                        selectedTimeInMillis = timer.getTime() * 1000;
                        startTimer(selectedTimeInMillis);
                        btnStart.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                this.finish();
                break;
        }
    }

    private void startTimer(long timeInMillis) {
        if (isTimerRunning && countDownTimer != null) {
            countDownTimer.cancel();
        }

        donutProgress.setMax(100);

        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isTimerRunning = true;

                int progress = (int) (millisUntilFinished * 100 / timeInMillis);
                donutProgress.setProgress(progress);

                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int totalSeconds = minutes * 60 + seconds;

                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                donutProgress.setText(timeFormatted);

                Timer timer = new Timer(totalSeconds, 1);

                timerFirebaseRTDBHelper.save("timer", timer, new FirebaseRTDBHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                donutProgress.setProgress(0);
                donutProgress.setText("Done");
                btnStart.setVisibility(View.VISIBLE);
                loadSelectedTime();

                Timer timer = new Timer((int)selectedTimeInMillis / 1000, 0);

                timerFirebaseRTDBHelper.save("timer", timer, new FirebaseRTDBHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

                Map<String, Object> updates = new HashMap<>();
                updates.put("device_status", 1);

                // Use the FirebaseHelper instance to update the contact
                deviceFirebaseRTDBHelper.update("device", updates, new FirebaseRTDBHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

                Toast.makeText(SleepActivity.this, "Device is waking up!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void saveSelectedTime() {

        deviceFirebaseRTDBHelper.getRef().child("device/sleep_duration").setValue(selectedTimeInMillis);
    }

    private void loadSelectedTime() {
        deviceFirebaseRTDBHelper.getRef().child("device/sleep_duration").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    selectedTimeInMillis = dataSnapshot.getValue(Long.class);
                    setRadioButtonChecked(selectedTimeInMillis);
                }
            }
        });
    }

    private void setRadioButtonChecked(long millis) {
        if (millis == 5 * 60 * 1000) {
            radioGroup.check(R.id.radio_5);
        } else if (millis == 10 * 60 * 1000) {
            radioGroup.check(R.id.radio_10);
        } else if (millis == 15 * 60 * 1000) {
            radioGroup.check(R.id.radio_15);
        } else if (millis == 30 * 60 * 1000) {
            radioGroup.check(R.id.radio_30);
        } else if (millis == 60 * 60 * 1000) {
            radioGroup.check(R.id.radio_60);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
