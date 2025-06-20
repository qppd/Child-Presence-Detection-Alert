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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.qppd.carmonitoring.Classes.User;
import com.qppd.carmonitoring.Libs.AutoTimez.AutotimeClass;
import com.qppd.carmonitoring.Libs.Firebasez.FirebaseAuthHelper;
import com.qppd.carmonitoring.Libs.Firebasez.FirebaseDatabaseHelper;
import com.qppd.carmonitoring.Libs.Firebasez.FirebaseRTDBHelper;
import com.qppd.carmonitoring.Libs.Functionz.FunctionClass;
import com.qppd.carmonitoring.Libs.IntentManager.IntentManagerClass;
import com.qppd.carmonitoring.Libs.Permissionz.PermissionClass;
import com.qppd.carmonitoring.Libs.Validatorz.ValidatorClass;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseRTDBHelper<User> userFirebaseDatabaseHelper = new FirebaseRTDBHelper<>("carmonitoring");

    private FunctionClass function = new FunctionClass(this);
    private AutotimeClass autotime = new AutotimeClass(this);
    private PermissionClass permission = new PermissionClass(this, this);

    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.INTERNET,
            android.Manifest.permission.CALL_PHONE, android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private EditText edtName;
    private EditText edtDeviceId;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

    private Button btnSignUp;
    private TextView txtSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        function.setActionbar(getSupportActionBar(), 0, "", 0);

        autotime.checkAutotime();

        if (!permission.hasPermissions()) {
            permission.requestPermissions(PERMISSION_ALL);
        }

        initializeComponents();

    }

    private void attempSignUp() {

        edtName.setError(null);
        edtDeviceId.setError(null);
        edtPassword.setError(null);
        edtConfirmPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        String signup_name = edtName.getText().toString();
        String signup_device_id = edtDeviceId.getText().toString();
        String signup_password = edtPassword.getText().toString();
        String signup_confirm_password = edtConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(signup_name)) {
            edtName.setError("Name is empty!");
            focusView = edtName;
            cancel = true;
        } else if (!ValidatorClass.validateLetterOnly(signup_name)) {
            edtName.setError("Invalid Name!");
            focusView = edtName;
            cancel = true;
        }

        if (TextUtils.isEmpty(signup_device_id)) {
            edtDeviceId.setError("Device ID is empty!");
            focusView = edtDeviceId;
            cancel = true;
        } else if (!ValidatorClass.validateNumberOnly(signup_device_id)) {
            edtDeviceId.setError("Invalid Device ID!");
            focusView = edtDeviceId;
            cancel = true;
        }

        if (TextUtils.isEmpty(signup_password)) {
            edtPassword.setError("Password is empty!");
            focusView = edtPassword;
            cancel = true;
        } else if (!ValidatorClass.validatePasswordNoSymbolOnly(signup_password)) {
            edtPassword.setError("Invalid Password!");
            focusView = edtPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(signup_confirm_password)) {
            edtConfirmPassword.setError("Confirm Password is empty!");
            focusView = edtConfirmPassword;
            cancel = true;
        } else if (!ValidatorClass.validatePasswordNoSymbolOnly(signup_confirm_password)) {
            edtConfirmPassword.setError("Invalid Password!");
            focusView = edtConfirmPassword;
            cancel = true;
        } else if (!signup_confirm_password.equals(signup_password)) {
            edtConfirmPassword.setError("Password do not match!");
            focusView = edtConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            User user = new User();
            user.setName(signup_name);
            user.setDevice_id(signup_device_id);
            user.setPassword(signup_password);
            user.setStatus(1);

            userFirebaseDatabaseHelper.save("user" + "/" + signup_device_id, user, new FirebaseRTDBHelper.DatabaseCallback() {
                @Override
                public void onSuccess() {
                    function.showMessage("Registration successful!");
                    RegisterActivity.this.finish();
                }

                @Override
                public void onFailure(Exception e) {
                    function.showMessage("Registration failed!");
                }
            });
        }
    }

    void initializeComponents() {

        edtName = findViewById(R.id.edtName);
        edtDeviceId = findViewById(R.id.edtDeviceId);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        txtSignIn = findViewById(R.id.txtSignIn);
        txtSignIn.setOnClickListener(this);

        SpannableString spanstring = new SpannableString(txtSignIn.getText().toString());
        ClickableSpan clickspn = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
            }


        };
        spanstring.setSpan(clickspn, 20, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSignIn.setText(spanstring);
        txtSignIn.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                attempSignUp();
                //IntentManagerClass.intentsify(this, DashboardActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.txtSignIn:
                this.finish();
                break;
        }
    }
}