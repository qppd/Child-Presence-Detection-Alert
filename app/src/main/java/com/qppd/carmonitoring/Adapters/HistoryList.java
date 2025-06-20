package com.qppd.carmonitoring.Adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.qppd.carmonitoring.Classes.History;
import com.qppd.carmonitoring.R;

import java.util.List;

public class HistoryList extends ArrayAdapter<History> {

    private Activity context;
    private List<History> historyList;
    private List<String> historyKeys;

    public HistoryList(Activity context, List<History> historyList) {
        super(context, R.layout.activity_history, historyList);
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_alert_history, null, true);

        History history = historyList.get(position);

        TextView txtDate = (TextView) listViewItem.findViewById(R.id.txtDate);
        TextView txtTime = (TextView) listViewItem.findViewById(R.id.txtTime);

        txtDate.setText(history.getDate());
        txtTime.setText(history.getDeactivated_on());

        return listViewItem;

    }

}
