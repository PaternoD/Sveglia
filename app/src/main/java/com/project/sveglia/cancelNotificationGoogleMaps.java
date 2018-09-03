package com.project.sveglia;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by simonerigon on 28/08/18.
 */

public class cancelNotificationGoogleMaps extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int notification_ID = intent.getExtras().getInt("notification_ID");
        int position = intent.getExtras().getInt("position");

        // Attivo servizio per cancellare la musica della notifica --
        context.stopService(new Intent(context, Notification_Sound_Service.class));

        // Recupero informazioni dal database --
        DB_Manager db_manager = new DB_Manager(context);
        db_manager.open();
        int id_travel_to = Integer.parseInt(db_manager.getAllID().get(position));
        //System.out.println("id_travel_to_3 = " + id_travel_to);
        Cancel_Alarm_Class.cancel_Alarm(id_travel_to,context,db_manager,true);
        db_manager.close();

        // cancello la notifica --
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_ID);

    }
}
