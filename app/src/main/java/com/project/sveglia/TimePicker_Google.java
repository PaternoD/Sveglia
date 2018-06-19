package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by simonerigon on 27/02/18.
 */

public class TimePicker_Google extends Activity {

    // Variabili globali -------------------------------------------------
    int date_year;
    int date_month;
    int date_day;
    int time_hour;
    int time_min;
    int animationTime = 400;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_picker_google);

        // Recupero riferimenti oggetti layout ---------------------------
        // ImageView
        ImageView calendar_imageView = (ImageView)findViewById(R.id.calendar_ImageView_ID);

        // Relative Layout
        final RelativeLayout time_layout = (RelativeLayout)findViewById(R.id.RelativeLayout_time);
        final RelativeLayout date_layout = (RelativeLayout)findViewById(R.id.Relative_Layout_Date);

        // TimePicker & DatePicker
        final TimePicker timePicker_google = (TimePicker)findViewById(R.id.timePicker_google_ID);
        final DatePicker datePicker_google = (DatePicker)findViewById(R.id.datePicker_google_ID);

        // Button
        Button btn_cancel_time = (Button)findViewById(R.id.btn_cancel_time_google_ID);
        Button btn_cancel_date = (Button)findViewById(R.id.btn_cancel_date_google_ID);
        Button btn_save_time = (Button)findViewById(R.id.btn_save_time_google_ID);
        Button btn_save_date = (Button)findViewById(R.id.btn_save_date_google_ID);

        // TextView
        final TextView datePicker_button = (TextView)findViewById(R.id.datePicker_text_ID);

        // CardView
        CardView cardViewTimePicker = (CardView)findViewById(R.id.cardView_TimePickerGoogle_ID);

        // Inizializzo animazine cardViewTimePicker ----------------------
        timePickerGoogleAnimationIn(cardViewTimePicker);


        // Recupero immagini da assegnare al layout ----------------------
        Bitmap calendar_image = BitmapFactory.decodeResource(TimePicker_Google.this.getResources(), R.drawable.icons8_calendar_24);

        // Setto immagini al layout --------------------------------------
        calendar_imageView.setImageBitmap(calendar_image);

        // Setto TimePicker
        timePicker_google.setIs24HourView(true);

        // Setto visualizzazione DatePicker e timePicker
        time_layout.setVisibility(View.VISIBLE);
        date_layout.setVisibility(View.GONE);

        // Setto data corrente -------------------------------------------
        String current_date = getActualDate();
        datePicker_button.setText(current_date);

        // Setto azione datePicker_button
        datePicker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_layout.setVisibility(View.GONE);
                date_layout.setVisibility(View.VISIBLE);

            }
        });

        // Setto azioni bottoni cancel e save di datePicker
        btn_cancel_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        date_layout.setVisibility(View.GONE);
                        time_layout.setVisibility(View.VISIBLE);
                    }
                }, 500);


            }
        });

        btn_save_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_year = datePicker_google.getYear();
                date_month = datePicker_google.getMonth();
                date_day = datePicker_google.getDayOfMonth();

                String date = date_day + "/" + date_month + "/" + date_year;
                datePicker_button.setText(date);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        date_layout.setVisibility(View.GONE);
                        time_layout.setVisibility(View.VISIBLE);
                    }
                }, 500);
            }
        });

        // Setto azioni bottoni cancel e save di timePicker
        btn_cancel_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TimePicker_Google.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 500);


            }
        });

        btn_save_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_hour = timePicker_google.getHour();
                time_min = timePicker_google.getMinute();

                Intent resultIntent = new Intent();
                long arrival_time = getArrivalTime();
                resultIntent.putExtra("arrival_time", arrival_time);
                setResult(Activity.RESULT_OK, resultIntent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TimePicker_Google.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 500);
            }
        });



    }

    /**
     * Funzione che restituisce al chiamante il tempo in millesecondi scelto
     * @return time in millisecond
     */
    private long getArrivalTime(){
        Calendar cal = Calendar.getInstance();

        cal.set(date_year, date_month, date_day, time_hour, time_min, 0);

        long res = cal.getTimeInMillis();

        return res;
    }

    /**
     * Funzione che restituisce la data corrente
     * @return current date
     */
    private String getActualDate(){
        Calendar cal = Calendar.getInstance();
        Date dateTime = cal.getTime();

        DateFormat dateFormat_Year = new SimpleDateFormat("YYY");
        DateFormat dateFormat_Month = new SimpleDateFormat("M");
        DateFormat dateFormat_Day = new SimpleDateFormat("d");

        String year = dateFormat_Year.format(dateTime);
        String month = dateFormat_Month.format(dateTime);
        String day = dateFormat_Day.format(dateTime);

        date_year = Integer.parseInt(year);
        date_month = Integer.parseInt(month);
        date_day = Integer.parseInt(day);

        String res =  date_day +"/"+ date_month +"/"+ date_year;

        return res;
    }

    private void timePickerGoogleAnimationIn(CardView cardView){

        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,1500, 0);
        translateAnimation.setDuration(animationTime);
        translateAnimation.setFillAfter(true);
        cardView.startAnimation(translateAnimation);

    }

}
