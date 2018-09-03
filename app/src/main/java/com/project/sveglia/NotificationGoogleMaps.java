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
import android.util.Log;

import java.util.Calendar;

/**
 * Created by simonerigon on 06/08/18.
 */

public class NotificationGoogleMaps extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String maps_direction_request = intent.getExtras().getString("maps_direction_request");
        boolean isDelayAlarm = intent.getExtras().getBoolean("isDelayAlarm");
        String alarmName = intent.getExtras().getString("alarm_name");
        int repeatAlarmNumberTimes = intent.getExtras().getInt("repeatAlarmNumberTimes");
        int position = intent.getExtras().getInt("View_ID_position");
        long alarmTimeForGoogleMaps = intent.getExtras().getLong("alarmTimeForGoogleMaps");
        boolean isRebootAlarm = intent.getExtras().getBoolean("isRebootAlarm");
        int ALARM_ID = intent.getExtras().getInt("alarm_ID");
        int id_travel_to = intent.getExtras().getInt("id_travel_to");

        Calendar calendar = Calendar.getInstance();
        int NOT_ID = createID(calendar.getTimeInMillis());

        DB_Manager db_manager = new DB_Manager(context);
        db_manager.open();
        long delayTimeForCancelForNotification = db_manager.getDurataSuoneria();
        db_manager.close();

        // Controllo la versione di android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            int alarmMusic_ID = R.raw.mp3_quite_impressed;
            Uri uriSong = Uri.parse("android.resource://" + context.getPackageName() + "/" + alarmMusic_ID);

            // Aggiungo azione per aprire google maps --
            Intent openGoogleMapsApp = new Intent(context, openGoogleMapsReceiver.class);
            openGoogleMapsApp.putExtra("notification_ID", NOT_ID);
            openGoogleMapsApp.putExtra("maps_direction_request", maps_direction_request);
            openGoogleMapsApp.putExtra("id_travel_to", id_travel_to);
            openGoogleMapsApp.putExtra("position", position);
            PendingIntent openGoogleMapsAppPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, openGoogleMapsApp, PendingIntent.FLAG_UPDATE_CURRENT);

            // Aggiungo azione per annullare l'apertura di google maps --
            Intent cancelnotificationGoogleMaps = new Intent(context, openGoogleMapsReceiver.class);
            cancelnotificationGoogleMaps.putExtra("notification_ID", NOT_ID);
            openGoogleMapsApp.putExtra("maps_direction_request", maps_direction_request);
            openGoogleMapsApp.putExtra("id_travel_to", id_travel_to);
            openGoogleMapsApp.putExtra("position", position);
            PendingIntent cancelnotificationGoogleMapsPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelnotificationGoogleMaps, PendingIntent.FLAG_UPDATE_CURRENT);

            // fullScreen notification intent ----------------------------------
            Intent fullScreen = new Intent(context, FullScreen_Notification.class);
            fullScreen.putExtra("notification_ID", NOT_ID);
            fullScreen.putExtra("alarm_music_ID", alarmMusic_ID);
            fullScreen.putExtra("isDelayAlarm", isDelayAlarm);
            fullScreen.putExtra("alarm_name", alarmName);
            fullScreen.putExtra("delayTimeForCancelForNotification", delayTimeForCancelForNotification);
            fullScreen.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            fullScreen.putExtra("isRepetitionDayAlarm", false);
            fullScreen.putExtra("maps_direction_request", maps_direction_request);
            fullScreen.putExtra("View_ID_position", position);
            fullScreen.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
            fullScreen.putExtra("isRebootAlarm", isRebootAlarm);
            fullScreen.putExtra("alarm_ID", ALARM_ID);
            fullScreen.putExtra("isGoogleMapsNavigationNot", true);
            fullScreen.putExtra("id_travel_to", id_travel_to);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreen, PendingIntent.FLAG_UPDATE_CURRENT);

            String not_content = "Desideri aprire Google Maps per la navigazione?";

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("È ora di partire")
                    .setContentText(not_content)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSound(uriSong)
                    .addAction(0, "APRI", openGoogleMapsAppPendingIntent)
                    .addAction(0, "ANNULLA", cancelnotificationGoogleMapsPendingIntent)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
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
            context.startForegroundService(service_intent);

            // Aggiungo azione per aprire google maps --
            Intent openGoogleMapsApp = new Intent(context, openGoogleMapsReceiver.class);
            openGoogleMapsApp.putExtra("notification_ID", NOT_ID);
            openGoogleMapsApp.putExtra("maps_direction_request", maps_direction_request);
            openGoogleMapsApp.putExtra("id_travel_to", id_travel_to);
            openGoogleMapsApp.putExtra("position", position);
            PendingIntent openGoogleMapsAppPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, openGoogleMapsApp, PendingIntent.FLAG_UPDATE_CURRENT);

            // Aggiungo azione per annullare l'apertura di google maps --
            Intent cancelnotificationGoogleMaps = new Intent(context, cancelNotificationGoogleMaps.class);
            cancelnotificationGoogleMaps.putExtra("notification_ID", NOT_ID);
            cancelnotificationGoogleMaps.putExtra("position", position);
            PendingIntent cancelnotificationGoogleMapsPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelnotificationGoogleMaps, PendingIntent.FLAG_UPDATE_CURRENT);

            // fullScreen notification intent ----------------------------------
            Intent fullScreen = new Intent(context, FullScreen_Notification.class);
            fullScreen.putExtra("notification_ID", NOT_ID);
            fullScreen.putExtra("alarm_music_ID", alarmMusic_ID);
            fullScreen.putExtra("isDelayAlarm", true);
            fullScreen.putExtra("alarm_name", alarmName);
            fullScreen.putExtra("delayTimeForCancelForNotification", delayTimeForCancelForNotification);
            fullScreen.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            fullScreen.putExtra("isRepetitionDayAlarm", false);
            fullScreen.putExtra("maps_direction_request", maps_direction_request);
            fullScreen.putExtra("View_ID_position", position);
            fullScreen.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
            fullScreen.putExtra("isRebootAlarm", isRebootAlarm);
            fullScreen.putExtra("alarm_ID", ALARM_ID);
            fullScreen.putExtra("isGoogleMapsNavigationNot", true);
            fullScreen.putExtra("id_travel_to", id_travel_to);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreen, PendingIntent.FLAG_UPDATE_CURRENT);

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
                    .setFullScreenIntent(fullScreenPendingIntent, true)
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
