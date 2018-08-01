package com.project.sveglia;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Vector;

/**
 * Created by simonerigon on 22/05/18.
 */

public class Cancel_Alarm_Class extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void cancel_Alarm(int id, Context context, DB_Manager db_manager, boolean cancella_view_e_sveglie){

        Vector<Integer> vector = new Vector<>(1);
        vector=db_manager.getVectorID_Ripetizioni(id);

        // Cancello Allarmi ---------------------
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent alarmToBeDeleted = new Intent(context, AlarmReceiver.class);

        for(int i=0; i<vector.size(); i++){

            int id_Alarm = vector.elementAt(i);
            //cancellazione alarm manager
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id_Alarm, alarmToBeDeleted, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);

            Log.i("CANCEL ALARM", "cancel_Alarm: alarm id = " + id_Alarm + "\n");

            //cancellazione table_sveglie
            db_manager.delete_sveglia(id_Alarm);
        }

        //cancellazione table view
        if(cancella_view_e_sveglie)
            db_manager.delete_view(id);





    }
}
