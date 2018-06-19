package com.project.sveglia;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Vector;

/**
 * Created by simonerigon on 15/03/18.
 */

public class SetAlarmManager {

    public static void SetAlarmManager(Context context,
                                       long timeInMillis,
                                       int alarm_music_ID,
                                       boolean isDelayAlarm,
                                       String alarm_name,
                                       boolean[] repetitionArray,
                                       boolean isTravelToAlarm,
                                       int listPositionMusic,
                                       String start_address_detail,
                                       String end_address_detail){

        long currentTime = getCurrentTime();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Inizializzo allarme principale
        if(!there_Are_Repetitions_Days(repetitionArray)){
            startAlarm(timeInMillis,
                    currentTime,
                    alarm_music_ID,
                    isDelayAlarm,
                    alarm_name,
                    isTravelToAlarm,
                    context,
                    repetitionArray,
                    alarmManager,
                    listPositionMusic,
                    start_address_detail,
                    end_address_detail);
        }else if(isTravelToAlarm) {
            startAlarm(timeInMillis,
                    currentTime,
                    alarm_music_ID,
                    isDelayAlarm,
                    alarm_name,
                    isTravelToAlarm,
                    context,
                    repetitionArray,
                    alarmManager,
                    listPositionMusic,
                    start_address_detail,
                    end_address_detail);
        }else{
            starRepeatAlarm(timeInMillis,
                    currentTime,
                    alarm_music_ID,
                    isDelayAlarm,
                    alarm_name,
                    context,
                    repetitionArray,
                    alarmManager);
        }

    }

    /**
     * Funzione che ritorna l'orario in millisecondi nel momento in cui è stata invocata
     * @return Current time in milliseconds
     */
    private static long getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        long res = calendar.getTimeInMillis();
        return res;
    }

    /**
     * Funzione che setta l'allarme in base al tempo in input
     * @param timeInMillis
     * @param currentTimeInMillis
     * @param isTravelToAlarm
     * @param context
     * @param alarmManager
     */
    private static void startAlarm(long timeInMillis,
                                   long currentTimeInMillis,
                                   int alarm_music_ID,
                                   boolean isDelayAlarm,
                                   String alarm_name,
                                   boolean isTravelToAlarm,
                                   Context context,
                                   boolean[] repetitionArray,
                                   AlarmManager alarmManager,
                                   int listPositionMusic,
                                   String start_address_detail,
                                   String end_address_detail){

        // Controlliamo se l'orario è minore del tempo corrente, in caso affermativo setto la sveglia
        // al giorno successivo, non deve essere fatto sulla sveglia settata con travel_to
        if(!isTravelToAlarm){
            if(timeInMillis < currentTimeInMillis){
                timeInMillis += 86400000;
            }
        }

        int ALARM_ID = createID(timeInMillis);

        Intent startPrincipalAlarmIntent = new Intent(context, AlarmReceiver.class);
        startPrincipalAlarmIntent.putExtra("alarm_music_ID", alarm_music_ID);
        startPrincipalAlarmIntent.putExtra("isDelayAlarm", isDelayAlarm);
        startPrincipalAlarmIntent.putExtra("alarmName", alarm_name);
        startPrincipalAlarmIntent.putExtra("isRepetitionDayAlarm", false);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, startPrincipalAlarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, alarmPendingIntent);

        /**
         * ------> ******* Qui bisogna salavare la sveglia nel database *******
         */



    }

    /**
     * Funzione che setta la sveglia tramite i giorni di ripetizione
     * @param timeInMilllis
     * @param currentTime
     * @param alarm_music_ID
     * @param isDelayAlarm
     * @param context
     * @param repetitionsArray
     * @param alarmManager
     */
    private static void starRepeatAlarm(long timeInMilllis,
                                        long currentTime,
                                        int alarm_music_ID,
                                        boolean isDelayAlarm,
                                        String alarm_name,
                                        Context context,
                                        boolean[] repetitionsArray,
                                        AlarmManager alarmManager){

        Calendar calendar = Calendar.getInstance();
        int ALARM_ID = createID(timeInMilllis);
        long repeatTimeInMillis = timeInMilllis;
        Vector<Integer> vector_id_sveglia = new Vector<>(2);

        DB_Manager db_manager = new DB_Manager(context);

        for(int i=0; i<repetitionsArray.length; i++){
            if(repetitionsArray[i]){
                if(i == 0){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_monday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 1){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    ALARM_ID += 1;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_tuesday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    //db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 2){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    ALARM_ID += 2;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_wednesday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    //db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 3){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    ALARM_ID += 3;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_thursday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    //db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 4){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    ALARM_ID += 4;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_friday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    //db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 5){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    ALARM_ID += 5;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_saturday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    //db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 6){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    ALARM_ID += 6;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_sunday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    //db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }

                Intent startRepeatAlarmIntent = new Intent(context, AlarmReceiver.class);
                startRepeatAlarmIntent.putExtra("alarm_music_ID", alarm_music_ID);
                startRepeatAlarmIntent.putExtra("isDelayAlarm", isDelayAlarm);
                startRepeatAlarmIntent.putExtra("alarmName", alarm_name);
                startRepeatAlarmIntent.putExtra("isRepetitionDayAlarm", true);
                startRepeatAlarmIntent.putExtra("alarmTimeInMillis", timeInMilllis);
                PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,ALARM_ID, startRepeatAlarmIntent, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, repeatTimeInMillis, alarmPendingIntent);

                // Salvo id sveglia in vector_id_sveglia -----------------------------
                vector_id_sveglia.add(ALARM_ID);

                /**
                 * ------> ******* Qui bisogna salavare la sveglia nel database *******
                 */



            }
        }

        // Salvo vector_id_sveglia nel database -----------------

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

    /**
     * Funzione che mi dice se ci sono dei giorni di ripetizione settati
     * @param repetitionArray
     * @return return true if there are repetitions days
     */
    private static boolean there_Are_Repetitions_Days(boolean[] repetitionArray){
        boolean res = false;
        for(int i=0; i<repetitionArray.length && res == false; i++){
            res = repetitionArray[i];
        }
        return res;
    }

    /**
     * Funnzione che controlla il tempo in input è minore dell'ora corrente
     * @param timeInMillis
     * @param currentTime
     * @return if input time is less than current time, the input time is updated
     */
    private static long checkRepeatTimeInMillis(long timeInMillis, long currentTime){
        if(timeInMillis <= currentTime){
            timeInMillis += 604800000;
        }
        return timeInMillis;
    }

}
