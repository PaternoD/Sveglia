package com.project.sveglia;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by simonerigon on 26/07/18.
 */

public class StartApplication extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Sono entrato in StartApplication", Toast.LENGTH_SHORT).show();

        // Apro database --
        DB_Manager db_manager = new DB_Manager(context);
        db_manager.open();

        // recupero sveglie salvate nel database --
        ArrayList<String> all_Database_ID = db_manager.getAllID();
        ArrayList<String> all_time_in_millis = db_manager.getAllTimeView();
        ArrayList<String> all_alarm_name = db_manager.getAllNameView();
        ArrayList<String> all_ripetition_day = db_manager.getAllRepetitionsDay();
        ArrayList<String> all_ritarda = db_manager.getAllRitarda();
        ArrayList<String> all_alarm_music_id = db_manager.getAllIDsuoneria();
        ArrayList<String> all_alarm_music_position = db_manager.getAllPosSuoneria();
        ArrayList<String> all_is_travel_to = db_manager.getAllTravelTO();
        ArrayList<String> all_from_google = db_manager.getAllFrom();
        ArrayList<String> all_to_google = db_manager.getAllTo();
        ArrayList<String> all_traffic_model = db_manager.getAllMezzo();


        for (int i = 0;i<all_Database_ID.size()-1;i++){

            // Recupero informazioni base --
            long time_in_millis = Long.parseLong(all_time_in_millis.get(i));
            int alarm_music_id = Integer.parseInt(all_alarm_music_id.get(i));
            boolean is_delay_alarm;
            if(all_ritarda.get(i).equals("1")){
                is_delay_alarm = true;
            }else{
                is_delay_alarm = false;
            }
            String alarm_name = all_alarm_name.get(i);
            boolean[] repetitionsArray = fill_Ripetition_Array(false);
            String repetitionsArrayString = all_ripetition_day.get(i);
            //ciclo che mi restituisce il vettore di booleani
            for (int j=0;j<7;j++){
                boolean day=true;
                Character character;
                character = repetitionsArrayString.charAt(j);

                if (character.equals((Character)'0')){
                    day=false;
                }
                if (character.equals((Character)'1')){
                    day=true;
                }
                repetitionsArray[j]=day;
            }
            boolean is_travel_to;
            if(all_is_travel_to.get(i).equals("1")){
                is_travel_to = true;
            }else {
                is_travel_to = false;
            }
            int list_position_music = Integer.parseInt(all_alarm_music_position.get(i));
            String start_address = all_from_google.get(i);
            String destination_address = all_to_google.get(i);
            String traffic_model = all_traffic_model.get(i);

            // Recupero istanza Calendar --
            Calendar calendar = Calendar.getInstance();
            long current_time = calendar.getTimeInMillis();

            if(time_in_millis > current_time){

                if(is_travel_to == false){

                    Toast.makeText(context, "Sto settando la sveglia", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "is_delay_alarm: " + is_delay_alarm, Toast.LENGTH_LONG).show();

                    // Cancello informazioni nel database --
                    cancelAlarm(Integer.parseInt(all_Database_ID.get(i)), context, db_manager, true);
                    Toast.makeText(context, "Ho cancellato la sveglia", Toast.LENGTH_SHORT).show();

                    // Setto nuovamente la sveglia --
                    setAlarmAfterReboot(context, time_in_millis, alarm_music_id, is_delay_alarm, alarm_name, repetitionsArray, is_travel_to, list_position_music, start_address, destination_address, traffic_model);
                    Toast.makeText(context, "Ho risettato la sveglia", Toast.LENGTH_SHORT).show();

                }else{

                    // Cancello informazioni nel database --
                    cancelAlarm(Integer.parseInt(all_Database_ID.get(i)), context, db_manager, true);
                }

            }else{

                Toast.makeText(context, "non sono riuscito a settare la sveglia", Toast.LENGTH_SHORT).show();

                db_manager.SetOn_Off(Integer.parseInt(all_Database_ID.get(i)), false);

            }


        }

    }

    /**
     * Funzione la quale riempie l'array dei giorni di ripetizione
     * @param modify_alarm
     * @return array riempito (boolean)
     */
    private boolean[] fill_Ripetition_Array(boolean modify_alarm){
        boolean[] res = new boolean[7];

        if(modify_alarm){
            // ----> Settare array ricavando dati da database
        }else{
            for(int i=0; i<7; i++){
                res[i] = false;
            }
        }

        return res;
    }

    /**
     * Funzione per cancellare allarme sia dall'Alarm Manager sia dal database
     * @param id
     * @param context
     * @param db_manager
     * @param cancella_view_e_sveglie
     */
    private void cancelAlarm(int id, Context context, DB_Manager db_manager, boolean cancella_view_e_sveglie){

        Vector<Integer> vector = new Vector<>(1);
        vector=db_manager.getVectorID_Ripetizioni(id);

        // Cancello Allarmi ---------------------
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent alarmToBeDeleted = new Intent(context, AlarmReceiver.class);

        for(int i=0; i<vector.size(); i++){

            int id_Alarm = vector.elementAt(i);
            //cancellazione alarm manager
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id_Alarm, alarmToBeDeleted, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.cancel(pendingIntent);

            Log.i("CANCEL ALARM", "cancel_Alarm: alarm id = " + id_Alarm + "\n");

            //cancellazione table_sveglie
            db_manager.delete_sveglia(id_Alarm);
        }

        //cancellazione table view
        if(cancella_view_e_sveglie)
            db_manager.delete_view(id);

    }


    private void setAlarmAfterReboot(Context context,
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

        // Apro database ---------------------
        DB_Manager db_manager = new DB_Manager(context);
        db_manager.open();

        // recuperare numero di volte di questa fariabile dal database --
        int repeatAlarmNumberTimes = db_manager.getRitardaVolte();

        String maps_direction_request = null;

        // Inizializzo allarme principale-----
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
                    db_manager,
                    repeatAlarmNumberTimes,
                    maps_direction_request);
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
                    db_manager,
                    repeatAlarmNumberTimes,
                    maps_direction_request);
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
                    db_manager,
                    repeatAlarmNumberTimes,
                    maps_direction_request);
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
                                   DB_Manager db_manager,
                                   int repeatAlarmNumberTimes,
                                   String maps_direction_request){

        // Controlliamo se l'orario è minore del tempo corrente, in caso affermativo setto la sveglia
        // al giorno successivo, non deve essere fatto sulla sveglia settata con travel_to
        boolean isCorrectTime = true;
        if(!isTravelToAlarm){
            while(isCorrectTime) {
                if (timeInMillis < currentTimeInMillis) {
                    timeInMillis += 86400000;
                }else{
                    isCorrectTime = false;
                }
            }
        }

        int ALARM_ID = createID(timeInMillis);
        int ID_View = (int) getRandomID(timeInMillis);

        Intent startPrincipalAlarmIntent = new Intent(context, AlarmReceiver.class);
        startPrincipalAlarmIntent.putExtra("View_ID", ID_View);
        startPrincipalAlarmIntent.putExtra("alarm_music_ID", alarm_music_ID);
        startPrincipalAlarmIntent.putExtra("isDelayAlarm", isDelayAlarm);
        startPrincipalAlarmIntent.putExtra("alarmName", alarm_name);
        startPrincipalAlarmIntent.putExtra("isRepetitionDayAlarm", false);
        startPrincipalAlarmIntent.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
        startPrincipalAlarmIntent.putExtra("maps_direction_request", maps_direction_request);
        startPrincipalAlarmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, startPrincipalAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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

        db_manager.insert_view(ID_View,
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

        db_manager.insert_repetition_id((int) getRandomID(timeInMillis), vector_id_alarm);
        db_manager.insert_sveglia(ALARM_ID, timeInMillis);


    }

    /**
     * Funzione che setta la sveglia tramite i giorni di ripetizione
     * @param timeInMillis
     * @param currentTime
     * @param alarm_music_ID
     * @param isDelayAlarm
     * @param context
     * @param repetitionsArray
     * @param alarmManager
     */
    private static void starRepeatAlarm(long timeInMillis,
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
                                        DB_Manager db_manager,
                                        int repeatAlarmNumberTimes,
                                        String maps_direction_request){

        Calendar calendar = Calendar.getInstance();
        int ALARM_ID = createID(timeInMillis);
        int ID_View = (int) getRandomID(timeInMillis);
        int PRIMARY_ALARM_ID = ALARM_ID;
        long repeatTimeInMillis = timeInMillis;
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

        db_manager.insert_view((int) getRandomID(timeInMillis),
                timeInMillis,
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
                    calendar.setTimeInMillis(timeInMillis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    ALARM_ID += 1;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_monday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 1){
                    calendar.setTimeInMillis(timeInMillis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    ALARM_ID += 2;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_tuesday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 2){
                    calendar.setTimeInMillis(timeInMillis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    ALARM_ID += 3;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_wednesday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 3){
                    calendar.setTimeInMillis(timeInMillis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    ALARM_ID += 4;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_thursday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 4){
                    calendar.setTimeInMillis(timeInMillis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    ALARM_ID += 5;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_friday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 5){
                    calendar.setTimeInMillis(timeInMillis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    ALARM_ID += 6;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_saturday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }else if(i == 6){
                    calendar.setTimeInMillis(timeInMillis);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    ALARM_ID += 7;
                    repeatTimeInMillis = checkRepeatTimeInMillis(calendar.getTimeInMillis(), currentTime);
                    Log.i("Alarm_sunday", "starRepeatAlarm: Set OK, time in millis: " + repeatTimeInMillis);

                    db_manager.insert_sveglia(ALARM_ID,repeatTimeInMillis);
                }

                Intent startRepeatAlarmIntent = new Intent(context, AlarmReceiver.class);
                startRepeatAlarmIntent.putExtra("View_ID", ID_View);
                startRepeatAlarmIntent.putExtra("alarm_music_ID", alarm_music_ID);
                startRepeatAlarmIntent.putExtra("isDelayAlarm", isDelayAlarm);
                startRepeatAlarmIntent.putExtra("alarmName", alarm_name);
                startRepeatAlarmIntent.putExtra("isRepetitionDayAlarm", true);
                startRepeatAlarmIntent.putExtra("alarmTimeInMillis", timeInMillis);
                startRepeatAlarmIntent.putExtra("repeatAlarmNumberTimes", repeatAlarmNumberTimes);
                startRepeatAlarmIntent.putExtra("maps_direction_request", maps_direction_request);
                startRepeatAlarmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,ALARM_ID, startRepeatAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, repeatTimeInMillis, alarmPendingIntent);

                // Salvo id sveglia in vector_id_sveglia -----------------------------
                vector_id_sveglia.add(ALARM_ID);

                /**
                 * ------> ******* Qui bisogna salavare la sveglia nel database *******
                 */

                db_manager.insert_repetition_id((int) getRandomID(timeInMillis), vector_id_sveglia);



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
