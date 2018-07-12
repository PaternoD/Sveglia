package com.project.sveglia;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;

/**
 * Created by simonerigon on 12/07/18.
 */

public class From_Bed_To_Car_PopUp extends Activity {

    int duration = 15;

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

        // Setto colori di default ----------------------------
        card_15.setBackgroundColor(getResources().getColor(R.color.CircleRepetitionAlarm));
        card_30.setBackgroundColor(Color.TRANSPARENT);
        card_45.setBackgroundColor(Color.TRANSPARENT);
        card_60.setBackgroundColor(Color.TRANSPARENT);
        card_75.setBackgroundColor(Color.TRANSPARENT);
        card_90.setBackgroundColor(Color.TRANSPARENT);

        card_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 15;

                card_15.setBackgroundColor(getResources().getColor(R.color.CircleRepetitionAlarm));
                card_30.setBackgroundColor(Color.TRANSPARENT);
                card_45.setBackgroundColor(Color.TRANSPARENT);
                card_60.setBackgroundColor(Color.TRANSPARENT);
                card_75.setBackgroundColor(Color.TRANSPARENT);
                card_90.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        card_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 30;

                card_15.setBackgroundColor(Color.TRANSPARENT);
                card_30.setBackgroundColor(getResources().getColor(R.color.CircleRepetitionAlarm));
                card_45.setBackgroundColor(Color.TRANSPARENT);
                card_60.setBackgroundColor(Color.TRANSPARENT);
                card_75.setBackgroundColor(Color.TRANSPARENT);
                card_90.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        card_45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 45;

                card_15.setBackgroundColor(Color.TRANSPARENT);
                card_30.setBackgroundColor(Color.TRANSPARENT);
                card_45.setBackgroundColor(getResources().getColor(R.color.CircleRepetitionAlarm));
                card_60.setBackgroundColor(Color.TRANSPARENT);
                card_75.setBackgroundColor(Color.TRANSPARENT);
                card_90.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        card_60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 60;

                card_15.setBackgroundColor(Color.TRANSPARENT);
                card_30.setBackgroundColor(Color.TRANSPARENT);
                card_45.setBackgroundColor(Color.TRANSPARENT);
                card_60.setBackgroundColor(getResources().getColor(R.color.CircleRepetitionAlarm));
                card_75.setBackgroundColor(Color.TRANSPARENT);
                card_90.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        card_75.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 75;

                card_15.setBackgroundColor(Color.TRANSPARENT);
                card_30.setBackgroundColor(Color.TRANSPARENT);
                card_45.setBackgroundColor(Color.TRANSPARENT);
                card_60.setBackgroundColor(Color.TRANSPARENT);
                card_75.setBackgroundColor(getResources().getColor(R.color.CircleRepetitionAlarm));
                card_90.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        card_90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = 90;

                card_15.setBackgroundColor(Color.TRANSPARENT);
                card_30.setBackgroundColor(Color.TRANSPARENT);
                card_45.setBackgroundColor(Color.TRANSPARENT);
                card_60.setBackgroundColor(Color.TRANSPARENT);
                card_75.setBackgroundColor(Color.TRANSPARENT);
                card_90.setBackgroundColor(getResources().getColor(R.color.CircleRepetitionAlarm));
            }
        });

        // Converto "fromBedToCarDuration" in long -------------------
        Long fromBedToCarDuration = convertIntToTimeInMillis(duration);

        //----> Salvare fromBedToCarDuration nel database ----

    }

    /**
     * Funzione che converte l'intero in input in TimeInMillis
     * @param duration
     * @return Correct TimeInMillis
     */
    private long convertIntToTimeInMillis(int duration){

        long defaultLong = 60000;
        long res = duration * defaultLong;

        return res;
    }

}
