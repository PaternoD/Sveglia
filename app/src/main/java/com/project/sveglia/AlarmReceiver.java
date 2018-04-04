package com.project.sveglia;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by simonerigon on 15/03/18.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Recupero dati da intent chiamante ---------------------
        int alarm_music_ID = intent.getExtras().getInt("alarm_music_ID");
        boolean isDelayAlarm = intent.getExtras().getBoolean("isDelayAlarm");
        String alarmName = intent.getExtras().getString("alarmName");

        // Recupero dati da impostazioni nel database ------------
        // ----> recupero informazioni sulla vibrazione
        boolean enableVibrate = true;

        // Inizializzo notifica ----------------------------------
        if(isDelayAlarm){
            startRepeatNotification(context, alarmName, alarm_music_ID, enableVibrate, isDelayAlarm);
        }else{
            startNotificationWithoutRepeat(context, alarmName, alarm_music_ID, enableVibrate, isDelayAlarm);
        }


    }

    /**
     * Funzione per attivare le notifiche con la possibilità di ripetizione
     * @param context
     * @param alarmName
     * @param alarmMusic_ID
     * @param enableVibrate
     * @param isDelayAlarm
     */
    private void startRepeatNotification(Context context, String alarmName, int alarmMusic_ID, boolean enableVibrate, boolean isDelayAlarm) {

        // Controllo la versione di android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            Calendar cal = Calendar.getInstance();
            int NOT_ID = createID(cal.getTimeInMillis());

            Uri uriSong = Uri.parse("android.resource://" + context.getPackageName() + "/" + alarmMusic_ID);

            // Aggiungo azione cancella alla notifica --------------------------
            Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
            cancelAction.putExtra("notification_ID", NOT_ID);
            cancelAction.putExtra("notification_Channel", "");
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_ONE_SHOT);

            // Aggiungo azione Ritarda alla notifica ---------------------------
            Intent delayAction = new Intent(context, delayNotificationReceiver.class);
            delayAction.putExtra("notification_ID", NOT_ID);
            delayAction.putExtra("alarm_music_ID", alarmMusic_ID);
            delayAction.putExtra("isDelayAlarm", isDelayAlarm);
            delayAction.putExtra("alarm_name", alarmName);
            delayAction.putExtra("notification_Channel", "");
            PendingIntent delayPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, delayAction, PendingIntent.FLAG_ONE_SHOT);


            // ----> test di fullScreen notification
            Intent fullScreen = new Intent(context, alarm_screen_test.class);
            fullScreen.putExtra("notification_ID", NOT_ID);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreen, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("Notification Alarm")
                    .setContentText(alarmName)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSound(uriSong)
                    .addAction(0, "RITARDA", delayPendingIntent)
                    .addAction(0, "CANCELLA", cancelPendingIntent)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .setAutoCancel(true)
                    .setUsesChronometer(true);

            if(enableVibrate){
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification mNotification = notificationBuilder.build();
            mNotification.flags |= Notification.FLAG_INSISTENT;
            notificationManager.notify(NOT_ID, mNotification);
            removeDelayNotification(NOT_ID, notificationManager, context, isDelayAlarm, alarmMusic_ID, alarmName);

        } else {

            Calendar cal = Calendar.getInstance();
            int NOT_ID = createID(cal.getTimeInMillis());

            String not_Channel_ID = "com.project.sveglia.Channel.ONE.one";

            Uri uriSong = Uri.parse("android.resource://" + context.getPackageName() + "/" + alarmMusic_ID);

            // Aggiungo azione cancella alla notifica --------------------------
            Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
            cancelAction.putExtra("notification_ID", NOT_ID);
            cancelAction.putExtra("notification_Channel", not_Channel_ID);
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_ONE_SHOT);

            // Aggiungo azione Ritarda alla notifica ---------------------------
            Intent delayAction = new Intent(context, delayNotificationReceiver.class);
            delayAction.putExtra("notification_ID", NOT_ID);
            delayAction.putExtra("alarm_music_ID", alarmMusic_ID);
            delayAction.putExtra("isDelayAlarm", isDelayAlarm);
            delayAction.putExtra("alarm_name", alarmName);
            delayAction.putExtra("notification_Channel", not_Channel_ID);
            PendingIntent delayPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, delayAction, PendingIntent.FLAG_ONE_SHOT);

            // Creo un notification Channel ------------------------------------
            NotificationChannel notificationChannel = new NotificationChannel(not_Channel_ID, "not_channel_one", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, not_Channel_ID)
                    .setContentTitle("Notification Alarm")
                    .setContentText(alarmName)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(0, "RITARDA", delayPendingIntent)
                    .addAction(0, "CANCELLA", cancelPendingIntent)
                    .setAutoCancel(true)
                    .setUsesChronometer(true);

            if(enableVibrate){
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification mNotification = notificationBuilder.build();
            mNotification.flags |= Notification.FLAG_INSISTENT;
            notificationManager.notify(NOT_ID, mNotification);
            notificationManager.createNotificationChannel(notificationChannel);
            removeDelayNotification(NOT_ID, notificationManager, context, isDelayAlarm, alarmMusic_ID, alarmName);

        }
    }

    /**
     * Funzione per attivare le notifiche senza il bottone ritarda
     * @param context
     * @param alarmName
     * @param alarmMusic_ID
     * @param enableVibrate
     * @param isDelayAlarm
     */
    private void startNotificationWithoutRepeat(Context context, String alarmName, int alarmMusic_ID, boolean enableVibrate, boolean isDelayAlarm){

        // Controllo la versione di android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            Calendar cal = Calendar.getInstance();
            int NOT_ID = createID(cal.getTimeInMillis());

            Uri uriSong = Uri.parse("android.resource://" + context.getPackageName() + "/" + alarmMusic_ID);

            // Aggiungo azione cancella alla notifica --------------------------
            Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
            cancelAction.putExtra("notification_ID", NOT_ID);
            cancelAction.putExtra("notification_Channel", "");
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("Notification Alarm")
                    .setContentText(alarmName)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSound(uriSong)
                    .setOngoing(true)
                    .addAction(0, "CANCELLA", cancelPendingIntent)
                    .setAutoCancel(true)
                    .setUsesChronometer(true);

            if(enableVibrate){
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification mNotification = notificationBuilder.build();
            mNotification.flags |= Notification.FLAG_INSISTENT;
            notificationManager.notify(NOT_ID, mNotification);

        }else{

            Calendar cal = Calendar.getInstance();
            int NOT_ID = createID(cal.getTimeInMillis());

            String not_Channel_ID = "com.project.sveglia.Channel.ONE.tre";

            Uri uriSong = Uri.parse("android.resource://" + context.getPackageName() + "/" + alarmMusic_ID);

            // Aggiungo azione cancella alla notifica --------------------------
            Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
            cancelAction.putExtra("notification_ID", NOT_ID);
            cancelAction.putExtra("notification_Channel", not_Channel_ID);
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_ONE_SHOT);

            // Creo un notification Channel ------------------------------------
            NotificationChannel notificationChannel = new NotificationChannel(not_Channel_ID, "not_channel_id", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.enableLights(true);
            notificationChannel.setSound(uriSong, null);
            notificationChannel.setShowBadge(true);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, not_Channel_ID)
                    .setContentTitle("Notification Alarm")
                    .setContentText(alarmName)
                    .setSound(uriSong)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(0, "CANCELLA", cancelPendingIntent)
                    .setAutoCancel(true)
                    .setUsesChronometer(true);

            if(enableVibrate){
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification mNotification = notificationBuilder.build();
            mNotification.flags |= Notification.FLAG_INSISTENT;
            notificationManager.notify(NOT_ID, mNotification);
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }

    /**
     * Funzione che cancella la notifica dopo un tempo stabilito
     * @param id
     * @param notificationManager
     */
    private void removeDelayNotification(final int id,
                                    final NotificationManager notificationManager,
                                    final Context context,
                                    final boolean isDelayAlarm,
                                    final int alarm_music_ID,
                                    final String alarm_name) {
        Handler handler = new Handler();
        final long delayInMilliseconds = 600000;
        handler.postDelayed(new Runnable() {
            public void run() {
                if(isNotificationActive(id, context)){
                    notificationManager.cancel(id);
                    Toast.makeText(context, "posso cancellare la notificata", Toast.LENGTH_LONG).show();
                    // Risetto la sveglia dopo un certo tempo indicato dall'utente ...
                    long delayAlarm = 600000;
                    Calendar cal = Calendar.getInstance();
                    long timeToSetDelayAlarm = cal.getTimeInMillis() + delayAlarm;

                    int ALARM_ID = createID(timeToSetDelayAlarm);
                    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

                    Intent startPrincipalAlarmIntent = new Intent(context, AlarmReceiver.class);
                    startPrincipalAlarmIntent.putExtra("isRepeatAlarm", false);
                    startPrincipalAlarmIntent.putExtra("alarm_music_ID", alarm_music_ID);
                    startPrincipalAlarmIntent.putExtra("isDelayAlarm", isDelayAlarm);
                    startPrincipalAlarmIntent.putExtra("alarmName", alarm_name);
                    PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,ALARM_ID, startPrincipalAlarmIntent, PendingIntent.FLAG_ONE_SHOT);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToSetDelayAlarm, alarmPendingIntent);

                }else{
                    Log.i("REMOVE_NOTIFICATION", "run: la notifica è gia stata cancellata");
                }
            }
        }, delayInMilliseconds);
    }

    /**
     * Funzione che mi restituisce true se la notifica è attiva
     * @param notificationId
     * @param context
     * @return true, if the notification is active
     */
    private boolean isNotificationActive(int notificationId, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] barNotifications = notificationManager.getActiveNotifications();
        for(StatusBarNotification notification: barNotifications) {
            if (notification.getId() == notificationId) {
                return true;
            }
        }
        return false;
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
