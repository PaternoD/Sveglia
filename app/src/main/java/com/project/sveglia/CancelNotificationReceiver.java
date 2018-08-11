package com.project.sveglia;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
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
import java.util.Calendar;

/**
 * Created by simonerigon on 27/03/18.
 */

public class CancelNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmReceiver.mySensorManager.unregisterListener(AlarmReceiver.proximitySensorEventListener);
        //System.out.println("start class cancel notification receiver______________________________");

        // Recupero id notifica da intent chiamante ----------------
        int notification_ID = intent.getExtras().getInt("notification_ID");
        boolean isRepetitionDayAlarm = intent.getExtras().getBoolean("isRepetitionDayAlarm");
        String maps_direction_request = intent.getExtras().getString("maps_direction_request");
        int position = intent.getExtras().getInt("View_ID_position");
        int alarm_music_ID = intent.getExtras().getInt("alarm_music_ID");
        boolean isDelayAlarm = intent.getExtras().getBoolean("isDelayAlarm");
        String alarm_name = intent.getExtras().getString("alarmName");
        int repeatAlarmNumberTimes = intent.getExtras().getInt("repeatAlarmNumberTimes");
        long alarmTimeForGoogleMaps = intent.getExtras().getLong("alarmTimeForGoogleMaps");

        Log.e("REBOOT TEST CANCEL NOT", "onReceive: ++++++++++++++ Not_id - Reboot = " + notification_ID);

        System.out.println("Sono entrato in cancel notification receiver");

        DB_Manager db_manager = new DB_Manager(context);
        db_manager.open();
        ArrayList<String> all_Database_ID = db_manager.getAllID();

        if (!isRepetitionDayAlarm){
            db_manager.SetOn_Off(Integer.parseInt(all_Database_ID.get(position)), false);
            SetViewSveglie.aggiornaAdapter_disattiva(position);
        }

        //elimino sveglia con il travel to dopo che ha suonato
        if (db_manager.getAllTravelTO().get(position).charAt(0)==(Character)'1'){
            System.out.println("entrato nell'if di riga 86");
            alarmTimeForGoogleMaps = Long.parseLong(db_manager.getAllTimeView().get(position));
            int id_travel_to = Integer.parseInt(db_manager.getAllID().get(position));
            Cancel_Alarm_Class.cancel_Alarm(id_travel_to,context,db_manager,true);
            SetViewSveglie.aggiornaAdapter_rimuovi(position);
        }

        db_manager.close();

        // Cancello allarme CountDown ------------------------------
        try{
            int countDownAlarmID = intent.getExtras().getInt("alarm_ID");
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmToBeDeleted = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, countDownAlarmID, alarmToBeDeleted, PendingIntent.FLAG_UPDATE_CURRENT);
            // Cancello l'allarme ---
            alarmManager.cancel(pendingIntent);

        }catch (Exception e){
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
            startRepetitionAlarm.putExtra("View_ID_position", position);
            startRepetitionAlarm.putExtra("alarm_music_ID", alarm_music_ID);
            startRepetitionAlarm.putExtra("isDelayAlarm", isDelayAlarm);
            startRepetitionAlarm.putExtra("alarmName", alarm_name);
            startRepetitionAlarm.putExtra("isRepetitionDayAlarm", false);
            startRepetitionAlarm.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            startRepetitionAlarm.putExtra("maps_direction_request", maps_direction_request);
            startRepetitionAlarm.putExtra("isFirstTimeAlarm", false);

            PendingIntent startRepetitionAlarm_PendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, startRepetitionAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, newAlarmTimeInMillis, startRepetitionAlarm_PendingIntent);

        }

        if(maps_direction_request != null){
            DB_Manager db_manager_1 = new DB_Manager(context);
            db_manager_1.open();
            long googleMapsTime = db_manager_1.getBadToCar();

            // Setto ora e minuti in cui aprire google Maps --
            long timeForFirenotification = alarmTimeForGoogleMaps + googleMapsTime;

            System.out.println("+++++++++++ alarmTimeForGOogleMaps = "+ alarmTimeForGoogleMaps);

            // Setto la notifica per aprire google maps ---------------
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            int ALARM_ID = createID(timeForFirenotification);
            Intent popUPGoogleMapsAPP = new Intent(context, NotificationGoogleMaps.class);
            popUPGoogleMapsAPP.putExtra("maps_direction_request", maps_direction_request);
            PendingIntent startRepetitionAlarm_PendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, popUPGoogleMapsAPP, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeForFirenotification, startRepetitionAlarm_PendingIntent);

            db_manager_1.close();
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
