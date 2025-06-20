package com.qppd.carmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.qppd.carmonitoring.Libs.Functionz.FunctionClass;

public class MainActivity extends AppCompatActivity {

    private FunctionClass functionClass = new FunctionClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        functionClass.setActionbar(getSupportActionBar(), 0, "", 0);

        // Delay for 3 seconds before transitioning to another activity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Optional: Closes the current activity so it doesn't stay in the back stack
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}
