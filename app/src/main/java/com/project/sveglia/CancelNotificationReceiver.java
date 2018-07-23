package com.project.sveglia;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by simonerigon on 27/03/18.
 */

public class CancelNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //System.out.println("start class cancel notification receiver______________________________");

        // Recupero id notifica da intent chiamante ----------------
        int notification_ID = intent.getExtras().getInt("notification_ID");
        boolean isRepetitionDayAlarm = intent.getExtras().getBoolean("isRepetitionDayAlarm");

        System.out.println("Sono entrato in cancel notification receiver");

        // Cancello allarme CountDown ------------------------------
        try{
            int countDownAlarmID = intent.getExtras().getInt("alarm_ID");
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmToBeDeleted = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, countDownAlarmID, alarmToBeDeleted, PendingIntent.FLAG_ONE_SHOT);
            // Cancello l'allarme ---
            alarmManager.cancel(pendingIntent);

        }catch ( Exception e){
            Log.i("CountDownAlarmCancel", "onReceive: La richiesta di cancellazione non avviene dalla notifica di CountDown Timer. \n" + e);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_ID);

        // Cancello CountDownTimer ---------------------------------
        try{
            CountDownTimer.cancelCountDown();
        }catch (Exception e){
            e.printStackTrace();
        }

        // Attivo servizio per cancellare la musica della notifica --
        context.stopService(new Intent(context, Notification_Sound_Service.class));

        // Se è una sveglia con ripetizione imposto la ripetizione successiva --
        if(isRepetitionDayAlarm){
            // Recupero il tempo in millesecondi della sveglia ------
            long alarmTimeInMillis = intent.getExtras().getLong("alarmTimeInMillis");
            long newAlarmTimeInMillis = alarmTimeInMillis + 604800000;

            // Setto la nuova sveglia -------------------------------
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            int ALARM_ID = createID(newAlarmTimeInMillis);
            Intent startRepetitionAlarm = new Intent(context, AlarmReceiver.class);
            PendingIntent startRepetitionAlarm_PendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, startRepetitionAlarm, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, newAlarmTimeInMillis, startRepetitionAlarm_PendingIntent);

            //

        }

    }

    /**
     * Funzione per creare un ID per l'allarme
     * @param time current time in millisecond
     * @return Alarm ID
     */
    private static int createID(long time){
        int res = 0;
        int tmp = (int)time;
        res = Math.abs(tmp);
        return res;
    }
}
