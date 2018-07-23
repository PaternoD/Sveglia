package com.project.sveglia;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by simonerigon on 09/04/18.
 */

public class CountDownTimer extends Activity{

    private static android.os.CountDownTimer countTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void startCountDownTimer(final NotificationCompat.Builder mBuilder, int id, final NotificationManager notificationManager, Bundle bundle, final Context context){

        final int idNot = id;
        final Bundle[] bundle1 = {bundle};
        long delayTime = bundle1[0].getLong("numero");

        //Toast.makeText(context, "StarCountDownTimer: - numero:" + delayTime, Toast.LENGTH_LONG).show();

        countTimer = new android.os.CountDownTimer(delayTime, 1000) {
            @Override
            public void onTick(long l) {
                bundle1[0] = mBuilder.getExtras();
                long time = bundle1[0].getLong("numero");
                long res = time - 1000;

                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                String res1 = dateFormat.format(new Date(res));

                String testo = "SNOOZE: " + res1;
                bundle1[0].putLong("numero", res);
                mBuilder.setExtras(bundle1[0]);
                mBuilder.setContentText(testo);
                notificationManager.notify(idNot, mBuilder.build());
            }

            @Override
            public void onFinish() {
                notificationManager.cancel(idNot);
            }
        };

    }

    public static void startCountDown(){
        countTimer.start();
    }

    public static void cancelCountDown(){

        countTimer.cancel();

    }

}
