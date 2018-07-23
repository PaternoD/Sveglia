package com.project.sveglia;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by simonerigon on 23/07/18.
 */

public class AutomaticCancelNotification extends Activity {

    private static android.os.CountDownTimer countTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void startCountDownTimer(int delayTime,
                                           final int notification_ID,
                                           Activity activity,
                                           int alarm_Music_ID,
                                           boolean is_Delay_Alarm,
                                           String alarm_Name,
                                           SensorManager mySensorManager,
                                           SensorEventListener proxymityEvent){

        countTimer = new android.os.CountDownTimer(delayTime, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mySensorManager.unregisterListener(proxymityEvent);
                Intent snoozeNotificationIntent = new Intent(activity, delayNotificationReceiver.class);
                snoozeNotificationIntent.putExtra("notification_ID", notification_ID);
                snoozeNotificationIntent.putExtra("alarm_music_ID", alarm_Music_ID);
                snoozeNotificationIntent.putExtra("isDelayAlarm", is_Delay_Alarm);
                snoozeNotificationIntent.putExtra("alarm_name", alarm_Name);
                PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(activity, 0, snoozeNotificationIntent, PendingIntent.FLAG_ONE_SHOT);
                try {
                    snoozePendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                    Log.i("SendPendingIntentSnzNot", "onClick: Non posso inviare (send) il pending intent per ritardare la sveglia");
                }

                activity.finishAffinity();

            }
        };

    }

    public static void startCountDown(){
        countTimer.start();
    }

    public static void cancelCountDown(){ countTimer.cancel(); }

}
