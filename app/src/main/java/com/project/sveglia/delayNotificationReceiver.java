package com.project.sveglia;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by simonerigon on 27/03/18.
 */

public class delayNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "sono entrato in delay notification", Toast.LENGTH_LONG).show();

        // Recupero id notifica -----------------------------
        int notification_ID = intent.getExtras().getInt("notification_ID");
        int alarm_music_ID = intent.getExtras().getInt("alarm_music_ID");
        boolean isDelayAlarm = intent.getExtras().getBoolean("isDelayAlarm");
        String alarm_name = intent.getExtras().getString("alarm_name");
        String notification_Channel = intent.getExtras().getString("notification_Channel");

        // cancello la notifica -----------------------------
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_ID);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(notification_Channel);
        }

        // Recupero tempo di attesa della nuova sveglia -----
        long delayTime = 600000;

        // Setto la nuova sveglia ---------------------------
        Calendar cal = Calendar.getInstance();
        long timeDelayAlarm = cal.getTimeInMillis() + delayTime;

        int ALARM_ID = createID(timeDelayAlarm);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent startPrincipalAlarmIntent = new Intent(context, AlarmReceiver.class);
        startPrincipalAlarmIntent.putExtra("isRepeatAlarm", false);
        startPrincipalAlarmIntent.putExtra("alarm_music_ID", alarm_music_ID);
        startPrincipalAlarmIntent.putExtra("isDelayAlarm", isDelayAlarm);
        startPrincipalAlarmIntent.putExtra("alarmName", alarm_name);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,ALARM_ID, startPrincipalAlarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeDelayAlarm, alarmPendingIntent);

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
