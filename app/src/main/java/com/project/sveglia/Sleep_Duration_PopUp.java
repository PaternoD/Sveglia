package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

/**
 * Created by simonerigon on 13/07/18.
 */

public class Sleep_Duration_PopUp extends Activity {

    long alarm_interval;
    int alarm_rep_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_duration_setting_layout);

        // recupero riferimenti layout ------------------------
        // CardView
        CardView card_1_interval = (CardView)findViewById(R.id.Card_1_Intervalli_Sveglia_ID);
        CardView card_5_interval = (CardView)findViewById(R.id.Card_5_Intervalli_Sveglia_ID);
        CardView card_10_interval = (CardView)findViewById(R.id.Card_10_Intervalli_Sveglia_ID);
        CardView card_15_interval = (CardView)findViewById(R.id.Card_15_Intervalli_Sveglia_ID);
        CardView card_20_interval = (CardView)findViewById(R.id.Card_20_Intervalli_Sveglia_ID);
        CardView card_25_interval = (CardView)findViewById(R.id.Card_25_Intervalli_Sveglia_ID);
        CardView card_30_interval = (CardView)findViewById(R.id.Card_30_Intervalli_Sveglia_ID);
        CardView card_1_ripetizioni = (CardView)findViewById(R.id.Card_1_Ripetizioni_ID);
        CardView card_3_ripetizioni = (CardView)findViewById(R.id.Card_3_Ripetizioni_ID);
        CardView card_5_ripetizioni = (CardView)findViewById(R.id.Card_5_Ripetizioni_ID);
        CardView card_10_ripetizioni = (CardView)findViewById(R.id.Card_10_Ripetizioni_ID);


        // Button
        Button btn_save = (Button)findViewById(R.id.btn_salva_sleep_setting_ID);
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel_sleep_setting_ID);

        // Recupero informazioni da Intent chiamante -----------
        long interval_duration = getIntent().getExtras().getLong("interval_duration");
        int repetition_time = getIntent().getExtras().getInt("repetition_time");


        if(interval_duration == 60000){
            alarm_interval = 60000;
            // Setto colori di default ----------------------------
            card_1_interval.setBackgroundResource(R.drawable.color_day_active);
            card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(interval_duration == 300000){
            alarm_interval = 300000;
            // Setto colori di default ----------------------------
            card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5_interval.setBackgroundResource(R.drawable.color_day_active);
            card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(interval_duration == 600000){
            alarm_interval = 600000;
            // Setto colori di default ----------------------------
            card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10_interval.setBackgroundResource(R.drawable.color_day_active);
            card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(interval_duration == 900000){
            alarm_interval = 900000;
            // Setto colori di default ----------------------------
            card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15_interval.setBackgroundResource(R.drawable.color_day_active);
            card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(interval_duration == 1200000){
            alarm_interval = 12000000;
            // Setto colori di default ----------------------------
            card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_20_interval.setBackgroundResource(R.drawable.color_day_active);
            card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(interval_duration == 1500000){
            alarm_interval = 1500000;
            // Setto colori di default ----------------------------
            card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_25_interval.setBackgroundResource(R.drawable.color_day_active);
            card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(interval_duration == 1800000){
            alarm_interval = 1800000;
            // Setto colori di default ----------------------------
            card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30_interval.setBackgroundResource(R.drawable.color_day_active);
        }

        if(repetition_time == 1){
            alarm_rep_time = 1;
            // Setto colori di default ----------------------------
            card_1_ripetizioni.setBackgroundResource(R.drawable.color_day_active);
            card_3_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(repetition_time == 3){
            alarm_rep_time = 3;
            // Setto colori di default ----------------------------
            card_1_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_3_ripetizioni.setBackgroundResource(R.drawable.color_day_active);
            card_5_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(repetition_time == 5){
            alarm_rep_time = 5;
            // Setto colori di default ----------------------------
            card_1_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_3_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5_ripetizioni.setBackgroundResource(R.drawable.color_day_active);
            card_10_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(repetition_time == 10){
            alarm_rep_time = 10;
            // Setto colori di default ----------------------------
            card_1_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_3_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10_ripetizioni.setBackgroundResource(R.drawable.color_day_active);
        }




        card_1_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_interval = 60000;

                card_1_interval.setBackgroundResource(R.drawable.color_day_active);
                card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_5_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_interval = 300000;

                card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5_interval.setBackgroundResource(R.drawable.color_day_active);
                card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_10_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_interval = 600000;

                card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10_interval.setBackgroundResource(R.drawable.color_day_active);
                card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_15_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_interval = 900000;

                card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15_interval.setBackgroundResource(R.drawable.color_day_active);
                card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_20_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_interval = 1200000;

                card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_20_interval.setBackgroundResource(R.drawable.color_day_active);
                card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_25_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_interval = 1500000;

                card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_25_interval.setBackgroundResource(R.drawable.color_day_active);
                card_30_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_30_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_interval = 1800000;

                card_1_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_20_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_25_interval.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30_interval.setBackgroundResource(R.drawable.color_day_active);
            }
        });

        card_1_ripetizioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_rep_time = 1;

                card_1_ripetizioni.setBackgroundResource(R.drawable.color_day_active);
                card_3_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_3_ripetizioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_rep_time = 3;

                card_1_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_3_ripetizioni.setBackgroundResource(R.drawable.color_day_active);
                card_5_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_5_ripetizioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_rep_time = 5;

                card_1_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_3_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5_ripetizioni.setBackgroundResource(R.drawable.color_day_active);
                card_10_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_10_ripetizioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_rep_time = 10;

                card_1_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_3_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5_ripetizioni.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10_ripetizioni.setBackgroundResource(R.drawable.color_day_active);
            }
        });




        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //----> Salvare fromBedToCarDuration nel database ----
                Intent resultIntent = new Intent();
                resultIntent.putExtra("interval_duration", alarm_interval);
                resultIntent.putExtra("repetition_time", alarm_rep_time);
                setResult(Activity.RESULT_OK, resultIntent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Sleep_Duration_PopUp.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 100);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Sleep_Duration_PopUp.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 100);
            }
        });

    }


}
