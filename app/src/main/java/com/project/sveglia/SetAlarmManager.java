package com.project.sveglia;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
                                       String end_address_detail,
                                       String traffic_model){

        long currentTime = getCurrentTime();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);



        DB_Manager db_manager = new DB_Manager(context);
        db_manager.open();

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
                    end_address_detail,
                    traffic_model,
                    db_manager);
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
                    end_address_detail,
                    traffic_model,
                    db_manager);
        }else{
            starRepeatAlarm(timeInMillis,
                    currentTime,
                    alarm_music_ID,
                    listPositionMusic,
                    isTravelToAlarm,
                    isDelayAlarm,
                    alarm_name,
                    context,
                    repetitionArray,
                    alarmManager,
                    start_address_detail,
                    end_address_detail,
                    traffic_model,
                    db_manager);
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
                                   String end_address_detail,
                                   String traffic_model,
                                   DB_Manager db_manager){

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

        int isTravelTo;
        if(isTravelToAlarm){
            isTravelTo = 1;
        }else{
            isTravelTo = 0;
        }

        int isDelay;
        if(isDelayAlarm){
            isDelay = 1;
        }else{
            isDelay = 0;
        }

        db_manager.insert_view((int) getRandomID(timeInMillis),
                timeInMillis,
                alarm_name,
                repetitionArray,
                "1",
                isDelay,
                alarm_music_ID,
                listPositionMusic,
                isTravelTo,
                start_address_detail,
                end_address_detail,
                traffic_model);

        Vector<Integer> vector_id_alarm = new Vector<>();
        vector_id_alarm.add(ALARM_ID);

        db_manager.insert_repetition_id(ALARM_ID, vector_id_alarm);
        db_manager.insert_sveglia(ALARM_ID, timeInMillis);


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
                                        int listPositionMusic,
                                        boolean isTravelToAlarm,
                                        boolean isDelayAlarm,
                                        String alarm_name,
                                        Context context,
                                        boolean[] repetitionsArray,
                                        AlarmManager alarmManager,
                                        String start_address_detail,
                                        String end_address_detail,
                                        String traffic_model,
                                        DB_Manager db_manager){

        Calendar calendar = Calendar.getInstance();
        int ALARM_ID = createID(timeInMilllis);
        int PRIMARY_ALARM_ID = ALARM_ID;
        long repeatTimeInMillis = timeInMilllis;
        Vector<Integer> vector_id_sveglia = new Vector<>(2);

        int isTravelTo;
        if(isTravelToAlarm){
            isTravelTo = 1;
        }else{
            isTravelTo = 0;
        }

        int isDelay;
        if(isDelayAlarm){
            isDelay = 1;
        }else{
            isDelay = 0;
        }

        db_manager.insert_view((int) getRandomID(timeInMilllis),
                timeInMilllis,
                alarm_name,
                repetitionsArray,
                "1",
                isDelay,
                alarm_music_ID,
                listPositionMusic,
                isTravelTo,
                start_address_detail,
                end_address_detail,
                traffic_model);



        for(int i=0; i<repetitionsArray.length; i++){
            if(repetitionsArray[i]){
                if(i == 0){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    ALARM_ID += 1;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_monday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 1){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    ALARM_ID += 2;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_tuesday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 2){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    ALARM_ID += 3;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_wednesday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 3){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    ALARM_ID += 4;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_thursday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 4){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    ALARM_ID += 5;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_friday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 5){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    ALARM_ID += 6;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_saturday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 6){
                    calendar.setTimeInMillis(timeInMilllis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    ALARM_ID += 7;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_sunday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
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

                db_manager.insert_repetition_id(PRIMARY_ALARM_ID, vector_id_sveglia);



            }
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


    //LAVORI IN CORSO-------------------------------
    public static long getRandomID(long timeInMillis){
        long res=0;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);

        DateFormat ore_format = new SimpleDateFormat("HH");
        DateFormat minuti_format = new SimpleDateFormat("mm");
        DateFormat giorno_format = new SimpleDateFormat("DDD");


        long ore = Long.parseLong(ore_format.format(cal.getTime()));
        long minuti = Long.parseLong(minuti_format.format(cal.getTime()));
        long giorno = Long.parseLong(giorno_format.format(cal.getTime()));

        long coeff_ore=   10000000;
        long coeff_minuti=10000;
        long coeff_giorno=10;

        res = res + (ore * coeff_ore);
        res = res + (minuti * coeff_minuti);
        res = res + (giorno * coeff_giorno);

        return res;

    }


}
