package com.project.sveglia;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

/**
 * Created by simonerigon on 06/08/18.
 */

public class NotificationGoogleMaps extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //System.out.println("++++++++++ Sono entrato in Notification Google Maps +++++++++++++");

        String maps_direction_request = intent.getExtras().getString("maps_direction_request");
        Calendar calendar = Calendar.getInstance();
        int NOT_ID = createID(calendar.getTimeInMillis());

        // Controllo la versione di android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            // Aggiungo azione per aprire google maps --
            Intent openGoogleMapsApp = new Intent(context, openGoogleMapsReceiver.class);
            openGoogleMapsApp.putExtra("openGoogleMaps", true);
            openGoogleMapsApp.putExtra("maps_direction_request", maps_direction_request);
            PendingIntent openGoogleMapsAppPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, openGoogleMapsApp, PendingIntent.FLAG_UPDATE_CURRENT);

            // Aggiungo azione per annullare l'apertura di google maps --
            Intent cancelnotificationGoogleMaps = new Intent(context, openGoogleMapsReceiver.class);
            cancelnotificationGoogleMaps.putExtra("openGoogleMaps", false);
            PendingIntent cancelnotificationGoogleMapsPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelnotificationGoogleMaps, PendingIntent.FLAG_UPDATE_CURRENT);


        }else{

            String not_Channel_ID = "com.project.sveglia.GoogleMaps";

            // Aggiungo azione per aprire google maps --
            Intent openGoogleMapsApp = new Intent(context, openGoogleMapsReceiver.class);
            openGoogleMapsApp.putExtra("openGoogleMaps", true);
            openGoogleMapsApp.putExtra("maps_direction_request", maps_direction_request);
            PendingIntent openGoogleMapsAppPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, openGoogleMapsApp, PendingIntent.FLAG_UPDATE_CURRENT);

            // Aggiungo azione per annullare l'apertura di google maps --
            Intent cancelnotificationGoogleMaps = new Intent(context, openGoogleMapsReceiver.class);
            cancelnotificationGoogleMaps.putExtra("openGoogleMaps", false);
            PendingIntent cancelnotificationGoogleMapsPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelnotificationGoogleMaps, PendingIntent.FLAG_UPDATE_CURRENT);

            // Creo un notification Channel ------------------------------------
            NotificationChannel notificationChannel = new NotificationChannel(not_Channel_ID, "Alarm Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.enableLights(true);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(true);


        }

    }

    /**
     * Funzione per creare un ID per la notifica
     * @param time current time in millisecond
     * @return Notification ID
     */
    private static int createID(long time){
        int res = 0;
        int tmp = (int)time;
        res = Math.abs(tmp);
        return res;
    }
}
