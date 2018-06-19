package com.project.sveglia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by simonerigon on 21/02/18.
 */

public class Add_Alarm extends AppCompatActivity {

    // Variabili globali ------------------------------------------
    int modify_alarm_id;
    boolean modify_alarm = false;
    boolean[] repetitionsArray = fill_Ripetition_Array(modify_alarm);
    long alarm_time;
    String start_address_detail;
    String end_address_detail;
    boolean disable_modify_time = false;
    boolean disable_repetition_days = false;
    int listPositionMusic = 2;
    String alarm_Name = "Sveglia";
    String alarm_music_name;
    int alarm_music_ID;
    boolean delay_Alarm = true;
    String traffic_model;


    // TextView
    TextView repetition_Text;
    TextView day_info;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm);

        // Recupero i riferimenti nel layout -----------------------
        // TextView
        final TextView time = (TextView)findViewById(R.id.time_text_ID);
        final TextView etichetta_name = (TextView)findViewById(R.id.etichetta_name_ID);
        final TextView music_name = (TextView)findViewById(R.id.music_name_ID);
        repetition_Text = (TextView)findViewById(R.id.text_ripetizione_ID);
        day_info = (TextView)findViewById(R.id.text_day_info_alarm_time_ID);

        // CardView
        CardView etichetta_name_card = (CardView)findViewById(R.id.Card_etichetta_ID);
        CardView timePicker_Card = (CardView)findViewById(R.id.Show_time_Card_ID);
        CardView ripetizione_Card = (CardView)findViewById(R.id.Ripetizione_Card_ID);
        CardView travel_To_Card = (CardView)findViewById(R.id.Travel_to_card_ID);
        CardView add_music_card = (CardView)findViewById(R.id.Suono_Card_ID);

        // Switch
        final Switch travel_to_switch = (Switch)findViewById(R.id.Travel_to_Switch);
        final Switch delay_Alarm_switch = (Switch)findViewById(R.id.Ritarda_Switch);

        // Button
        Button btn_travel_switch = (Button)findViewById(R.id.btn_travel_svitch);
        Button btn_save_alarm = (Button)findViewById(R.id.btn_save_alarm_ID);

        // Relative Layout
        final RelativeLayout detail_transit_Layout = (RelativeLayout)findViewById(R.id.relLay_detail_transit_ID);
        final RelativeLayout line_transit = (RelativeLayout)findViewById(R.id.line);

        // Recupero dati da Intent chiamante -----------------------
        // Recupero informazione se la chiamata è per una nuova sveglia o la modifica di una gia esistente
        //modify_alarm = getIntent().getExtras().getBoolean("isModifyAlarm");

        if(modify_alarm) {
            modify_alarm_id = getIntent().getExtras().getInt("modify_alarm_id");
        }

        // Setto testo bottone salva/modifica allarme --------------
        if(modify_alarm){
            btn_save_alarm.setText("Modifica");
        }else{
            btn_save_alarm.setText("Salva");
        }

        // Attivo o disattivo visualizzazione Detail_Transit_Card in base a variabile modify_alarm --
        if(modify_alarm){
            detail_transit_Layout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            line_transit.getLayoutParams().height = 1;
        }else{
            detail_transit_Layout.getLayoutParams().height = 0;
            line_transit.getLayoutParams().height = 0;
        }

        // Recupero Musica di default per l'allarme ----------------



        if(modify_alarm){
            // Se modifico l'allarme gia esistente recupero musica dal database
            //alarm_music_name = getMusicNameFromDatabase(modify_alarm_id);
            //alarm_music_ID = getMusicIDFromDatabase(modify_alarm_id);
        }else{
            String[] res = getMusicData.getDefaultMusic(Add_Alarm.this);
            alarm_music_name = res[0];
            alarm_music_ID = Integer.parseInt(res[1]);
        }



        // Setto nome musica nel layout ----------------------------
        music_name.setText(alarm_music_name);

        // Setto l'ora corrente ------------------------------------
        String date = getTime();
        String currentDayTime = getDayTimeFromMillis(0);
        time.setText(date);
        day_info.setText(currentDayTime);

        // Aggiungo azione per cambiare nome all'etichetta ---------
        etichetta_name_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int NAME_INTENT_ID = 1;
                Intent add_alarm_name = new Intent(Add_Alarm.this, Add_Alarm_Name.class);
                String old_name = etichetta_name.getText().toString();
                add_alarm_name.putExtra("old_name", old_name);
                startActivityForResult(add_alarm_name, NAME_INTENT_ID);
            }
        });

        // Aggiungo azione per attivare il timePicker --------------
        timePicker_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(disable_modify_time){
                    Toast.makeText(Add_Alarm.this, "Non puoi modificare l'ora se stai utilizzando l'opzione 'Trave_to'", Toast.LENGTH_LONG).show();
                }else {
                    int TIME_PICKER_ID = 2;

                    Intent timePicker_intent = new Intent(Add_Alarm.this, TimePicker_Alarm.class);
                    startActivityForResult(timePicker_intent, TIME_PICKER_ID);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });

        // Aggiungo azione per aprire pop-up per la scelta dei giorni di ripetizine --
        ripetizione_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(disable_repetition_days){

                    Toast.makeText(Add_Alarm.this, "Non puoi usare questa funzione se utilizzi Travel To", Toast.LENGTH_LONG).show();

                }else {

                    repetition_Text.setTextColor(getResources().getColor(R.color.DefaultColorText));

                    int RIPETIZIONE_INTENT_ID = 3;

                    Intent ripetizione_intent = new Intent(Add_Alarm.this, Add_Repetitions.class);
                    ripetizione_intent.putExtra("array_giorni", repetitionsArray);
                    startActivityForResult(ripetizione_intent, RIPETIZIONE_INTENT_ID);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });

        // Aggiungo azione a bottone/Switch per la funzione Travel_To -----
        btn_travel_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()) {

                    if (travel_to_switch.isChecked()) {
                        travel_to_switch.setChecked(false);
                        detail_transit_Layout.getLayoutParams().height = 0;
                        line_transit.getLayoutParams().height = 0;
                        detail_transit_Layout.requestLayout();
                        line_transit.requestLayout();
                        disable_modify_time = false;
                        disable_repetition_days = false;
                        repetition_Text.setTextColor(getResources().getColor(R.color.DefaultColorText));
                    } else {
                        int GOOGLE_MAPS_ID = 4;

                        Intent googleMapsIntent = new Intent(Add_Alarm.this, MapsActivity.class);
                        googleMapsIntent.putExtra("modify_intent", false);
                        startActivityForResult(googleMapsIntent, GOOGLE_MAPS_ID);

                        // modifico layout inserendo i dettagli di travel_to
                        travel_to_switch.setChecked(true);
                    }
                }else{

                    Log.i("***** No Internet *****", "Non puoi utilizzare Travel-To, nessuna connessione a Internet");

                    Intent no_internet_conn_intent = new Intent(Add_Alarm.this, NoInternetConnectionActivity.class);
                    startActivity(no_internet_conn_intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            }
        });

        // Aggiugo azione per modificare parametri di travel to --------
        detail_transit_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int GOOGLE_MAPS_ID = 4;

                Intent googleMapsModifyIntent = new Intent(Add_Alarm.this, MapsActivity.class);
                googleMapsModifyIntent.putExtra("startAddress", start_address_detail);
                googleMapsModifyIntent.putExtra("endAddress", end_address_detail);
                googleMapsModifyIntent.putExtra("modify_intent", true);
                startActivityForResult(googleMapsModifyIntent, GOOGLE_MAPS_ID);
            }
        });

        // Aggiungo azione per scegliere una musica per l'allarme --------
        add_music_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int MUSIC_ID = 5;

                Intent musicIntent = new Intent(Add_Alarm.this, add_alarm_music.class);
                musicIntent.putExtra("listPositionMusic", listPositionMusic);
                musicIntent.putExtra("music_name", alarm_music_name);
                musicIntent.putExtra("alarm_music_ID", alarm_music_ID);
                startActivityForResult(musicIntent, MUSIC_ID);
            }
        });

        // Aggiungo azione per scegliere se ripetere o no l'allarme -------
        delay_Alarm_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delay_Alarm_switch.isChecked()){
                    delay_Alarm = true;
                }else{
                    delay_Alarm = false;
                }
            }
        });

        // Bottone per salvare la sveglia ---------------------------------
        btn_save_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAlarmManager.SetAlarmManager(Add_Alarm.this,
                        alarm_time,
                        alarm_music_ID,
                        delay_Alarm,
                        alarm_Name,
                        repetitionsArray,
                        false,
                        listPositionMusic,
                        start_address_detail,
                        end_address_detail,
                        traffic_model);

            }
        });

    }

    /**
     * Funzione invocata per ricevere i dati da StartActivityForResult()
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Ottengo risposta per cambiare nome alla sveglia ------
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String new_name = data.getExtras().getString("new_name");
                alarm_Name = new_name;
                TextView etichetta_new_name = (TextView)findViewById(R.id.etichetta_name_ID);
                etichetta_new_name.setText(new_name);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }

        // Ottengo risposta per modificare l'orario della sveglia --
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String newTime = data.getExtras().getString("newTime");
                alarm_time = data.getExtras().getLong("timeInMillis");
                TextView time = (TextView)findViewById(R.id.time_text_ID);
                time.setText(newTime);
                String day_time = getDayTimeFromMillis(alarm_time);
                day_info.setText(day_time);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }

        // Ottengo risposta per scegliere i giorni di ripetizione della sveglia --
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                boolean[] res = data.getExtras().getBooleanArray("res_repetitions_array");

                set_String_ripetitions(res);
                repetitionsArray = res;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }

        // Ottengo risposta da MapsActivity -----------
        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {
                // Recupero Extra da Activity chiamata
                long google_maps_time_in_millis = data.getExtras().getLong("alarm_time");
                traffic_model = data.getExtras().getString("transit_model");

                start_address_detail = data.getExtras().getString("start_address");
                end_address_detail = data.getExtras().getString("end_address");

                // Recupero riferimenti layout ------------------------
                // TextView
                TextView time = (TextView)findViewById(R.id.time_text_ID);
                TextView detail_origin_textView = (TextView)findViewById(R.id.text_detail_transit_origin);
                TextView detail_destination_textView = (TextView)findViewById(R.id.text_detail_transit_destination);

                // ImageView
                ImageView detail_transit_imageView = (ImageView)findViewById(R.id.detail_transit_image);
                ImageView place_detail_ImageView = (ImageView)findViewById(R.id.detail_transit_image);
                ImageView point_detail_ImageView = (ImageView)findViewById(R.id.detail_transit_image);

                // Setto i parametri -----------------------------------
                String google_maps_time = getFormattedTimeFromMillis(google_maps_time_in_millis);
                time.setText(google_maps_time);

                Bitmap place_image = BitmapFactory.decodeResource(Add_Alarm.this.getResources(), R.drawable.icons8_marker_24);
                place_detail_ImageView.setImageBitmap(place_image);
                place_detail_ImageView.setImageTintList(Add_Alarm.this.getColorStateList(R.color.buttonTransitButton_1));

                Bitmap point_image = BitmapFactory.decodeResource(Add_Alarm.this.getResources(), R.drawable.icons8_menu_vertical_24);
                point_detail_ImageView.setImageBitmap(point_image);
                point_detail_ImageView.setImageTintList(Add_Alarm.this.getColorStateList(R.color.my_DarkerGrey));

                detail_origin_textView.setText(start_address_detail);
                detail_destination_textView.setText(end_address_detail);

                disable_modify_time = true;
                disable_repetition_days = true;

                String day_time = getDayTimeFromMillis(google_maps_time_in_millis);
                day_info.setText(day_time);

                repetition_Text.setTextColor(getResources().getColor(R.color.my_DarkerGrey));

                if(traffic_model.equals("DRIVING")){
                    Bitmap detail_driving_image = BitmapFactory.decodeResource(Add_Alarm.this.getResources(), R.drawable.icons8_car_24);
                    detail_transit_imageView.setImageBitmap(detail_driving_image);
                }else if(traffic_model.equals("TRANSIT")){
                    Bitmap detail_transit_image = BitmapFactory.decodeResource(Add_Alarm.this.getResources(), R.drawable.icons8_train_24);
                    detail_transit_imageView.setImageBitmap(detail_transit_image);
                }else if(traffic_model.equals("WALKING")){
                    Bitmap detail_walking_image = BitmapFactory.decodeResource(Add_Alarm.this.getResources(), R.drawable.icons8_walking_24);
                    detail_transit_imageView.setImageBitmap(detail_walking_image);
                }


                enableDetailTransitLayuot();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
                // Recupero riferimenti layout -------------------------
                // Switch
                final Switch travel_to_switch = (Switch) findViewById(R.id.Travel_to_Switch);

                try {
                    boolean result = data.getExtras().getBoolean("modify_result_intent");
                    if (!result) {

                        disableDetailTransitLayuot();
                        disable_modify_time = false;
                        travel_to_switch.setChecked(false);
                    }
                }catch (Exception e){
                    String TAG = "Excep result_canceled";
                    Log.e(TAG, "onActivityResult: ", e);

                    if(disable_modify_time = true) {
                        travel_to_switch.setChecked(false);
                    }
                }
            }
        }

        // Ottengo risposta per cambiare musica alla sveglia ---
        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                // Recupero riferimento layout
                TextView music_name = (TextView)findViewById(R.id.music_name_ID);

                alarm_music_name = data.getExtras().getString("music_name");
                alarm_music_ID = data.getExtras().getInt("music_ID");
                listPositionMusic = data.getExtras().getInt("listPositionMusic");

                music_name.setText(alarm_music_name);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
                Toast.makeText(Add_Alarm.this, "Utilizza la freccia in alto per confermare la scelta della musica.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Funzione che ritorna l'orario nel preciso istante in cui viene invocata
     * @return orario corrente (String)
     */
    private String getTime(){

        // ----> Recuperare dati da database se è una modifica di una sveglia gia settata

        String date = "-";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        date = dateFormat.format(cal.getTime());
        alarm_time = cal.getTimeInMillis();

        return date;
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
     * Funzione che setta la stringa da stampare a schermo dei giorni di ripetizione scelti
     * @param array_ripetition
     */
    private void set_String_ripetitions(boolean[] array_ripetition){
        // Recupero riferimento a TextView --------------
        TextView text_ripetition = (TextView)findViewById(R.id.text_giorni_ripetizione_ID);

        // Variabili ------------------------------------
        String res_ripetition_string;

        if(is_False_Array(array_ripetition)){
            res_ripetition_string = "Mai";
            text_ripetition.setText(res_ripetition_string);
        }else if(is_True_Array(array_ripetition)){
            res_ripetition_string = "Tutti i giorni";
            text_ripetition.setText(res_ripetition_string);
        }else{
            String res = get_repetition_days(array_ripetition);
            text_ripetition.setText(res);
        }
    }

    /**
     * Funzione che restituisce true se l'array in input è tutto a false
     * @param array
     * @return
     */
    private boolean is_False_Array(boolean[] array){
        boolean res = false;
        int false_num = 0;

        for(int i=0; i<7; i++){
            if(array[i] == false){
                false_num++;
            }
        }

        if(false_num == 7){
            res = true;
        }

        return res;
    }

    /**
     * Funzione che restituisce true se l'array in input è tutto a true
     * @param array
     * @return
     */
    private boolean is_True_Array(boolean[] array){
        boolean res = false;
        int true_num = 0;

        for(int i=0; i<7; i++){
            if(array[i] == true){
                true_num++;
            }
        }

        if(true_num == 7){
            res = true;
        }

        return res;
    }

    /**
     * Funzione la quale restituisce il nome (abbreviato) dei giorni scelti per la ripetizione
     * dall'utente per settare la ripetizione
     * @param array
     * @return
     */
    private String get_repetition_days(boolean[] array){
        String res = "";

        for(int i=0; i<7; i++){
            if(array[i] == true){
                if(i == 0){
                    res += " Lun";
                }else if(i == 1){
                    res += " Mar";
                }else if(i == 2){
                    res += " Mer";
                }else if(i == 3){
                    res += " Gio";
                }else if(i == 4){
                    res += " Ven";
                }else if(i == 5){
                    res += " Sab";
                }else if(i == 6){
                    res += " Dom";
                }
            }
        }

        return res;
    }

    /**
     * Funzione che formatta il tempo in input (milliseconds) in HH:mm
     * @param time
     * @return formatted time (HH:mm)
     */
    private String getFormattedTimeFromMillis(long time){

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String res = dateFormat.format(cal.getTime());

        return res;
    }

    /**
     * Funzione che abilita la visualizzazione delle informazioni di Google Maps
     */
    private void enableDetailTransitLayuot(){

        // Repero riferimenti layout --------------------------
        // Relative Layout
        final RelativeLayout detail_transit_Layout = (RelativeLayout)findViewById(R.id.relLay_detail_transit_ID);
        final RelativeLayout line_transit = (RelativeLayout)findViewById(R.id.line);


        detail_transit_Layout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        line_transit.getLayoutParams().height = 1;
        detail_transit_Layout.requestLayout();
        line_transit.requestLayout();
    }

    /**
     * Funzione che disabilita la visualizzazione delle informazioni di Google Maps
     */
    private void disableDetailTransitLayuot(){

        // Repero riferimenti layout --------------------------
        // Relative Layout
        final RelativeLayout detail_transit_Layout = (RelativeLayout)findViewById(R.id.relLay_detail_transit_ID);
        final RelativeLayout line_transit = (RelativeLayout)findViewById(R.id.line);

        detail_transit_Layout.getLayoutParams().height = 0;
        line_transit.getLayoutParams().height = 0;
        detail_transit_Layout.requestLayout();
        line_transit.requestLayout();

    }

    /**
     * Funzione che restituisce vero se la connessione a internet è abilitata, false in caso contrario
     * @return true if the connection is available
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Funzione per ricavare il giorno (es. Lunedì  23-04-2018) da time in millis in input
     * @param time
     * @return current date (es. Lunedì  23-04-2018)
     */
    private String getDayTimeFromMillis(long time){

        Calendar cal = Calendar.getInstance();
        if(time != 0) {
            cal.setTimeInMillis(time);
        }

        DateFormat dateFormat_dayOfWeek = new SimpleDateFormat("E");
        DateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
        String dayofWeekEnglish = dateFormat_dayOfWeek.format(cal.getTime());
        String dayOfYear = dateFormat.format(cal.getTime());

        String dayOfWeekIta = getdayOfWeekIta(dayofWeekEnglish);

        String res = dayOfWeekIta + "  " + dayOfYear;

        return res;
    }

    /**
     * Funzione la quale mi ritorna il nome completo del giorno della settimana in input
     * @param dayOfWeek
     * @return long day name
     */
    private String getdayOfWeekIta(String dayOfWeek){
        String dayTime = "";
        switch(dayOfWeek){
            case "Lun":
                dayTime = "Lunedì";
                break;
            case "Mar":
                dayTime = "Martedì";
                break;
            case "Mer":
                dayTime = "Mercoledì";
                break;
            case "Gio":
                dayTime = "Giovedì";
                break;
            case "Ven":
                dayTime = "Venerdì";
                break;
            case "Sab":
                dayTime = "Sabato";
                break;
            case "Dom":
                dayTime = "Domenica";
                break;
        }

        return dayTime;
    }


}
