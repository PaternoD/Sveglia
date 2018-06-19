package com.project.sveglia;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by simonerigon on 22/05/18.
 */

public class Cancel_Alarm_Class extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void cancel_Alarm(int[] alarm_array, Context context){

        // Cancello Allarmi ---------------------
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent alarmToBeDeleted = new Intent(context, AlarmReceiver.class);

        for(int i=0; i<alarm_array.length; i++){

            int id_Alarm = alarm_array[i];
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id_Alarm, alarmToBeDeleted, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.cancel(pendingIntent);

            Log.i("CANCEL ALARM", "cancel_Alarm: alarm id = " + id_Alarm + "\n");

        }

    }
}
