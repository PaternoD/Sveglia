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

    public static void startCountDownTimer(long delayTime,
                                           final int notification_ID,
                                           Activity activity,
                                           int alarm_Music_ID,
                                           boolean is_Delay_Alarm,
                                           String alarm_Name,
                                           SensorManager mySensorManager,
                                           SensorEventListener proxymityEvent,
                                           int position,
                                           int repeatAlarmNumberTimes,
                                           boolean isRepetitionDayAlarm,
                                           int ALARM_ID,
                                           String maps_direction_request){

        countTimer = new android.os.CountDownTimer(delayTime, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(MainActivity.isActive){
                    activity.finish();
                }else {
                    activity.finishAffinity();
                }
            }
        };

    }

    public static void startCountDown(){
        countTimer.start();
    }

    public static void cancelCountDown(){ countTimer.cancel(); }

}
