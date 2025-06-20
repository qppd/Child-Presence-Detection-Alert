package com.qppd.carmonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.qppd.carmonitoring.Classes.Camera;
import com.qppd.carmonitoring.Classes.Device;
import com.qppd.carmonitoring.Classes.Setting;
import com.qppd.carmonitoring.Classes.Sound;
import com.qppd.carmonitoring.Libs.DateTimez.DateTimeClass;
import com.qppd.carmonitoring.Libs.Firebasez.FirebaseRTDBHelper;
import com.qppd.carmonitoring.Libs.Functionz.FunctionClass;
import com.qppd.carmonitoring.Libs.IntentManager.IntentManagerClass;

import net.gotev.speech.Logger;
import net.gotev.speech.Speech;

import java.util.Date;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = DashboardActivity.class.getSimpleName();

    private FunctionClass functionClass = new FunctionClass(this);

    private FirebaseRTDBHelper<Device> deviceFirebaseRTDBHelper = new FirebaseRTDBHelper<Device>("carmonitoring");
    private FirebaseRTDBHelper<Setting> settingFirebaseRTDBHelper = new FirebaseRTDBHelper<>("carmonitoring");
    private FirebaseRTDBHelper<Camera> cameraFirebaseRTDBHelper = new FirebaseRTDBHelper<>("carmonitoring");
    private FirebaseRTDBHelper<Sound> soundFirebaseRTDBHelper = new FirebaseRTDBHelper<>("carmonitoring");


    private ImageButton imbInformation;
