package com.qppd.carmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.qppd.carmonitoring.Libs.Functionz.FunctionClass;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private FunctionClass functionClass = new FunctionClass(this);

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        functionClass.setActionbar(getSupportActionBar(), 0, "", 0);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnBack:
                this.finish();
                break;
        }
    }

}