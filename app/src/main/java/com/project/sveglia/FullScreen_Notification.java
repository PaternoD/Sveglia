package com.project.sveglia;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by simonerigon on 03/04/18.
 */

public class FullScreen_Notification extends Activity {

    int NOT_ID;
    int alarm_Music_ID;
    boolean is_Delay_Alarm;
    String alarm_Name;
    String notification_Channel;
    String maps_direction_request;
    long DelayTimeForCancel;
    //per sensore di prossimità
    SensorManager mySensorManager;
    Sensor myProximitySensor;
    boolean nero_nero=false;
    boolean bianco_nero=false;
    boolean isRepetitionDayAlarm;
    int repeatAlarmNumberTimes;
    long alarmTimeInMillis = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_screen_notification);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // Recupero dati da Intent chiamante -------------
        NOT_ID = getIntent().getExtras().getInt("notification_ID");
        alarm_Music_ID = getIntent().getExtras().getInt("alarm_music_ID");
        is_Delay_Alarm = getIntent().getExtras().getBoolean("isDelayAlarm");
        alarm_Name = getIntent().getExtras().getString("alarm_name");
        notification_Channel = getIntent().getExtras().getString("notification_Channel");
        DelayTimeForCancel = getIntent().getExtras().getLong("delayTimeForCancelForNotification");
        isRepetitionDayAlarm = getIntent().getExtras().getBoolean("isRepetitionDayAlarm");
        repeatAlarmNumberTimes = getIntent().getExtras().getInt("repeatAlarmNumberTimes");
        maps_direction_request = getIntent().getExtras().getString("maps_direction_request");
        int alarmViewID = getIntent().getExtras().getInt("View_ID");
        long alarmTimeForGoogleMaps = getIntent().getExtras().getLong("alarmTimeForGoogleMaps");

        if(isRepetitionDayAlarm){
            alarmTimeInMillis = getIntent().getExtras().getLong("alarmTimeInMillis");
        }

        // recupero riferimenti layout -------------------
        // CardView
        CardView delete_notification = (CardView)findViewById(R.id.card_delete_notification_ID);
        CardView snooze_notification = (CardView)findViewById(R.id.card_snooze_notification_ID);

        // ImageView
        ImageView snooze_ImageView = (ImageView)findViewById(R.id.imageView_snooze_notification);
        ImageView delete_ImageView = (ImageView)findViewById(R.id.imageView_delete_notification);

        // Layout
        RelativeLayout gradient_anim_layout = (RelativeLayout)findViewById(R.id.gradient_anim_layout_id);

        // TextView
        TextView alarmTime_TextView = (TextView)findViewById(R.id.text_time_alarm_notificatino_ID);
        TextView alarmName_TextView = (TextView)findViewById(R.id.text_alarm_name_notification_ID);

        // Setto il tempo e il nome della sveglia nel layout --
        alarmTime_TextView.setText(getTime());
        alarmName_TextView.setText(alarm_Name);

        // Setto animazione layout -----------------------
        AnimationDrawable animationDrawable = (AnimationDrawable)gradient_anim_layout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // Recupero immagini da usare nel layout ---------
        Bitmap snooze_Image = BitmapFactory.decodeResource(FullScreen_Notification.this.getResources(), R.drawable.alarm_snooze);
        Bitmap delete_Image = BitmapFactory.decodeResource(FullScreen_Notification.this.getResources(), R.drawable.alarm_off);

        // Aggiungo immagini a ImageView -----------------
        snooze_ImageView.setImageBitmap(snooze_Image);
        delete_ImageView.setImageBitmap(delete_Image);

        snooze_ImageView.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        delete_ImageView.setImageTintList(ColorStateList.valueOf(Color.WHITE));

        // Nascondi bottone Snooze se la sveglia è senza ripetizione

        //SENSORE per spegnere o rimandare la sveglia
        DB_Manager db = new DB_Manager(FullScreen_Notification.this);
        db.open();
        boolean sensor_on = db.getSensoriOn();
        db.close();
        SensorEventListener proximitySensorEventListener = new SensorEventListener() {


            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){

                    if (sensor_on){
                        if (sensorEvent.values[0]==0) {//se a pancia in giu
                            System.out.println("pancia in giu");
                            nero_nero=true;

                        if (bianco_nero && nero_nero) {
                            mySensorManager.unregisterListener(this);
                            DB_Manager db = new DB_Manager(FullScreen_Notification.this);
                            db.open();
                            if (db.getSensoriOpzione().equals((String)"cancella")){
                                //CANCELLO SVEGLIA
                                Intent cancelNotificationIntent = new Intent(FullScreen_Notification.this, CancelNotificationReceiver.class);
                                cancelNotificationIntent.putExtra("notification_ID", NOT_ID);
                                cancelNotificationIntent.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
                                cancelNotificationIntent.putExtra("alarmTimeInMillis", alarmTimeInMillis);
                                cancelNotificationIntent.putExtra("maps_direction_request", maps_direction_request);
                                cancelNotificationIntent.putExtra("View_ID", alarmViewID);
                                cancelNotificationIntent.putExtra("alarm_music_ID", alarm_Music_ID);
                                cancelNotificationIntent.putExtra("isDelayAlarm", is_Delay_Alarm);
                                cancelNotificationIntent.putExtra("alarmName", alarm_Name);
                                cancelNotificationIntent.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                                cancelNotificationIntent.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
                                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(FullScreen_Notification.this, 0, cancelNotificationIntent, PendingIntent.FLAG_ONE_SHOT);
                                try {
                                    cancelPendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                    Log.i("SendPendingIntentDelNot", "onClick: Non posso inviare (send) il pending intent per cancellare la notifica");
                                }
                                finishAffinity();
                            }
                            if(!is_Delay_Alarm){
                                mySensorManager.unregisterListener(this);
                            } else if (db.getSensoriOpzione().equals((String)"ritarda")){
                                //RIMANDO SVEGLIA
                                Intent snoozeNotificationIntent = new Intent(FullScreen_Notification.this, delayNotificationReceiver.class);
                                snoozeNotificationIntent.putExtra("notification_ID", NOT_ID);
                                snoozeNotificationIntent.putExtra("alarm_music_ID", alarm_Music_ID);
                                snoozeNotificationIntent.putExtra("isDelayAlarm", is_Delay_Alarm);
                                snoozeNotificationIntent.putExtra("alarm_name", alarm_Name);
                                snoozeNotificationIntent.putExtra("View_ID", alarmViewID);
                                snoozeNotificationIntent.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                                snoozeNotificationIntent.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
                                PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(FullScreen_Notification.this, 0, snoozeNotificationIntent, PendingIntent.FLAG_ONE_SHOT);
                                try {
                                    snoozePendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                    Log.i("SendPendingIntentSnzNot", "onClick: Non posso inviare (send) il pending intent per ritardare la sveglia");
                                }
                                finishAffinity();
                            }


                        }
                    }else{//se a pancia in su
                        System.out.println("pancia in su");
                        bianco_nero=true;

                        }
                    }else {
                        mySensorManager.unregisterListener(this);

                    }

                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };


        mySensorManager =  (SensorManager) FullScreen_Notification.this.getSystemService(Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mySensorManager.registerListener(proximitySensorEventListener,
                myProximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);


        // Nascondo la Navigation Bar --------------------
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        // Cancello la notifica dopo un tempo stabilito --
        AutomaticCancelNotification.startCountDownTimer(DelayTimeForCancel,
                NOT_ID,
                FullScreen_Notification.this,
                alarm_Music_ID,
                is_Delay_Alarm,
                alarm_Name,
                mySensorManager,
                proximitySensorEventListener);
        AutomaticCancelNotification.startCountDown();


        // Bottone per cancellare la notifica --------------
        delete_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySensorManager.unregisterListener(proximitySensorEventListener);
                Intent cancelNotificationIntent = new Intent(FullScreen_Notification.this, CancelNotificationReceiver.class);
                cancelNotificationIntent.putExtra("notification_ID", NOT_ID);
                cancelNotificationIntent.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
                cancelNotificationIntent.putExtra("alarmTimeInMillis", alarmTimeInMillis);
                cancelNotificationIntent.putExtra("maps_direction_request", maps_direction_request);
                cancelNotificationIntent.putExtra("View_ID", alarmViewID);
                cancelNotificationIntent.putExtra("alarm_music_ID", alarm_Music_ID);
                cancelNotificationIntent.putExtra("isDelayAlarm", is_Delay_Alarm);
                cancelNotificationIntent.putExtra("alarmName", alarm_Name);
                cancelNotificationIntent.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                cancelNotificationIntent.putExtra("alarmTimeForGoogleMaps", alarmTimeForGoogleMaps);
                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(FullScreen_Notification.this, 0, cancelNotificationIntent, PendingIntent.FLAG_ONE_SHOT);
                try {
                    cancelPendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                    Log.i("SendPendingIntentDelNot", "onClick: Non posso inviare (send) il pending intent per cancellare la notifica");
                }

                System.out.println("Ho premuto il tasto cancel");

                // termino il conto alla rovesca per la cancellazione automatica della notifica
                AutomaticCancelNotification.cancelCountDown();

                finishAffinity();
            }
        });

        // Bottone per cancellare la notifica e ritardare la sveglia --
        snooze_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySensorManager.unregisterListener(proximitySensorEventListener);
                Intent snoozeNotificationIntent = new Intent(FullScreen_Notification.this, delayNotificationReceiver.class);
                snoozeNotificationIntent.putExtra("notification_ID", NOT_ID);
                snoozeNotificationIntent.putExtra("alarm_music_ID", alarm_Music_ID);
                snoozeNotificationIntent.putExtra("isDelayAlarm", is_Delay_Alarm);
                snoozeNotificationIntent.putExtra("alarm_name", alarm_Name);
                snoozeNotificationIntent.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                snoozeNotificationIntent.putExtra("isRepetitionDayAlarm", isRepetitionDayAlarm);
                PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(FullScreen_Notification.this, 0, snoozeNotificationIntent, PendingIntent.FLAG_ONE_SHOT);
                try {
                    snoozePendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                    Log.i("SendPendingIntentSnzNot", "onClick: Non posso inviare (send) il pending intent per ritardare la sveglia");
                }

                // termino il conto alla rovesca per la cancellazione automatica della notifica
                AutomaticCancelNotification.cancelCountDown();

                finishAffinity();
            }
        });


    }

    /**
     * Funzione che ritorna l'orario nel preciso istante in cui viene invocata
     * @return orario corrente (String)
     */
    private String getTime(){

        String date = "-";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        date = dateFormat.format(cal.getTime());

        return date;
    }

    /**
     * Funzione che cancella la notifica in FullScreen dopo un tempo stabilito
     *
     */
    private void removeFullScreenNotification(int delayTime, final int notification_ID) {
        Handler handler = new Handler();
        final long delayInMilliseconds = delayTime;
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent snoozeNotificationIntent = new Intent(FullScreen_Notification.this, delayNotificationReceiver.class);
                snoozeNotificationIntent.putExtra("notification_ID", NOT_ID);
                snoozeNotificationIntent.putExtra("alarm_music_ID", alarm_Music_ID);
                snoozeNotificationIntent.putExtra("isDelayAlarm", is_Delay_Alarm);
                snoozeNotificationIntent.putExtra("alarm_name", alarm_Name);
                PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(FullScreen_Notification.this, 0, snoozeNotificationIntent, PendingIntent.FLAG_ONE_SHOT);
                try {
                    snoozePendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                    Log.i("SendPendingIntentSnzNot", "onClick: Non posso inviare (send) il pending intent per ritardare la sveglia");
                }

                finishAffinity();
            }
        }, delayInMilliseconds);
    }
}