//    private ImageButton imbProfile;


    private DonutProgress dntDevicePercentage;
    int device_percentage;

    private TextView txtDeviceStatus;
    private int device_status;

    private CardView cardAlertRecords;
    private CardView cardSleepMode;
    private CardView cardEmergencyHotlines;
    private CardView cardSoundAlarm;

    private Button btnExit;

    private AlertDialog.Builder dialogAlert;
    private View dialogAlertView;
    private AlertDialog alertDialog;
    private TextView txtAlertDatetime;
    private Button btnOk;

    private AlertDialog.Builder dialogSoundActivateAlert;
    private View dialogSoundActivateAlertView;
    private AlertDialog dialogSoundActivateDialog;
    private Button btnActivate;

    private AlertDialog.Builder dialogSoundDeactivateAlert;
    private View dialogSoundDeactivateAlertView;
    private AlertDialog dialogSoundDeactivateDialog;
    private Button btnDeactivate;

    boolean _isSoundActive = false;

    private Handler speechHandler = new Handler(Looper.getMainLooper());
    private Runnable speechRunnable;
    private boolean keepSpeaking = false;


    private TextToSpeech.OnInitListener mTttsInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(final int status) {
            switch (status) {
                case TextToSpeech.SUCCESS:
                    Logger.info(LOG_TAG, "TextToSpeech engine successfully started");
                    break;

                case TextToSpeech.ERROR:
                    Logger.error(LOG_TAG, "Error while initializing TextToSpeech engine!");
                    break;

                default:
                    Logger.error(LOG_TAG, "Unknown TextToSpeech status: " + status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        functionClass.setActionbar(getSupportActionBar(), 0, "", 0);

        Speech.init(this, this.getPackageName(), mTttsInitListener);

        initializeComponents();
        loadDevice();
        loadDetection();
        loadSoundAlarm();
        buildAlertViewDialog();
        builddialogSoundAlarmDeactivate();
        builddialogSoundAlarmActivate();
        //alertDialog.show();

    }

    private void loadSoundAlarm() {
        soundFirebaseRTDBHelper.getRef().child("sound").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Sound sound = snapshot.getValue(Sound.class);
                    if (sound.getStatus() == 1) {
                        _isSoundActive = true;
                    } else {
                        _isSoundActive = false;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    int x = 0;

    private void loadDetection() {
        cameraFirebaseRTDBHelper.getRef().child("camera").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Camera camera = snapshot.getValue(Camera.class);

                    if (x == 0) {
                        x++;
                        return;
                    }

                    if (camera.getDetection() == 1) {
                        alertDialog.show();
                        DateTimeClass dateTimeClass = new DateTimeClass("MM/dd/yyyy hh:mm:ss a");
                        txtAlertDatetime.setText(dateTimeClass.getFormattedTime());
                        startRepeatingSpeech("Person is detected inside the car!");
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startRepeatingSpeech(String message) {
        keepSpeaking = true;

        speechRunnable = new Runnable() {
            @Override
            public void run() {
                if (keepSpeaking) {
                    Speech.getInstance().say(message);
                    speechHandler.postDelayed(this, 4000); // Repeat every 4 seconds
                }
            }
        };

        speechHandler.post(speechRunnable);
    }


    private void buildAlertViewDialog() {

        dialogAlert = new AlertDialog.Builder(this);
        dialogAlertView = getLayoutInflater().inflate(R.layout.dialog_alert_view, null);


        txtAlertDatetime = dialogAlertView.findViewById(R.id.txtAlertDatetime);
        btnOk = dialogAlertView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRepeatingSpeech();
                cameraFirebaseRTDBHelper.save("camera", new Camera(0), new FirebaseRTDBHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess() {
                        alertDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        });
        dialogAlert.setView(dialogAlertView);
        alertDialog = dialogAlert.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void builddialogSoundAlarmActivate() {

        dialogSoundActivateAlert = new AlertDialog.Builder(this);
        dialogSoundActivateAlertView = getLayoutInflater().inflate(R.layout.dialog_sound_alarm_activate, null);

        btnActivate = dialogSoundActivateAlertView.findViewById(R.id.btnActivate);
        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soundFirebaseRTDBHelper.save("sound", new Sound(1), new FirebaseRTDBHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess() {
                        dialogSoundActivateDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        });

        dialogSoundActivateAlert.setView(dialogSoundActivateAlertView);
        dialogSoundActivateDialog = dialogSoundActivateAlert.create();
        dialogSoundActivateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void builddialogSoundAlarmDeactivate() {

        dialogSoundDeactivateAlert = new AlertDialog.Builder(this);
        dialogSoundDeactivateAlertView = getLayoutInflater().inflate(R.layout.dialog_sound_alarm_deactivate, null);

        btnDeactivate = dialogSoundDeactivateAlertView.findViewById(R.id.btnDeactivate);
        btnDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soundFirebaseRTDBHelper.save("sound", new Sound(0), new FirebaseRTDBHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess() {
                        dialogSoundDeactivateDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        });

        dialogSoundDeactivateAlert.setView(dialogSoundDeactivateAlertView);
        dialogSoundDeactivateDialog = dialogSoundDeactivateAlert.create();
        dialogSoundDeactivateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void stopRepeatingSpeech() {
        keepSpeaking = false;
        if (speechHandler != null && speechRunnable != null) {
            speechHandler.removeCallbacks(speechRunnable);
        }
    }


    private void loadDevice() {
        deviceFirebaseRTDBHelper.getRef().child("device").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Device device = snapshot.getValue(Device.class);

                    device_percentage = device.getDevice_percentage();
                    dntDevicePercentage.setProgress((float) device_percentage);
                    dntDevicePercentage.setText(device_percentage + " %");

                    device_status = device.getDevice_status();

                    if (device_status == 0) {
                        txtDeviceStatus.setText("Device is off");
                        txtDeviceStatus.setBackgroundResource(R.drawable.rectangle_round_red);

                    } else if (device_status == 1) {
                        txtDeviceStatus.setText("Device is on");
                        txtDeviceStatus.setBackgroundResource(R.drawable.rectangle_round_green);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void initializeComponents() {
        imbInformation = findViewById(R.id.imbInformation);
        imbInformation.setOnClickListener(this);
//        imbProfile = findViewById(R.id.imbProfile);
//        imbProfile.setOnClickListener(this);

        dntDevicePercentage = findViewById(R.id.dntDevicePercentage);

        txtDeviceStatus = findViewById(R.id.txtDeviceStatus);
        txtDeviceStatus.setOnClickListener(this);

        cardAlertRecords = findViewById(R.id.cardAlertRecords);
        cardAlertRecords.setOnClickListener(this);

        cardSleepMode = findViewById(R.id.cardSleepMode);
        cardSleepMode.setOnClickListener(this);

        cardEmergencyHotlines = findViewById(R.id.cardEmergencyHotlines);
        cardEmergencyHotlines.setOnClickListener(this);

        cardSoundAlarm = findViewById(R.id.cardSoundAlarm);
        cardSoundAlarm.setOnClickListener(this);

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);
        //Speech.getInstance().say("Person is detected inside the car!");
    }


    private void setAlarmStatus(int status) {
        Map<String, Object> updates = null;
        updates.put("alarm_status", status);
        settingFirebaseRTDBHelper.update("", updates, new FirebaseRTDBHelper.DatabaseCallback() {
            @Override
            public void onSuccess() {

                if (status == 0) {
                    functionClass.showMessage("Alarm sound is turned off!");
                } else if (status == 1) {
                    functionClass.showMessage("Alarm sound is turned on!");
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        })
        ;
    }

    int alarm_status = 0;
    int sleep_mode = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imbInformation:
                IntentManagerClass.intentsify(DashboardActivity.this, InformationActivity.class);
                break;
//            case R.id.imbProfile:
//                IntentManagerClass.intentsify(DashboardActivity.this, InformationActivity.class);
//                break;
            case R.id.txtDeviceStatus:
                operateDevice();
                break;
            case R.id.cardAlertRecords:
                IntentManagerClass.intentsify(this, HistoryActivity.class);
                break;
            case R.id.cardSleepMode:
                IntentManagerClass.intentsify(this, SleepActivity.class);
                break;
            case R.id.cardEmergencyHotlines:
                IntentManagerClass.intentsify(this, HotlineActivity.class);
                break;
            case R.id.cardSoundAlarm:

                if (!_isSoundActive) {
                    dialogSoundActivateDialog.show();
                } else {
                    dialogSoundDeactivateDialog.show();
                }

                break;
            case R.id.btnExit:
                IntentManagerClass.intentsify(this, LoginActivity.class);
                break;
        }
    }

    private void operateDevice() {
        if (device_status == 0) {
            device_status = 1;
            txtDeviceStatus.setText("Device is off");
            txtDeviceStatus.setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, R.color.red));
            txtDeviceStatus.setBackgroundResource(R.drawable.rectangle_round_red);
        } else if (device_status == 1) {
            device_status = 0;
            txtDeviceStatus.setText("Device is on");
            txtDeviceStatus.setBackgroundColor(ContextCompat.getColor(DashboardActivity.this, R.color.green));
            txtDeviceStatus.setBackgroundResource(R.drawable.rectangle_round_green);
        }
    }

}