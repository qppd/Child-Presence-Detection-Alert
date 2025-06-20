//package com.qppd.eggincubator;
//
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.qppd.eggincubator.Classes.Token;
//
//import java.util.Map;
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    @Override
//    public void onNewToken(String token) {
//
//        // Log or save the token as needed
//        Log.d("FIREBASE TOKEN", token);
//        //Toast.makeText(MyFirebaseMessagingService.this, tokesn, Toast.LENGTH_LONG).show();
//
//        Token token1 = new Token(token);
//        FirebaseDatabase.getInstance().getReference().child("incubator/tokens").push().setValue(token1, (error, ref) -> {
//            if (error != null) {
//                Log.d("Resetting Incubator failed", error.getMessage(), null);
//            } else {
//            }
//        });
//
//    }
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.d("FROM", "From: " + remoteMessage.getFrom());
//
//        if (remoteMessage.getData().size() > 0) {
//
//
//            // Process the received data, e.g., show a notification
//            showNotification(remoteMessage.getData());
//        }
//
//        if (remoteMessage.getNotification() != null) {
//            Log.d("BODY", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        super.onMessageReceived(remoteMessage);
//    }
//
//    private void showNotification(Map<String, String> data) {
//        // Extract data from the payload and show a notification
//        String title = data.get("title");
//        String message = data.get("message");
//        Log.d("PAYLOAD", "Message data payload title: " + data.get("title"));
//        Log.d("PAYLOAD", "Message data payload message: " + data.get("message"));
//        // You can customize the notification as needed
//        // For simplicity, a basic notification is shown here
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
//                .setSmallIcon(R.drawable.applogo)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // NotificationId is a unique int for each notification that you must define
//        int notificationId = 1;
//        notificationManager.notify(notificationId, builder.build());
//    }
//}

package com.qppd.alger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.qppd.alger.Classes.Token;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        // Log or save the token as needed
        Log.d("FIREBASE TOKEN", token);

        Token token1 = new Token(token);
        FirebaseDatabase.getInstance().getReference().child("incubator/tokens").push().setValue(token1, (error, ref) -> {
            if (error != null) {
                Log.d("Resetting Incubator failed", error.getMessage(), null);
            }
        });
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FROM", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            // Process the received data, e.g., show a notification
            showNotification(remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("BODY", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        super.onMessageReceived(remoteMessage);
    }

    private void showNotification(Map<String, String> data) {
        // Create the Notification Channel (usually in your application initialization)
        createNotificationChannel();

        // Extract data from the payload and show a notification
        String title = data.get("title");
        String message = data.get("message");
        Log.d("PAYLOAD", "Message data payload title: " + title);
        Log.d("PAYLOAD", "Message data payload message: " + message);

        // Example PendingIntent (replace with your actual intent)
        Intent intent = new Intent(this, MonitorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Create a PendingIntent from the Intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // You can customize the notification as needed
        // For simplicity, a basic notification is shown here
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)  // Automatically dismiss the notification when tapped
                .setContentIntent(pendingIntent); // Set the PendingIntent

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // NotificationId is a unique int for each notification that you must define
        int notificationId = (int) System.currentTimeMillis(); // Using current time as a unique ID
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        // Create the Notification Channel (for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

