package com.qppd.carmonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qppd.carmonitoring.Classes.User;
import com.qppd.carmonitoring.Libs.AutoTimez.AutotimeClass;
import com.qppd.carmonitoring.Libs.Firebasez.FirebaseAuthHelper;
import com.qppd.carmonitoring.Libs.Firebasez.FirebaseRTDBHelper;
import com.qppd.carmonitoring.Libs.Functionz.FunctionClass;
import com.qppd.carmonitoring.Libs.IntentManager.IntentManagerClass;
import com.qppd.carmonitoring.Libs.Permissionz.PermissionClass;
import com.qppd.carmonitoring.Libs.Validatorz.ValidatorClass;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseRTDBHelper<User> userFirebaseDatabaseHelper = new FirebaseRTDBHelper<>("carmonitoring");

    private FunctionClass function = new FunctionClass(this);
    private AutotimeClass autotime = new AutotimeClass(this);
    private PermissionClass permission = new PermissionClass(this, this);

    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.INTERNET,
            android.Manifest.permission.CALL_PHONE, android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private EditText edtDeviceId;
    private EditText edtPassword;

    private TextView txtSignUp;


    private Button btnSignIn;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        function.setActionbar(getSupportActionBar(), 0, "", 0);

        autotime.checkAutotime();

        // Get the current time
        //        Calendar calendar = Calendar.getInstance();
        //        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //        String currentDate = dateFormat.format(calendar.getTime());
        //
        // Save the current date to Firebase Realtime Database
        //        firebaseDatase.getReference().child("datas/time").setValue(currentDate);

        if (!permission.hasPermissions()) {
            permission.requestPermissions(PERMISSION_ALL);
        }

        initializeComponents();
    }

    private void attemptSignin() {
        // Reset errors.
        edtDeviceId.setError(null);
        edtPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        String signin_device_id = edtDeviceId.getText().toString();
        String signin_password = edtPassword.getText().toString();


        if (TextUtils.isEmpty(signin_device_id)) {
            edtDeviceId.setError("Device ID is empty!");
            focusView = edtDeviceId;
            cancel = true;
        } else if (!ValidatorClass.validateNumberOnly(signin_device_id)) {
            edtDeviceId.setError("Invalid Device ID!");
            focusView = edtDeviceId;
            cancel = true;
        }

        if (TextUtils.isEmpty(signin_password)) {
            edtPassword.setError("Password is empty!");
            focusView = edtPassword;
            cancel = true;
        } else if (!ValidatorClass.validatePasswordNoSymbolOnly(signin_password)) {
            edtPassword.setError("Invalid Password!");
            focusView = edtPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the
            // field with an error.
            focusView.requestFocus();
        } else {

            userFirebaseDatabaseHelper.getRef().child("user/" + signin_device_id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);

                                if(user.getPassword().equals(signin_password)){
                                    IntentManagerClass.intentsify(LoginActivity.this, DashboardActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }else{
                                    function.showMessage("Invalid Device ID or password!");
                                }

                            } else {
                                function.showMessage("Invalid Device ID or password!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }

    void initializeComponents() {
        edtDeviceId = findViewById(R.id.edtDeviceId);
        edtDeviceId.setText("");
        edtPassword = findViewById(R.id.edtPassword);
        edtPassword.setText("");
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);

        txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);

        SpannableString spanstring = new SpannableString(txtSignUp.getText().toString());
        ClickableSpan clickspn = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }


        };
        spanstring.setSpan(clickspn, 16, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSignUp.setText(spanstring);
        txtSignUp.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                attemptSignin();
                //IntentManagerClass.intentsify(this, DashboardActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.txtSignUp:
                IntentManagerClass.intentsify(this, RegisterActivity.class);
                break;
        }
    }
}