package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;

/**
 * Created by simonerigon on 12/07/18.
 */

public class Setting_Class extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        // Recupero riferimenti layout ---------------------------
        CardView cardFromBedToCar = (CardView)findViewById(R.id.Card_From_Bed_To_Car_ID);
        CardView cardRingToneDuration = (CardView)findViewById(R.id.Card_RingTone_Duration_ID);
        CardView cardSleepDuration = (CardView)findViewById(R.id.Card_Sleep_Duration_ID);
        CardView cardSensor = (CardView)findViewById(R.id.Card_Sensor_ID);

        cardFromBedToCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int FromBedToCarID = 1;

                Intent FromBedToCarIntent = new Intent(Setting_Class.this, From_Bed_To_Car_PopUp.class);
                startActivityForResult(FromBedToCarIntent, FromBedToCarID);
            }
        });
    }
}
