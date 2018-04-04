package com.project.sveglia;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by simonerigon on 27/03/18.
 */

public class CancelNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Recupero id notifica da intent chiamante ----------------
        int notification_ID = intent.getExtras().getInt("notification_ID");
        String notification_Channel = intent.getExtras().getString("notification_Channel");

        //Toast.makeText(context, "sono entrato in cancelAlarm", Toast.LENGTH_LONG).show();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_ID);


    }
}
