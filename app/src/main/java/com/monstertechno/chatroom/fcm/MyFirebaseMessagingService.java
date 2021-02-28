package com.monstertechno.chatroom.fcm;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.monstertechno.chatroom.MainActivity;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size()>0){
            Map<String,String> data  = remoteMessage.getData();
            handleData(data);
        }else  if(remoteMessage.getNotification()!=null){
            handleNotification(remoteMessage.getNotification());
        }
    }

    private void handleNotification(RemoteMessage.Notification notification) {
        String message  = notification.getBody();
        String title = notification.getTitle();
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO,resultIntent);
        notificationUtils.playNotificationSound();
    }

    private void handleData(Map<String, String> data) {
        String title  = data.get("title");
        String message = data.get("message");
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent  = new Intent(getApplicationContext(),MainActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO,resultIntent);
        notificationUtils.playNotificationSound();
    }
}
