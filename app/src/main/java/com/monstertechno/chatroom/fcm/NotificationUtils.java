package com.monstertechno.chatroom.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.monstertechno.chatroom.MainActivity;
import com.monstertechno.chatroom.R;

import java.util.HashMap;
import java.util.Map;

public class NotificationUtils {

    private static final String CHANEL_ID = "myChannel";
    private  static  final  String CHANNEL_NAME = "myChannelName";
    private static final int NOTIFICATION_ID = 200;
    private Context context;
    Map<String,Class> activityMap = new HashMap<>();

    public NotificationUtils(Context context) {
        this.context = context;
        activityMap.put("MainActivity", MainActivity.class);
    }

    public void displayNotification(NotificationVO notificationVO, Intent resultIntent) {
        String message  = notificationVO.getMessage();
        String title = notificationVO.getTitle();
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANEL_ID);
        Notification notification = mBuilder.setAutoCancel(true)
                .setTicker(title)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(NOTIFICATION_ID,notification);

    }

    public void playNotificationSound() {
    }
}
