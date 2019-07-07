package com.example.sava_.wiadomosci;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;

public class Receiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String fromAddress = "";
        byte[] data = null;
        SmsMessage recMsg = null;
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                recMsg = SmsMessage.createFromPdu((byte[]) pdus[i]);
                try {
                    data = recMsg.getUserData();
                }
                catch (Exception e) {
                }
            }
            fromAddress = recMsg.getOriginatingAddress();
        }

        boolean var=MainActivity.isActivityVisible();
        showNotification(context,fromAddress);

            Intent result = new Intent(context, MainActivity.class);
            result.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(result);
    }

    private void showNotification(Context context,String address) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Nowa wiadomość")
                        .setContentText(address);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}


