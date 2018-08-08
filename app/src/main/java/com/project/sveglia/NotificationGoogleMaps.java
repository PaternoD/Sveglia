package com.project.sveglia;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by simonerigon on 06/08/18.
 */

public class NotificationGoogleMaps extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("++++++++++ Sono entrato in Notification Google Maps +++++++++++++");

        String maps_direction_request = intent.getExtras().getString("maps_direction_request");
        Calendar calendar = Calendar.getInstance();
        int NOT_ID = createID(calendar.getTimeInMillis());

        System.out.println("maps_direction_request_1 = " + maps_direction_request);

        // Controllo la versione di android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            // Attivo servizio per attivare la musica della notifica -----------
            int alarmMusic_ID = R.raw.mp3_quite_impressed;
            Intent service_intent = new Intent(context, Notification_Sound_Service.class);
            service_intent.putExtra("alarm_music_ID", alarmMusic_ID);
            context.startService(service_intent);

            // Aggiungo azione per aprire google maps --
            Intent openGoogleMapsApp = new Intent(context, openGoogleMapsReceiver.class);
            openGoogleMapsApp.putExtra("openGoogleMaps", true);
            openGoogleMapsApp.putExtra("notification_ID", NOT_ID);
            openGoogleMapsApp.putExtra("maps_direction_request", maps_direction_request);
            PendingIntent openGoogleMapsAppPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, openGoogleMapsApp, PendingIntent.FLAG_ONE_SHOT);

            // Aggiungo azione per annullare l'apertura di google maps --
            Intent cancelnotificationGoogleMaps = new Intent(context, openGoogleMapsReceiver.class);
            cancelnotificationGoogleMaps.putExtra("openGoogleMaps", false);
            PendingIntent cancelnotificationGoogleMapsPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelnotificationGoogleMaps, PendingIntent.FLAG_ONE_SHOT);

            String not_content = "È ora di partire, desideri aprire Google Maps per la navigazione?";

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("Apri Google Maps")
                    .setContentText(not_content)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(0, "APRI", openGoogleMapsAppPendingIntent)
                    .addAction(0, "ANNULLA", cancelnotificationGoogleMapsPendingIntent)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification mNotification = notificationBuilder.build();
            mNotification.flags |= Notification.FLAG_INSISTENT;
            notificationManager.notify(NOT_ID, mNotification);

        }else{
            String not_Channel_ID = "com.project.sveglia.GoogleMaps";

            // Attivo servizio per attivare la musica della notifica -----------
            int alarmMusic_ID = R.raw.mp3_quite_impressed;
            Intent service_intent = new Intent(context, Notification_Sound_Service.class);
            service_intent.putExtra("alarm_music_ID", alarmMusic_ID);
            context.startService(service_intent);

            // Aggiungo azione per aprire google maps --
            Intent openGoogleMapsApp = new Intent(context, openGoogleMapsReceiver.class);
            openGoogleMapsApp.putExtra("openGoogleMaps", true);
            openGoogleMapsApp.putExtra("notification_ID", NOT_ID);
            openGoogleMapsApp.putExtra("maps_direction_request", maps_direction_request);
            PendingIntent openGoogleMapsAppPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, openGoogleMapsApp, PendingIntent.FLAG_ONE_SHOT);

            // Aggiungo azione per annullare l'apertura di google maps --
            Intent cancelnotificationGoogleMaps = new Intent(context, openGoogleMapsReceiver.class);
            cancelnotificationGoogleMaps.putExtra("openGoogleMaps", false);
            PendingIntent cancelnotificationGoogleMapsPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelnotificationGoogleMaps, PendingIntent.FLAG_ONE_SHOT);

            // Creo un notification Channel ------------------------------------
            NotificationChannel notificationChannel = new NotificationChannel(not_Channel_ID, "Alarm Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.enableLights(true);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(NOT_ID);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);

            String not_content = "Desideri aprire Google Maps per la navigazione?";

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, not_Channel_ID)
                    .setContentTitle("È ora di partire")
                    .setContentText(not_content)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(0, "APRI", openGoogleMapsAppPendingIntent)
                    .addAction(0, "ANNULLA", cancelnotificationGoogleMapsPendingIntent)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification mNotification = notificationBuilder.build();
            mNotification.flags |= Notification.FLAG_INSISTENT;
            notificationManager.notify(NOT_ID, mNotification);
            notificationManager.createNotificationChannel(notificationChannel);

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
