package com.qppd.carmonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.qppd.carmonitoring.Adapters.HistoryList;
import com.qppd.carmonitoring.Classes.History;
import com.qppd.carmonitoring.Globals.HistoryGlobal;
import com.qppd.carmonitoring.Libs.Firebasez.FirebaseRTDBHelper;
import com.qppd.carmonitoring.Libs.Functionz.FunctionClass;
import com.qppd.carmonitoring.Libs.Imagez.ImageBase64;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private FunctionClass functionClass = new FunctionClass(this);
    private ImageBase64 imageBase64 = new ImageBase64(100);
    private FirebaseRTDBHelper<History> historyFirebaseRTDBHelper = new FirebaseRTDBHelper<History>("carmonitoring");

    private ImageButton btnBack;

    private ListView listViewHistory;

    private String history_key;
    private ArrayList<String> history_keys;
    private ArrayList<History> history_list;

    private HistoryList historyAdapter;
    private History history;

    private AlertDialog.Builder dialogHistory;
    private View dialogHistoryView;
    private AlertDialog alertHistoryDialog;

    private ImageButton imbHide;
    private ImageView imgSnapshot;
    private TextView txtDate;
    private TextView txtActivatedOn;
    private TextView txtDeactivatedOn;
    private TextView txtDuration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        functionClass.setActionbar(getSupportActionBar(), 0, "", 0);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        listViewHistory = findViewById(R.id.listViewHistory);
        registerForContextMenu(listViewHistory);
        loadHistory();
        buildHistoryViewDialog();


    }

    private void loadHistory() {

        historyFirebaseRTDBHelper.getRef().child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                history_list = new ArrayList<>();
                history_keys = new ArrayList<>();

                for (DataSnapshot historySnapshot : snapshot.getChildren()) {
                    History history = historySnapshot.getValue(History.class);

                    history_list.add(history);
                    history_keys.add(historySnapshot.getKey());
                }

                // Sort the list in ascending order based on getDeactivatedOn()
                Collections.sort(history_list, new Comparator<History>() {
                    @Override
                    public int compare(History h1, History h2) {
                        return h2.getDate().compareTo(h1.getDate());
                    }
                });

                historyAdapter = new HistoryList(HistoryActivity.this, history_list);
                listViewHistory.setAdapter(historyAdapter);

                listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        history_key = history_keys.get(i);
                        history = history_list.get(i);

                        HistoryGlobal.setHistory_key(history_key);
                        HistoryGlobal.setHistory(history);

                        showHistoryDialog();
                    }
                });

                listViewHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        history_key = history_keys.get(i);
                        history = history_list.get(i);

                        HistoryGlobal.setHistory_key(history_key);
                        HistoryGlobal.setHistory(history);
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                functionClass.showMessage(error.getMessage());
            }
        });


    }

    private void buildHistoryViewDialog() {
        dialogHistory = new AlertDialog.Builder(this);
        dialogHistoryView = getLayoutInflater().inflate(R.layout.dialog_history_view, null);

        imbHide = dialogHistoryView.findViewById(R.id.imbHide);
        imbHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertHistoryDialog.dismiss();
            }
        });
        imgSnapshot = dialogHistoryView.findViewById(R.id.imgSnapshot);
        txtDate = dialogHistoryView.findViewById(R.id.txtDate);
        txtActivatedOn = dialogHistoryView.findViewById(R.id.txtActivatedOn);
        txtDeactivatedOn = dialogHistoryView.findViewById(R.id.txtDeactivatedOn);
        txtDuration = dialogHistoryView.findViewById(R.id.txtDuration);

        //dialogModifyProduct.setCancelable(false);

        dialogHistory.setView(dialogHistoryView);
        alertHistoryDialog = dialogHistory.create();
    }



    private void showHistoryDialog(){
        buildHistoryViewDialog();
        alertHistoryDialog.show();
        imgSnapshot.setImageBitmap(imageBase64.deCode(HistoryGlobal.getHistory().getSnapshot()));
        txtDate.setText(HistoryGlobal.getHistory().getActivated_on());
        txtActivatedOn.setText(HistoryGlobal.getHistory().getActivated_on());
        txtDeactivatedOn.setText(HistoryGlobal.getHistory().getDeactivated_on());
        txtDuration.setText("3 minutes");

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_history, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menuDelete:

                historyFirebaseRTDBHelper.delete("history/" + HistoryGlobal.getHistory_key(), new FirebaseRTDBHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess() {
                        functionClass.showMessage(HistoryGlobal.getHistory_key() + "");
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                break;
        }
        return false;
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