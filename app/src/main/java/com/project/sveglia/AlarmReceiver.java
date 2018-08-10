package com.project.sveglia;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by simonerigon on 15/03/18.
 */

public class AlarmReceiver extends BroadcastReceiver {

    // Variabili Globali --
    long delayTimeForCancelForNotification;
    long alarmTimeForGoogleMaps = 0;
    MediaPlayer mediaPlayer;
    long alarmTimeInMillis = 0;
    boolean isRepetitionDayAlarm;
    int repeatAlarmNumberTimes;
//per sensore di prossimità
    public static SensorManager mySensorManager;
    Sensor myProximitySensor;
    public static SensorEventListener proximitySensorEventListener;

    boolean nero_nero=false;
    boolean bianco_nero=false;

    boolean ritarda;
    int position = 0;
    boolean isRebootAlarm;
    int FLAG;


    @Override
    public void onReceive(Context context, Intent intent) {

        // Recupero dati da intent chiamante ---------------------
        int alarmViewID = intent.getExtras().getInt("View_ID");
        int alarm_music_ID = intent.getExtras().getInt("alarm_music_ID");
        boolean isDelayAlarm = intent.getExtras().getBoolean("isDelayAlarm");
        String alarmName = intent.getExtras().getString("alarmName");
        isRepetitionDayAlarm = intent.getExtras().getBoolean("isRepetitionDayAlarm");
        repeatAlarmNumberTimes = intent.getExtras().getInt("repeatAlarmNumberTimes");
        ritarda=isDelayAlarm;
        if(isRepetitionDayAlarm){
            alarmTimeInMillis = intent.getExtras().getLong("alarmTimeInMillis");
        }
        String maps_direction_request = intent.getExtras().getString("maps_direction_request");
        boolean isFirstTimeAlarm = intent.getExtras().getBoolean("isFirstTimeAlarm");
        isRebootAlarm = intent.getExtras().getBoolean("isRebootAlarm");


        if(isRebootAlarm){
            FLAG = PendingIntent.FLAG_UPDATE_CURRENT;
        }else{
            FLAG = PendingIntent.FLAG_UPDATE_CURRENT;
        }

        // Apro il database --------------------------------------
        DB_Manager db_manager = new DB_Manager(context);
        db_manager.open();

        // Recupero dati da impostazioni nel database ------------
        // ----> recupero informazioni sulla vibrazione
        boolean enableVibrate = true;
        delayTimeForCancelForNotification = db_manager.getDurataSuoneria();

        Log.e("REBOOT TEST", "onReceive: ++++++++++++++ Alarm View ID - Reboot = " + alarmViewID);


        // setto visualizzazione sveglia a "non attiva" --
        if(isFirstTimeAlarm) {
            //position = CustomAdapterView.getPosition(alarmViewID);
            for(int i=0; i<db_manager.getAllID().size(); i++){
                if(alarmViewID == Integer.parseInt(db_manager.getAllID().get(i))){
                    position = i;
                }
            }

        }else{
            position = intent.getExtras().getInt("View_ID_position");
        }

        Log.e("REBOOT TEST", "onReceive: ++++++++++++++ Position - Reboot = " + position);
        System.out.println("++++++++++++ alarm_music_id ++++++++ " + alarm_music_ID);

        // Inizializzo notifica ----------------------------------
        if(isDelayAlarm){
            startRepeatNotification(context, alarmName, alarm_music_ID, enableVibrate, isDelayAlarm, maps_direction_request, alarmViewID);
        }else{
            startNotificationWithoutRepeat(context, alarmName, alarm_music_ID, enableVibrate, isDelayAlarm, maps_direction_request, alarmViewID);
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
    private void startRepeatNotification(Context context, String alarmName, int alarmMusic_ID, boolean enableVibrate, boolean isDelayAlarm, String maps_direction_request, int alarmViewID)  {

        Calendar cal = Calendar.getInstance();
        int NOT_ID = createID(cal.getTimeInMillis());

        DB_Manager db = new DB_Manager(context);
        db.open();
        boolean sensor_on = db.getSensoriOn();
        db.close();
        proximitySensorEventListener = new SensorEventListener() {


            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){

                    if (!sensor_on) {
                        mySensorManager.unregisterListener(this);
                    }
                    if (sensorEvent.values[0]==0) {//se a pancia in giu
                        System.out.println("pancia in giu");
                        nero_nero=true;

                        if (bianco_nero && nero_nero) {
                            mySensorManager.unregisterListener(this);
                            DB_Manager db = new DB_Manager(context);
                            db.open();
                            if (db.getSensoriOpzione().equals((String)"elimina")){
                                //CANCELLO SVEGLIA
                                Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
                                cancelAction.putExtra("notification_ID", NOT_ID);
                                cancelAction.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
                                cancelAction.putExtra("alarmTimeInMillis", alarmTimeInMillis);
                                cancelAction.putExtra("maps_direction_request", maps_direction_request);
                                cancelAction.putExtra("View_ID_position", position);
                                cancelAction.putExtra("alarm_music_ID", alarmMusic_ID);
                                cancelAction.putExtra("isDelayAlarm", isDelayAlarm);
                                cancelAction.putExtra("alarmName", alarmName);
                                cancelAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                                cancelAction.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
                                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_UPDATE_CURRENT);
                                try {
                                    cancelPendingIntent.send();

                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(!ritarda){
                                mySensorManager.unregisterListener(this);
                            } else if (db.getSensoriOpzione().equals((String)"ritarda")){
                                //RIMANDO SVEGLIA
                                Intent delayAction = new Intent(context, delayNotificationReceiver.class);
                                delayAction.putExtra("notification_ID", NOT_ID);
                                delayAction.putExtra("alarm_music_ID", alarmMusic_ID);
                                delayAction.putExtra("isDelayAlarm", isDelayAlarm);
                                delayAction.putExtra("alarm_name", alarmName);
                                delayAction.putExtra("notification_Channel", "");
                                delayAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                                delayAction.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
                                delayAction.putExtra("View_ID_position", position);
                                delayAction.putExtra("isRebootAlarm", isRebootAlarm);
                                PendingIntent delayPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, delayAction, PendingIntent.FLAG_UPDATE_CURRENT);
                                try {
                                    delayPendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    }else{//se a pancia in su
                        System.out.println("pancia in su");
                        bianco_nero=true;

                    }
                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };


        mySensorManager =  (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mySensorManager.registerListener(proximitySensorEventListener,
                myProximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);


        // Controllo la versione di android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            Uri uriSong = Uri.parse("android.resource://" + context.getPackageName() + "/" + alarmMusic_ID);

            // Aggiungo azione cancella alla notifica --------------------------
            Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
            cancelAction.putExtra("notification_ID", NOT_ID);
            cancelAction.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            cancelAction.putExtra("alarmTimeInMillis", alarmTimeInMillis);
            cancelAction.putExtra("maps_direction_request", maps_direction_request);
            cancelAction.putExtra("View_ID_position", position);
            cancelAction.putExtra("alarm_music_ID", alarmMusic_ID);
            cancelAction.putExtra("isDelayAlarm", isDelayAlarm);
            cancelAction.putExtra("alarmName", alarmName);
            cancelAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            cancelAction.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);

            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_UPDATE_CURRENT);

            // Aggiungo azione Ritarda alla notifica ---------------------------
            Intent delayAction = new Intent(context, delayNotificationReceiver.class);
            delayAction.putExtra("notification_ID", NOT_ID);
            delayAction.putExtra("alarm_music_ID", alarmMusic_ID);
            delayAction.putExtra("isDelayAlarm", isDelayAlarm);
            delayAction.putExtra("alarm_name", alarmName);
            delayAction.putExtra("notification_Channel", "");
            delayAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            delayAction.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            delayAction.putExtra("View_ID_position", position);
            delayAction.putExtra("isRebootAlarm", isRebootAlarm);
            PendingIntent delayPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, delayAction, PendingIntent.FLAG_UPDATE_CURRENT);

            // fullScreen notification intent ----------------------------------
            Intent fullScreen = new Intent(context, FullScreen_Notification.class);
            fullScreen.putExtra("notification_ID", NOT_ID);
            fullScreen.putExtra("alarm_music_ID", alarmMusic_ID);
            fullScreen.putExtra("isDelayAlarm", isDelayAlarm);
            fullScreen.putExtra("alarm_name", alarmName);
            fullScreen.putExtra("notification_Channel", "");
            fullScreen.putExtra("delayTimeForCancelForNotification", delayTimeForCancelForNotification);
            fullScreen.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            fullScreen.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            fullScreen.putExtra("alarmTimeInMillis", alarmTimeInMillis);
            fullScreen.putExtra("maps_direction_request", maps_direction_request);
            fullScreen.putExtra("View_ID_position", position);
            fullScreen.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
            fullScreen.putExtra("isRebootAlarm", isRebootAlarm);
            //fullScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreen, PendingIntent.FLAG_UPDATE_CURRENT);

            // Creo la notifica ------------------------------------------------
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
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setUsesChronometer(true);

            if(enableVibrate){
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification mNotification = notificationBuilder.build();
            mNotification.flags |= Notification.FLAG_INSISTENT;
            notificationManager.notify(NOT_ID, mNotification);
            removeDelayNotification(NOT_ID, notificationManager, context, isDelayAlarm, alarmMusic_ID, alarmName, fullScreenPendingIntent);

        } else {

            String not_Channel_ID = "com.project.sveglia.Channel.one";

            Uri uriSong = Uri.parse("android.resource://" + context.getPackageName() + "/" + alarmMusic_ID);

            // Attivo servizio per attivare la musica della notifica -----------
            Intent service_intent = new Intent(context, Notification_Sound_Service.class);
            service_intent.putExtra("alarm_music_ID", alarmMusic_ID);
            if(isRebootAlarm){
                context.startForegroundService(service_intent);
            }else{
                context.startForegroundService(service_intent);
            }


            // Aggiungo azione cancella alla notifica --------------------------
            Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
            cancelAction.putExtra("notification_ID", NOT_ID);
            cancelAction.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            cancelAction.putExtra("alarmTimeInMillis", alarmTimeInMillis);
            cancelAction.putExtra("maps_direction_request", maps_direction_request);
            cancelAction.putExtra("View_ID_position", position);
            cancelAction.putExtra("alarm_music_ID", alarmMusic_ID);
            cancelAction.putExtra("isDelayAlarm", isDelayAlarm);
            cancelAction.putExtra("alarmName", alarmName);
            cancelAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            cancelAction.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_UPDATE_CURRENT);

            // Aggiungo azione Ritarda alla notifica ---------------------------
            Intent delayAction = new Intent(context, delayNotificationReceiver.class);
            delayAction.putExtra("notification_ID", NOT_ID);
            delayAction.putExtra("alarm_music_ID", alarmMusic_ID);
            delayAction.putExtra("isDelayAlarm", isDelayAlarm);
            delayAction.putExtra("alarm_name", alarmName);
            delayAction.putExtra("notification_Channel", not_Channel_ID);
            delayAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            delayAction.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            delayAction.putExtra("View_ID_position", position);
            delayAction.putExtra("isRebootAlarm", isRebootAlarm);
            PendingIntent delayPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, delayAction, PendingIntent.FLAG_UPDATE_CURRENT);

            // Creo un notification Channel ------------------------------------
            NotificationChannel notificationChannel = new NotificationChannel(not_Channel_ID, "Alarm Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            // fullScreen notification intent ----------------------------------
            Intent fullScreen = new Intent(context, FullScreen_Notification.class);
            fullScreen.putExtra("notification_ID", NOT_ID);
            fullScreen.putExtra("alarm_music_ID", alarmMusic_ID);
            fullScreen.putExtra("isDelayAlarm", isDelayAlarm);
            fullScreen.putExtra("alarm_name", alarmName);
            fullScreen.putExtra("notification_Channel", not_Channel_ID);
            fullScreen.putExtra("delayTimeForCancelForNotification", delayTimeForCancelForNotification);
            fullScreen.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            fullScreen.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            fullScreen.putExtra("alarmTimeInMillis", alarmTimeInMillis);
            fullScreen.putExtra("maps_direction_request", maps_direction_request);
            fullScreen.putExtra("View_ID_position", position);
            fullScreen.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
            fullScreen.putExtra("isRebootAlarm", isRebootAlarm);
            //fullScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreen, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, not_Channel_ID)
                    .setContentTitle("Notification Alarm")
                    .setContentText(alarmName)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(0, "RITARDA", delayPendingIntent)
                    .addAction(0, "CANCELLA", cancelPendingIntent)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setUsesChronometer(true);

            if(enableVibrate){
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }



            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification mNotification = notificationBuilder.build();
            mNotification.flags |= Notification.FLAG_INSISTENT;
            notificationManager.notify(NOT_ID, mNotification);
            notificationManager.createNotificationChannel(notificationChannel);
            removeDelayNotification(NOT_ID, notificationManager, context, isDelayAlarm, alarmMusic_ID, alarmName, fullScreenPendingIntent);

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
    private void startNotificationWithoutRepeat(Context context, String alarmName, int alarmMusic_ID, boolean enableVibrate, boolean isDelayAlarm, String maps_direction_request, int alarmViewID){

        Calendar cal = Calendar.getInstance();
        int NOT_ID = createID(cal.getTimeInMillis());
        DB_Manager db = new DB_Manager(context);
        db.open();
        boolean sensor_on = db.getSensoriOn();
        db.close();
        proximitySensorEventListener = new SensorEventListener() {


            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){
                    if (sensor_on){
                        if (sensorEvent.values[0]==0) {//se a pancia in giu
                            System.out.println("pancia in giu");
                            nero_nero=true;

                        if (bianco_nero && nero_nero) {
                            mySensorManager.unregisterListener(this);
                            DB_Manager db = new DB_Manager(context);
                            db.open();
                            if (db.getSensoriOpzione().equals((String)"cancella")){
                                //CANCELLO SVEGLIA
                                Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
                                cancelAction.putExtra("notification_ID", NOT_ID);
                                cancelAction.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
                                cancelAction.putExtra("alarmTimeInMillis", alarmTimeInMillis);
                                cancelAction.putExtra("maps_direction_request", maps_direction_request);
                                cancelAction.putExtra("View_ID_position", position);
                                cancelAction.putExtra("alarm_music_ID", alarmMusic_ID);
                                cancelAction.putExtra("isDelayAlarm", isDelayAlarm);
                                cancelAction.putExtra("alarmName", alarmName);
                                cancelAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                                cancelAction.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
                                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_UPDATE_CURRENT);
                                try {
                                    cancelPendingIntent.send();

                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(!ritarda){
                                mySensorManager.unregisterListener(this);
                            } else if (db.getSensoriOpzione().equals((String)"ritarda")){

                                System.out.println("Sensore prossimità. rimando allarme");

                                //RIMANDO SVEGLIA
                                Intent delayAction = new Intent(context, delayNotificationReceiver.class);
                                delayAction.putExtra("notification_ID", NOT_ID);
                                delayAction.putExtra("alarm_music_ID", alarmMusic_ID);
                                delayAction.putExtra("isDelayAlarm", isDelayAlarm);
                                delayAction.putExtra("alarm_name", alarmName);
                                delayAction.putExtra("notification_Channel", "");
                                delayAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                                delayAction.putExtra("View_ID_position", position);
                                delayAction.putExtra("isRebootAlarm", isRebootAlarm);
                                PendingIntent delayPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, delayAction, PendingIntent.FLAG_UPDATE_CURRENT);
                                try {
                                    delayPendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }


                            }
                        }else{//se a pancia in su
                            System.out.println("pancia in su");
                            bianco_nero=true;

                        }
                    }else{
                        mySensorManager.unregisterListener(this);
                    }

                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };


        mySensorManager =  (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mySensorManager.registerListener(proximitySensorEventListener,
                myProximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);


        // Controllo la versione di android
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {


            Uri uriSong = Uri.parse("android.resource://" + context.getPackageName() + "/" + alarmMusic_ID);

            // Aggiungo azione cancella alla notifica --------------------------
            Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
            cancelAction.putExtra("notification_ID", NOT_ID);
            cancelAction.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            cancelAction.putExtra("alarmTimeInMillis", alarmTimeInMillis);
            cancelAction.putExtra("maps_direction_request", maps_direction_request);
            cancelAction.putExtra("View_ID_position", position);
            cancelAction.putExtra("alarm_music_ID", alarmMusic_ID);
            cancelAction.putExtra("isDelayAlarm", isDelayAlarm);
            cancelAction.putExtra("alarmName", alarmName);
            cancelAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            cancelAction.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_UPDATE_CURRENT);

            // fullScreen notification intent ----------------------------------
            Intent fullScreen = new Intent(context, FullScreen_Notification.class);
            fullScreen.putExtra("notification_ID", NOT_ID);
            fullScreen.putExtra("alarm_music_ID", alarmMusic_ID);
            fullScreen.putExtra("isDelayAlarm", isDelayAlarm);
            fullScreen.putExtra("alarm_name", alarmName);
            fullScreen.putExtra("notification_Channel", "");
            fullScreen.putExtra("delayTimeForCancelForNotification", delayTimeForCancelForNotification);
            fullScreen.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            fullScreen.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            fullScreen.putExtra("alarmTimeInMillis", alarmTimeInMillis);
            fullScreen.putExtra("maps_direction_request", maps_direction_request);
            fullScreen.putExtra("View_ID_position", position);
            fullScreen.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
            fullScreen.putExtra("isRebootAlarm", isRebootAlarm);
            //fullScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreen, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("Notification Alarm")
                    .setContentText(alarmName)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSound(uriSong)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .addAction(0, "CANCELLA", cancelPendingIntent)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setUsesChronometer(true);

            if(enableVibrate){
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification mNotification = notificationBuilder.build();
            mNotification.flags |= Notification.FLAG_INSISTENT;
            notificationManager.notify(NOT_ID, mNotification);

        }else{

            String not_Channel_ID = "com.project.sveglia.Channel.one";

            Uri uriSong = Uri.parse("android.resource://" + context.getPackageName() + "/" + alarmMusic_ID);

            // Attivo servizio per attivare la musica della notifica -----------
            Intent service_intent = new Intent(context, Notification_Sound_Service.class);
            service_intent.putExtra("alarm_music_ID", alarmMusic_ID);
            context.startForegroundService(service_intent);

            // Aggiungo azione cancella alla notifica --------------------------
            Intent cancelAction = new Intent(context, CancelNotificationReceiver.class);
            cancelAction.putExtra("notification_ID", NOT_ID);
            cancelAction.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            cancelAction.putExtra("alarmTimeInMillis", alarmTimeInMillis);
            cancelAction.putExtra("maps_direction_request", maps_direction_request);
            cancelAction.putExtra("View_ID_position", position);
            cancelAction.putExtra("alarm_music_ID", alarmMusic_ID);
            cancelAction.putExtra("isDelayAlarm", isDelayAlarm);
            cancelAction.putExtra("alarmName", alarmName);
            cancelAction.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            cancelAction.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, NOT_ID, cancelAction, PendingIntent.FLAG_UPDATE_CURRENT);

            // Creo un notification Channel ------------------------------------
            NotificationChannel notificationChannel = new NotificationChannel(not_Channel_ID, "Alarm Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.enableLights(true);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(true);

            // fullScreen notification intent ----------------------------------
            Intent fullScreen = new Intent(context, FullScreen_Notification.class);
            fullScreen.putExtra("notification_ID", NOT_ID);
            fullScreen.putExtra("alarm_music_ID", alarmMusic_ID);
            fullScreen.putExtra("isDelayAlarm", isDelayAlarm);
            fullScreen.putExtra("alarm_name", alarmName);
            fullScreen.putExtra("notification_Channel", not_Channel_ID);
            fullScreen.putExtra("delayTimeForCancelForNotification", delayTimeForCancelForNotification);
            fullScreen.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
            fullScreen.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
            fullScreen.putExtra("alarmTimeInMillis", alarmTimeInMillis);
            fullScreen.putExtra("maps_direction_request", maps_direction_request);
            fullScreen.putExtra("View_ID_position", position);
            fullScreen.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
            fullScreen.putExtra("isRebootAlarm", isRebootAlarm);
            //fullScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreen, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, not_Channel_ID)
                    .setContentTitle("Notification Alarm")
                    .setContentText(alarmName)
                    .setSound(uriSong)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .setSmallIcon(R.drawable.icons8_alarm_clock_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .addAction(0, "CANCELLA", cancelPendingIntent)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
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
                                    final String alarm_name,
                                         final PendingIntent fullScreenPendingIntent) {
        Handler handler = new Handler();
        final long delayInMilliseconds = delayTimeForCancelForNotification;
        if(repeatAlarmNumberTimes > 0) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (isNotificationActive(id, context)) {
//da aggiungere stop sensore
                        Intent delayNotificationIntent = new Intent(context, delayNotificationReceiver.class);
                        delayNotificationIntent.putExtra("notification_ID", id);
                        delayNotificationIntent.putExtra("alarm_music_ID", alarm_music_ID);
                        delayNotificationIntent.putExtra("isDelayAlarm", isDelayAlarm);
                        delayNotificationIntent.putExtra("alarm_name", alarm_name);
                        delayNotificationIntent.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                        delayNotificationIntent.putExtra("isRebootAlarm", isRebootAlarm);
                        PendingIntent delayPendingIntent = PendingIntent.getBroadcast(context, 0, delayNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        try {
                            delayPendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                            Log.i("SendPendingIntentSnzNot", "onClick: Non posso inviare (send) il pending intent per ritardare la sveglia - AlarmReceiver");
                        }

                    } else {
                        Log.i("REMOVE_NOTIFICATION", "run: la notifica è gia stata cancellata");
                    }
                }
            }, delayInMilliseconds);
        }
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
