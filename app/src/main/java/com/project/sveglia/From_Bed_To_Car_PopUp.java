package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

/**
 * Created by simonerigon on 12/07/18.
 */

public class From_Bed_To_Car_PopUp extends Activity {

    long duration = 900000;
    long animationTime = 400;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.from_bed_to_car);

        // recupero riferimenti layout ------------------------
        // CardView
        CardView card_15 = (CardView)findViewById(R.id.Card_15_FBTC_ID);
        CardView card_30 = (CardView)findViewById(R.id.Card_30_FBTC_ID);
        CardView card_45 = (CardView)findViewById(R.id.Card_45_FBTC_ID);
        CardView card_60 = (CardView)findViewById(R.id.Card_60_FBTC_ID);
        CardView card_75 = (CardView)findViewById(R.id.Card_75_FBTC_ID);
        CardView card_90 = (CardView)findViewById(R.id.Card_90_FBTC_ID);
        CardView cardViewFromBedToCar = (CardView)findViewById(R.id.cardView_fromBedToCar);

        // Button
        Button btn_save = (Button)findViewById(R.id.btn_salva_from_bed_to_car_ID);
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel_from_bed_to_car_ID);

        cardView_Animation(cardViewFromBedToCar);

        // Recupero informazioni da Intent chiamante -----------
        long fromBedToCarValue = getIntent().getExtras().getLong("fromBedToCarValue");

        //System.out.println("fromBedToCarString: "+ fromBedToCarValue);

        if(fromBedToCarValue == 900000){
            duration = 900000;
            // Setto colori di default ----------------------------
            card_15.setBackgroundResource(R.drawable.color_day_active);
            card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(fromBedToCarValue == 1800000){
            duration = 1800000;
            // Setto colori di default ----------------------------
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30.setBackgroundResource(R.drawable.color_day_active);
            card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(fromBedToCarValue == 2700000){
            duration = 2700000;
            // Setto colori di default ----------------------------
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_45.setBackgroundResource(R.drawable.color_day_active);
            card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(fromBedToCarValue == 3600000){
            duration = 3600000;
            // Setto colori di default ----------------------------
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_60.setBackgroundResource(R.drawable.color_day_active);
            card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(fromBedToCarValue == 4500000){
            duration = 4500000;
            // Setto colori di default ----------------------------
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_75.setBackgroundResource(R.drawable.color_day_active);
            card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
        }else if(fromBedToCarValue == 5400000){
            duration = 5400000;
            // Setto colori di default ----------------------------
            card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
            card_90.setBackgroundResource(R.drawable.color_day_active);
        }

        card_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 900000;

                card_15.setBackgroundResource(R.drawable.color_day_active);
                card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 1800000;

                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30.setBackgroundResource(R.drawable.color_day_active);
                card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 2700000;

                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_45.setBackgroundResource(R.drawable.color_day_active);
                card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 3600000;

                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_60.setBackgroundResource(R.drawable.color_day_active);
                card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_75.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 4500000;

                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_75.setBackgroundResource(R.drawable.color_day_active);
                card_90.setBackgroundResource(R.drawable.color_day_nonattivo);
            }
        });

        card_90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 5400000;

                card_15.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_30.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_45.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_60.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_75.setBackgroundResource(R.drawable.color_day_nonattivo);
                card_90.setBackgroundResource(R.drawable.color_day_active);
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //----> Salvare fromBedToCarDuration nel database ----
                Intent resultIntent = new Intent();
                resultIntent.putExtra("FromBedToCarResult", duration);
                setResult(Activity.RESULT_OK, resultIntent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        From_Bed_To_Car_PopUp.this.finish();
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
                        From_Bed_To_Car_PopUp.this.finish();
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
