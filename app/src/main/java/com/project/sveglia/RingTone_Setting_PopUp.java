package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

/**
 * Created by simonerigon on 13/07/18.
 */

public class RingTone_Setting_PopUp extends Activity {

    int duration = 60000;
    long animationTime = 400;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringtone_setting_layout);

        // recupero riferimenti layout ------------------------
        // CardView
        CardView cardView_suoneria = (CardView)findViewById(R.id.cardView_suoneria_setting);
        CardView card_1 = (CardView)findViewById(R.id.Card_1_Ringtone_ID);
        CardView card_2 = (CardView)findViewById(R.id.Card_2_Ringtone_ID);
        CardView card_3 = (CardView)findViewById(R.id.Card_3_Ringtone_ID);
        CardView card_5 = (CardView)findViewById(R.id.Card_5_Ringtone_ID);
        CardView card_10 = (CardView)findViewById(R.id.Card_10_Ringtone_ID);
        CardView card_15 = (CardView)findViewById(R.id.Card_15_Ringtone_ID);

        // Button
        Button btn_save = (Button)findViewById(R.id.btn_salva_ringTone_setting_ID);
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel_ringTone_setting_ID);

        cardView_Animation(cardView_suoneria);

        // Recupero informazioni da Intent chiamante -----------
        long ringTone_Duration = getIntent().getExtras().getLong("ringTone_Duration");


        if(ringTone_Duration == 60000){
            duration = 60000;
            // Setto colori di default ----------------------------
            card_1.setBackgroundResource(R.drawable.color_day_active);
            card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(ringTone_Duration == 120000){
            duration = 120000;
            // Setto colori di default ----------------------------
            card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_2.setBackgroundResource(R.drawable.color_day_active);
            card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(ringTone_Duration == 180000){
            duration = 180000;
            // Setto colori di default ----------------------------
            card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_3.setBackgroundResource(R.drawable.color_day_active);
            card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(ringTone_Duration == 300000){
            duration = 900000;
            // Setto colori di default ----------------------------
            card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5.setBackgroundResource(R.drawable.color_day_active);
            card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(ringTone_Duration == 600000){
            duration = 600000;
            // Setto colori di default ----------------------------
            card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10.setBackgroundResource(R.drawable.color_day_active);
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(ringTone_Duration == 900000){
            duration = 900000;
            // Setto colori di default ----------------------------
            card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_15.setBackgroundResource(R.drawable.color_day_active);
        }


        card_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 60000;

                card_1.setBackgroundResource(R.drawable.color_day_active);
                card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 120000;

                card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_2.setBackgroundResource(R.drawable.color_day_active);
                card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 180000;

                card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_3.setBackgroundResource(R.drawable.color_day_active);
                card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 300000;

                card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5.setBackgroundResource(R.drawable.color_day_active);
                card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 600000;

                card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10.setBackgroundResource(R.drawable.color_day_active);
                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 900000;

                card_1.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_2.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_3.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_5.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_10.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_15.setBackgroundResource(R.drawable.color_day_active);
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //----> Salvare fromBedToCarDuration nel database ----
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ringTone_Duration", duration);
                setResult(Activity.RESULT_OK, resultIntent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RingTone_Setting_PopUp.this.finish();
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
                        RingTone_Setting_PopUp.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 100);
            }
        });


    }

    private void cardView_Animation(CardView cardView){

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 1000, 0);
        translateAnimation.setDuration(animationTime);
        translateAnimation.setFillAfter(true);
        cardView.startAnimation(translateAnimation);

    }


}
