package com.qppd.carmonitoring;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.qppd.carmonitoring.Libs.Functionz.FunctionClass;

public class HotlineActivity extends AppCompatActivity implements View.OnClickListener {

    private String phoneNumberToCall = "";

    private FunctionClass functionClass = new FunctionClass(this);

    private LinearLayout lnrPnp;
    private LinearLayout lnrBfp;
    private LinearLayout lnrDotr;

    private ImageButton btnBack;

    // Define the launcher for requesting permission
    private final ActivityResultLauncher<String> callPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCall(); // Permission granted
                } else {
                    functionClass.showMessage("Permission denied to make calls.");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotline);

        functionClass.setActionbar(getSupportActionBar(), 0, "", 0);

        initializeComponents();
    }

    private void initializeComponents() {

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        lnrPnp = findViewById(R.id.lnrPnp);
        lnrPnp.setOnClickListener(this);

        lnrBfp = findViewById(R.id.lnrBfp);
        lnrBfp.setOnClickListener(this);

        lnrDotr = findViewById(R.id.lnrDotr);
        lnrDotr.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                this.finish();
                break;
            case R.id.lnrPnp:
                phoneNumberToCall = "911";
                makePhoneCall();
                break;
            case R.id.lnrBfp:
                phoneNumberToCall = "911";
                makePhoneCall();
                break;
            case R.id.lnrDotr:
                phoneNumberToCall = "09634905586";
                makePhoneCall();
                break;
        }
    }

    private void makePhoneCall() {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Check if user has permanently denied the permission
            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                // Show permission request again
                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
            } else {
                // User selected "Don't ask again" OR first time request
                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
            }
        } else {
            // Already granted
            startCall();
        }
    }


    private void startCall() {
        if (!phoneNumberToCall.isEmpty()) {
            String dial = "tel:" + phoneNumberToCall;
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(dial));
            startActivity(intent);
        } else {
            functionClass.showMessage("Phone number is empty.");
        }
    }
}
