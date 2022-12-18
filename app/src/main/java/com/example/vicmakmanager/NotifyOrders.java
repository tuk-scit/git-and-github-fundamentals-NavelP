package com.example.vicmakmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class NotifyOrders extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";
    ArrayList<String> b;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String notificationTitle = null, notificationBody = null;

        // Check if message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "empty empty");
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        }

        // If you want to fire a local notification (that notification on the top of the phone screen)
        // you should fire it from here

        sendNotification(notificationTitle, notificationBody);

    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    getString(R.string.VICMAK_ORDERS_ID), getString(R.string.VICMAK_ORDERS), NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationChannel.setDescription("Someone has ordered an item from your shop");
            notificationChannel.setShowBadge(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void sendNotification(String title, String body){
        createNotificationChannel();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.VICMAK_ORDERS_ID))
                .setSmallIcon(R.drawable.vicmakicon)
                .setContentTitle("Vicmak order")
                .setContentText(title)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vicmakicon))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setChannelId(getString(R.string.VICMAK_ORDERS_ID));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(getResources().getInteger(R.integer.notificationId), builder.build());
    }
}
